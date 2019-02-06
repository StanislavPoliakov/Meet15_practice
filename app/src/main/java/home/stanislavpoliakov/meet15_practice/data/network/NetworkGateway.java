package home.stanislavpoliakov.meet15_practice.data.network;

import android.util.Log;

import java.io.IOException;

import home.stanislavpoliakov.meet15_practice.domain.Weather;
import home.stanislavpoliakov.meet15_practice.domain.DomainContract;
import retrofit2.Response;

public class NetworkGateway implements DomainContract.NetworkOperations {
    private static final String TAG = "meet15_logs";

    @Override
    public Weather fetchData(String cityLocation) {
        Response<Weather> weatherResponse = getWeather(cityLocation);
        return weatherResponse.body();
    }

    private Response<Weather> getWeather(String locationPoint) {
        RetrofitHelper helper = new RetrofitHelper();
        try {
            return helper.getService().getWeather(locationPoint).execute();
        } catch (IOException ex) {
            Log.w(TAG, "Response Error ", ex);
        }
        return null;
    }
}
