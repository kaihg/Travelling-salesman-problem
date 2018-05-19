package kaihg.nchu.tsp;

import kaihg.nchu.tsp.model.AntModel;
import kaihg.nchu.tsp.util.FileParser;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
            logger.append(model.getShortestTourDistance()).append("\n");
        }

        // print tour
        Integer[] tour = model.getShortestTour();
        for (Integer city : tour) {
            logger.append(city + 1).append(",");
        }

        System.out.println(logger.toString());

//        saveToFile(seed, model.getShortestTourDistance(), model.getShortestTour());
    }

    private static void saveToFile(int seed, double shortestTourDistance, Integer[] shortestTour) throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\kaihg\\Documents\\NCHU_AI\\tour.txt");
        writer.write("seed : " + seed + "\n");
        writer.write("total distance : " + shortestTourDistance + "\n");
        writer.write("path : \n");
        for (Integer city : shortestTour) {
            writer.write(String.valueOf(city + 1));
            writer.write("\n");
        }

        writer.close();
    }


}
