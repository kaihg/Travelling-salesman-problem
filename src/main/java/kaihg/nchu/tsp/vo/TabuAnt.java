package kaihg.nchu.tsp.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabuAnt extends Ant{

    private int tabuLimit = 3;  // magic number
    private List<List<TabuCity>> tabuCities;
//    private List<>
    private TabuCity tempTabu;


    public TabuAnt(int citySize) {
        super(citySize);

        tempTabu = new TabuCity();
        this.tabuCities = new ArrayList<>(citySize);
        for (int i = 0; i < citySize; i++) {
            List<TabuCity> list = new ArrayList<>();
            tabuCities.add(list);
        }
    }

    @Override
    public void onInit() {
        super.onInit();
        for (List<TabuCity> list : tabuCities){
            list.clear();
        }
    }

    public void startTour(double[][] pheroTable, double[][] distTable, double[][] probAry) {
        super.startTour(pheroTable,distTable,probAry);
        // 排序list，讓數量最多的
    }

    protected boolean canArrival(int current, int want) {
        return !isCityArrival(want) && inTabuList(current,want);
    }

    private boolean inTabuList(int current, int want){
        List<TabuCity> tabuList = tabuCities.get(current);
        tempTabu.city = want;
        int index = tabuList.indexOf(tempTabu);
        if (index != -1){
            TabuCity city = tabuList.get(index);
            city.arrivledTimes ++;
            return true;
        }else{
            TabuCity city = new TabuCity();
            city.city = want;
            city.arrivledTimes = 1;
            tabuList.add(city);
            if(tabuList.size()>tabuLimit){
                tabuList.remove(0);
            }
            return false;
        }
//        for (int i = 0, size = tabuList.size(); i < tabuLimit && i < size; i++) {
//
//        }
    }

    class TabuCity{
        int city;
        int arrivledTimes;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TabuCity tabuCity = (TabuCity) o;
            return city == tabuCity.city;
        }

        @Override
        public int hashCode() {

            return city;
        }
    }
}
