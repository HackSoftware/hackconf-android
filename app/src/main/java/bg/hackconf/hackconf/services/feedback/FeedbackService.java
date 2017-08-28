package bg.hackconf.hackconf.services.feedback;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FeedbackService {
    @POST("/talks/feedback")
    Call<Integer> create(@Body Feedback body);

    @POST("/talks/feedback")
    Call<Integer> createDetailed(@Body DetailedFeedback body);
}
