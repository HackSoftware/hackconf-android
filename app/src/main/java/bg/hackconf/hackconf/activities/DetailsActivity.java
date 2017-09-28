package bg.hackconf.hackconf.activities;

import android.app.Activity;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import org.parceler.Parcels;

import java.util.List;

import bg.hackconf.hackconf.R;
import bg.hackconf.hackconf.StreamHelper;
import bg.hackconf.hackconf.TalkDetailsAdapter;
import bg.hackconf.hackconf.models.Talk;

public class DetailsActivity extends AppCompatActivity {

    private List<Talk> talks;
    private int currentTalk;

    private PagerSnapHelper snapHelper;
    private RecyclerView detailsRecyclerView;
    private LinearLayoutManager layoutManager;
    private TalkDetailsAdapter adapter;

    private YouTubePlayerFragment ytPlayerFragment;
    private YouTubePlayer ytPlayer;

    private ImageView leftArrow;
    private ImageView rightArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar tb = findViewById(R.id.details_toolbar);
        tb.setTitle("");
        setSupportActionBar(tb);

        Parcelable extra = getIntent().getParcelableExtra("talks");
        talks = Parcels.unwrap(extra);
        currentTalk = getIntent().getIntExtra("clicked_talk", 0);

        final Activity a = this;

        ytPlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        ytPlayerFragment.initialize("AIzaSyDggDk_3VBvcYzlMyO0ruOeNjTHC3wJemA", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                layoutManager = new LinearLayoutManager(a);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                detailsRecyclerView = findViewById(R.id.details_recycler_view);
                detailsRecyclerView.setLayoutManager(layoutManager);
                adapter = new TalkDetailsAdapter(a, talks);
                detailsRecyclerView.setAdapter(adapter);
                snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(detailsRecyclerView);
                detailsRecyclerView.bringToFront();
                ytPlayer = youTubePlayer;
                ytPlayer.cueVideo(talks.get(currentTalk).getSpeaker().getYoutubeId());

                detailsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int scrolledTalk = layoutManager.findFirstCompletelyVisibleItemPosition();
                        if (scrolledTalk != -1 && currentTalk != scrolledTalk) {
                            ytPlayer.pause();
                            ytPlayer.cueVideo(talks.get(scrolledTalk).getSpeaker().getYoutubeId());
                            currentTalk = scrolledTalk;
                        }
                    }
                });
                layoutManager.scrollToPosition(currentTalk);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("YOUTUBE-ERROR", youTubeInitializationResult.name());
            }
        });

        leftArrow = findViewById(R.id.left_arrow);
        rightArrow = findViewById(R.id.right_arrow);
    }
}
