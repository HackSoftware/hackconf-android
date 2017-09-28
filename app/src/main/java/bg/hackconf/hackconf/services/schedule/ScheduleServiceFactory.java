package bg.hackconf.hackconf.services.schedule;

import bg.hackconf.hackconf.services.HackConfApi;

public class ScheduleServiceFactory {
    private static final ScheduleService schedule = HackConfApi
            .retrofit.create(ScheduleService.class);

    public static ScheduleService getInstance() { return schedule; }
}
