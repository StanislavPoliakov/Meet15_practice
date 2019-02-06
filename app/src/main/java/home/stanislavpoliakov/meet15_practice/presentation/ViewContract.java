package home.stanislavpoliakov.meet15_practice.presentation;

import java.util.List;

import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import home.stanislavpoliakov.meet15_practice.presentation.presenter.Briefed;
import home.stanislavpoliakov.meet15_practice.presentation.presenter.Detailed;

public interface ViewContract {

    void setLabel(String label);

    void setUserChoice(List<String> cities);

    void displayBrief(List<Briefed> briefed);

    void showDetails(Detailed detailed);

    void bindPresenter(DomainContract.Presenter presenter);
}
