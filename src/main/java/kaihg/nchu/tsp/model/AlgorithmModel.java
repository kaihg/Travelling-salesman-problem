package kaihg.nchu.tsp.model;

public interface AlgorithmModel {

    void init(int seed);
    void iterationOnce();

    double getShortestTourDistance();
    Integer[][] getAllPossibleTour();
}
