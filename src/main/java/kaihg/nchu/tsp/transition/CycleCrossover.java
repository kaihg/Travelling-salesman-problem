package kaihg.nchu.tsp.transition;

import kaihg.nchu.tsp.evalutor.CycleEvaluator;

import java.util.*;

public class CycleCrossover implements ITransition<int[][]> {

    private double crossRate;
    private Random random;

    private boolean[] flags;
//    private TempPair[] tempPairs;
    private Map<Integer, TempPair> tempPairMap;
    private List<List<TempPair>> cycles;


    public CycleCrossover(double rate, int seed, int cityCount) {
        this.crossRate = rate;
        this.random = new Random(seed);

        flags = new boolean[cityCount];
//        tempPairs = new TempPair[cityCount];
        tempPairMap = new HashMap<>(cityCount);

        cycles = new ArrayList<>();
    }


    @Override
    public void update(int[][] solution) {

    }

    @Override
    public void update(int[][] source, int[][] target) {
        for (int i = 0, size = source.length / 2; i < size; i++) {
            if (random.nextDouble() > crossRate) {
                continue;
            }

            int[] parent1 = source[i];
            int[] parent2 = source[i + size];

            int[] child1 = target[i];
            int[] child2 = target[i + size];

            Arrays.fill(flags, false);
            cycles.clear();
            tempPairMap.clear();

            createLookUpMap(parent1, parent2);
            findCycles(parent1, parent2);
            copyToTarget(child1, child2);
        }
    }


    private void createLookUpMap(int[] parent1, int[] parent2) {
        for (int i = 0, size = parent1.length; i < size; i++) {
//            TempPair pair = tempPairs[parent2[i]];
            TempPair pair = tempPairMap.get(parent2[i]);
            if (pair == null) {
                pair = new TempPair();
//                tempPairs[parent2[i]] = pair;
                tempPairMap.put(parent2[i],pair);
            }

            pair.index = i;
            pair.city1 = parent1[i];
            pair.city2 = parent2[i];

        }
    }

    private void findCycles(int[] parent1, int[] parent2) {

        int startIndex;
        for (int i = 0, size = parent1.length; i < size; i++) {
            if (!flags[i]) {
                List<TempPair> pairList = new ArrayList<>();
                startIndex = i;

//                TempPair pair = tempPairs[parent1[startIndex]];
                TempPair pair = tempPairMap.get(parent1[startIndex]);
                pairList.add(pair);
                flags[pair.index] = true;

                while (pair.index != startIndex) {
//                    pair = tempPairs[parent1[pair.index]];

                    pair = tempPairMap.get(parent1[pair.index]);
                    pairList.add(pair);
                    flags[pair.index] = true;
                }
                cycles.add(pairList);
            }
        }
    }

    private void copyToTarget(int[] child1, int[] child2) {
        for (int i = 0, size = cycles.size(); i < size; i++) {
            List<TempPair> cycle = cycles.get(i);

            for (TempPair pair : cycle) {
                child1[pair.index] = i % 2 == 0 ? pair.city1 : pair.city2;
                child2[pair.index] = i % 2 == 0 ? pair.city2 : pair.city1;
            }
        }
    }

    private class TempPair {
        int index;
        int city1;
        int city2;
    }

}

