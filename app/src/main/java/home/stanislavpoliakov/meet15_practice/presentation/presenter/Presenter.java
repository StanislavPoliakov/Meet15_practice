package home.stanislavpoliakov.meet15_practice.presentation.presenter;

import java.util.List;
import java.util.Map;


import home.stanislavpoliakov.meet15_practice.domain.BriefData;
import home.stanislavpoliakov.meet15_practice.domain.DetailData;
import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import home.stanislavpoliakov.meet15_practice.presentation.ViewContract;


public class Presenter implements DomainContract.Presenter{
    private ViewContract mView;
    private DomainContract.UserInput userInput;
    private Map<String, String> cities;

    public Presenter(ViewContract view) {
        this.mView = view;
        init();
    }

    private void init() {
        cities.put("Москва", "55.7522200, 37.6155600");
        cities.put("Владивосток", "43.1056200, 131.8735300");
        cities.put("Бангкок", "13.7539800, 100.5014400");
        cities.put("Бали", "22.6485900, 88.3411500");
        cities.put("Дубай", "25.0657000, 55.1712800");
        cities.put("Санта-Крус-де-Тенерифе", "28.4682400, -16.2546200");
        cities.put("Нью-Йорк", "40.7142700, -74.0059700");
    }

    @Override
    public void displayBriefData(List<BriefData> briefData) {

    }

    @Override
    public void displayDetailsData(DetailData detailData) {

    }

    @Override
    public void onSpinnerSelected(String cityName) {

    }

    @Override
    public void onViewHolderSelected(int itemPosition) {

    }
}
