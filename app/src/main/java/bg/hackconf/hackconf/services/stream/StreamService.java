package bg.hackconf.hackconf.services.stream;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StreamService {
    @GET("/streams/1")
    Call<Stream> getHackConfStream();
}
