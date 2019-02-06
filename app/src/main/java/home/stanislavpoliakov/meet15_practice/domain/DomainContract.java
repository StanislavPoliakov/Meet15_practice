package home.stanislavpoliakov.meet15_practice.domain;

import java.util.List;

public interface DomainContract {


    /**
     * Интерфейс презентора
     */
    interface Presenter {

        /**
         * Метод отображения данных, полученных из базы
         * @param weather данные
         */
        void show(Weather weather);

        /**
         * Callback из View. Обработка выбора города из списка
         * @param cityName название города
         */
        void onSpinnerSelected(String cityName);

        /**
         * Callback из View. Обработка нажатия на конкретный день
         * @param itemPosition позиция в списке отображения
         */
        void onViewHolderSelected(int itemPosition);

        /**
         * Метод привязки реализаций к абстракциям.
         * @param useCaseInteractor реализация вариантов использования
         * @param networkGateway реализация сетевого шлюза
         * @param databaseGateway реализация шлюза базы данных
         */
        void bindImplementations(DomainContract.UseCase useCaseInteractor,
                                 DomainContract.NetworkOperations networkGateway,
                                 DomainContract.DatabaseOperations databaseGateway);
    }


    /**
     * Интерфейс вариантов использования
     */
    interface UseCase {

        /**
         * Callback из Presentor. Метод, запускающий работу после выбора пользователем города,
         * данные о котором необходимо загрузить из сети, сохранить в базе и получить из базы для
         * дальнейшей отрисовки
         * @param cityLocation координаты города в формате String
         */
        void onCitySelected(String cityLocation);


        /**
         * Метод привязки реализаций к абстракциям.
         * @param presenter реализация презентора (для обратного вызова)
         * @param networkGateway реализация сетевого шлюза
         * @param databaseGateway реализация шлюза базы данных
         */
        void bindImplementations(DomainContract.Presenter presenter,
                                 DomainContract.NetworkOperations networkGateway,
                                 DomainContract.DatabaseOperations databaseGateway);
    }


    /**
     * Интерфейс шлюза базы данных
     */
    interface DatabaseOperations {

        /**
         * Метод сохранения данных в базе
         * @param weather - данные, которые необходимо сохранить
         */
        void saveData(Weather weather);

        /**
         * Метод получения данных из базы
         * @return данные
         */
        Weather loadData();
    }

    /**
     * Интерфейс сетевого шлюза
     */
    interface NetworkOperations {

        /**
         * Метод получения данных из сети (API)
         * @param cityLocation координаты города
         * @return данные о погоде
         */
        Weather fetchData(String cityLocation);
    }
}
