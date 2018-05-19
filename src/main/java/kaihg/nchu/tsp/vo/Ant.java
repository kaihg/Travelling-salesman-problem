package kaihg.nchu.tsp.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant {

    private List<Integer> tour;

    private double pheroRate;
    private double distRate;
    /**
     * 直接挑最大值，不用輪盤挑下一個了
     */
    private double biasedRate;
    private Random random;

    private double tourDistance;


    public Ant() {
        tour = new ArrayList<>();
        random = new Random();
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
        tour.clear();
        this.tourDistance = 0;
    }

    public void moveToCity(int num) {
        this.tour.add(num);
    }

    public void startTour(double[][] pheroTable, double[][] distTable, double[] probAry) {
        int lastCity = this.tour.get(0);
        // 起始點已設定，所以要扣掉一個
        for (int i = 1; i < distTable.length; i++) {
            int nextCity = findNextCity(pheroTable, distTable, probAry);
            this.tour.add(nextCity);
            this.tourDistance += distTable[lastCity][nextCity];
            lastCity = nextCity;
        }
        // 要回到起點，所以要加最後一段
        this.tourDistance += distTable[lastCity][this.tour.get(0)];
    }

    public double getTourDistance() {
        return tourDistance;
    }

    public List<Integer> getTour() {
        return this.tour;
    }

    private int getLocalMaxPhero(double[][] pheroTable,int currentIndex){
        int cities = pheroTable.length;
        int maxIndex = 0;
        double maxPhero = pheroTable[currentIndex][0];
        for (int i = 1; i < cities; i++) {
            if (pheroTable[currentIndex][i] > maxPhero && !this.tour.contains(i)) {
                maxPhero = pheroTable[currentIndex][i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    int findNextCity(double[][] pheroTable, double[][] distTable, double[] probAry) {
        int cities = distTable.length;
        int currentIndex = this.tour.get(tour.size() - 1);

        // 某機率下會直接使用最大值，減少計算量
        if (random.nextDouble() < biasedRate) {
            return getLocalMaxPhero(pheroTable,currentIndex);
        }


        double total = 0;
        for (int i = 0; i < cities; i++) {
            double prob = getProbability(pheroTable, distTable, currentIndex, i);
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
        if (this.tour.contains(city)) {
            return 0;
        } else {
            return Math.pow(pheroTable[currentIndex][city], pheroRate) * Math.pow(1 / distTable[currentIndex][city], distRate);
        }
    }
}
