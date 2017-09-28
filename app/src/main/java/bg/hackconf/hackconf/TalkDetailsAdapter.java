package bg.hackconf.hackconf;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;
import java.util.Timer;
import java.util.UUID;

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

        private String deviceId;

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

        private ImageView sending;

        private Button sendFeedback;

        private RelativeLayout thankYou;

        private TextView delayText;

        private long currentTalk;

        private SharedPreferences getPrefs() {
            return context.getSharedPreferences("hackconf_2017", 0);
        }

        TalkDetailsHolder(final Context context, View itemView) {
            super(itemView);
            this.context = context;
            parent = itemView;

            deviceId = getPrefs().getString("deviceId", UUID.randomUUID().toString());

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

            sending = parent.findViewById(R.id.ic_sending);

            sendFeedback = parent.findViewById(R.id.btn_send_feedback);
            sendFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int lightBg = ContextCompat.getColor(context, R.color.colorAccent);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
                    final AlertDialog dialog = builder.setCancelable(false)
                            .setTitle("Share your thoughts")
                            .setPositiveButton("Send", null)
                            .setNegativeButton("Cancel", null)
                            .setView(R.layout.feedback_details)
                            .create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(lightBg);
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String currentTalkId = Long.toString(currentTalk);
                                    TextView feedbackText = dialog.findViewById(R.id.feedback_details);
                                    String feedback = getPrefs().getString(currentTalkId, "okay");
                                    if (feedbackText.getText().toString().length() < 16) {
                                        Toast.makeText(context, "Feedback details too short", Toast.LENGTH_SHORT).show();
                                    } else {
                                        dialog.dismiss();
                                        getPrefs().edit().putBoolean(currentTalkId + "_details", true).apply();
                                        YoYo.with(Techniques.FadeOutDown).duration(195).onEnd(new YoYo.AnimatorCallback() {
                                            @Override
                                            public void call(Animator animator) {
                                                thankYou.setVisibility(View.VISIBLE);
                                                YoYo.with(Techniques.FadeInUp).duration(225).playOn(thankYou);
                                            }
                                        }).playOn(sendFeedback);
                                        feedbackService.createDetailed(new DetailedFeedback(currentTalk, feedback, feedbackText.getText().toString(), deviceId)).enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(Call<Integer> call, Response<Integer> response) {}
                                            @Override
                                            public void onFailure(Call<Integer> call, Throwable t) {}
                                        });
                                    }
                                }
                            });
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(lightBg);
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    dialog.show();
                }
            });
            delayText = parent.findViewById(R.id.delay_text);
        }

        void bindTalk(Talk talk) {
            title.setText(talk.getTitle());
            slot.setText(talk.getSchedule().getStartTime() + " - " + talk.getSchedule().getEndTime());
            String separator = " ";
            speaker.setText(talk.getSpeaker().getFirstName() + separator + talk.getSpeaker().getLastName());
            speakerBio.setText(talk.getSpeaker().getBio());
            currentTalk = talk.getId();

            ImageView selectedIcon = null;

            iconTerrible.setColorFilter(ContextCompat.getColor(context, R.color.gray400));
            iconBad.setColorFilter(ContextCompat.getColor(context, R.color.gray400));
            iconGood.setColorFilter(ContextCompat.getColor(context, R.color.gray400));
            iconGreat.setColorFilter(ContextCompat.getColor(context, R.color.gray400));
            iconOkay.setColorFilter(ContextCompat.getColor(context, R.color.gray400));

            sendFeedback.setEnabled(false);
            String currentTalkId = Long.toString(currentTalk);
            if (getPrefs().contains(currentTalkId)) {
                if (getPrefs().contains(currentTalkId+"_details")) {
                    thankYou.setVisibility(View.VISIBLE);
                    sendFeedback.setVisibility(View.INVISIBLE);
                } else {
                    sendFeedback.setEnabled(true);
                }
                ImageView icon = getIconFromFeedback(getPrefs().getString(currentTalkId, ""));
                if (icon != null) {
                    icon.setColorFilter(ContextCompat.getColor(context, R.color.lightBackground));
                    selectedIcon = icon;
                }
            }

            FeedbackOnClickListener focl = new FeedbackOnClickListener(selectedIcon);
            terrible.setOnClickListener(focl);
            bad.setOnClickListener(focl);
            okay.setOnClickListener(focl);
            good.setOnClickListener(focl);
            great.setOnClickListener(focl);

            if (talk.getSchedule().isCanceled()) {
                delayText.setVisibility(View.VISIBLE);
                delayText.setText("Canceled");
            } else if (talk.getSchedule().getDelay() > 0) {
                delayText.setVisibility(View.VISIBLE);
                delayText.setText(talk.getSchedule().getDelay() + "min delay");
            }
        }

        private class FeedbackOnClickListener implements View.OnClickListener {
            private ImageView currentIcon;
            private Runnable feedbackRunnable;
            private Handler feedbackHandler = new Handler();
            private boolean isBusy = false;

            public FeedbackOnClickListener(ImageView currentIcon) {
                this.currentIcon = currentIcon;
            }

            @Override
            public void onClick(View clicked) {
                sendFeedback.setEnabled(true);
                final String feedback = getFeedbackFromLayout(clicked);
                ImageView clickedIcon = getIconFromFeedback(feedback);

                if (currentIcon == null || clickedIcon.getId() != currentIcon.getId()) {
                    feedbackHandler.removeCallbacks(feedbackRunnable);
                    if (!isBusy) {
                        RotateAnimation rotateAnimation = new RotateAnimation(0, 359f,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        rotateAnimation.setInterpolator(new LinearInterpolator());
                        rotateAnimation.setDuration(1000);
                        rotateAnimation.setRepeatCount(Animation.INFINITE);
                        sending.startAnimation(rotateAnimation);
                        YoYo.with(Techniques.FadeIn).duration(195).playOn(sending);
                    }
                    isBusy = true;

                    ObjectAnimator clickedFade = ObjectAnimator
                            .ofObject(clickedIcon, "colorFilter", new ArgbEvaluator(), COLOR_GRAY_400, COLOR_LIGHT_BG);
                    clickedFade.setDuration(225);
                    clickedFade.start();

                    feedbackRunnable = new Runnable() {
                        @Override
                        public void run() {
                            feedbackService.create(new Feedback(currentTalk, feedback, deviceId)).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    YoYo.with(Techniques.FadeOut).duration(195).onEnd(new YoYo.AnimatorCallback() {
                                        @Override
                                        public void call(Animator animator) {
                                            isBusy = false;
                                        }
                                    }).playOn(sending);
                                }
                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {}
                            });
                        }
                    };

                    feedbackHandler.postDelayed(feedbackRunnable, 1000);
                }
                if (currentIcon != null && clickedIcon.getId() != currentIcon.getId()) {
                    ObjectAnimator currentFade = ObjectAnimator
                            .ofObject(currentIcon, "colorFilter", new ArgbEvaluator(), COLOR_LIGHT_BG, COLOR_GRAY_400);
                    currentFade.setDuration(195);
                    currentFade.start();
                }
                currentIcon = clickedIcon;
                getPrefs().edit().putString(Long.toString(currentTalk), feedback).apply();
                YoYo.with(Techniques.Pulse).duration(225).playOn(currentIcon);
            }
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

        private String getFeedbackFromLayout(View layout) {
            switch (layout.getId()) {
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
            return null;
        }
    }
}
