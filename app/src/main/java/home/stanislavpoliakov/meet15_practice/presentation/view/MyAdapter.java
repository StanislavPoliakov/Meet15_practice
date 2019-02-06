package home.stanislavpoliakov.meet15_practice.presentation.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import home.stanislavpoliakov.meet15_practice.R;
import home.stanislavpoliakov.meet15_practice.domain.response_data.WDailyData;
import home.stanislavpoliakov.meet15_practice.presentation.presenter.BriefData;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "meet15_logs";
    private List<BriefData> data;
    private Callback mActivity;

    public MyAdapter(Context context, List<BriefData> data) {
        this.data = data;
        this.mActivity = (Callback) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Date date = new Date(data.get(position).getTime() * 1000);

        //Старый метод работы со временем. Оставлю, как напоминание. В остальной программе - Java.Util.Time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
        String dateString = dateFormat.format(date);
        holder.dailyTime.setText(dateString);

        double fMin = data.get(position).getTemperatureMin();
        long tempMin = Math.round((fMin - 32) * 5 / 9);
        String tMinString = (tempMin > 0) ? ("+" + String.valueOf(tempMin)) : String.valueOf(tempMin);

        double fMax = data.get(position).getTemperatureMax();
        long tempMax = Math.round((fMax - 32) * 5 / 9);
        String tMaxString = (tempMax > 0) ? ("+" + String.valueOf(tempMax)) : String.valueOf(tempMax);
        // Лень прикручивать StringBuilder
        holder.dailyTempMin.setText(String.valueOf(tMinString) + "˚С");
        holder.dailyTempMax.setText(String.valueOf(tMaxString) + "˚С");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void onNewData(List<BriefData> newData) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCall(data, newData));
        result.dispatchUpdatesTo(this);

        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dailyTime, dailyTempMin, dailyTempMax;

        public MyViewHolder(View itemView) {
            super(itemView);
            dailyTime = itemView.findViewById(R.id.dailyTime);
            dailyTempMin = itemView.findViewById(R.id.dailyTemperatureMin);
            dailyTempMax = itemView.findViewById(R.id.dailyTemperatureMax);

            // Через интерфейс взаимодействия запускаем метод в Activity по нажатию на элемент RecyclerView
            itemView.setOnClickListener((v -> {
                mActivity.viewHolderClicked(getAdapterPosition());
            }));
        }
    }
}
