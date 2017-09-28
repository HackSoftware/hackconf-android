package bg.hackconf.hackconf.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import bg.hackconf.hackconf.services.schedule.ScheduleRequest;
import bg.hackconf.hackconf.services.schedule.ScheduleResponse;
import bg.hackconf.hackconf.services.schedule.ScheduleServiceFactory;
import org.joda.time.LocalDate;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bg.hackconf.hackconf.services.schedule.ScheduleService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private ScheduleService scheduleService = ScheduleServiceFactory.getInstance();
    private List<LocalDate> dates = new ArrayList<>();

    private Callback<ScheduleResponse> callback = new Callback<ScheduleResponse>() {
        @Override
        public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("schedule", Parcels.wrap(response.body()));
            startActivity(intent);
            finish();
        }

        @Override
        public void onFailure(Call<ScheduleResponse> call, Throwable t) {
            Log.e(TAG, t.toString());
            Toast.makeText(getApplicationContext(), "Could not load schedule", Toast.LENGTH_LONG).show();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dates.add(new LocalDate(2017, 9, 30));
        dates.add(new LocalDate(2017, 9, 30));
        dates.add(new LocalDate(2017, 10, 1));

        SharedPreferences prefs = getSharedPreferences("hackconf_2017", 0);
        if (!prefs.contains("deviceId")) {
            prefs.edit().putString("deviceId", UUID.randomUUID().toString()).apply();
        }

        loadScheduleAndFinish();
    }

    private void loadScheduleAndFinish() {
        scheduleService.get(new ScheduleRequest(dates)).enqueue(callback);
    }
}
