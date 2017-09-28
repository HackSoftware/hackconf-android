package bg.hackconf.hackconf.services.stream;

import bg.hackconf.hackconf.services.HackConfApi;

public class StreamServiceFactory {
    private static final StreamService stream = HackConfApi
            .retrofit.create(StreamService.class);

    public static StreamService getInstance() { return stream; }
}
