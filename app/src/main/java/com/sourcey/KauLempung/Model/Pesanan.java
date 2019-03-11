package com.sourcey.KauLempung.Model;

public class Pesanan {
   public String gambar, namaproduk,jumlahpesanan,tglpesan,hargaproduk,total,key,id,user,status,bukti;

    public Pesanan(){

    }
    public Pesanan(String gambar, String namaproduk,String jumlahpesanan,String tglpesan,String hargaproduk, String total, String key, String id,String user, String status, String bukti){
        this.gambar = gambar;
        this.namaproduk = namaproduk;
        this.jumlahpesanan = jumlahpesanan;
        this.tglpesan = tglpesan;
        this.hargaproduk = hargaproduk;
        this.total = total;
        this.key = key;
        this.id = id;
        this.user = user;
        this.status = status;
        this.bukti = bukti;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public void setNamaproduk(String namaproduk) {
        this.namaproduk = namaproduk;
    }

    public String getJumlahpesanan() {
        return jumlahpesanan;
    }

    public void setJumlahpesanan(String jumlahpesanan) {
        this.jumlahpesanan = jumlahpesanan;
    }

    public String getTglpesan() {
        return tglpesan;
    }

    public void setTglpesan(String tglpesan) {
        this.tglpesan = tglpesan;
    }

    public String getHargaproduk() {
        return hargaproduk;
    }

    public void setHargaproduk(String hargaproduk) {
        this.hargaproduk = hargaproduk;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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
