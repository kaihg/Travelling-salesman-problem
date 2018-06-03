package kaihg.nchu.tsp.model;

import kaihg.nchu.tsp.util.FileParser;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class AntModelTest {

    private AntModel model;

    @Before
    public void setUp() throws Exception {
        City[] cities = FileParser.parseCityFromFile("eil51.tsp");
        Config config = FileParser.parseConfigFromFile("berlinConf.json");

        model = new AntModel(50, cities, config);

    }

    @Test
    public void testModel() {
        model.init(-708050255);
        int iteration = 10000;
        for (int i = 0; i < iteration; i++) {
            model.iterationOnce();
        }
//        model.iterationOnce();

        int[] tour = model.getShortestTour();
        System.out.println(model.getShortestTourDistance());
        Assert.assertEquals(tour.length,51);
        Assert.assertEquals( Arrays.stream(tour).distinct().count(),51);

    }
}
