package home.stanislavpoliakov.meet15_practice.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import home.stanislavpoliakov.meet15_practice.domain.Weather;

@Database(entities = Weather.class, version = 3)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDAO getWeatherDAO();
}
