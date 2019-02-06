package home.stanislavpoliakov.meet15_practice.presentation.view;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.ZoneId;

import home.stanislavpoliakov.meet15_practice.R;



/**
 * Класс фрагмента детальной информации
 */
public class DetailFragment extends DialogFragment {
    private static final String TAG = "meet13_logs";

    /**
     * Получаем объект класса в статическом методе для FragmentManager
     *
     * @return
     */
    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    /**
     * Растягиваем фрагмент по ширине на размер экрана, по высоте - на размер переменных
     */
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //this.view = view;
        initViews(view);
    }


    /**
     * Bundle с подробной информацией о погоде, переданный в качестве аргументов фрагмента
     */
    private void initViews(View view) {
        Bundle args = getArguments();

        // Элемент текущей даты
        TextView timeView = view.findViewById(R.id.timeView);
        String time = args.getString("time");
        timeView.setText(time);

        // Элемент краткой сводки (на английском)
        TextView summaryView = view.findViewById(R.id.summaryView);
        String summary = args.getString("summary");
        summaryView.setText(summary);

        // Время восхода солнца
        TextView sunriseView = view.findViewById(R.id.sunriseView);
        String sunriseTime = args.getString("sunriseTime");
        sunriseView.setText(sunriseTime);

        // Время заката солнца
        TextView sunsetView = view.findViewById(R.id.sunsetView);
        String sunsetTime = args.getString("sunsetTime");
        sunsetView.setText(sunsetTime);

        // Тип осадков
        TextView precipTypeView = view.findViewById(R.id.precipType);
        String precipType = args.getString("precipType");
        precipTypeView.setText(precipType);

        // Вероятность осадков и их интенсивность
        TextView precip = view.findViewById(R.id.precip);
        String preicpInfo = args.getString("precipInfo");
        precip.setText(preicpInfo);

        // В какое время максимальное количество осадков
        TextView precipMax = view.findViewById(R.id.precipMax);
        String precipMaxInfo = args.getString("precipInfoMax");
        precipMax.setText(precipMaxInfo);

        // Влажность воздуха и температура точки росы
        TextView humDew = view.findViewById(R.id.humDew);
        String humDewInfo = args.getString("humDew");
        humDew.setText(humDewInfo);

        // Давление
        TextView pressure = view.findViewById(R.id.pressure);
        String pressureInfo = args.getString("pressure");
        pressure.setText(pressureInfo);

        // Направление ветра, скорость и скорость в порывах
        TextView wind = view.findViewById(R.id.wind);
        String windInfo = args.getString("windInfo");
        wind.setText(windInfo);

        // Облачность
        TextView cloudy = view.findViewById(R.id.cloudy);
        String cloudInfo = args.getString("cloudCover");
        cloudy.setText(cloudInfo);

        // Ультрафиолетовый индекс
        TextView uvIndex = view.findViewById(R.id.uvIndex);
        String uvIndexInfo = args.getString("uvIndex");
        uvIndex.setText(uvIndexInfo);

        // Максимальная температура и время пика
        TextView tempMax = view.findViewById(R.id.tempMax);
        String tempMaxInfo = args.getString("tempMaxInfo");
        tempMax.setText(tempMaxInfo);

        // Минимальная температура и время пика
        TextView tempMin = view.findViewById(R.id.tempMin);
        String tempMinInfo = args.getString("tempMinInfo");
        tempMin.setText(tempMinInfo);
    }
}