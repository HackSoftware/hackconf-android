package bg.hackconf.hackconf.services.feedback;

import com.google.gson.annotations.SerializedName;

public class DetailedFeedback {
    @SerializedName("talkId")
    long talkId;

    @SerializedName("feedback")
    String feedback;

    @SerializedName("deviceId")
    String deviceId;

    @SerializedName("details")
    String details;

    public DetailedFeedback(long talkId, String feedback, String details, String deviceId) {
        this.talkId = talkId;
        this.feedback = feedback;
        this.details = details;
        this.deviceId = deviceId;
    }
}
