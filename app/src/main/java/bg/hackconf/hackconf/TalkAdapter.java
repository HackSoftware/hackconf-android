package bg.hackconf.hackconf;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.vipulasri.timelineview.TimelineView;

import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import bg.hackconf.hackconf.activities.DetailsActivity;
import bg.hackconf.hackconf.models.Talk;

public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.TalkHolder> {
    private List<Talk> talks;
    private RecyclerView schedule;

    public TalkAdapter(List<Talk> talks) {
        this.talks = talks;
    }

    public static class TalkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View parent;
        private TimelineView timeline;

        private List<Talk> talks;
        private int currentTalk;

        private TextView title;
        private TextView speaker;
        private TextView timeSlot;

        private CardView card;
        private RecyclerView schedule;

        private ImageView setReminder;

        public TalkHolder(final View v, int viewType, final RecyclerView schedule) {
            super(v);
            this.parent = v;

            timeline = itemView.findViewById(R.id.time_marker);
            timeline.initLine(viewType);
            timeline.setMarker(ContextCompat.getDrawable(v.getContext(), R.drawable.marker_upcoming));

            title = v.findViewById(R.id.talk_title);
            speaker = v.findViewById(R.id.talk_speaker);
            timeSlot = v.findViewById(R.id.talk_time_slot);

            card = v.findViewById(R.id.talk_card);
            this.schedule = schedule;

            setReminder = v.findViewById(R.id.talk_set_reminder);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), DetailsActivity.class);
            intent.putExtra("talks", Parcels.wrap(talks));
            intent.putExtra("clicked_talk", getAdapterPosition());
            view.getContext().startActivity(intent);
        }

        public void bindTalk(List<Talk> talks, int position) {
            this.talks = talks;
            this.currentTalk = position;
            final Talk talk = talks.get(position);

            final LocalDate startDate = LocalDate.parse(talk.getSchedule().getStartDate());
            final LocalDate endDate = LocalDate.parse(talk.getSchedule().getEndDate());

            final LocalTime startTime = LocalTime.parse(talk.getSchedule().getStartTime());
            startTime.withFieldAdded(DurationFieldType.minutes(), talk.getSchedule().getDelay());
            final LocalTime endTime = LocalTime.parse(talk.getSchedule().getEndTime());
            endTime.withFieldAdded(DurationFieldType.minutes(), talk.getSchedule().getDelay());

            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            if (startDate.equals(today)) {
                if (startTime.isBefore(now)) {
                    if (endTime.isAfter(now)) {
                        timeline.setMarker(ContextCompat.getDrawable(parent.getContext(), R.drawable.marker_active));
                    } else {
                        timeline.setMarker(ContextCompat.getDrawable(parent.getContext(), R.drawable.marker_inactive));
                    }
                } else {
                    timeline.setMarker(ContextCompat.getDrawable(parent.getContext(), R.drawable.marker_inactive));
                }
            } else {
                timeline.setMarker(ContextCompat.getDrawable(parent.getContext(), R.drawable.marker_inactive));
            }

            title.setText(talk.getTitle());
            String separator = " ";
            if (talk.getSpeaker().getFirstName().contains(" ")) {
                separator = " & ";
            }
            speaker.setText(talk.getSpeaker().getFirstName() + separator + talk.getSpeaker().getLastName());
            timeSlot.setText(talk.getSchedule().getStartTime() + " - " + talk.getSchedule().getEndTime());

            setReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar start = Calendar.getInstance();
                    start.set(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth(), startTime.getHourOfDay(), startTime.getMinuteOfHour());
                    Calendar end = Calendar.getInstance();
                    end.set(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), endTime.getHourOfDay(), endTime.getMinuteOfHour());
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("allDay", false);
                    intent.putExtra("beginTime", start.getTimeInMillis());
                    intent.putExtra("endTime", end.getTimeInMillis());
                    String eventTitle = talk.getTitle() + " - " + talk.getSpeaker().getFirstName() + " " + talk.getSpeaker().getLastName();
                    intent.putExtra("title", eventTitle);
                    intent.putExtra("hasAlarm", 1);
                    parent.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public TalkAdapter.TalkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_recyclerview_talk, parent, false);
        return new TalkHolder(inflated, viewType, schedule);
    }

    @Override
    public void onBindViewHolder(TalkAdapter.TalkHolder holder, int position) {
        holder.bindTalk(talks, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        schedule = recyclerView;
    }

    @Override
    public int getItemCount() {
        return talks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }
}
