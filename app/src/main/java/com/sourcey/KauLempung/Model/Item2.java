package com.sourcey.KauLempung.Model;

public class Item2 {

    //melakukan inisiasi untuk setter getter
    private int mImageResources;
    private String text;
    private int color;

    public Item2(int mimageResources, String text1, int color1){
        mImageResources = mimageResources;
        text = text1;
        color = color1;
    }

    // setter getter untuk image, text 1 = nama, text2 = pekerjaan agar bisa diambil nilai nya saat list di klik pada recyclerview
    public int getmImageResources() {
        return mImageResources;
    }

    public void setmImageResources(int mImageResources) {
        this.mImageResources = mImageResources;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
