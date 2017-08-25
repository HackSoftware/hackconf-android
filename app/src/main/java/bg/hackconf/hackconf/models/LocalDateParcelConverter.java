package bg.hackconf.hackconf.models;

import android.os.Parcel;

import org.joda.time.LocalDate;
import org.parceler.ParcelConverter;

import bg.hackconf.hackconf.services.LocalDateAdapter;

public class LocalDateParcelConverter implements ParcelConverter<LocalDate> {
    @Override
    public void toParcel(LocalDate input, Parcel parcel) {
        parcel.writeString(input.toString(LocalDateAdapter.FORMAT));
    }

    @Override
    public LocalDate fromParcel(Parcel parcel) {
        return LocalDate.parse(parcel.readString());
    }
}
