package bg.hackconf.hackconf.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bg.hackconf.hackconf.models.Talk;
import bg.hackconf.hackconf.services.ScheduleService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final String API_URL = "http://192.168.0.2:9000";

    private List<LocalDate> dates = new ArrayList<>();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private ScheduleService schedule = retrofit.create(ScheduleService.class);

    private Callback<List<Talk>> callback = new Callback<List<Talk>>() {
        @Override
        public void onResponse(Call<List<Talk>> call, Response<List<Talk>> response) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("schedule", Parcels.wrap(response.body()));
            startActivity(intent);
            finish();
        }

        @Override
        public void onFailure(Call<List<Talk>> call, Throwable t) {
            Log.e(TAG, t.toString());
            Toast.makeText(getApplicationContext(), "Could not load schedule", Toast.LENGTH_LONG).show();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dates.add(new LocalDate(2018, 2, 13));
        dates.add(new LocalDate(2018, 2, 14));
        dates.add(new LocalDate(2018, 2, 15));

        try {
            Thread.sleep(500);
        } catch (Exception ignored) {}

        loadScheduleAndFinish();
    }

    private void loadScheduleAndFinish() {
        schedule.get(dates).enqueue(callback);
    }
}
