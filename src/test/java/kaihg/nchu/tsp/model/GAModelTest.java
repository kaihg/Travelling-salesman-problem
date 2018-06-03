package kaihg.nchu.tsp.model;

import kaihg.nchu.tsp.evalutor.CycleEvaluator;
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

    @Before
    public void setUp() throws Exception {
        City[] cities = FileParser.parseCityFromFile("eil51.tsp");
        cities = Arrays.copyOfRange(cities,0,5);


        GAConfig gaConfig = FileParser.parseGAConfigFromFile("gaConfig.json");
        cxRate = gaConfig.crossoverRate;

        model = new GAModel(cities, gaConfig);

        model.setEvaluator(new CycleEvaluator(cities));
        model.setMutation(new Mutation(gaConfig.mutationRate));
    }

    @Test
    public void testPMX() {
        int seed = new Random().nextInt();
        model.setCrossover(new PMCrossover(cxRate, seed));

        model.init(seed);
        model.iterationOnce();
        double score = model.getShortestTourDistance();
        System.out.println("Initial score : " + score);
        for (int i = 0; i < 100; i++) {
            model.iterationOnce();
        }
        System.out.println("End score : " + model.getShortestTourDistance());
        Assert.assertTrue(score > model.getShortestTourDistance());
    }
}
