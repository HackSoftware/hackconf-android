package bg.hackconf.hackconf.services;

import org.joda.time.LocalDate;
import org.parceler.Parcel;
import org.parceler.ParcelClass;

import java.util.List;
import java.util.Map;

import bg.hackconf.hackconf.models.LocalDateParcelConverter;
import bg.hackconf.hackconf.models.Talk;

@ParcelClass(
    value = LocalDate.class,
    annotation = @Parcel(converter = LocalDateParcelConverter.class)
)
@Parcel
public class ScheduleResponse {
    Map<LocalDate, List<Talk>> result;

    public Map<LocalDate, List<Talk>> getResult() {
        return result;
    }

    public void setResult(Map<LocalDate, List<Talk>> result) {
        this.result = result;
    }
}