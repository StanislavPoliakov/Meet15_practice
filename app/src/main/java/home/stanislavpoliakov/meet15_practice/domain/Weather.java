package home.stanislavpoliakov.meet15_practice.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import home.stanislavpoliakov.meet15_practice.domain.response_data.WCurrently;
import home.stanislavpoliakov.meet15_practice.domain.response_data.WDaily;
import home.stanislavpoliakov.meet15_practice.domain.response_data.WFlags;
import home.stanislavpoliakov.meet15_practice.domain.response_data.WHourly;

@Entity (tableName = "weather")
public class Weather {
    @PrimaryKey (autoGenerate = true)
    public int id;

    public double latitude;
    public double longitude;
    public String timezone;

    @Embedded (prefix = "currently")
    public WCurrently currently;

    @Embedded (prefix = "hourly")
    public WHourly hourly;

    @Embedded (prefix = "daily")
    public WDaily daily;

    @Embedded (prefix = "flags")
    public WFlags flags;
    public double offset;
}
