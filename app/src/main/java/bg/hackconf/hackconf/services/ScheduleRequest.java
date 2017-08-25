package bg.hackconf.hackconf.services;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;

import java.util.List;

public class ScheduleRequest {
    @SerializedName("dates")
    List<LocalDate> dates;

    public ScheduleRequest(List<LocalDate> dates) {
        this.dates = dates;
    }
}
