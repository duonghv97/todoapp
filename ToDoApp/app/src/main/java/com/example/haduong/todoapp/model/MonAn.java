package com.example.haduong.todoapp.model;

public class MonAn {
    private int maMonAn;
    private int maLoai;
    private String tenMonAn;
    private String giaTien;
    private String hinhAnh;
    public MonAn() {

    }

    public MonAn(int maMonAn, int maLoai, String tenMonAn, String giaTien,String hinhAnh) {
        this.maMonAn = maMonAn;
        this.maLoai = maLoai;
        this.tenMonAn = tenMonAn;
        this.giaTien = giaTien;
        this.hinhAnh = hinhAnh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(int maMonAn) {
        this.maMonAn = maMonAn;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public String getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(String giaTien) {
        this.giaTien = giaTien;
    }
}
