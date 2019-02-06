package home.stanislavpoliakov.meet15_practice.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import home.stanislavpoliakov.meet15_practice.R;
import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import home.stanislavpoliakov.meet15_practice.presentation.ViewContract;
import home.stanislavpoliakov.meet15_practice.presentation.presenter.BriefData;

public class ViewActivity extends AppCompatActivity implements ViewContract, Callback {
    private static final String TAG = "meet15_logs";
    private TextView weatherLabel;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private Spinner spinner;
    private List<BriefData> data;
    private DomainContract.Presenter mPresenter;
    private static Handler mHandler;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);
        Message message = Message.obtain(null, 1, this);
        mHandler.sendMessage(message);
    }

    @Override
    public void bindImplementations(DomainContract.Presenter presenter,
                                    DomainContract.UseCase useCaseInteractor,
                                    DomainContract.NetworkOperations networkGateway,
                                    DomainContract.DatabaseOperations databaseGateway) {
        this.mPresenter = presenter;
        mPresenter.bindImplementations(useCaseInteractor, networkGateway, databaseGateway);
        initUIViews();

    }

    @Override
    public void setLabel(String label) {
        weatherLabel.setText(label);
    }

    @Override
    public void setUserChoice(List<String> cities) {

    }

    @Override
    public void displayBrief(List<BriefData> wBriefData) {
        runOnUiThread(() -> {
            if (mAdapter == null) initRecyclerView(wBriefData);
            else updateRecyclerView(wBriefData);
        });
    }

    @Override
    public void showDetails(Bundle detailInfo) {
        runOnUiThread(() -> {
            DetailFragment fragment = DetailFragment.newInstance();
            fragment.setArguments(detailInfo);
            fragmentManager.beginTransaction()
                    .add(fragment, "Detail")
                    .commitNow();
        });
    }

    @Override
    public void initUIViews() {
        initLabel();
        //initRecyclerView();
        initSpinner();

    }

    private void initLabel() {
        weatherLabel = findViewById(R.id.weatherLabel);
    }

    private void initRecyclerView(List<BriefData> wBriefData) {
        this.data = wBriefData;
        recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new MyAdapter(this, data);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void updateRecyclerView(List<BriefData> newData) {
        mAdapter.onNewData(newData);
    }

    private void initSpinner() {
        spinner = findViewById(R.id.spinner);
        ArrayList<String> cities = this.getIntent().getStringArrayListExtra("cities");
        //Log.d(TAG, "initSpinner: cities.size = " + cities.size());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerAdapter.getPosition("Москва")); // по умолчанию "Москва"

        // Если выбрали другой город - начинаем загрузку данных
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cityName = (String) spinner.getSelectedItem();
                mPresenter.onSpinnerSelected(cityName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void viewHolderClicked(int itemPosition) {
        mPresenter.onViewHolderSelected(itemPosition);
    }

    public static Intent newIntent(Context context, Handler handler) {
        mHandler = handler;
        return new Intent(context, ViewActivity.class);
    }
}
