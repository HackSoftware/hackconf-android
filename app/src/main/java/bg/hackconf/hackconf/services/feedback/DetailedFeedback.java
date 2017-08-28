package bg.hackconf.hackconf.services.feedback;

import com.google.gson.annotations.SerializedName;

public class DetailedFeedback {
    @SerializedName("talkId")
    int talkId;

    @SerializedName("feedback")
    String feedback;

    @SerializedName("details")
    String details;

    public DetailedFeedback(int talkId, String feedback, String details) {
        this.talkId = talkId;
        this.feedback = feedback;
        this.details = details;
    }
}
