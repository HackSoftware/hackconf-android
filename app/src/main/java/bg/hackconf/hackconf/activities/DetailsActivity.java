package bg.hackconf.hackconf.activities;

import android.app.Activity;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.joda.time.LocalDate;
import org.parceler.Parcels;

import java.util.List;

import bg.hackconf.hackconf.R;
import bg.hackconf.hackconf.TalkDetailsAdapter;
import bg.hackconf.hackconf.models.Talk;

public class DetailsActivity extends FragmentActivity {

    private List<Talk> talks;

    private TabLayout dayTabs;

    private ImageView leftArrow;
    private ImageView rightArrow;

    private PagerSnapHelper snapHelper;
    private RecyclerView detailsRecyclerView;
    private LinearLayoutManager layoutManager;
    private TalkDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar tb = findViewById(R.id.details_toolbar);
        //setSupportActionBar(tb);

        Parcelable extra = getIntent().getParcelableExtra("talks");
        talks = Parcels.unwrap(extra);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        detailsRecyclerView = findViewById(R.id.detailsRecyclerView);
        detailsRecyclerView.setLayoutManager(layoutManager);
        adapter = new TalkDetailsAdapter(getFragmentManager(), talks);
        detailsRecyclerView.setAdapter(adapter);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(detailsRecyclerView);
        detailsRecyclerView.bringToFront();

        dayTabs = findViewById(R.id.details_day);
        dayTabs.getTabAt(0).setText(
                LocalDate.parse(
                        talks.get(0).getSchedule().getStartDate()
                ).toString("dd MMM")
        );
    }
}
