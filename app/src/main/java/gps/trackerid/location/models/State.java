package gps.trackerid.location.models;

public class State {

    String operator;

    String state;

    public int icon;

    public int getSetMobileno() {
        return setMobileno;
    }

    public void setSetMobileno(int setMobileno) {
        this.setMobileno = setMobileno;
    }

    public int setMobileno;

    String mLat;

    String mLang;

    public String getOpertator() {
        return this.operator;
    }

    
    public void setOperator(String str) {
        this.operator = str;
    }

    
    public String getState() {
        return this.state;
    }

    
    public void setstate(String str) {
        this.state = str;
    }

    
    public int getIcon() {
        return this.icon;
    }

    
    public void setIcon(int i) {
        this.icon = i;
    }

    
    public String getLat() {
        return this.mLat;
    }

    
    public void setLat(String str) {
        this.mLat = str;
    }

    
    public String getLang() {
        return this.mLang;
    }

    
    public void setLang(String str) {
        this.mLang = str;
    }
}
