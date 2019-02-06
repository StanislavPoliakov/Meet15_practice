package home.stanislavpoliakov.meet15_practice.presentation;

import android.os.Bundle;

import java.util.List;

import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import home.stanislavpoliakov.meet15_practice.presentation.presenter.BriefData;

public interface ViewContract {

    void setLabel(String label);

    void setUserChoice(List<String> cities);

    void displayBrief(List<BriefData> briefData);

    void showDetails(Bundle detailInfo);

    void initUIViews();

    void bindImplementations(DomainContract.Presenter presenter,
                             DomainContract.UseCase useCaseInteractor,
                             DomainContract.NetworkOperations networkGateway,
                             DomainContract.DatabaseOperations databaseGateway);
}

