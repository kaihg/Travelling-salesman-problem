package kaihg.nchu.tsp.transition;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OrderCrossover implements ITransition<int[][]> {

    private double crossRate;
    private Random random;

    private Set<Integer> set1;
    private Set<Integer> set2;

    public OrderCrossover(double crossRate, int seed) {
        this.crossRate = crossRate;
        this.random = new Random(seed);

        set1 = new HashSet<>();
        set2 = new HashSet<>();
    }

    @Override
    public void update(int[][] solution) {

    }

    void crossover(int[] parent1, int[] parent2, int start, int end, int[] child1, int[] child2) {
        // 先生成中間留下來的部位，並存對應位置
        fillMiddleToChild(parent1, start, end, child1, set1);
        fillMiddleToChild(parent2, start, end, child2, set2);

        fillHeadAndTail(parent2, start, end, child1, set1);
        fillHeadAndTail(parent1, start, end, child2, set2);
    }

    @Override
    public void update(int[][] source, int[][] target) {
        for (int i = 0, size = source.length / 2; i < size; i++) {
            if (random.nextDouble() < crossRate) {
                int[] parent1 = source[i];
                int[] parent2 = source[i + size];
                int[] child1 = target[i], child2 = target[i + size];

                int start = random.nextInt(parent1.length);
                int end = random.nextInt(parent1.length - start) + start + 1;

                set1.clear();
                set2.clear();

                crossover(parent1, parent2, start, end, child1, child2);
            } else {
                System.arraycopy(source[i], 0, target[i], 0, source[i].length);
                System.arraycopy(source[i + size], 0, target[i + size], 0, source[i + size].length);
            }
        }
    }

    @Override
    public void resetRandom(int seed) {
        this.random.setSeed(seed);
    }

    private void fillHeadAndTail(int[] parent2, int start, int end, int[] child1, Set<Integer> set1) {
        int length = child1.length;
        int marker = (end == length ? 0 : end);

        for (int i = end; i < length; i++) {
            if (!set1.contains(parent2[i])) {
                child1[marker] = parent2[i];
                set1.add(parent2[i]);
                marker = (marker + 1 == length ? 0 : marker + 1);
            }
        }

        for (int i = 0; i < end; i++) {
            if (!set1.contains(parent2[i])) {
                child1[marker] = parent2[i];
                set1.add(parent2[i]);
                marker = (marker + 1 == length ? 0 : marker + 1);
            }
        }
    }

    private void fillMiddleToChild(int[] parent, int start, int end, int[] child, Set<Integer> saveSet) {
        for (int i = start; i < end; i++) {
            child[i] = parent[i];
            saveSet.add(parent[i]);
        }
    }
}
