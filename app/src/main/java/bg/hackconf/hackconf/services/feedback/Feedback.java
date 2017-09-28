package bg.hackconf.hackconf.services.feedback;

import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("talkId")
    long talkId;

    @SerializedName("feedback")
    String feedback;

    public Feedback(long talkId, String feedback) {
        this.talkId = talkId;
        this.feedback = feedback;
    }
}
