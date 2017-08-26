package bg.hackconf.hackconf.activities;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.joda.time.LocalDate;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import bg.hackconf.hackconf.R;
import bg.hackconf.hackconf.StreamHelper;
import bg.hackconf.hackconf.TalkAdapter;
import bg.hackconf.hackconf.models.Talk;
import bg.hackconf.hackconf.services.ScheduleRequest;
import bg.hackconf.hackconf.services.ScheduleResponse;
import bg.hackconf.hackconf.services.ScheduleService;
import bg.hackconf.hackconf.services.ScheduleServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ScheduleService scheduleService = ScheduleServiceFactory.getInstance();
    private ScheduleResponse schedule;
    private List<LocalDate> dates = new ArrayList<>();

    private RecyclerView scheduleRecyclerView;
    private LinearLayoutManager layoutManager;
    private TalkAdapter adapter;

    private TabLayout dayTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("");
//        tb.setLogo(R.drawable.ic_hackconf_logo);
        setSupportActionBar(tb);

        dates.add(new LocalDate(2018, 2, 13));
        dates.add(new LocalDate(2018, 2, 14));
        dates.add(new LocalDate(2018, 2, 15));

        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        dayTabs = findViewById(R.id.day_tabs);

        dayTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                changeTalks(schedule.getResult().get(dates.get(tab.getPosition())));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        layoutManager = new LinearLayoutManager(this);
        scheduleRecyclerView.setLayoutManager(layoutManager);

        schedule = Parcels.unwrap(getIntent().getExtras().getParcelable("schedule"));

        adapter = new TalkAdapter(schedule.getResult().get(new LocalDate(2018, 2, 13)));
        scheduleRecyclerView.setAdapter(adapter);

        findViewById(R.id.live_stream_button).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                scheduleService.get(new ScheduleRequest(dates)).enqueue(new Callback<ScheduleResponse>() {
                    @Override
                    public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                        schedule = response.body();
                        changeTalks(schedule.getResult().get(dates.get(dayTabs.getSelectedTabPosition())));
                        Toast.makeText(getApplicationContext(), "Schedule updated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error during refresh", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
        return true;
    }

    public void onLiveStreamClick(final View v) {
        StreamHelper.onLiveStreamClick(this, v);
    }

    private void changeTalks(final List<Talk> talks) {
        YoYo.with(Techniques.SlideOutDown).duration(250).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                YoYo.with(Techniques.SlideInUp).duration(250).playOn(scheduleRecyclerView);
                adapter = new TalkAdapter(talks);
                scheduleRecyclerView.setAdapter(adapter);
            }
        }).playOn(scheduleRecyclerView);
    }
}
