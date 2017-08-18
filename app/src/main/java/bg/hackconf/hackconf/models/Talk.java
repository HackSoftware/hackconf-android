package bg.hackconf.hackconf.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Talk {
    @SerializedName("id")
    long id;

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("imageUrl")
    String imageUrl;

    @SerializedName("schedule")
    TalkSchedule schedule;

    @SerializedName("speaker")
    Speaker speaker;

    public Talk() {};

    public Talk(long id, String title, String description, String imageUrl, TalkSchedule schedule, Speaker speaker) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.schedule = schedule;
        this.speaker = speaker;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public TalkSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(TalkSchedule schedule) {
        this.schedule = schedule;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }
}
