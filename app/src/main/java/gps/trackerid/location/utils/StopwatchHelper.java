package gps.trackerid.location.utils;

import java.util.ArrayList;
import java.util.Iterator;

import gps.trackerid.location.models.ItemStopwatch;

public class StopwatchHelper {
    public long add;
    public long addLoop;
    public ArrayList<ItemStopwatch> arr = new ArrayList<>();
    public long loop;
    public boolean play = false;
    public long start;
    public long time;

    public void start() {
        if (this.time == 0) {
            this.add = 0;
            long currentTimeMillis = System.currentTimeMillis();
            this.start = currentTimeMillis;
            this.loop = currentTimeMillis;
        } else {
            this.start = System.currentTimeMillis();
            this.loop = System.currentTimeMillis();
        }
        this.play = true;
    }

    public void pause() {
        this.play = false;
        this.add += System.currentTimeMillis() - this.start;
        this.addLoop += System.currentTimeMillis() - this.loop;
    }

    public void stop() {
        this.play = false;
        this.add = 0;
        this.addLoop = 0;
        this.time = 0;
        this.arr.clear();
    }

    public long getTime() {
        return this.time;
    }

    public void makeTime() {
        this.time = (this.add + System.currentTimeMillis()) - this.start;
    }

    public void loop() {
        this.arr.add(new ItemStopwatch((((this.addLoop + System.currentTimeMillis()) - this.loop) / 10) * 10));
        this.loop = System.currentTimeMillis();
        this.addLoop = 0;
        ItemStopwatch itemStopwatch = this.arr.get(0);
        ItemStopwatch itemStopwatch2 = this.arr.get(0);
        Iterator<ItemStopwatch> it = this.arr.iterator();
        while (it.hasNext()) {
            ItemStopwatch next = it.next();
            next.setStatus(0);
            if (next.getTime() < itemStopwatch.getTime()) {
                itemStopwatch = next;
            }
            if (next.getTime() > itemStopwatch2.getTime()) {
                itemStopwatch2 = next;
            }
        }
        itemStopwatch.setStatus(-1);
        if (itemStopwatch2.getStatus() == 0) {
            itemStopwatch2.setStatus(1);
        }
    }
}
