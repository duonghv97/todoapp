package com.example.haduong.todoapp.model;

public class ChiTietGoiMon {
    private int maGoiMon;
    private int maMonAn;
    private int soLuong;

    public ChiTietGoiMon() {

    }
    public ChiTietGoiMon(int maGoiMon, int maMonAn, int soLuong) {
        this.maGoiMon = maGoiMon;
        this.maMonAn = maMonAn;
        this.soLuong = soLuong;
    }

    public int getMaGoiMon() {
        return maGoiMon;
    }

    public void setMaGoiMon(int maGoiMon) {
        this.maGoiMon = maGoiMon;
    }

    public int getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(int maMonAn) {
        this.maMonAn = maMonAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
