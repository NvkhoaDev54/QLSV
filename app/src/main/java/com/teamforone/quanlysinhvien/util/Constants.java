package com.teamforone.quanlysinhvien.util;

public class Constants {
    // Database
    public static final String DATABASE_NAME = "QLSV.db";
    public static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_LOP = "LOP";
    public static final String TABLE_SINHVIEN = "SINHVIEN";

    // LOP Table Columns
    public static final String COL_MA_LOP = "maLop";
    public static final String COL_TEN_LOP = "tenLop";
    public static final String COL_KHOA = "khoa";

    // SINHVIEN Table Columns
    public static final String COL_MA_SV = "maSV";
    public static final String COL_HO_TEN = "hoTen";
    public static final String COL_NGAY_SINH = "ngaySinh";
    public static final String COL_GIOI_TINH = "gioiTinh";
    public static final String COL_DIA_CHI = "diaChi";

    // Gender
    public static final String GENDER_MALE = "Nam";
    public static final String GENDER_FEMALE = "Nữ";

    // Filter Options
    public static final String FILTER_ALL = "Tất cả";

    // Intent Extras
    public static final String EXTRA_MA_SV = "maSV";
    public static final String EXTRA_HO_TEN = "hoTen";
    public static final String EXTRA_NGAY_SINH = "ngaySinh";
    public static final String EXTRA_GIOI_TINH = "gioiTinh";
    public static final String EXTRA_DIA_CHI = "diaChi";
    public static final String EXTRA_MA_LOP = "maLop";

    // Messages
    public static final String MSG_ADD_SUCCESS = "Thêm sinh viên thành công!";
    public static final String MSG_ADD_FAILED = "Thêm sinh viên thất bại!";
    public static final String MSG_UPDATE_SUCCESS = "Cập nhật sinh viên thành công!";
    public static final String MSG_UPDATE_FAILED = "Cập nhật sinh viên thất bại!";
    public static final String MSG_DELETE_SUCCESS = "Xóa thành công!";
    public static final String MSG_DELETE_FAILED = "Xóa thất bại!";
    public static final String MSG_EMPTY_FIELD = "Vui lòng điền đầy đủ thông tin!";
    public static final String MSG_DUPLICATE_ID = "Mã sinh viên đã tồn tại!";
    public static final String MSG_NO_DATA = "Không có sinh viên nào";

    // Log Tags
    public static final String LOG_TAG_DB = "DatabaseHelper";
    public static final String LOG_TAG_DAO = "SinhVienDAO";
    public static final String LOG_TAG_SERVICE = "SinhVienService";
    public static final String LOG_TAG_UI = "UI";
}
