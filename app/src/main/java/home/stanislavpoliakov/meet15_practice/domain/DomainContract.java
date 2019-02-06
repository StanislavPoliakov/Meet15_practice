package home.stanislavpoliakov.meet15_practice.domain;

import java.util.List;

public interface DomainContract {

    interface Presenter {

        void displayBriefData(List<BriefData> briefData);

        void displayDetailsData(DetailData detailData);

        void onSpinnerSelected(String cityName);

        void onViewHolderSelected(int itemPosition);
    }

    interface UserInput {

        void citySelected();

        void fetchDetailData();
    }
}
