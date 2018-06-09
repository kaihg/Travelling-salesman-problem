package kaihg.nchu.tsp.transition;

import java.util.*;

public class PMCrossover implements ITransition<int[][]> {

    private double rate;
    private Random random;

    private Map<Integer, Integer> crossMap;


    public PMCrossover(double rate, int seed) {
        this.rate = rate;
        random = new Random(seed);
        crossMap = new HashMap<>();

    }

    @Override
    public void update(int[][] solution) {

        for (int i = 0, size = solution.length / 2; i < size; i++) {
            if (random.nextDouble() > rate) {
                continue;
            }

            int[] ant1 = solution[i];
            int[] ant2 = solution[i + size];

            int citySize = ant1.length;
            int startIndex = random.nextInt(citySize);
            int endIndex = random.nextInt(citySize - startIndex) + startIndex;

//            cross(ant1, ant2, startIndex, endIndex);
        }
    }

    private void createMap(int[] tour1, int[] tour2) {
        crossMap.clear();
        for (int i = 0; i < tour1.length; i++) {
            crossMap.put(tour2[i], tour1[i]);
        }
    }

    private void changeElement(int[] tour1, int startIndex, int endIndex, int[] sub2, Map<Integer, Integer> crossMap, int[] target) {
        for (int i = 0, size = tour1.length; i < size; i++) {

            if (i >= startIndex && i <= endIndex) {
//                tour1.set(i, sub2.get(i - startIndex));
                target[i] = sub2[i - startIndex];
            } else {
                int city = tour1[i];
                if (crossMap.containsKey(city)) {
                    Integer toBeReplace = crossMap.get(city);

                    int test = 0;
                    while (crossMap.containsKey(toBeReplace)) {
                        toBeReplace = crossMap.get(toBeReplace);
                        if (test++ > 100){
                            System.out.println(test);
                        }
                    }

                    city = toBeReplace;
                }
                target[i] = city;
            }
        }
    }

    void cross(int[] tour1, int[] tour2, int startIndex, int endIndex, int[] target1, int[] target2) {
        int[] sub1 = Arrays.copyOfRange(tour1, startIndex, endIndex + 1);
        int[] sub2 = Arrays.copyOfRange(tour2, startIndex, endIndex + 1);

        // save to map
        createMap(sub1, sub2);
        changeElement(tour1, startIndex, endIndex, sub2, crossMap, target1);

        createMap(sub2, sub1);
        changeElement(tour2, startIndex, endIndex, sub1, crossMap, target2);

//        for (int i = startIndex; i <= endIndex; i++) {
//            int temp = tour1[i];
//            tour1[i] = tour2[i];
//            tour2[i] = temp;
//        }
    }


    @Override
    public void update(int[][] source, int[][] target) {
        for (int i = 0, size = source.length / 2; i < size; i++) {
            if (random.nextDouble() > rate) {
                // 不執行，只複製
                System.arraycopy(source[i], 0, target[i], 0, source[i].length);
                System.arraycopy(source[i + size], 0, target[i + size], 0, source[i + size].length);
            } else {
                int[] ant1 = source[i];
                int[] ant2 = source[i + size];

                int citySize = ant1.length;
                int startIndex = random.nextInt(citySize);
                int endIndex = random.nextInt(citySize - startIndex) + startIndex;

                cross(ant1, ant2, startIndex, endIndex, target[i], target[i + size]);
            }
        }
    }

    @Override
    public void resetRandom(int seed) {
        this.random.setSeed(seed);
    }
}
