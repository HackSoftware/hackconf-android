package bg.hackconf.hackconf.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Speaker {
    @SerializedName("id")
    long id;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("bio")
    String bio;

    @SerializedName("youtubeId")
    String youtubeId;

    public Speaker() {}

    public Speaker(long id, String firstName, String lastName, String bio, String youtubeId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.youtubeId = youtubeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }
}
