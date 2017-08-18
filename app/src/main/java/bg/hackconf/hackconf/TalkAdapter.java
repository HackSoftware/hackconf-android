package bg.hackconf.hackconf;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import bg.hackconf.hackconf.models.Talk;

public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.TalkHolder> {
    private List<Talk> talks;

    public TalkAdapter(List<Talk> talks) {
        this.talks = talks;
    }

    public static class TalkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View parent;
        private TimelineView timeline;

        private Talk talk;
        private TextView title;
        private TextView speaker;
        private TextView timeSlot;

        private TextView delayText;
        private ImageView delayIcon;

        private static final String TALK_KEY = "TALK";

        public TalkHolder(View v, int viewType) {
            super(v);
            this.parent = v;

            timeline = (TimelineView) itemView.findViewById(R.id.time_marker);
            timeline.initLine(viewType);
            timeline.setMarker(ContextCompat.getDrawable(v.getContext(), R.drawable.marker_upcoming));

            title = (TextView) v.findViewById(R.id.talk_title);
            speaker = (TextView) v.findViewById(R.id.talk_speaker);
            timeSlot = (TextView) v.findViewById(R.id.talk_time_slot);

//            delayText = (TextView) v.findViewById(R.id.delay_text);
//            delayIcon = (ImageView) v.findViewById(R.id.delay_icon);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindTalk(Talk talk) {
            this.talk = talk;
            if (talk.getTitle().equals("Scala")) {
                timeline.setMarker(ContextCompat.getDrawable(parent.getContext(), R.drawable.marker_inactive));
            }
            if (talk.getTitle().equals("Мутанти, зомбита и тестове")) {
                timeline.setMarker(ContextCompat.getDrawable(parent.getContext(), R.drawable.marker_active));
//                delayIcon.setVisibility(View.VISIBLE);
//                delayText.setVisibility(View.VISIBLE);
            }
            title.setText(talk.getTitle());
            speaker.setText(talk.getSpeaker().getFirstName() + " " + talk.getSpeaker().getLastName());
            timeSlot.setText(talk.getSchedule().getStartTime() + " - " + talk.getSchedule().getEndTime());
        }
    }

    @Override
    public TalkAdapter.TalkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_recyclerview_talk, parent, false);
        return new TalkHolder(inflated, viewType);
    }

    @Override
    public void onBindViewHolder(TalkAdapter.TalkHolder holder, int position) {
        Talk current = talks.get(position);
        holder.bindTalk(current);
    }

    @Override
    public int getItemCount() {
        return talks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }
}
