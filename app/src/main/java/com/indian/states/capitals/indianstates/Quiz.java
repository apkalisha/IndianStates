package com.indian.states.capitals.indianstates;

public class Quiz {
    private String Type;
    private String Image;
    public Quiz(){

    }
    public Quiz(String type, String image) {
        Type = type;
        Image = image;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
