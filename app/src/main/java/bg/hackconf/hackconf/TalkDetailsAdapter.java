package bg.hackconf.hackconf;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import bg.hackconf.hackconf.models.Talk;
import bg.hackconf.hackconf.services.feedback.DetailedFeedback;
import bg.hackconf.hackconf.services.feedback.Feedback;
import bg.hackconf.hackconf.services.feedback.FeedbackService;
import bg.hackconf.hackconf.services.feedback.FeedbackServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TalkDetailsAdapter extends RecyclerView.Adapter<TalkDetailsAdapter.TalkDetailsHolder> {
    private Context context;
    private static final FeedbackService feedbackService = FeedbackServiceFactory.getInstance();
    private List<Talk> talks;

    private static final int COLOR_TEXT = Color.rgb(117, 117, 117);
    private static final int COLOR_GRAY_400 = Color.rgb(189, 189, 189);
    private static final int COLOR_LIGHT_BG = Color.rgb(254, 203, 87);

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

        private RelativeLayout thankYou;

        private TextView delayText;

        private long currentTalk;

        private static int currentFeedback = R.id.layout_feedback_satisfied;

        private SharedPreferences getPrefs() {
            return context.getSharedPreferences("hackconf2017_feedback", 0);
        }

        TalkDetailsHolder(final Context context, View itemView) {
            super(itemView);
            this.context = context;
            parent = itemView;

            thankYou = parent.findViewById(R.id.feedback_thank_you);

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

            sendFeedback = parent.findViewById(R.id.btn_send_feedback);
            sendFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int lightBg = context.getResources().getColor(R.color.colorAccent);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
                    final AlertDialog dialog = builder.setCancelable(false)
                            .setTitle("Would you like to go into details?")
                            .setPositiveButton("Send detailed", null)
                            .setNeutralButton("Skip and send", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    markFeedback();
                                    feedbackService.create(new Feedback(currentTalk, getFeedbackFromLayout(currentFeedback))).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {}
                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {}
                                    });
                                }
                            })
                            .setView(R.layout.feedback_details)
                            .create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(lightBg);
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TextView feedbackText = dialog.findViewById(R.id.feedback_details);
                                    if (feedbackText.getText().toString().length() < 16) {
                                        Toast.makeText(context, "Feedback details too short", Toast.LENGTH_SHORT).show();
                                    } else {
                                        dialog.dismiss();
                                        markFeedback();
                                        feedbackService.createDetailed(new DetailedFeedback(currentTalk, getFeedbackFromLayout(currentFeedback), feedbackText.getText().toString())).enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(Call<Integer> call, Response<Integer> response) {}
                                            @Override
                                            public void onFailure(Call<Integer> call, Throwable t) {}
                                        });
                                    }
                                }
                            });
                            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(lightBg);
                        }
                    });
                    dialog.show();
                }
            });

            delayText = parent.findViewById(R.id.delay_text);
        }

        void markFeedback() {
            terrible.setOnClickListener(null);
            bad.setOnClickListener(null);
            okay.setOnClickListener(null);
            good.setOnClickListener(null);
            great.setOnClickListener(null);

            ObjectAnimator clickedFade = ObjectAnimator
                    .ofObject(getIconFromLayout(currentFeedback), "colorFilter", new ArgbEvaluator(), COLOR_LIGHT_BG, COLOR_TEXT);
            clickedFade.setDuration(225);
            clickedFade.start();

            getPrefs().edit().putString(Long.toString(currentTalk), getFeedbackFromLayout(currentFeedback)).apply();
            YoYo.with(Techniques.FadeOutDown).duration(195).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(Animator animator) {
                    thankYou.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInUp).duration(225).playOn(thankYou);
                }
            }).playOn(sendFeedback);
        }

        void bindTalk(Talk talk) {
            title.setText(talk.getTitle());
            slot.setText(talk.getSchedule().getStartTime() + " - " + talk.getSchedule().getEndTime());
            String separator = " ";
            if (talk.getSpeaker().getFirstName().contains(" ")) {
                separator = " & ";
            }
            speaker.setText(talk.getSpeaker().getFirstName() + separator + talk.getSpeaker().getLastName());
            speakerBio.setText(talk.getSpeaker().getBio());
            currentTalk = talk.getId();

            String currentTalkId = Long.toString(currentTalk);
            if (getPrefs().contains(currentTalkId)) {
                terrible.setOnClickListener(null);
                bad.setOnClickListener(null);
                okay.setOnClickListener(null);
                good.setOnClickListener(null);
                great.setOnClickListener(null);
                ImageView icon = getIconFromFeedback(getPrefs().getString(currentTalkId, ""));
                if (icon != null) {
                    icon.setColorFilter(
                            context.getResources().getColor(R.color.gray600)
                    );
                }
                thankYou.setVisibility(View.VISIBLE);
                sendFeedback.setVisibility(View.INVISIBLE);
            } else {
                iconGood.setColorFilter(context.getResources().getColor(R.color.lightBackground));
                FeedbackOnClickListener focl = new FeedbackOnClickListener(iconGood);
                terrible.setOnClickListener(focl);
                bad.setOnClickListener(focl);
                okay.setOnClickListener(focl);
                good.setOnClickListener(focl);
                great.setOnClickListener(focl);
            }

            if (talk.getSchedule().isCanceled()) {
                delayText.setVisibility(View.VISIBLE);
                delayText.setText("Canceled");
            } else if (talk.getSchedule().getDelay() > 0) {
                delayText.setVisibility(View.VISIBLE);
                delayText.setText(talk.getSchedule().getDelay() + "min delay");
            }
        }

        private static class FeedbackOnClickListener implements View.OnClickListener {
            private ImageView currentIcon;

            public FeedbackOnClickListener(ImageView defaultIcon) {
                currentIcon = defaultIcon;
            }

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

        private String getFeedbackFromLayout(int layoutId) {
            switch (layoutId) {
                case R.id.layout_feedback_very_dissatisfied:
                    return "terrible";

                case R.id.layout_feedback_dissatisfied:
                    return "bad";

                case R.id.layout_feedback_neutral:
                    return "okay";

                case R.id.layout_feedback_satisfied:
                    return "good";

                case R.id.layout_feedback_very_satisfied:
                    return "great";
            }
            return "";
        }

        private ImageView getIconFromFeedback(String feedback) {
            switch (feedback) {
                case "terrible":
                    return iconTerrible;
                case "bad":
                    return iconBad;
                case "okay":
                    return iconOkay;
                case "good":
                    return iconGood;
                case "great":
                    return iconGreat;
            }
            return null;
        }

        private ImageView getIconFromLayout(int layoutId) {
            switch (layoutId) {
                case R.id.layout_feedback_very_dissatisfied:
                    return iconTerrible;

                case R.id.layout_feedback_dissatisfied:
                    return iconBad;

                case R.id.layout_feedback_neutral:
                    return iconOkay;

                case R.id.layout_feedback_satisfied:
                    return iconGood;

                case R.id.layout_feedback_very_satisfied:
                    return iconGreat;
            }
            return null;
        }
    }
}
