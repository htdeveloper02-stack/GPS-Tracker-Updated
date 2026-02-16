package gps.trackerid.location.models.users;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class DataUser implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;
    private  String code;
    private String name;

    private  double latitude;
    private  double longitude;
    private boolean isSelected;
    private  boolean isSharing;
    private String fcmToken;

    // Constructors
    public DataUser(String code, String name, double latitude, double longitude,
                    boolean isSelected, boolean isSharing, String fcmToken) {
        this.code = code;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSelected = isSelected;
        this.isSharing = isSharing;
        this.fcmToken = fcmToken;
    }

    // Default constructor (needed for Firebase)
    public DataUser() {
        this("", "", 0.0, 0.0, false, false, null);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSharing(boolean sharing) {
        isSharing = sharing;
    }
    // Getters and Setters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isSharing() {
        return isSharing;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    // Optional: equals(), hashCode(), toString() for better logging/debugging
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DataUser)) return false;
        DataUser other = (DataUser) obj;
        return code.equals(other.code) &&
                name.equals(other.name) &&
                latitude == other.latitude &&
                longitude == other.longitude &&
                isSelected == other.isSelected &&
                isSharing == other.isSharing &&
                ((fcmToken == null && other.fcmToken == null) ||
                        (fcmToken != null && fcmToken.equals(other.fcmToken)));
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + name.hashCode();
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (isSelected ? 1 : 0);
        result = 31 * result + (isSharing ? 1 : 0);
        result = 31 * result + (fcmToken != null ? fcmToken.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataUser{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isSelected=" + isSelected +
                ", isSharing=" + isSharing +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
