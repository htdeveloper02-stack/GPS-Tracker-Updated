package gps.trackerid.location.models;

public class AreaModel {
    public String area;
    public String code;
    public String country;

    public AreaModel(String area, String code, String country) {
        this.area = area;
        this.code = code;
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

