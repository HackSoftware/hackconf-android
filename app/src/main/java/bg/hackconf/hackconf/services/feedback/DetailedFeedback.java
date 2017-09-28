package bg.hackconf.hackconf.services.feedback;

import com.google.gson.annotations.SerializedName;

public class DetailedFeedback {
    @SerializedName("talkId")
    long talkId;

    @SerializedName("feedback")
    String feedback;

    @SerializedName("details")
    String details;

    public DetailedFeedback(long talkId, String feedback, String details) {
        this.talkId = talkId;
        this.feedback = feedback;
        this.details = details;
    }
}
