package kaihg.nchu.tsp.transition;

import kaihg.nchu.tsp.vo.Ant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PMCrossoverTest {

    private PMCrossover transition;
    private int[][] ants;

    @Before
    public void setUp() throws Exception {
        transition = new PMCrossover(0);

        int[] ant1 = {
                1,2,3,4,5,6,7,8
        };

        int[] ant2 = {
                3,7,5,1,6,8,2,4
        };

        ants = new int[][]{ant1, ant2};
    }

    @Test
    public void testCrossover() {
        transition.cross(ants[0], ants[1], 3, 5);

        Assert.assertArrayEquals(new int[]{4, 2, 3, 1, 6, 8, 7, 5}, ants[0]);

        Assert.assertArrayEquals(new int[]{3, 7, 8, 4, 5, 6, 2, 1}, ants[1]);

    }
}
