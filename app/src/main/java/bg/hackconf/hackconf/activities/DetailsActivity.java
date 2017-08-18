package bg.hackconf.hackconf.activities;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.parceler.Parcels;

import java.util.List;

import bg.hackconf.hackconf.R;
import bg.hackconf.hackconf.models.Talk;

public class DetailsActivity extends AppCompatActivity {

    private List<Talk> schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Parcelable extra = getIntent().getParcelableExtra("schedule");
        schedule = Parcels.unwrap(extra);
    }
}
