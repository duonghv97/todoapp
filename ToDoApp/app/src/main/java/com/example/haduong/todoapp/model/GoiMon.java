package com.example.haduong.todoapp.model;

public class GoiMon {
    // gọi món thì cần món ăn cần gọi, mã nhân viên là để biết người nào quản lý
    // và mã gọi món để phân biệt đối tượng gọi món của ai
    private int maBan;
    private int maNhanVien;
    private int maGoiMon;
    private String statusPayment; // status lưu trạng thái đã thanh toán hay chưa thanh toán của khách hàng
    private String dayOrder;

    public GoiMon() {

    }
    public GoiMon(int maBan,int maNhanVien,int maGoiMon,String statusPayment,String dayOrder) {
        this.maBan = maBan;
        this.maNhanVien = maNhanVien;
        this.maGoiMon = maGoiMon;
        this.statusPayment = statusPayment;
        this.dayOrder = dayOrder;
    }

    public int getMaBan() {
        return maBan;
    }

    public void setMaBan(int maBan) {
        this.maBan = maBan;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getMaGoiMon() {
        return maGoiMon;
    }

    public void setMaGoiMon(int maGoiMon) {
        this.maGoiMon = maGoiMon;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }

    public String getDayOrder() {
        return dayOrder;
    }

    public void setDayOrder(String dayOrder) {
        this.dayOrder = dayOrder;
    }
}
