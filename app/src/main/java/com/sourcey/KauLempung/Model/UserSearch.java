package com.sourcey.KauLempung.Model;

import java.io.Serializable;

public class UserSearch implements Serializable {

    public String name,email,image,alamat,nohp,id,key,user;

    public UserSearch(){

    }

    public UserSearch(String name, String email, String image, String alamat, String nohp, String id, String key, String user){
        this.name= name;
        this.email = email;
        this.image = image;
        this.alamat = alamat;
        this.nohp = nohp;
        this.id = id;
        this.key = key;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
