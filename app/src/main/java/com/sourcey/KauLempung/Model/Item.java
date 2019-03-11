package com.sourcey.KauLempung.Model;

public class Item {

    //melakukan inisiasi untuk setter getter
    private int mImageResources;
    private String text;
    private String text2;

    public Item(int mimageResources,String text1,String text3){
        mImageResources = mimageResources;
        text = text1;
        text2 = text3;
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

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

}
