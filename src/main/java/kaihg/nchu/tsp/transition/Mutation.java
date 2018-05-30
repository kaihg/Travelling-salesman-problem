package kaihg.nchu.tsp.transition;

import kaihg.nchu.tsp.vo.Ant;

import java.util.Random;

public class Mutation implements ITransition<Ant[]> {

    private double rate;
    private Random random;

    public Mutation(double rate) {
        this.rate = rate;
        random = new Random();
    }

    @Override
    public void update(Ant[] solution) {
        for (Ant ant : solution) {
            if (random.nextDouble() < rate) {
                int index1 = random.nextInt(ant.getTour().size());
                int index2 = random.nextInt(ant.getTour().size());

                Integer temp = ant.getTour().get(index2);
                ant.getTour().set(index2, ant.getTour().get(index1));
                ant.getTour().set(index1, temp);
            }
        }
    }

    @Override
    public void update(Ant[] source, Ant[] target) {

    }
}
