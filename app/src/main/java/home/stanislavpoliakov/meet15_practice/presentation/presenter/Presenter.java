package home.stanislavpoliakov.meet15_practice.presentation.presenter;

import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import home.stanislavpoliakov.meet15_practice.domain.Weather;
import home.stanislavpoliakov.meet15_practice.presentation.ViewContract;


public class Presenter implements DomainContract.Presenter{
    private static final String TAG = "meet15_logs";
    private ViewContract mView;
    private DomainContract.UseCase useCaseInteractor; // Interactor
    private Map<String, String> cities = new HashMap<>();
    private String timeZone;
    private List<BriefData> briefData;
    private List<Bundle> details;

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

    private List<BriefData> getBriefData(Weather weather) {

        return Stream.of(weather.daily.data)
                .map(data -> {
                    BriefData briefData = new BriefData();
                    briefData.setTemperatureMin(data.temperatureMin);
                    briefData.setTemperatureMax(data.temperatureMax);
                    briefData.setTime(data.time);
                    return briefData;
                }).collect(Collectors.toList());
    }

    private List<Bundle> getDetails(Weather weather) {
        return Stream.of(weather.daily.data)
                .map(data -> {
                    Bundle detailInfo = new Bundle();
                    detailInfo.putLong("time", data.time);
                    detailInfo.putString("summary", data.summary);
                    detailInfo.putLong("sunriseTime", data.sunriseTime);
                    detailInfo.putLong("sunsetTime", data.sunsetTime);
                    detailInfo.putDouble("precipIntensity", data.precipIntensity);
                    detailInfo.putDouble("precipProbability", data.precipProbability);
                    detailInfo.putDouble("precipIntensityMax", data.precipIntensityMax);
                    detailInfo.putLong("precipIntensityMaxTime", data.precipIntensityMaxTime);
                    detailInfo.putString("precipType", data.precipType);
                    detailInfo.putDouble("dewPoint", data.dewPoint);
                    detailInfo.putDouble("humidity", data.humidity);
                    detailInfo.putDouble("pressure", data.pressure);
                    detailInfo.putDouble("windSpeed", data.windSpeed);
                    detailInfo.putDouble("windBearing", data.windBearing);
                    detailInfo.putDouble("windGust", data.windGust);
                    detailInfo.putDouble("cloudCover", data.cloudCover);
                    detailInfo.putDouble("uvIndex", data.uvIndex);
                    detailInfo.putDouble("temperatureMin", data.temperatureMin);
                    detailInfo.putLong("temperatureMinTime", data.temperatureMinTime);
                    detailInfo.putDouble("temperatureMax", data.temperatureMax);
                    detailInfo.putLong("temperatureMaxTime", data.temperatureMaxTime);
                    detailInfo.putString("timeZone", timeZone);
                    return detailInfo;
                }).collect(Collectors.toList());
    }

    @Override
    public void show(Weather weather) {
        timeZone = weather.timezone;
        Log.d(TAG, "show: timezone = " + timeZone);
        mView.setLabel(timeZone);
        displayBriefData(getBriefData(weather));
        details = getDetails(weather);

    }


    private void displayBriefData(List<BriefData> briefData) {
        mView.displayBrief(briefData);
    }


    private void displayDetails(Bundle detailInfo) {
        mView.showDetails(detailInfo);
    }

    @Override
    public void onSpinnerSelected(String cityName) {
        String cityLocation = cities.get(cityName);
        Log.d(TAG, "onSpinnerSelected: loc = " + cityName);
        useCaseInteractor.onCitySelected(cityLocation);
    }

    @Override
    public void onViewHolderSelected(int itemPosition) {
        displayDetails(details.get(itemPosition));
    }

    @Override
    public void bindImplementations(DomainContract.UseCase useCaseInteractor,
                                    DomainContract.NetworkOperations networkGateway,
                                    DomainContract.DatabaseOperations databaseGateway) {
        this.useCaseInteractor = useCaseInteractor;
        useCaseInteractor.bindImplementations(this, networkGateway, databaseGateway);
    }
}
