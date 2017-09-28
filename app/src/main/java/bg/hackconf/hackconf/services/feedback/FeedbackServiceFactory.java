package bg.hackconf.hackconf.services.feedback;

import bg.hackconf.hackconf.services.HackConfApi;

public class FeedbackServiceFactory {
    private static final FeedbackService feedback = HackConfApi
            .retrofit.create(FeedbackService.class);

    public static FeedbackService getInstance() { return feedback; }
}
