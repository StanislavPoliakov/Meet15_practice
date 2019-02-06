package home.stanislavpoliakov.meet15_practice;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = Weather.class, version = 3)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDAO getWeatherDAO();
}
