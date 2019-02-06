package home.stanislavpoliakov.meet15_practice.data.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import home.stanislavpoliakov.meet15_practice.domain.Weather;

/**
 * Реализация интерфейса Database.
 */
public class DatabaseGateway implements DomainContract.DatabaseOperations {
    private Context context;
    private WeatherDAO dao;

    /**
     * Получаем вызывающий контекст в конструкторе. Это для инициализации из main
     * @param context контекст приложения
     */
    public DatabaseGateway(Context context) {
        this.context = context;
        dao = init();
    }

    /**
     * Метод сохранения данных в базе.
     * Если запись в базе не существует - создаем, если существует - обновляем
     * @param weather - данные, которые необходимо сохранить
     */
    @Override
    public void saveData(Weather weather) {
        Weather currentWeather = dao.getWeather();
        if (currentWeather == null) dao.insert(weather);
        else {
            weather.id = currentWeather.id;
            dao.update(weather);
        }
    }

    /**
     * Метод загрузки данных из базы
     * @return сущность данных
     */
    @Override
    public Weather loadData() {
        return dao.getWeather();
    }

    /**
     * Метод инициализации базы (запускаем через инициализацию в main)
     * @return Data Access Object для работы с базой
     */
    private WeatherDAO init() {
        WeatherDatabase database = Room.databaseBuilder(context, WeatherDatabase.class, "weather")
                .fallbackToDestructiveMigration()
                .build();
        return database.getWeatherDAO();
    }
}
