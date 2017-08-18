package bg.hackconf.hackconf.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class TalkSchedule {
    @SerializedName("id")
    long id;

    @SerializedName("startDate")
    String startDate;

    @SerializedName("startTime")
    String startTime;

    @SerializedName("endDate")
    String endDate;

    @SerializedName("endTime")
    String endTime;

    @SerializedName("delay")
    int delay;

    @SerializedName("canceled")
    boolean canceled;

    public TalkSchedule() {}

    public TalkSchedule(long id, String startDate, String startTime, String endDate, String endTime, int delay, boolean canceled) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.delay = delay;
        this.canceled = canceled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
