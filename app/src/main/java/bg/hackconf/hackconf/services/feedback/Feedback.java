package bg.hackconf.hackconf.services.feedback;

import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("talkId")
    int talkId;

    @SerializedName("feedback")
    String feedback;

    public Feedback(int talkId, String feedback) {
        this.talkId = talkId;
        this.feedback = feedback;
    }
}
