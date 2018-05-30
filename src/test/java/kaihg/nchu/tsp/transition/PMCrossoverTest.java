package kaihg.nchu.tsp.transition;

import kaihg.nchu.tsp.vo.Ant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PMCrossoverTest {

    private PMCrossover transition;
    private Ant[] ants;

    @Before
    public void setUp() throws Exception {
        transition = new PMCrossover(0);

        Ant ant1 = new Ant();
        ant1.moveToCity(1);
        ant1.moveToCity(2);
        ant1.moveToCity(3);
        ant1.moveToCity(4);
        ant1.moveToCity(5);
        ant1.moveToCity(6);
        ant1.moveToCity(7);
        ant1.moveToCity(8);


        Ant ant2 = new Ant();
        ant2.moveToCity(3);
        ant2.moveToCity(7);
        ant2.moveToCity(5);
        ant2.moveToCity(1);
        ant2.moveToCity(6);
        ant2.moveToCity(8);
        ant2.moveToCity(2);
        ant2.moveToCity(4);

        ants = new Ant[]{ant1, ant2};
    }

    @Test
    public void testCrossover() {
        transition.cross(ants[0].getTour(), ants[1].getTour(), 3, 5);

        Assert.assertArrayEquals(new Integer[]{4, 2, 3, 1, 6, 8, 7, 5}, ants[0].getTour().toArray());

        Assert.assertArrayEquals(new Integer[]{3, 7, 8, 4, 5, 6, 2, 1}, ants[1].getTour().toArray());

    }
}
