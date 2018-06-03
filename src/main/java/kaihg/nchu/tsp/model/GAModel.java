package kaihg.nchu.tsp.model;

import kaihg.nchu.tsp.evalutor.IEvaluator;
import kaihg.nchu.tsp.transition.ITransition;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.GAConfig;

import java.util.Arrays;
import java.util.Random;

public class GAModel implements AlgorithmModel {


    private IEvaluator evaluator;
    private ITransition<int[][]> crossover;
    private ITransition<int[][]> mutation;

    protected int[][] current;
    private int[][] fitPopulation;
    private int[][] temp1;
    private int[][] temp2;
    private double[] scoreMap;
    private int[] cdf;
    private int bitCount;
    protected int tourmentRepeatTimes = 1;

    private Random random;

    public GAModel(City[] cities, GAConfig config) {

        int populationSize = config.populationSize;
        bitCount = cities.length;

        this.current = new int[populationSize][bitCount];
        this.fitPopulation = new int[populationSize][bitCount];
        this.temp1 = new int[populationSize][bitCount];
        this.temp2 = new int[populationSize][bitCount];
        this.scoreMap = new double[populationSize];
        this.cdf = new int[populationSize];


    }


    @Override
    public void init(int seed) {
        this.random = new Random(seed);

        for (int i = 0; i < current.length; i++) {
            createRandomTour(current[i]);
        }
    }

    private int[] createRandomTour(int[] space) {
        for (int i = 0; i < space.length; i++) {
            space[i] = i;
        }
        for (int i = space.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            int temp = space[index];
            space[index] = space[i];
            space[i] = temp;
        }
        return space;
    }

    public void setEvaluator(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setCrossover(ITransition<int[][]> crossover) {
        this.crossover = crossover;
    }

    public void setMutation(ITransition<int[][]> mutation) {
        this.mutation = mutation;
    }

    private boolean checkNotSame(int[][] tours){
        for (int[] tour : tours){
            if (Arrays.stream(tour).distinct().count() != tour.length){
                return true;
            }
        }
        return false;
    }

    @Override
    public void iterationOnce() {
        double[] fitnessScores = fitnessFunction(current);
        int[][] selected = select(current, fitnessScores);

//        crossover.update(current);
//        mutation.update(current);

        checkNotSame(selected);

        crossover.update(selected, temp1);
        checkNotSame(temp1);
        mutation.update(temp1, temp2);
        checkNotSame(temp2);

//        double[] fitnessScores2 = fitnessFunction(temp2);
//        int[][] selected2 = select(temp2, fitnessScores);

        changeCurrentPopulation(temp2, current);

    }


    protected void changeCurrentPopulation(int[][] temp, int[][] current) {
        for (int i = 0; i < temp.length; i++) {
            System.arraycopy(temp[i], 0, current[i], 0, temp[i].length);
        }
    }

    private double[] fitnessFunction(int[][] population) {
        for (int i = 0; i < population.length; i++) {
            scoreMap[i] = calculateFitnessScore(population[i], i);
        }
        return scoreMap;
    }

    protected double calculateFitnessScore(int[] gene, int index) {
        return evaluator.evaluate(gene);
    }

    private int[][] select(int[][] population, double[] fitnessScores) {
        // roulette or tournament

        return tournamentSelection(population, tourmentRepeatTimes, fitnessScores);

    }

    private int[][] rouletteWheelSelection(int[][] population, int[] fitnessScores) {
        int sum = 0;
        for (int fitnessScore : fitnessScores) {
            sum += fitnessScore;
        }

        cdf[0] = fitnessScores[0];
        for (int i = 1; i < population.length; i++) {
            cdf[i] = cdf[i - 1] + fitnessScores[i];
        }

        for (int i = 0; i < population.length; i++) {
            fitPopulation[i] = shootRoulette(population, sum, cdf);
        }

        return fitPopulation;
    }

    private int[] shootRoulette(int[][] population, int sum, int[] cdf) {
        int shoot = random.nextInt(sum);
        for (int i = 0; i < cdf.length; i++) {
            if (shoot < cdf[i]) {
                return population[i];
            }
        }

        return population[0];
    }

    private int[][] tournamentSelection(int[][] population, int repeat, double[] scoreMap) {

        for (int i = 0; i < fitPopulation.length; i++) {
            int winner = random.nextInt(population.length);

            for (int j = 0; j < repeat; j++) {
                int competitor = random.nextInt(population.length);
                winner = judgmentWinner(scoreMap, competitor, winner);
            }

            fitPopulation[i] = population[winner];
        }

        return fitPopulation;
    }

    protected int judgmentWinner(double[] scoreMap, int competitor, int winner) {
        return scoreMap[competitor] < scoreMap[winner] ? competitor : winner;
    }

    @Override
    public double getShortestTourDistance() {
        return Arrays.stream(scoreMap).min().orElse(9999);
    }

    @Override
    public int[] getBestTour() {
        double dist = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < scoreMap.length; i++) {
            double d = scoreMap[i];
            if (d < dist) {
                dist = d;
                index = i;
            }
        }
        return current[index];
    }

    @Override
    public int[][] getAllPossibleTour() {
        return current;
    }

    @Override
    public void updatePossibleTours(int[][] tours) {
        this.current = tours;
    }
}
