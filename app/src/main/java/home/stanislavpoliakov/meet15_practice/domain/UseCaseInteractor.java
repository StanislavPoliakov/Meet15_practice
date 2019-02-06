package home.stanislavpoliakov.meet15_practice.domain;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import home.stanislavpoliakov.meet15_practice.data.network.NetworkGateway;

public class UseCaseInteractor implements DomainContract.UseCase {
    private static final String TAG = "meet15_logs";
    private DomainContract.NetworkOperations networkGateway;
    private DomainContract.DatabaseOperations databaseGateway;
    private WorkThread workThread = new WorkThread();
    private DomainContract.Presenter presenter;

    public UseCaseInteractor() {
        workThread.start();
    }

    @Override
    public void onCitySelected(String cityLocation) {
        //Weather weather = networkGateway.fetchData(cityLocation);
        startWorkFlow(cityLocation);
    }

    //Последовательно!!!
    private void startWorkFlow(String cityLocation) {
        workThread.fetchWeather(cityLocation);
        //workThread.retieveAndUpdate();
    }

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

                        // Сообщение с просьбой начать загрузку данных из внешнего источника через
                        // Service.
                        case FETCH_WEATHER_DATA:
                            String location = (String) msg.obj;
                            weather = getWeatherFromNetwork(location);
                            Log.d(TAG, "handleMessage: from network timezone = " + weather.timezone);
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
                        // Получаем только часть данных, необходимые для работы (посуточный прогноз).
                        // Также получаем timeZone для коррекции даты и времени в зависимости от
                        // выбранного города
                        case RETRIEVE_INFO:
                            /*weather = dao.getWeather();
                            data = weather.daily.data;
                            timeZone = weather.timezone;
                            updateRecycler(timeZone);*/
                            weather = databaseGateway.loadData();
                            Log.d(TAG, "handleMessage: RETRIVE timezone = " + weather.timezone);
                            presenter.show(weather);
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
            //mHandler.sendEmptyMessage(FETCH_WEATHER_DATA);
        }

        /**
         * Внутренний метод класса для получения данных
         * @return объект данных из Интернета
         */
        private Weather getWeatherFromNetwork(String cityLocation) {
            //return networkService.getWeatherFromNetwork(cityLocation);
            return networkGateway.fetchData(cityLocation);
        }

        /**
         * Внутренний метод класса для сохранения данных в базе.
         * Если записей нет - создаем, если есть - обновляем
         * @param weather данные, которые необходимо сохранить
         */
        private void saveWeatherData(Weather weather) {
            /*Weather currentWeather = dao.getWeather();
            if (currentWeather == null) dao.insert(weather);
            else {
                weather.id = currentWeather.id;
                dao.update(weather);
            }*/
            databaseGateway.saveData(weather);
            Log.d(TAG, "saveWeatherData: timezone = " + weather.timezone);
        }

        public void retieveAndUpdate() {
            mHandler.sendEmptyMessage(RETRIEVE_INFO);
        }
    }


    @Override
    public void bindNetworkGateway(DomainContract.NetworkOperations networkGateway) {
        this.networkGateway = networkGateway;
    }

    @Override
    public void bindImplementations(DomainContract.Presenter presenter,
                                    DomainContract.NetworkOperations networkGateway,
                                    DomainContract.DatabaseOperations databaseGateway) {
        this.presenter = presenter;
        this.networkGateway = networkGateway;
        this.databaseGateway = databaseGateway;
        Log.d(TAG, "bindImplementations: ");
        
    }

}
