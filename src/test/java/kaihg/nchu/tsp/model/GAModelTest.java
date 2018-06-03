package kaihg.nchu.tsp.model;

import kaihg.nchu.tsp.evalutor.CycleEvaluator;
import kaihg.nchu.tsp.transition.CycleCrossover;
import kaihg.nchu.tsp.transition.Mutation;
import kaihg.nchu.tsp.transition.PMCrossover;
import kaihg.nchu.tsp.util.FileParser;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;
import kaihg.nchu.tsp.vo.GAConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class GAModelTest {

    private GAModel model;
    private double cxRate;
    private int cityCount;

    @Before
    public void setUp() throws Exception {
        City[] cities = FileParser.parseCityFromFile("eil51.tsp");
        GAConfig gaConfig = FileParser.parseGAConfigFromFile("gaConfig.json");
        cxRate = gaConfig.crossoverRate;
        cityCount = cities.length;

        model = new GAModel(cities, gaConfig);

        model.setEvaluator(new CycleEvaluator(cities));
        model.setMutation(new Mutation(gaConfig.mutationRate));
    }

    @Test
    public void testPMX() {
//        int seed = new Random().nextInt();
        int seed = 7777;
        model.setCrossover(new PMCrossover(cxRate, seed));

        model.init(seed);
        model.iterationOnce();
        double score = model.getShortestTourDistance();
        System.out.println("Initial PMX score : " + score);
        for (int i = 0; i < 1000; i++) {
            model.iterationOnce();
        }
        System.out.println("End score : " + model.getShortestTourDistance());
        Assert.assertTrue(score >= model.getShortestTourDistance());
    }

    @Test
    public void testCX() {
//        int seed = new Random().nextInt();
        int seed = 7777;
        model.setCrossover(new CycleCrossover(cxRate,seed,cityCount));

        model.init(seed);
        model.iterationOnce();
        double score = model.getShortestTourDistance();
        System.out.println("Initial CX score : " + score);
        for (int i = 0; i < 1000; i++) {
            model.iterationOnce();
        }
        System.out.println("End score : " + model.getShortestTourDistance());
        Assert.assertTrue(score >= model.getShortestTourDistance());
    }
}
