package bg.hackconf.hackconf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bg.hackconf.hackconf.models.Talk;

public class TalkDetailsAdapter extends RecyclerView.Adapter<TalkDetailsAdapter.TalkDetailsHolder> {
    private List<Talk> talks;

    public TalkDetailsAdapter(List<Talk> talks) {
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
        Talk t = talks.get(position);
        holder.bindTalk(t);
    }

    @Override
    public int getItemCount() {
        return talks.size();
    }

    static class TalkDetailsHolder extends RecyclerView.ViewHolder {
        private View parent;

        private TextView title;
        private TextView slot;
        private TextView speaker;
        private TextView speakerBio;

        TalkDetailsHolder(View itemView) {
            super(itemView);
            parent = itemView;

            title = parent.findViewById(R.id.details_talk_title);
            slot = parent.findViewById(R.id.details_talk_slot);
            speaker = parent.findViewById(R.id.details_talk_speaker);
            speakerBio = parent.findViewById(R.id.details_talk_speaker_bio);
        }

        void bindTalk(Talk talk) {
            title.setText(talk.getTitle());
            slot.setText(talk.getSchedule().getStartTime() + " - " + talk.getSchedule().getEndTime());
            speaker.setText("Лектор: " + talk.getSpeaker().getFirstName() + " " + talk.getSpeaker().getLastName());
            speakerBio.setText(talk.getSpeaker().getBio());
        }
    }
}
