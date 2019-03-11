package com.sourcey.KauLempung.Model;

/**
 * Created by Umam on image_4/16/2018.
 */

public class Comment {

    String mPengomentar;

    String mKomentar;

    String mFoto;


    public Comment() {

    }



    public Comment(String pengomentar, String komentar, String foto) {

        this.mPengomentar = pengomentar;
        this.mKomentar = komentar;
        this.mFoto = foto;
    }

    public String getmPengomentar() {
        return mPengomentar;
    }

    public String getmKomentar() {
        return mKomentar;
    }

    public String getmFoto() {
        return mFoto;
    }
}
