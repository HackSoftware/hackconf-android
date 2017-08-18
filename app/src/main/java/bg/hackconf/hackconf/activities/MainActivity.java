package bg.hackconf.hackconf.activities;

import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

import bg.hackconf.hackconf.R;
import bg.hackconf.hackconf.TalkAdapter;
import bg.hackconf.hackconf.models.Talk;

public class MainActivity extends AppCompatActivity {

    private List<Talk> schedule;

    private RecyclerView scheduleRecyclerView;
    private LinearLayoutManager layoutManager;
    private TalkAdapter adapter;

    private TabLayout dayTabs;

    private Date firstDay = new Date(2017, 9, 29);
    private Date secondDay = new Date(2017, 9, 30);
    private Date thirdDay = new Date(2017, 10, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleRecyclerView = (RecyclerView) findViewById(R.id.scheduleRecyclerView);
        dayTabs = (TabLayout) findViewById(R.id.day_tabs);

        layoutManager = new LinearLayoutManager(this);
        scheduleRecyclerView.setLayoutManager(layoutManager);

        Parcelable extra = getIntent().getParcelableExtra("schedule");
        schedule = Parcels.unwrap(extra);

        adapter = new TalkAdapter(schedule);
        scheduleRecyclerView.setAdapter(adapter);
    }
}
