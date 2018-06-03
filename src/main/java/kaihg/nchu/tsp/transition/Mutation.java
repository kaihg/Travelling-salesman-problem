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

    public Mutation(double rate, int seed) {
        this.rate = rate;
        random = new Random(seed);
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
        for (int i = 0; i < source.length; i++) {
            int[] tour = source[i];
            int[] tar = target[i];
            System.arraycopy(source[i], 0, tar, 0, tar.length);
            if (random.nextDouble() < rate) {
                int index1 = random.nextInt(tour.length);
                int index2 = random.nextInt(tour.length);

//                int temp = tour[index2];
                tar[index2] = tour[index1];
                tar[index1] = tour[index2];
            }
        }
    }
}
