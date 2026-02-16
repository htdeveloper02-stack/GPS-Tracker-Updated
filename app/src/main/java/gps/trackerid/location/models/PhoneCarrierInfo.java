package gps.trackerid.location.models;

import java.io.Serializable;
import java.util.Objects;

public class PhoneCarrierInfo implements Serializable {

    private final String country;
    private final String carrier;

    public PhoneCarrierInfo(String country, String carrier) {
        this.country = country;
        this.carrier = carrier;
    }

    public String getCountry() {
        return country;
    }

    public String getCarrier() {
        return carrier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneCarrierInfo)) return false;
        PhoneCarrierInfo that = (PhoneCarrierInfo) o;
        return Objects.equals(country, that.country) &&
                Objects.equals(carrier, that.carrier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, carrier);
    }

    @Override
    public String toString() {
        return "PhoneCarrierInfo{" +
                "country='" + country + '\'' +
                ", carrier='" + carrier + '\'' +
                '}';
    }
}

