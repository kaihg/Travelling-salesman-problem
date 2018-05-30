package kaihg.nchu.tsp.transition;

import kaihg.nchu.tsp.vo.Ant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PMCrossover implements ITransition<Ant[]> {

    private double rate;
    private Random random;

    private Map<Integer, Integer> crossMap;

    public PMCrossover(double rate) {
        this.rate = rate;
        random = new Random();
        crossMap = new HashMap<>();
    }

    @Override
    public void update(Ant[] solution) {

        for (int i = 0, size = solution.length / 2; i < size; i++) {
            if (random.nextDouble() > rate) {
                continue;
            }

            Ant ant1 = solution[i];
            Ant ant2 = solution[i + size];

            int citySize = ant1.getTour().size();
            int startIndex = random.nextInt(citySize);
            int endIndex = random.nextInt(citySize - startIndex) + startIndex;

            cross(ant1.getTour(), ant2.getTour(), startIndex, endIndex);
        }
    }

    private void createMap(List<Integer> tour1, List<Integer> tour2) {
        crossMap.clear();
        for (int i = 0; i < tour1.size(); i++) {
            crossMap.put(tour2.get(i), tour1.get(i));
        }
    }

    private void changeElement(List<Integer> tour1, int startIndex, int endIndex, List<Integer> sub2, Map<Integer, Integer> crossMap) {
        for (int i = 0, size = tour1.size(); i < size; i++) {

            if (i >= startIndex && i <= endIndex) {
//                tour1.set(i, sub2.get(i - startIndex));
            } else {
                Integer city = tour1.get(i);
                if (crossMap.containsKey(city)) {
                    Integer toBeReplace = crossMap.get(city);
                    while (crossMap.containsKey(toBeReplace)) {
                        toBeReplace = crossMap.get(toBeReplace);
                    }

                    tour1.set(i, toBeReplace);
                }
            }
        }
    }

    void cross(List<Integer> tour1, List<Integer> tour2, int startIndex, int endIndex) {

        List<Integer> sub1 = tour1.subList(startIndex, endIndex + 1);
        List<Integer> sub2 = tour2.subList(startIndex, endIndex + 1);

        // save to map
        createMap(sub1, sub2);
        changeElement(tour1, startIndex, endIndex, sub2, crossMap);

        createMap(sub2, sub1);
        changeElement(tour2, startIndex, endIndex, sub1, crossMap);

        for (int i = startIndex; i <= endIndex; i++) {
            Integer temp = tour1.get(i);
            tour1.set(i,tour2.get(i));
            tour2.set(i,temp);
        }
    }


    @Override
    public void update(Ant[] source, Ant[] target) {
        this.update(source);
    }
}
