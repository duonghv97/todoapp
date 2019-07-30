package com.example.haduong.todoapp.model;

public class BanAn {
    private int maBanAn;
    private String tenBanAn;
    private boolean tinhTrang;
    public BanAn() {

    }
    public BanAn(int maBanAn,String tenBanAn,boolean tinhTrang) {
        this.maBanAn = maBanAn;
        this.tenBanAn = tenBanAn;
        this.tinhTrang = tinhTrang;
    }
    public void setMaBanAn(int maBanAn) {
        this.maBanAn = maBanAn;
    }
    public int getMaBanAn() {
        return  maBanAn;
    }

    public String getTenBanAn() {
        return tenBanAn;
    }

    public void setTenBanAn(String tenBanAn) {
        this.tenBanAn = tenBanAn;
    }

    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
