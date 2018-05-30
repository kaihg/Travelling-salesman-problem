package kaihg.nchu.tsp.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Ant {

    private int pointer = 0;
    private int[] tour;

    private double pheroRate;
    private double distRate;
    /**
     * 直接挑最大值，不用輪盤挑下一個了
     */
    private double biasedRate;
    private Random random;

    private double tourDistance;


    public Ant(int citySize) {
        tour = new int[citySize];
    }

    public void setRandom(Random random){
        this.random = random;
    }

    public void setPheroRate(double pheroRate) {
        this.pheroRate = pheroRate;
    }

    public void setDistRate(double distRate) {
        this.distRate = distRate;
    }

    public void setBiasdRate(double rate) {
        this.biasedRate = rate;
    }

    public void resetTour() {
        Arrays.fill(this.tour, -1);

        this.pointer = -1;
        this.tourDistance = 0;
    }

    public void moveToCity(int num) {
//        this.tour.add(num);
        this.tour[++pointer] = num;
    }

    public void startTour(double[][] pheroTable, double[][] distTable, double[] probAry) {
        int lastCity = this.tour[0];
        // 起始點已設定，所以要扣掉一個
        for (int i = 1; i < distTable.length; i++) {
            int nextCity = findNextCity(pheroTable, distTable, probAry);
//            this.tour.add(nextCity);
            moveToCity(nextCity);
            this.tourDistance += distTable[lastCity][nextCity];
            lastCity = nextCity;
        }
        // 要回到起點，所以要加最後一段
        this.tourDistance += distTable[lastCity][this.tour[0]];
    }

    public double getTourDistance() {
        return tourDistance;
    }

    public int[] getTour() {
        return this.tour;
    }

    private int getLocalMaxPhero(double[][] pheroTable, int currentIndex) {
        int cities = pheroTable.length;
        int maxIndex = 0;
        double maxPhero = pheroTable[currentIndex][0];
        for (int i = 1; i < cities; i++) {
            if (pheroTable[currentIndex][i] > maxPhero && !isCityArrival(i)) {
                maxPhero = pheroTable[currentIndex][i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private boolean isCityArrival(int city){
        return Arrays.binarySearch(tour,city) != -1;
    }

    int findNextCity(double[][] pheroTable, double[][] distTable, double[] probAry) {
        int cities = distTable.length;
        int currentCity = this.tour[pointer];

        // 某機率下會直接使用最大值，減少計算量
        if (random.nextDouble() < biasedRate) {
            return getLocalMaxPhero(pheroTable, currentCity);
        }


        double total = 0;
        for (int i = 0; i < cities; i++) {
            double prob = getProbability(pheroTable, distTable, currentCity, i);
            probAry[i] = prob;
            total += prob;
        }

        // 射輪盤
        double probability = new Random().nextDouble() * total;
        for (int i = 0; i < cities; i++) {
            if (probability < probAry[i]) {
                return i;
            } else {
                probability -= probAry[i];
            }
        }

        return cities - 1;
    }

    private double getProbability(double[][] pheroTable, double[][] distTable, int currentIndex, int city) {
        if (isCityArrival(city)) {
            return 0;
        } else {
            return Math.pow(pheroTable[currentIndex][city], pheroRate) * Math.pow(1 / distTable[currentIndex][city], distRate);
        }
    }
}
