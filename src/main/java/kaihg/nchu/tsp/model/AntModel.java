package kaihg.nchu.tsp.model;

import kaihg.nchu.tsp.evalutor.IEvaluator;
import kaihg.nchu.tsp.util.DistanceCal;
import kaihg.nchu.tsp.vo.Ant;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;
import kaihg.nchu.tsp.vo.TabuAnt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AntModel implements AlgorithmModel {

    private IEvaluator evaluator;

    private double evaporationRate;
    private double constantQ;
    private double[][] pheromoneTable;
    private double[][] distanceTable;
    private double[][] probilityTable;
    private City[] cities;

    private List<Ant> ants;
    private Random random;

    private int[][] outputs;
    private double[] tempAry;

    private double shortestTour = Double.MAX_VALUE;
    private int[] bestTour;

    private double pheromoneRate;
    private double distanceRate;

    public AntModel(int numAnt, City[] cities, Config config) {
        this.cities = cities;

        this.pheromoneTable = new double[cities.length][cities.length];
        this.distanceTable = new double[cities.length][cities.length];
        this.probilityTable = new double[cities.length][cities.length];

        this.outputs = new int[numAnt][cities.length];
        this.tempAry = new double[cities.length];
        this.bestTour = new int[cities.length];

        this.constantQ = config.Q;
        this.evaporationRate = config.evaporationRate;

        this.pheromoneRate = config.pheromoneRate;
        this.distanceRate = config.distanceRate;

        Stream<Ant> stream = Stream.generate(() -> {
//            Ant ant = new Ant(cities.length);
            Ant ant = new TabuAnt(cities.length);
            ant.setPheroRate(config.pheromoneRate);
            ant.setDistRate(config.distanceRate);
            ant.setBiasdRate(config.biasedRate);
            return ant;
        }).limit(numAnt);

        this.ants = stream.collect(Collectors.toList());

    }

    @Override
    public void init(int seed) {
        this.random = new Random(seed);

        this.ants.forEach(ant -> ant.setRandom(random));
        calDistance();
        initPheromone();
        computerAllProbility();

        ants.forEach(Ant::onInit);

        this.shortestTour = Double.MAX_VALUE;
    }

    private void computerAllProbility(){
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j <i; j++) {
                double probability = Math.pow(pheromoneTable[i][j], pheromoneRate) * Math.pow(1 / distanceTable[i][j], distanceRate);
//                probability = Math.max(probability,Double.MIN_VALUE);   // 控制最小值
//                probability = Math.min(probability,1);  // 控制最大值

                probilityTable[i][j] = probilityTable[j][i] = probability;
            }
        }
    }

    public void setEvaluator(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    private void initPheromone() {
        double totalDistance = sumAllDistance();
        for (double[] one : this.pheromoneTable) {
            Arrays.fill(one,0);
        }

        int size = cities.length;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    if (this.pheromoneTable[j][i] != 0) {
                        this.pheromoneTable[i][j] = this.pheromoneTable[j][i];
                    } else {
                        this.pheromoneTable[i][j] = 1.0d / (this.distanceTable[i][j] * totalDistance);
                    }
                }
            }
        }
    }


    private double sumAllDistance() {
        double sum = 0;
        int size = cities.length;
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                sum += this.distanceTable[i][j];
            }
        }
        return sum;
    }

    private void calDistance() {
        int size = this.cities.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    this.distanceTable[i][j] = 0;
                } else {
                    if (this.distanceTable[j][i] != 0) {
                        this.distanceTable[i][j] = this.distanceTable[j][i];
                    } else {
                        this.distanceTable[i][j] = DistanceCal.getDistance(this.cities[i], this.cities[j]);
                    }
                }
            }
        }
    }

    @Override
    public void iterationOnce() {
        // init start city, and start tour
        ants.forEach(ant -> {
            ant.resetTour();
            ant.moveToCity(random.nextInt(this.cities.length));
//            ant.startTour(pheromoneTable, distanceTable, tempAry);
            ant.startTour(pheromoneTable, distanceTable, probilityTable);
        });

        pheromoneUpdate(ants);
        computerAllProbility(); // 更新完費洛蒙，重新計算機率

        // compare to best
        Ant ant = ants.stream().min(Comparator.comparingDouble(Ant::getTourDistance)).get();

        if (ant.getTourDistance() < shortestTour) {
            shortestTour = ant.getTourDistance();
            System.arraycopy(ant.getTour(), 0, bestTour, 0, cities.length);
        }
    }

    public double getShortestTourDistance() {
        return this.shortestTour;
    }

    @Override
    public int[] getBestTour() {
        return bestTour;
    }

    @Override
    public int[][] getAllPossibleTour() {
        for (int i = 0, length = this.bestTour.length; i < ants.size(); i++) {
            System.arraycopy(ants.get(i).getTour(), 0, outputs[i], 0, length);
        }
        return outputs;
    }

    @Override
    public void updatePossibleTours(int[][] allTours) {
        for (int i = 0; i < allTours.length; i++) {
            Ant ant = ants.get(i);
            int[] tour = allTours[i];

            ant.setTour(tour);
            ant.setTourDistance(sumTourDistance(tour));
        }

        List<Ant> goodAnts = ants.stream().sorted(Comparator.comparingDouble(Ant::getTourDistance)).limit(3).collect(Collectors.toList());

        pheromoneUpdate(goodAnts);
        computerAllProbility();
    }

    private double sumTourDistance(int[] tour) {
        return evaluator.evaluate(tour);
    }

    public int[] getShortestTour() {
        return bestTour;
    }

    void pheromoneUpdate(List<Ant> ants) {
        // 全部揮發
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j < cities.length; j++) {
                pheromoneTable[i][j] = (1 - evaporationRate) * pheromoneTable[i][j];
            }
        }

        // 加上這次走過的值
        ants.forEach((ant) -> {
            double localPheromone = constantQ / ant.getTourDistance();
            for (int city = 1; city < cities.length; city++) {
                int i = ant.getTour()[city];
                int j = ant.getTour()[city - 1];
                pheromoneTable[i][j] += localPheromone;
                pheromoneTable[j][i] += localPheromone;
            }
        });
    }
}
