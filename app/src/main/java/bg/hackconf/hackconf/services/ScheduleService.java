package bg.hackconf.hackconf.services;

import org.joda.time.LocalDate;

import java.util.List;

import bg.hackconf.hackconf.models.Talk;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ScheduleService {
    @POST("/schedule")
    Call<List<Talk>> get (@Body List<LocalDate> dates);
}
