package kaihg.nchu.tsp.model;

public interface AlgorithmModel {

    void init(int seed);
    void iterationOnce();

    double getShortestTourDistance();
    int[] getBestTour();
    int[][] getAllPossibleTour();
    void updatePossibleTours(int[][] allTours);

}
