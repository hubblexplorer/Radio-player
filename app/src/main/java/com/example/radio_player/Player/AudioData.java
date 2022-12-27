package com.example.radio_player.Player;

public class AudioData {
    private String type;
    private String displayname;
    private String data;
    private byte[] image;
    private String autor;
    private int duration;

    public AudioData(String type, String displayname, String data, byte[] image, String autor, int duration) {
        this.type = type;
        this.displayname = displayname;
        this.data = data;
        this.image = image;
        this.autor = autor;
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getData() {
        return data;
    }

    public byte[] getImage() {
        return image;
    }

    public String getAutor() {
        return autor;
    }

    public int getDuration() {
        return duration;
    }
}
