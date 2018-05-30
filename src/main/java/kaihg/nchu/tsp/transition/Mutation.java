package kaihg.nchu.tsp.transition;

import kaihg.nchu.tsp.vo.Ant;

import java.util.Random;

public class Mutation implements ITransition<int[][]> {

    private double rate;
    private Random random;

    public Mutation(double rate) {
        this.rate = rate;
        random = new Random();
    }

    @Override
    public void update(int[][] solution) {
        for (int[] tour : solution) {
            if (random.nextDouble() < rate) {
                int index1 = random.nextInt(tour.length);
                int index2 = random.nextInt(tour.length);

                int temp = tour[index2];
                tour[index2] = tour[index1];
                tour[index1] = tour[temp];
            }
        }
    }

    @Override
    public void update(int[][] source, int[][] target) {

    }
}
