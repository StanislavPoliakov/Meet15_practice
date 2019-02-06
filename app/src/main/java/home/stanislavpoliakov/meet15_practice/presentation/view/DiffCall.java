package home.stanislavpoliakov.meet15_practice.presentation.view;

import android.support.v7.util.DiffUtil;

import java.util.List;

import home.stanislavpoliakov.meet15_practice.domain.response_data.WDailyData;
import home.stanislavpoliakov.meet15_practice.presentation.presenter.BriefData;

public class DiffCall extends DiffUtil.Callback {
    private List<BriefData> oldData, newData;

    public DiffCall(List<BriefData> oldData, List<BriefData> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
