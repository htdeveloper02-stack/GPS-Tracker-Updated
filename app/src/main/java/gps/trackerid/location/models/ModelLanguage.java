package gps.trackerid.location.models;

import java.io.Serializable;

public class ModelLanguage implements Serializable {

    String language;

    int icon;

    public ModelLanguage(String language2, int icon) {
        this.language = language2;
        this.icon = icon;
    }

    public String getLanguage() {
        return this.language;
    }

    public int getIcon() {
        return icon;
    }

}

