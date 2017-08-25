package bg.hackconf.hackconf;

import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.List;

import bg.hackconf.hackconf.models.Talk;

public class TalkDetailsAdapter extends RecyclerView.Adapter<TalkDetailsAdapter.TalkDetailsHolder> {
    private List<Talk> talks;
    private FragmentManager fm;

    public TalkDetailsAdapter(FragmentManager fm, List<Talk> talks) {
        this.fm = fm;
        this.talks = talks;
    }

    @Override
    public TalkDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_recyclerview_talk, parent, false);
        return new TalkDetailsHolder(inflated);
    }

    @Override
    public void onBindViewHolder(TalkDetailsHolder holder, int position) {
        holder.bindFragment(fm).bindTalk(talks.get(position));
    }

    @Override
    public int getItemCount() {
        return talks.size();
    }

    public static class TalkDetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View parent;

        private TextView title;
        private TextView slot;
        private TextView speaker;
        private TextView speakerBio;
        private TextView description;
        YouTubePlayerFragment yt;

        public TalkDetailsHolder(View itemView) {
            super(itemView);
            parent = itemView;

            title = parent.findViewById(R.id.details_talk_title);
            slot = parent.findViewById(R.id.details_talk_slot);
            speaker = parent.findViewById(R.id.details_talk_speaker);
            speakerBio = parent.findViewById(R.id.details_talk_speaker_bio);
            description = parent.findViewById(R.id.details_talk_description);
        }

        public void bindTalk(Talk talk) {
            title.setText(talk.getTitle());
            slot.setText(talk.getSchedule().getStartTime() + " - " + talk.getSchedule().getEndTime());
            speaker.setText("Лектор: " + talk.getSpeaker().getFirstName() + " " + talk.getSpeaker().getLastName());
            speakerBio.setText(talk.getSpeaker().getBio());
            description.setText(talk.getDescription());
            yt.initialize("AIzaSyDggDk_3VBvcYzlMyO0ruOeNjTHC3wJemA", new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.cueVideo("Km0Wwl9OxjA");
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        }

        public TalkDetailsHolder bindFragment(FragmentManager fm) {
            yt = (YouTubePlayerFragment) fm.findFragmentById(R.id.youtube_fragment);
            return this;
        }

        @Override
        public void onClick(View view) {
        }
    }
}
