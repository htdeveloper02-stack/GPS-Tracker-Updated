package gps.trackerid.location.models;

public class NearbyItem {

    private int iconRes;
    private String title;
    private String mapUrl;

    public NearbyItem(int iconRes, String title, String mapUrl) {
        this.iconRes = iconRes;
        this.title = title;
        this.mapUrl = mapUrl;
    }

    public int getIconRes() {
        return iconRes;
    }

    public String getTitle() {
        return title;
    }

    public String getMapUrl() {
        return mapUrl;
    }
}
