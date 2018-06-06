package kaihg.nchu.tsp;

import kaihg.nchu.tsp.evalutor.CycleEvaluator;
import kaihg.nchu.tsp.evalutor.IEvaluator;
import kaihg.nchu.tsp.model.AlgorithmModel;
import kaihg.nchu.tsp.model.AntModel;
import kaihg.nchu.tsp.model.GAModel;
import kaihg.nchu.tsp.transition.CycleCrossover;
import kaihg.nchu.tsp.transition.Mutation;
import kaihg.nchu.tsp.transition.OrderCrossover;
import kaihg.nchu.tsp.transition.PMCrossover;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;
import kaihg.nchu.tsp.vo.GAConfig;

import java.util.Arrays;
import java.util.Random;

public class HyperRunner {


    private AlgorithmModel antModel;
    private AlgorithmModel gaModel;

    private int[] bestTour;
    private double bestDistance;

    private StringBuilder logger;
    private Random motherRandom;

    private String gaCrossType;

    public HyperRunner(City[] cities, Config config, GAConfig gaConfig) {
        motherRandom = new Random(777);

        IEvaluator evaluator = new CycleEvaluator(cities);

        initAnt(cities, config, evaluator);
        initGA(cities, gaConfig, evaluator);

        logger = new StringBuilder();
    }

    private void initGA(City[] cities, GAConfig gaConfig, IEvaluator evaluator) {
        this.gaModel = new GAModel(cities, gaConfig);
        this.gaCrossType = gaConfig.crossoverType;

        ((GAModel) this.gaModel).setEvaluator(evaluator);
//        ((GAModel) this.gaModel).setCrossover(new PMCrossover(gaConfig.crossoverRate, motherRandom.nextInt()));
        switch (gaConfig.crossoverType) {
            case "PMX":
                ((GAModel) this.gaModel).setCrossover(new PMCrossover(gaConfig.crossoverRate, motherRandom.nextInt()));
                break;
            case "CX":
                ((GAModel) this.gaModel).setCrossover(new CycleCrossover(gaConfig.crossoverRate, motherRandom.nextInt(), cities.length));
                break;
            case "OX":
                ((GAModel) this.gaModel).setCrossover(new OrderCrossover(gaConfig.crossoverRate, motherRandom.nextInt()));
                break;
            case "OMCP":
                break;
        }

        ((GAModel) this.gaModel).setMutation(new Mutation(gaConfig.mutationRate, motherRandom.nextInt()));
    }

    private void initAnt(City[] cities, Config config, IEvaluator evaluator) {
        this.antModel = new AntModel(config.numAnts, cities, config);

        ((AntModel) this.antModel).setEvaluator(evaluator);
    }

    private void log(Object msg) {
        this.logger.append(msg).append("\n");
    }


    public void start(int iteration, boolean withGA) {
        int runTime = 10;
        double score = 0;
        double gaScore = 0;
        double bestOne = Double.MAX_VALUE;
        double bestGA = Double.MAX_VALUE;
        int[] bestTour = null;

        log("Run with" + (withGA ? "" : "out") + " GA");    // + (withGA? " + "+this.gaCrossType : "")

        long time = System.currentTimeMillis();
        for (int run = 0; run < runTime; run++) {
            int seed = motherRandom.nextInt();
//            System.out.println("Start TSP, the seed is " + seed );

            this.antModel.init(seed);
            this.gaModel.init(seed);

            for (int i = 0; i < iteration; i++) {

                int step = 300;
                for (int j = 0; j < step; j++) {
                    this.antModel.iterationOnce();
                }
                i += step;
//                this.antModel.iterationOnce();


                if (withGA) {
                    this.gaModel.updatePossibleTours(antModel.getAllPossibleTour());
                    for (int j = 0; j < step; j++) {
                        gaModel.iterationOnce();
                    }
                    antModel.updatePossibleTours(gaModel.getAllPossibleTour());
//                    i+=step;
                }

//            this.bestDistance = gaModel.getShortestTourDistance();
//                this.bestDistance = antModel.getShortestTourDistance();

//                if(i % 100==0){
//                    log(i + ", ");
//
//                    log(bestDistance);
//                    log("\n");
//                }

            }
//            log(antModel.getShortestTourDistance()+"\n");
            double localScore = antModel.getShortestTourDistance();
            score += localScore;
            if (localScore < bestOne) {
                bestOne = localScore;
                bestTour = antModel.getBestTour();
            }
            double thisScore = gaModel.getShortestTourDistance();
            gaScore += thisScore;
            if (thisScore < bestGA) {
                bestGA = thisScore;
            }
        }
        score /= runTime;
        gaScore /= runTime;

        log("Done in " + (System.currentTimeMillis() - time) / 1000 + "s");
//        log("The final result is \n" + Arrays.toString(gaModel.getBestTour()));
//        log("The final result is \n" + Arrays.toString(antModel.getBestTour()));
        log("The avg result is ANT : " + score + ", GA : " + gaScore);
        log("And the best one is ANT : " + bestOne+", GA : "+ bestGA);
        log(Arrays.toString(bestTour));
    }

    public void showMessage() {
        System.out.println(logger.toString());
    }


}
