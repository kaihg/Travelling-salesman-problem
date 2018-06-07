package kaihg.nchu.tsp.vo;

import java.util.Arrays;
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

    // for tournament
    private int[] tempTour;

    private boolean[] arrivalFlag;

    public Ant(int citySize) {
        tour = new int[citySize];
        tempTour = new int[citySize];
        arrivalFlag = new boolean[citySize];

    }

    public void setRandom(Random random) {
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
        Arrays.fill(arrivalFlag, false);

        this.pointer = -1;
        this.tourDistance = 0;
    }

    public void moveToCity(int num) {
//        this.tour.add(num);
        this.tour[++pointer] = num;
        arrivalFlag[num] = true;
    }

    public void setTour(int[] tour) {
        this.tour = tour;
    }

    public void setTourDistance(double distance) {
        this.tourDistance = distance;
    }

//    public void startTour(double[][] pheroTable, double[][] distTable, double[] probAry) {
//        int lastCity = this.tour[0];
//        // 起始點已設定，所以要扣掉一個
//        for (int i = 1; i < distTable.length; i++) {
//            int nextCity = findNextCity(pheroTable, distTable, probAry);
////            this.tour.add(nextCity);
//            moveToCity(nextCity);
//            this.tourDistance += distTable[lastCity][nextCity];
//            lastCity = nextCity;
//        }
//        // 要回到起點，所以要加最後一段
//        this.tourDistance += distTable[lastCity][this.tour[0]];
//    }

    public void startTour(double[][] pheroTable, double[][] distTable, double[][] probAry) {
        int lastCity = this.tour[0];
        // 起始點已設定，所以要扣掉一個
        for (int i = 1; i < probAry.length; i++) {
            int nextCity = findNextCity(pheroTable, probAry);
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
        double maxPhero = -1;
        for (int i = 0; i < cities; i++) {
            if (pheroTable[currentIndex][i] > maxPhero && !isCityArrival(i)) {
                maxPhero = pheroTable[currentIndex][i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    protected boolean isCityArrival(int city) {
//        for (int i = 0; i <= pointer; i++) {
//            if (tour[i] == city) {
//                return true;
//            }
//        }
//        return false;
        return arrivalFlag[city];
    }

    int findNextCity(double[][] pheroTable, double[][] distTable, double[] probAry) {
//        int cities = distTable.length;
        int currentCity = this.tour[pointer];

        // 某機率下會直接使用最大值，減少計算量
        if (random.nextDouble() < biasedRate) {
            return getLocalMaxPhero(pheroTable, currentCity);
        }

        return tournamentSelect(pheroTable, distTable);
//        return rouletteSelect(pheroTable, distTable, probAry);
    }

    int findNextCity(double[][] pheroTable, double[][] probAry) {
        int cities = probAry.length;
        int currentCity = this.tour[pointer];

        // 某機率下會直接使用最大值，減少計算量
        if (random.nextDouble() < biasedRate) {
            return getLocalMaxPhero(pheroTable, currentCity);
        }

//        return tournamentSelect(probAry);
        return rouletteSelect(pheroTable, probAry);
    }

    private int tournamentSelect(double[][] pheroTable, double[][] distTable) {
        int cities = distTable.length;
        int currentCity = this.tour[pointer];

        // 找出尚未走過的點
        int mark = 0;
        for (int i = 0; i < cities; i++) {
            if (!isCityArrival(i)) {
                tempTour[mark] = i;
                mark++;
            }
        }
//        mark = cities - pointer;

        int retryTimes = (mark - 1);
        // 開始競賽
        int city = tempTour[random.nextInt(mark)];
        double score = getProbability(pheroTable, distTable, currentCity, city);
        for (int i = 0; i < retryTimes; i++) {
            int p2 = tempTour[random.nextInt(mark)];
            double comp = getProbability(pheroTable, distTable, currentCity, p2);
            if (score < comp) {
                city = p2;
                score = comp;
            }
        }

        return city;
    }

    private int tournamentSelect(double[][] probTable) {
        int cities = probTable.length;
        int currentCity = this.tour[pointer];

        // 找出尚未走過的點
        int mark = 0;
        for (int i = 0; i < cities; i++) {
            if (!isCityArrival(i)) {
                tempTour[mark] = i;
                mark++;
            }
        }
//        int mark = cities - pointer;

//        int retryTimes = (mark -1) ;
//        int retryTimes = (mark ) /2;
        int retryTimes = 2;
        // 開始競賽
        int city = tempTour[random.nextInt(mark)];
        double score = probTable[currentCity][city];
        for (int i = 0; i < retryTimes; i++) {
            int p2 = tempTour[random.nextInt(mark)];
            double comp = probTable[currentCity][p2];
            if (score < comp) {
                city = p2;
                score = comp;
            }
        }

        return city;
    }

    private int rouletteSelect(double[][] pheroTable, double[][] distTable, double[] probAry) {
        int cities = distTable.length;
        int currentCity = this.tour[pointer];

        double total = 0;
        for (int i = 0; i < cities; i++) {
            double prob = getProbability(pheroTable, distTable, currentCity, i);
            probAry[i] = prob;
            total += prob;
        }

        // 射輪盤
        double probability = random.nextDouble() * total;
        for (int i = 0; i < cities; i++) {
            if (probability < probAry[i]) {
                return i;
            } else {
                probability -= probAry[i];
            }
        }

//        return cities - 1;
        // 機率太低什麼都沒挑到，挑 phero 最大值
        return getLocalMaxPhero(pheroTable, currentCity);
    }

    protected boolean canArrival(int current, int want) {
        return !isCityArrival(want);
    }

    private int rouletteSelect(double[][] pheroTable, double[][] probAry) {
        int cities = pheroTable.length;
        int currentCity = this.tour[pointer];

        double total = 0;
        int mark = 0;
        for (int i = 0; i < cities; i++) {
            double prob = canArrival(currentCity, i) ?  probAry[currentCity][i] : 0;

            if (prob!=0){
                total += prob;
                tempTour[mark] = i;
                mark++;
            }
        }

        // 射輪盤
        double probability = random.nextDouble() * total;
        for (int i = 0; i < mark; i++) {
            int city = tempTour[i];
            double prob =  probAry[currentCity][city];
            if (probability < prob) {
                return city;
            } else {
                probability -= prob;
            }
        }
//        for (int i = 0; i < cities; i++) {
//            double prob = isCityArrival(i) ? 0 : probAry[currentCity][i];
//            if (probability < prob) {
//                return i;
//            } else {
//                probability -= prob;
//            }
//        }

//        return cities - 1;
        // 機率太低什麼都沒挑到，挑 phero 最大值
        return getLocalMaxPhero(pheroTable, currentCity);
    }

    private double getProbability(double[][] pheroTable, double[][] distTable, int currentIndex, int city) {
        if (isCityArrival(city)) {
            return 0;
        } else {
            double probability = Math.pow(pheroTable[currentIndex][city], pheroRate) * Math.pow(1 / distTable[currentIndex][city], distRate);
            if (probability == 0) {
                return Double.MIN_VALUE;
            } else {
                return probability;
            }
        }
    }

}
