package bg.hackconf.hackconf.activities;

import android.animation.Animator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.joda.time.LocalDate;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import bg.hackconf.hackconf.DayPageFragment;
import bg.hackconf.hackconf.R;
import bg.hackconf.hackconf.StreamHelper;
import bg.hackconf.hackconf.models.Talk;
import bg.hackconf.hackconf.services.schedule.ScheduleRequest;
import bg.hackconf.hackconf.services.schedule.ScheduleResponse;
import bg.hackconf.hackconf.services.schedule.ScheduleService;
import bg.hackconf.hackconf.services.schedule.ScheduleServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private StreamHelper streamHelper = new StreamHelper(this);

    private ScheduleService scheduleService = ScheduleServiceFactory.getInstance();
    private ScheduleResponse schedule;

    private List<LocalDate> dates = new ArrayList<>();

    private ViewPager dayPager;
    private PagerAdapter pagerAdapter;

    private TabLayout dayTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("");
        setSupportActionBar(tb);

        dates.add(new LocalDate(2017, 9, 30));
        dates.add(new LocalDate(2017, 10, 1));

        schedule = Parcels.unwrap(getIntent().getExtras().getParcelable("schedule"));

        dayTabs = findViewById(R.id.day_tabs);

        dayPager = findViewById(R.id.day_pager);
        pagerAdapter = new DayPagerAdapter(getSupportFragmentManager(), schedule);
        dayPager.setAdapter(pagerAdapter);

        dayPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                dayTabs.getTabAt(position).select();
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        dayTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                dayPager.setCurrentItem(tab.getPosition(), true);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        ImageView liveStream = findViewById(R.id.live_stream_button);
        liveStream.setVisibility(View.VISIBLE);
        liveStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLiveStreamClick(view);
            }
        });

        if (LocalDate.now().equals(new LocalDate(2017, 10, 1))) {
            dayTabs.getTabAt(1).select();
        }
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
                        updateTalks();
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
        streamHelper.onLiveStreamClick(this, v);
    }

    private void updateTalks() {
        YoYo.with(Techniques.SlideOutDown).duration(195).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                pagerAdapter = new DayPagerAdapter(getSupportFragmentManager(), schedule);
                dayPager.setAdapter(pagerAdapter);
                dayPager.setCurrentItem(dayTabs.getSelectedTabPosition());
                YoYo.with(Techniques.SlideInUp).duration(225).playOn(dayPager);
            }
        }).playOn(dayPager);
    }

    private class DayPagerAdapter extends FragmentStatePagerAdapter {

        private ScheduleResponse schedule;

        public DayPagerAdapter(FragmentManager fm, ScheduleResponse schedule) {
            super(fm);
            this.schedule = schedule;
        }

        @Override
        public int getCount() {
            return schedule.getResult().entrySet().size();
        }

        @Override
        public Fragment getItem(int position) {
            List<Talk> optResult = schedule.getResult().get(dates.get(position));
            List<Talk> result = optResult == null ? new ArrayList<Talk>(0) : optResult;
            Fragment fragment = new DayPageFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("talks", Parcels.wrap(result));
            fragment.setArguments(bundle);
            return fragment;
        }
    }
}
