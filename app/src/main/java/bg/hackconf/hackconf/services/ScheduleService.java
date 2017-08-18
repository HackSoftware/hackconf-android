package bg.hackconf.hackconf.services;

import java.util.List;

import bg.hackconf.hackconf.models.Talk;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ScheduleService {
    @GET("/schedule/{year}/{month}/{day}")
    Call<List<Talk>> get (@Path("year") int year, @Path("month") int month, @Path("day") int day);
}
