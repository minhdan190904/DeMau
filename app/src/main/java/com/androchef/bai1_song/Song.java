package com.androchef.bai1_song;

import java.io.Serializable;

public class Song implements Serializable {
    private Integer id;
    private String name;
    private String singer;
    private Double time;

    public Song(Integer id, String name, String singer, Double time) {
        this.id = id;
        this.name = name;
        this.singer = singer;
        this.time = time;
    }

    public Song() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public Double getTime() {
        return time;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
