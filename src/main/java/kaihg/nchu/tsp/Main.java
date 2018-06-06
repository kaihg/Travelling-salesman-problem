package kaihg.nchu.tsp;

import kaihg.nchu.tsp.model.AlgorithmModel;
import kaihg.nchu.tsp.model.AntModel;
import kaihg.nchu.tsp.util.FileParser;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;
import kaihg.nchu.tsp.vo.GAConfig;

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
        GAConfig gaConfig = FileParser.parseGAConfigFromFile(args[2]);

//        startByAntModel(config.numAnts, config.iteration, cities, config);
        startHyperModel(cities,config,gaConfig);
    }

    private static void startHyperModel(City[] cities, Config config, GAConfig gaConfig) {
        System.out.println("Run " + config.iteration + " times");

        HyperRunner runner;
        // only ANT
        System.out.println("Basic ANT");
        runner = new HyperRunner(cities, config, gaConfig);
        runner.start(config.iteration, false);
        runner.showMessage();

//        System.out.println("Test GA-PMX");
//        gaConfig.crossoverType = "PMX";
//        runner = new HyperRunner(cities, config, gaConfig);
//        runner.start(config.iteration, true);
//        runner.showMessage();
//
//        System.out.println("Test GA-CX");
//        gaConfig.crossoverType = "CX";
//        runner = new HyperRunner(cities, config, gaConfig);
//        runner.start(config.iteration, true);
//        runner.showMessage();
//
//        System.out.println("Test GA-OX");
//        gaConfig.crossoverType = "OX";
//        runner = new HyperRunner(cities, config, gaConfig);
//        runner.start(config.iteration, true);
//        runner.showMessage();
    }

    private static AlgorithmModel startByAntModel(int numAnts, int iteration, City[] cities, Config config) throws IOException {
        AntModel model = new AntModel(numAnts, cities, config);

        StringBuilder logger = new StringBuilder();


        long time = System.currentTimeMillis();

        int runTime = 5;
        double score = 0;
        int seed = 0;
        Random random = new Random();
        for (int run = 0; run < runTime; run++) {
            seed = random.nextInt();
            System.out.println("seed is " + seed);

            model.init(seed);
            for (int i = 0; i < iteration; i++) {
                model.iterationOnce();
            }
            score += model.getShortestTourDistance();
            logger.append(model.getShortestTourDistance()).append("\n");
        }

//        model.init(seed);
//        for (int i = 0; i < iteration; i++) {
//            model.iterationOnce();
//            logger.append(model.getShortestTourDistance()).append("\n");
//        }

        // print tour
        int[] tour = model.getShortestTour();
        for (Integer city : tour) {
            logger.append(city + 1).append(",");
        }

        logger.append("\navg score is ").append(score / runTime);

        System.out.println(logger.toString());
        System.out.println("time : " + (System.currentTimeMillis() - time));
        saveToFile(seed, model.getShortestTourDistance(), model.getShortestTour());

        return model;
    }


    private static void saveToFile(int seed, double shortestTourDistance, int[] shortestTour) throws IOException {
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
