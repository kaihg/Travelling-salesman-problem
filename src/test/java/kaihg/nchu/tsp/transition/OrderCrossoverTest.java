package kaihg.nchu.tsp.transition;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderCrossoverTest {

    private OrderCrossover crossover;

    @Before
    public void setUp() throws Exception {
        crossover = new OrderCrossover(1, 0);
    }

    @Test
    public void testCross() {
        int[][] data = {
                {8, 4, 7, 3, 6, 2, 5, 1, 9, 0},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
        };

        int[][] child = new int[2][10];

        crossover.crossover(data[0],data[1],3,8,child[0],child[1]);

//        System.out.println(Arrays.toString(child[0]));
//        System.out.println(Arrays.toString(child[1]));

        Assert.assertArrayEquals(new int[]{8, 4, 7, 3, 6, 2, 5, 1, 9, 0}, data[0]);
        Assert.assertArrayEquals(new int[]{0, 4, 7, 3, 6, 2, 5, 1, 8, 9}, child[0]);

        Assert.assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, data[1]);
        Assert.assertArrayEquals(new int[]{8, 2,1, 3, 4, 5, 6, 7,9,0}, child[1]);
    }
}
