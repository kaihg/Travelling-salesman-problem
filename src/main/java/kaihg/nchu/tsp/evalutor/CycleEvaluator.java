package kaihg.nchu.tsp.evalutor;

import kaihg.nchu.tsp.util.DistanceCal;
import kaihg.nchu.tsp.vo.City;

public class CycleEvaluator implements IEvaluator {

    private double[][] cityDistancesTable;

    public CycleEvaluator(City[] cities){
        this.cityDistancesTable = new double[cities.length][cities.length];

        int size = cities.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    this.cityDistancesTable[i][j] = 0;
                } else {
                    if (this.cityDistancesTable[j][i] != 0) {
                        this.cityDistancesTable[i][j] = this.cityDistancesTable[j][i];
                    } else {
                        this.cityDistancesTable[i][j] = DistanceCal.getDistance(cities[i], cities[j]);
                    }
                }
            }
        }
    }

    @Override
    public double evaluate(int[] tour) {
        double sum = 0;
        int cities = tour.length -1;
        for (int i=0;i<cities;i++){
            sum += cityDistancesTable[tour[i]][tour[i+1]];
        }
        sum += cityDistancesTable[tour[cities]][tour[0]];

        return sum;
    }
}
