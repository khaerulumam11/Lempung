package com.sourcey.KauLempung.Model;

import java.io.Serializable;

public class Produk implements Serializable {

    public String image,title,harga,namaprod,alamat,nohp,user,key,id;

    public Produk(){

    }

    public Produk(String title, String image,String harga, String namaprod,String nohp, String alamat,String id,String user){
        this.title = title;
        this.image = image;
        this.harga = harga;
        this.namaprod = namaprod;
        this.nohp = nohp;
        this.alamat = alamat;
        this.id = id;
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getNamaprod() {
        return namaprod;
    }

    public void setNamaprod(String namaprod) {
        this.namaprod = namaprod;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
