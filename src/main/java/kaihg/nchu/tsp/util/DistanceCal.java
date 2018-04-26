package kaihg.nchu.tsp.util;

import kaihg.nchu.tsp.vo.City;

public class DistanceCal {

    public static double getDistance(City c1, City c2) {
        double dist = Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2);
        return Math.sqrt(dist);
    }
}
