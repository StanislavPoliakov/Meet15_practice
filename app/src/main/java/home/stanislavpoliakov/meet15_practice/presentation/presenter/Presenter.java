package home.stanislavpoliakov.meet15_practice.presentation.presenter;

import android.os.Bundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import home.stanislavpoliakov.meet15_practice.domain.Weather;
import home.stanislavpoliakov.meet15_practice.presentation.ViewContract;
import static home.stanislavpoliakov.meet15_practice.presentation.presenter.Convert.*;


public class Presenter implements DomainContract.Presenter{
    private static final String TAG = "meet15_logs";
    private ViewContract mView;
    private DomainContract.UseCase useCaseInteractor; // Interactor
    private Map<String, String> cities = new HashMap<>();
    private String timeZone;
    private List<Bundle> details;

    public Presenter(ViewContract view) {
        this.mView = view;
        init();
    }

    /**
     * Инициализируем значения здесь (хотя можно было бы и окно с настройками сделать)
     */
    private void init() {
        cities.put("Москва", "55.7522200, 37.6155600");
        cities.put("Владивосток", "43.1056200, 131.8735300");
        cities.put("Бангкок", "13.7539800, 100.5014400");
        cities.put("Бали", "22.6485900, 88.3411500");
        cities.put("Дубай", "25.0657000, 55.1712800");
        cities.put("Санта-Крус-де-Тенерифе", "28.4682400, -16.2546200");
        cities.put("Нью-Йорк", "40.7142700, -74.0059700");
    }

    /**
     * Конвертируем данные в данные для отображения
     * @param weather данные из базы
     * @return данные для отображения
     */
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

    /**
     * Для детальной информации собираем список бандлов.
     * @param weather данные из базы
     * @return List<Bundle>
     */
    private List<Bundle> getDetails(Weather weather) {
        return Stream.of(weather.daily.data)
                .map(data -> {
                    setZoneId(timeZone);
                    Bundle detailInfo = new Bundle();
                    detailInfo.putString("time", toFormattedZoneDate(data.time));
                    detailInfo.putString("summary", data.summary);
                    detailInfo.putString("sunriseTime", toFormattedZoneTime(data.sunriseTime));
                    detailInfo.putString("sunsetTime", toFormattedZoneTime(data.sunsetTime));
                    detailInfo.putString("precipInfo", toPrecipInfo(data.precipProbability, data.precipIntensity));
                    detailInfo.putString("precipInfoMax", toPrecipMax(data.precipIntensityMax, data.precipIntensityMaxTime));
                    detailInfo.putString("precipType", "Тип осадков: " + data.precipType);
                    detailInfo.putString("humdew", toHumidyAndDewPoint(data.humidity, data.dewPoint));
                    detailInfo.putString("pressure", "Давление: " + toMercury(data.pressure));
                    detailInfo.putString("windInfo", toWindInfo(data.windBearing, data.windSpeed, data.windGust));
                    detailInfo.putString("cloudCover", "Облачность: " + toPercent(data.cloudCover));
                    detailInfo.putString("uvIndex", "Ультрафиолетовый индекс: " + data.uvIndex);
                    detailInfo.putString("tempMinInfo", toTempMinInfo(data.temperatureMin, data.temperatureMinTime));
                    detailInfo.putString("tempMaxInfo", toTempMaxInfo(data.temperatureMax, data.temperatureMaxTime));
                    //detailInfo.putString("timeZone", timeZone);
                    return detailInfo;
                }).collect(Collectors.toList());
    }

    /**
     * Метод отображения загруженных из базы данных
     * Подготавливаем данные, устанавливаем Label и запускаем методы отображения
     * @param weather данные
     */
    @Override
    public void show(Weather weather) {
        timeZone = weather.timezone;
        mView.setLabel(timeZone);
        displayBriefData(getBriefData(weather));
        details = getDetails(weather);

    }

    /**
     * Метод отображения данных в RecyclerView
     * @param briefData сокращенные данные
     */
    private void displayBriefData(List<BriefData> briefData) {
        mView.displayBrief(briefData);
    }

    /**
     * Метод отображения детальной информации в отдельном фрагменте
     * @param detailInfo детальная информация по выбранному дню
     */
    private void displayDetails(Bundle detailInfo) {
        mView.showDetails(detailInfo);
    }

    /**
     * Callback из View.
     * Получаем название города, сопоставляем с координатами и отправляем в UseCase для обработки
     * @param cityName название города
     */
    @Override
    public void onSpinnerSelected(String cityName) {
        String cityLocation = cities.get(cityName);
        useCaseInteractor.onCitySelected(cityLocation);
    }

    /**
     * Callback из View.
     * Обрабатываем нажатие на ViewHolder
     * @param itemPosition позиция в списке отображения
     */
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
