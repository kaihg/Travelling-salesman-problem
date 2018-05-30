package kaihg.nchu.tsp.transition;

import kaihg.nchu.tsp.vo.Ant;

import java.util.*;

public class PMCrossover implements ITransition<int[][]> {

    private double rate;
    private Random random;

    private Map<Integer, Integer> crossMap;

    public PMCrossover(double rate) {
        this.rate = rate;
        random = new Random();
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

            cross(ant1, ant2, startIndex, endIndex);
        }
    }

    private void createMap(int[] tour1, int[] tour2) {
        crossMap.clear();
        for (int i = 0; i < tour1.length; i++) {
            crossMap.put(tour2[i], tour1[i]);
        }
    }

    private void changeElement(int[] tour1, int startIndex, int endIndex,int[] sub2, Map<Integer, Integer> crossMap) {
        for (int i = 0, size = tour1.length; i < size; i++) {

            if (i >= startIndex && i <= endIndex) {
//                tour1.set(i, sub2.get(i - startIndex));
            } else {
                int city = tour1[i];
                if (crossMap.containsKey(city)) {
                    Integer toBeReplace = crossMap.get(city);
                    while (crossMap.containsKey(toBeReplace)) {
                        toBeReplace = crossMap.get(toBeReplace);
                    }

                    tour1[i] = toBeReplace;
                }
            }
        }
    }

    void cross(int[] tour1,int[] tour2, int startIndex, int endIndex) {


//        List<Integer> sub1 = tour1.subList(startIndex, endIndex + 1);
//        List<Integer> sub2 = tour2.subList(startIndex, endIndex + 1);
        int[] sub1 = Arrays.copyOfRange(tour1,startIndex,endIndex+1);
        int[] sub2 = Arrays.copyOfRange(tour2,startIndex,endIndex+1);

        // save to map
        createMap(sub1, sub2);
        changeElement(tour1, startIndex, endIndex, sub2, crossMap);

        createMap(sub2, sub1);
        changeElement(tour2, startIndex, endIndex, sub1, crossMap);

        for (int i = startIndex; i <= endIndex; i++) {
            int temp = tour1[i];
            tour1[i] = tour2[i];
            tour2[i] = temp;
        }
    }


    @Override
    public void update(int[][] source, int[][] target) {
        this.update(source);
    }
}
