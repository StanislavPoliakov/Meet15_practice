package home.stanislavpoliakov.meet15_practice.domain;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

public class UseCaseInteractor implements DomainContract.UseCase {
    private static final String TAG = "meet15_logs";
    private DomainContract.Presenter presenter;
    private DomainContract.NetworkOperations networkGateway;
    private DomainContract.DatabaseOperations databaseGateway;
    private WorkThread workThread = new WorkThread();

    /**
     * В конструкторе запускаем в работу workThread (из main)
     */
    public UseCaseInteractor() {
        workThread.start();
    }

    /**
     * Callback из Presenter.
     * Запускаем начало работы, далее workThread сам инициализирует отрисовку данных (по готовности)
     * @param cityLocation координаты города в формате String
     */
    @Override
    public void onCitySelected(String cityLocation) {
        workThread.fetchWeather(cityLocation);
    }

    /**
     * Work Thread
     */
    private class WorkThread extends HandlerThread {
        private static final int FETCH_WEATHER_DATA = 1;
        private static final int SAVE_WEATHER_DATA = 2;
        private static final int RETRIEVE_INFO = 3;

        // Важно отметить, что Handler мы специально делаем приватным, чтобы до него нельзя было
        // дотянуться (если перенести в отдельный класс реализацию). То есть Handler только для
        // внутренних задач.
        private Handler mHandler;

        /**
         * В конструкторе понижаем приоритет потока до фоновой задачи
         */
        WorkThread() {
            super("WorkThread", Process.THREAD_PRIORITY_BACKGROUND);
        }

        /**
         * Определяем Handler в методе подготовки Looper'-а
         */
        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler = new Handler(getLooper()) {

                /**
                 * Переопределяем поведение при приеме сообщений
                 * @param msg сообщение
                 */
                @Override
                public void handleMessage(Message msg) {
                    Weather weather;
                    Message message;

                    switch (msg.what) {

                        // Сообщение с просьбой начать загрузку данных из внешнего источника
                        case FETCH_WEATHER_DATA:
                            String location = (String) msg.obj;
                            weather = getWeatherFromNetwork(location);
                            message = mHandler.obtainMessage(SAVE_WEATHER_DATA, weather);
                            mHandler.sendMessage(message);
                            break;

                        // После получения данных из Интернета и успешного парсинга сохраняем
                        // данные в базе
                        case SAVE_WEATHER_DATA:
                            weather = (Weather) msg.obj;
                            saveWeatherData(weather);
                            mHandler.sendEmptyMessage(RETRIEVE_INFO);
                            break;

                        // После сохранения в базе данных - получаем содержимое базы, для работы.
                        case RETRIEVE_INFO:
                            weather = databaseGateway.loadData();
                            presenter.show(weather); // Запускаем отрисовку
                            break;
                    }
                }
            };
        }

        /**
         * Метод для начала цепочки действий
         */
        void fetchWeather(String cityLocation) {
            Message message = Message.obtain(null, FETCH_WEATHER_DATA, cityLocation);
            mHandler.sendMessage(message);
        }

        /**
         * Внутренний метод класса для получения данных
         * @return объект данных из Интернета
         */
        private Weather getWeatherFromNetwork(String cityLocation) {
            return networkGateway.fetchData(cityLocation);
        }

        /**
         * Внутренний метод класса для сохранения данных в базе.
         * Если записей нет - создаем, если есть - обновляем
         * @param weather данные, которые необходимо сохранить
         */
        private void saveWeatherData(Weather weather) {
            databaseGateway.saveData(weather);
        }
    }

    @Override
    public void bindImplementations(DomainContract.Presenter presenter,
                                    DomainContract.NetworkOperations networkGateway,
                                    DomainContract.DatabaseOperations databaseGateway) {
        this.presenter = presenter;
        this.networkGateway = networkGateway;
        this.databaseGateway = databaseGateway;
    }
}
