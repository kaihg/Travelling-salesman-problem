package kaihg.nchu.tsp.transition;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class CycleCrossoverTest {

    ITransition<int[][]> transition;

    @Before
    public void setUp() throws Exception {
        transition = new CycleCrossover(1, 0, 8);

    }

    @Test
    public void testUpdate() {
        int[] tour1 = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] tour2 = {2, 4, 6, 8, 7, 5, 3, 1};

        int[][] target = new int[2][8];

        transition.update(new int[][]{tour1, tour2}, target);

        Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, tour1);
        Assert.assertArrayEquals(new int[]{1, 2, 6, 4, 7, 5, 3, 8}, target[0]);

        Assert.assertArrayEquals(new int[]{2, 4, 6, 8, 7, 5, 3, 1}, tour2);
        Assert.assertArrayEquals(new int[]{2, 4, 3, 8, 5, 6, 7, 1}, target[1]);
    }

    @Test
    public void randomTest() {
        int seed = 1234567;
        Random random = new Random(seed);
        int[] ints = random.ints().limit(20).toArray();

        random.setSeed(seed);
        int[] int2 = random.ints().limit(20).toArray();

        random = new Random(seed);
        int[] int3 = random.ints().limit(20).toArray();

        Assert.assertArrayEquals(ints, int2);
        Assert.assertArrayEquals(int3, int2);
    }

    @Test
    public void testUpdate2() {
        int[] tour1 = {0, 1, 2, 3, 4, 5, 6, 7};
        int[] tour2 = {7, 6, 5, 4, 3, 2, 1, 0};

        int[][] target = new int[2][8];

        transition.update(new int[][]{tour1, tour2}, target);

        Assert.assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7}, tour1);
        Assert.assertArrayEquals(new int[]{0, 6, 2, 4, 3, 5, 1, 7}, target[0]);

        Assert.assertArrayEquals(new int[]{7, 6, 5, 4, 3, 2, 1, 0}, tour2);
        Assert.assertArrayEquals(new int[]{7, 1, 5, 3, 4, 2, 6, 0}, target[1]);
    }
}
