package bg.hackconf.hackconf.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ScheduleService {
    @POST("/schedule")
    Call<ScheduleResponse> get (@Body ScheduleRequest body);
}
