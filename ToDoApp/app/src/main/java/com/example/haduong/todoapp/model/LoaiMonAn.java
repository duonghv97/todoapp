package com.example.haduong.todoapp.model;

public class LoaiMonAn {
    private int maLoai;
    private String tenLoai;

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    private String hinhAnh;

    public LoaiMonAn(int maLoai,String tenLoai,String hinhAnh) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.hinhAnh = hinhAnh;
    }

    public LoaiMonAn() {

    }
    public int getMaLoai() {
        return maLoai;
    }
    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }
    public String getTenLoai() {
        return tenLoai;
    }
    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
}
