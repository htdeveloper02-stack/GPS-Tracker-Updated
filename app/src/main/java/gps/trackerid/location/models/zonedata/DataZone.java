package gps.trackerid.location.models.zonedata;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "zones")
public class DataZone implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    private String address;
    private boolean alertOnEnter;
    private boolean alertOnExit;

    private boolean isSafe;
    private Double latitude;
    private Double longitude;
    private String name;
    private Integer zoneRadius;

    public DataZone() {
    }

    public DataZone(Long l, String name, boolean z, boolean z2, boolean z3, String address, Integer num, Double d, Double d2) {
//        this.id = l;
        this.name = name;
        this.isSafe = z;
        this.alertOnEnter = z2;
        this.alertOnExit = z3;
        this.address = address;
        this.zoneRadius = num;
        this.latitude = d;
        this.longitude = d2;
    }

    public DataZone(Long l, String str, boolean z, boolean z2, boolean z3, String str2, Integer num, Double d, Double d2, int i) {
        this((i & 1) != 0 ? null : l, str, z, z2, z3, str2, (i & 64) != 0 ? null : num, (i & 128) != 0 ? null : d, (i & 256) != 0 ? null : d2);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAlertOnEnter() {
        return alertOnEnter;
    }

    public void setAlertOnEnter(boolean alertOnEnter) {
        this.alertOnEnter = alertOnEnter;
    }

    public boolean isAlertOnExit() {
        return alertOnExit;
    }

    public void setAlertOnExit(boolean alertOnExit) {
        this.alertOnExit = alertOnExit;
    }

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getZoneRadius() {
        return zoneRadius;
    }

    public void setZoneRadius(Integer zoneRadius) {
        this.zoneRadius = zoneRadius;
    }

}

