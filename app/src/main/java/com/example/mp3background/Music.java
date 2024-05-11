package com.example.mp3background;

public class Music {
    int mp3;

    long id;

    boolean isFav;

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public Music() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMp3() {
        return mp3;
    }

    public void setMp3(int mp3) {
        this.mp3 = mp3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Music(long id, int mp3, String name, Boolean isFav) {
        this.isFav = isFav;
        this.id = id;
        this.mp3 = mp3;
        this.name = name;
    }

    String name;
}
