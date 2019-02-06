package home.stanislavpoliakov.meet15_practice.data.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import home.stanislavpoliakov.meet15_practice.domain.Weather;

public class DatabaseGateway implements DomainContract.DatabaseOperations {
    private Context context;
    private WeatherDAO dao;

    public DatabaseGateway(Context context) {
        this.context = context;
        dao = init();
    }

    @Override
    public void saveData(Weather weather) {
        Weather currentWeather = dao.getWeather();
        if (currentWeather == null) dao.insert(weather);
        else {
            weather.id = currentWeather.id;
            dao.update(weather);
        }
    }

    @Override
    public Weather loadData() {
        return dao.getWeather();
    }

    private WeatherDAO init() {
        WeatherDatabase database = Room.databaseBuilder(context, WeatherDatabase.class, "weather")
                .fallbackToDestructiveMigration()
                .build();
        return database.getWeatherDAO();
    }
}
