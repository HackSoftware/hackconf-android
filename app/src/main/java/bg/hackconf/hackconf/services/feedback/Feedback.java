package bg.hackconf.hackconf.services.feedback;

import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("talkId")
    long talkId;

    @SerializedName("feedback")
    String feedback;

    @SerializedName("deviceId")
    String deviceId;

    public Feedback(long talkId, String feedback, String deviceId) {
        this.talkId = talkId;
        this.feedback = feedback;
        this.deviceId = deviceId;
    }
}
