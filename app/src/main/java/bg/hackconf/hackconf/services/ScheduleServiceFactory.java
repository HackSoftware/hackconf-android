package bg.hackconf.hackconf.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleServiceFactory {
    private static final String API_URL = "http://10.11.12.44:9000";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private static final ScheduleService schedule = retrofit.create(ScheduleService.class);

    public static ScheduleService getInstance() { return schedule; }
}
