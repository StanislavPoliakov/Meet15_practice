package home.stanislavpoliakov.meet15_practice.domain;

import java.util.List;

public interface DomainContract {

    interface Presenter {

        void show(Weather weather);

        /*void displayBriefData(List<BriefData> briefData);

        void displayDetailsData(DetailData detailData);*/

        void onSpinnerSelected(String cityName);

        void onViewHolderSelected(int itemPosition);

        void bindImplementations(DomainContract.UseCase useCaseInteractor,
                                 DomainContract.NetworkOperations networkGateway,
                                 DomainContract.DatabaseOperations databaseGateway);
    }

    interface UseCase {

        void onCitySelected(String cityLocation);

        void bindNetworkGateway(DomainContract.NetworkOperations networkGateway);

        void bindImplementations(DomainContract.Presenter presenter,
                                 DomainContract.NetworkOperations networkGateway,
                                 DomainContract.DatabaseOperations databaseGateway);
    }

    interface DatabaseLoad {

    }

    interface DatabaseOperations {

        void saveData(Weather weather);

        Weather loadData();

    }

    interface NetworkOperations {

        Weather fetchData(String cityLocation);
    }
}
