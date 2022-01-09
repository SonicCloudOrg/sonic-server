package org.cloud.sonic.folder.models;

public class FindResult {
    private int x;
    private int y;
    private String url;
    private int time;

    public FindResult() {
        x = 0;
        y = 0;
        url = "";
        time = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FindResult{" +
                "x=" + x +
                ", y=" + y +
                ", url='" + url + '\'' +
                ", time=" + time +
                '}';
    }
}
