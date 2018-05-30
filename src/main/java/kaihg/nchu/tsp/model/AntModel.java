package kaihg.nchu.tsp.model;

import kaihg.nchu.tsp.util.DistanceCal;
import kaihg.nchu.tsp.vo.Ant;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AntModel implements AlgorithmModel {

    private int iteration;
    private double evaporationRate;
    private double constantQ;
    private double[][] pheromoneTable;
    private double[][] distanceTable;
    private City[] cities;

    private List<Ant> ants;
    private Random random;

    private double[] tempAry;

    private double shortestTour = Double.MAX_VALUE;
    private Integer[] bestTour;

    public AntModel(int numAnt, int iteration, City[] cities, Config config) {
        this.iteration = iteration;
        this.cities = cities;

        this.pheromoneTable = new double[cities.length][cities.length];
        this.distanceTable = new double[cities.length][cities.length];

        this.tempAry = new double[cities.length];
        this.bestTour = new Integer[cities.length];

        this.constantQ = config.Q;
        this.evaporationRate = config.evaporationRate;


//        Stream<Ant> stream = Stream.generate(Ant::new).limit(numAnt);
        Stream<Ant> stream= Stream.generate(new Supplier<Ant>() {
            @Override
            public Ant get() {
                Ant ant = new Ant();
                ant.setPheroRate(config.pheromoneRate);
                ant.setDistRate(config.distanceRate);
                ant.setBiasdRate(config.biasedRate);
                return ant;
            }
        }).limit(numAnt);
//        // 先設定成共用同樣設定
//        stream.forEach((ant) -> {
//            ant.setPheroRate(config.pheromoneRate);
//            ant.setDistRate(config.distanceRate);
//            ant.setBiasdRate(config.biasedRate);
//        });

        this.ants = stream.collect(Collectors.toList());
    }

    @Override
    public void init(int seed) {
        this.random = new Random(seed);
        calDistance();
        initPheromone();

        // 設定隨機起始點
        ants.forEach((ant) -> {
            ant.resetTour();
//            ant.moveToCity(random.nextInt(cities.length));
        });

    }


    private void initPheromone() {
        double totalDistance = sumAllDistance();

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
            ant.startTour(pheromoneTable, distanceTable, tempAry);
        });

        pheromoneUpdate(ants);

        // compare to best
        Ant ant = ants.stream().min(Comparator.comparingDouble(Ant::getTourDistance)).get();
        if (ant.getTourDistance() < shortestTour) {
            shortestTour = ant.getTourDistance();
            ant.getTour().toArray(bestTour);
        }
    }

    public double getShortestTourDistance() {
        return this.shortestTour;
    }

    @Override
    public Integer[][] getAllPossibleTour() {

        //TODO
        return new Integer[0][];
    }

    public Integer[] getShortestTour(){
        return bestTour;
    }

    private void pheromoneUpdate(List<Ant> ants) {
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
                int i = ant.getTour().get(city);
                int j = ant.getTour().get(city - 1);
                pheromoneTable[i][j] += localPheromone;
                pheromoneTable[j][i] += localPheromone;
            }
        });
    }
}
