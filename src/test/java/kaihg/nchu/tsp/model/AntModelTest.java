package kaihg.nchu.tsp.model;

import kaihg.nchu.tsp.util.FileParser;
import kaihg.nchu.tsp.vo.Ant;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AntModelTest {

    private AntModel model;

    private List<Ant> ants;
    private Random random = new Random();

    @Before
    public void setUp() throws Exception {
        City[] cities = FileParser.parseCityFromFile("eil51.tsp");
        Config config = FileParser.parseConfigFromFile("berlinConf.json");

        int antNum = 50;
        model = new AntModel(antNum, cities, config);

        ants = Stream.generate(() -> {
            Ant ant = new Ant(cities.length);
            ant.setPheroRate(0.8);
            ant.setDistRate(0.8);
            ant.setBiasdRate(0);

            ant.setRandom(random);
            return ant;
        }).limit(antNum).collect(Collectors.toList());
    }

    @Test
    public void testModel() {
        model.init(-708050255);
        int iteration = 1000;
        for (int i = 0; i < iteration; i++) {
            model.iterationOnce();
        }
//        model.iterationOnce();

        int[] tour = model.getShortestTour();
        System.out.println(model.getShortestTourDistance());
        Assert.assertEquals(tour.length,51);
        Assert.assertEquals( Arrays.stream(tour).distinct().count(),51);

    }

    @Test
    public void testTime() {
        model.init(-708050255);
        int iteration = 1000;
        for (int i = 0; i < iteration; i++) {
            model.iterationOnce();
        }
    }

    @Test
    public void testUpdatePheroTime() {
        model.init(-708050255);
        int iteration = 1000;
        int cities = 51;

        for (Ant ant : ants){
            ant.resetTour();
            for (int i = 0; i < cities; i++) {
                ant.moveToCity(random.nextInt(cities));
            }
        }

        for (int i = 0; i < iteration; i++) {
            model.pheromoneUpdate(ants);
        }
    }

    @Test
    public void testAntTime() {
        model.init(-708050255);
        int iteration = 1000;
        int cities = 51;


        double[][] tempTable1 = new double[cities][cities];
        double[][] tempTable2 = new double[cities][cities];
        double[][] tempTable3 = new double[cities][cities];

        for (int i = 0; i < iteration; i++) {
            ants.forEach(ant -> {
                ant.resetTour();
                ant.moveToCity(random.nextInt(cities));
                ant.startTour(tempTable1, tempTable2, tempTable3);
            });
        }
    }
}
