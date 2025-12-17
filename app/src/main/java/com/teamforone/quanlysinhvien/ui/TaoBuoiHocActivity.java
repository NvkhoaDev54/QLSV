package com.teamforone.quanlysinhvien.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;
import com.teamforone.quanlysinhvien.service.BuoiHocService;
import com.teamforone.quanlysinhvien.service.SinhVienService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaoBuoiHocActivity extends AppCompatActivity {

    // ĐÃ XÓA spinnerGiangVien tại đây
    private Spinner spinnerMonHoc, spinnerLop;
    private Spinner spinnerTietBatDau, spinnerTietKetThuc;
    private EditText etNgayHoc, etGhiChu;
    private Button btnSave, btnCancel;

    private BuoiHocService buoiHocService;
    private SinhVienService sinhVienService;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_buoi_hoc);

        initViews();
        initServices();
        setupToolbar();
        setupSpinners();
        setupDatePicker();
        setupButtons();
    }

    private void initViews() {
        spinnerMonHoc = findViewById(R.id.spinnerMonHoc);
        // ĐÃ XÓA dòng ánh xạ spinnerGiangVien
        spinnerLop = findViewById(R.id.spinnerLop);
        spinnerTietBatDau = findViewById(R.id.spinnerTietBatDau);
        spinnerTietKetThuc = findViewById(R.id.spinnerTietKetThuc);
        etNgayHoc = findViewById(R.id.etNgayHoc);
        etGhiChu = findViewById(R.id.etGhiChu);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        calendar = Calendar.getInstance();
    }

    private void initServices() {
        buoiHocService = BuoiHocService.getInstance(this);
        sinhVienService = SinhVienService.getInstance(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupSpinners() {
        // Spinner Môn học
        List<String> monHocList = new ArrayList<>();
        monHocList.add("Chọn môn học");
        monHocList.add("MH001 - Lập trình Java");
        monHocList.add("MH002 - Cơ sở dữ liệu");
        monHocList.add("MH003 - Mạng máy tính");
        ArrayAdapter<String> monHocAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, monHocList);
        monHocAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonHoc.setAdapter(monHocAdapter);

        // ĐÃ XÓA toàn bộ đoạn code setup cho spinnerGiangVien tại đây

        // Spinner Lớp
        List<String> lopList = sinhVienService.getAllLopNames();
        if (!lopList.isEmpty()) {
            lopList.remove(0); // Xóa "Tất cả" nếu có
        }
        lopList.add(0, "Chọn lớp");
        ArrayAdapter<String> lopAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lopList);
        lopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(lopAdapter);

        // Spinner Tiết học
        List<String> tietList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            tietList.add("Tiết " + i);
        }
        ArrayAdapter<String> tietAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tietList);
        tietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTietBatDau.setAdapter(tietAdapter);
        spinnerTietKetThuc.setAdapter(tietAdapter);
    }

    private void setupDatePicker() {
        etNgayHoc.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        etNgayHoc.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });
    }

    private void setupButtons() {
        btnSave.setOnClickListener(v -> saveBuoiHoc());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveBuoiHoc() {
        // Validate
        if (spinnerMonHoc.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Vui lòng chọn môn học", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerLop.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Vui lòng chọn lớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etNgayHoc.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày học", Toast.LENGTH_SHORT).show();
            return;
        }

        int tietBatDau = spinnerTietBatDau.getSelectedItemPosition() + 1;
        int tietKetThuc = spinnerTietKetThuc.getSelectedItemPosition() + 1;

        if (tietBatDau > tietKetThuc) {
            Toast.makeText(this, "Tiết bắt đầu phải nhỏ hơn tiết kết thúc", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy dữ liệu
        String monHocStr = spinnerMonHoc.getSelectedItem().toString();
        String maMonHoc = monHocStr.split(" - ")[0];

        // ĐÃ XÓA logic lấy maGV từ Spinner. Gán tạm là null hoặc giá trị mặc định.
        String maGV = null;

        String lopStr = spinnerLop.getSelectedItem().toString();
        String maLop = lopStr.split(" - ")[0];

        String ngayHoc = etNgayHoc.getText().toString();
        String ghiChu = etGhiChu.getText().toString().trim();

        // Tạo đối tượng BuoiHoc
        BuoiHoc buoiHoc = new BuoiHoc();
        buoiHoc.setMaMonHoc(maMonHoc);
        buoiHoc.setMaGV(maGV); // Vẫn set để tránh lỗi logic, nhưng giá trị là null
        buoiHoc.setMaLop(maLop);
        buoiHoc.setNgayHoc(ngayHoc);
        buoiHoc.setTietBatDau(tietBatDau);
        buoiHoc.setTietKetThuc(tietKetThuc);
        buoiHoc.setGhiChu(ghiChu);

        // Lưu vào Database qua Service
        if (buoiHocService.createBuoiHoc(buoiHoc)) {
            Toast.makeText(this, "Tạo buổi học thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Tạo buổi học thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
