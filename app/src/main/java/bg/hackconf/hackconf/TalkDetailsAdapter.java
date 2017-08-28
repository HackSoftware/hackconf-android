package bg.hackconf.hackconf;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import bg.hackconf.hackconf.models.Talk;

public class TalkDetailsAdapter extends RecyclerView.Adapter<TalkDetailsAdapter.TalkDetailsHolder> {
    private Context context;
    private List<Talk> talks;

    public TalkDetailsAdapter(Context context, List<Talk> talks) {
        this.context = context;
        this.talks = talks;
    }

    @Override
    public TalkDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_recyclerview_talk, parent, false);
        return new TalkDetailsHolder(context, inflated);
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
        private final Context context;
        private View parent;

        private TextView title;
        private TextView slot;
        private TextView speaker;
        private TextView speakerBio;

        private RelativeLayout terrible;
        private RelativeLayout bad;
        private RelativeLayout okay;
        private RelativeLayout good;
        private RelativeLayout great;

        private ImageView iconTerrible;
        private ImageView iconBad;
        private ImageView iconOkay;
        private ImageView iconGood;
        private ImageView iconGreat;

        private Button sendFeedback;

        private static int currentFeedback = R.id.layout_feedback_neutral;

        TalkDetailsHolder(final Context context, View itemView) {
            super(itemView);
            this.context = context;
            parent = itemView;

            title = parent.findViewById(R.id.details_talk_title);
            slot = parent.findViewById(R.id.details_talk_slot);
            speaker = parent.findViewById(R.id.details_talk_speaker);
            speakerBio = parent.findViewById(R.id.details_talk_speaker_bio);

            terrible = parent.findViewById(R.id.layout_feedback_very_dissatisfied);
            bad = parent.findViewById(R.id.layout_feedback_dissatisfied);
            okay = parent.findViewById(R.id.layout_feedback_neutral);
            good = parent.findViewById(R.id.layout_feedback_satisfied);
            great = parent.findViewById(R.id.layout_feedback_very_satisfied);

            iconTerrible = parent.findViewById(R.id.ic_feedback_very_dissatisfied);
            iconBad = parent.findViewById(R.id.ic_feedback_dissatisfied);
            iconOkay = parent.findViewById(R.id.ic_feedback_neutral);
            iconGood = parent.findViewById(R.id.ic_feedback_satisfied);
            iconGreat = parent.findViewById(R.id.ic_feedback_very_satisfied);

            FeedbackOnClickListener focl = new FeedbackOnClickListener(iconOkay);
            terrible.setOnClickListener(focl);
            bad.setOnClickListener(focl);
            okay.setOnClickListener(focl);
            good.setOnClickListener(focl);
            great.setOnClickListener(focl);

            sendFeedback = parent.findViewById(R.id.btn_send_feedback);
            sendFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int lightBg = context.getResources().getColor(R.color.colorAccent);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
                    final AlertDialog dialog = builder
                            .setTitle("Would you like to go into details?")
                            .setPositiveButton("Send details", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNeutralButton("Skip details", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setView(R.layout.feedback_details)
                            .create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(lightBg);
                            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(lightBg);
                        }
                    });
                    dialog.show();
                }
            });
        }

        void bindTalk(Talk talk) {
            title.setText(talk.getTitle());
            slot.setText(talk.getSchedule().getStartTime() + " - " + talk.getSchedule().getEndTime());
            speaker.setText(talk.getSpeaker().getFirstName() + " " + talk.getSpeaker().getLastName());
            speakerBio.setText(talk.getSpeaker().getBio());
        }

        private static class FeedbackOnClickListener implements View.OnClickListener {
            private ImageView currentIcon;

            public FeedbackOnClickListener(ImageView defaultIcon) {
                currentIcon = defaultIcon;
            }

            private static int COLOR_GRAY_400 = Color.rgb(189, 189, 189);
            private static int COLOR_LIGHT_BG = Color.rgb(254, 203, 87);

            @Override
            public void onClick(View view) {
                int clickedFeedback = view.getId();

                if (clickedFeedback != currentFeedback) {
                    ImageView clickedIcon = view.findViewById(getIconIdFromLayout(clickedFeedback));
                    ObjectAnimator clickedFade = ObjectAnimator
                            .ofObject(clickedIcon, "colorFilter", new ArgbEvaluator(), COLOR_GRAY_400, COLOR_LIGHT_BG);
                    clickedFade.setDuration(225);
                    clickedFade.start();

                    ObjectAnimator currentFade = ObjectAnimator
                            .ofObject(currentIcon, "colorFilter", new ArgbEvaluator(), COLOR_LIGHT_BG, COLOR_GRAY_400);
                    currentFade.setDuration(195);
                    currentFade.start();

                    currentFeedback = clickedFeedback;
                    currentIcon = clickedIcon;
                    YoYo.with(Techniques.Pulse).duration(225).playOn(view);
                }
            }

            private int getIconIdFromLayout(int layoutId) {
                switch (layoutId) {
                    case R.id.layout_feedback_very_dissatisfied:
                        return R.id.ic_feedback_very_dissatisfied;

                    case R.id.layout_feedback_dissatisfied:
                        return R.id.ic_feedback_dissatisfied;

                    case R.id.layout_feedback_neutral:
                        return R.id.ic_feedback_neutral;

                    case R.id.layout_feedback_satisfied:
                        return R.id.ic_feedback_satisfied;

                    case R.id.layout_feedback_very_satisfied:
                        return R.id.ic_feedback_very_satisfied;
                }
                return -1;
            }
        }
    }
}
