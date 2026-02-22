package gps.trackerid.location.models;

public class ItemStopwatch {
    private int status = 0;
    private long time;

    public ItemStopwatch(long j) {
        this.time = j;
    }

    public long getTime() {
        return this.time;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

}
