package kaihg.nchu.tsp.model;

import kaihg.nchu.tsp.transition.ITransition;
import kaihg.nchu.tsp.vo.Ant;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;
import kaihg.nchu.tsp.vo.GAConfig;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GAModel implements AlgorithmModel {


    private final double[] tempAry;
    private final Integer[] bestTour;
    private ITransition<int[][]> crossover;
    private ITransition<int[][]> mutation;

    private Random random;

    public GAModel(City[] cities,GAConfig config) {

        this.tempAry = new double[cities.length];
        this.bestTour = new Integer[cities.length];

    }



    @Override
    public void init(int seed) {
        this.random = new Random(seed);
    }

    @Override
    public void iterationOnce() {

    }

    @Override
    public double getShortestTourDistance() {
        return 0;
    }

    @Override
    public Integer[][] getAllPossibleTour() {
        return new Integer[0][];
    }
}
