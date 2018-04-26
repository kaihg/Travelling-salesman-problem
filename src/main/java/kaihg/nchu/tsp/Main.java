package kaihg.nchu.tsp;

import kaihg.nchu.tsp.model.AntModel;
import kaihg.nchu.tsp.util.FileParser;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("no params");
        }

        City[] cities = FileParser.parseCityFromFile(args[0]);
        Config config = FileParser.parseConfigFromFile(args[1]);
        AntModel model = new AntModel(config.numAnts, config.iteration, cities, config);


        StringBuilder logger = new StringBuilder();
        int seed = new Random().nextInt();

        System.out.println("seed is " + seed);
        model.init(seed);
        for (int i = 0; i < config.iteration; i++) {
            model.iterationOnce();
            logger.append(model.getShortestTour()).append("\n");
        }

        System.out.println(logger.toString());
    }
}
