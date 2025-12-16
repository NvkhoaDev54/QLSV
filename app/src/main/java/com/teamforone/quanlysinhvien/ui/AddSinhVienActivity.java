package com.teamforone.quanlysinhvien.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;
import com.teamforone.quanlysinhvien.service.SinhVienService;
import com.teamforone.quanlysinhvien.util.AppLogger;
import com.teamforone.quanlysinhvien.util.Constants;

import java.util.Calendar;
import java.util.List;

public class AddSinhVienActivity extends AppCompatActivity {
    private TextInputEditText etMaSV, etHoTen, etNgaySinh, etDiaChi;
    private RadioGroup rgGioiTinh;
    private RadioButton rbNam, rbNu;
    private Spinner spinnerLop;
    private Button btnSave, btnCancel;

    private SinhVienService sinhVienService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sinhvien);

        AppLogger.d(Constants.LOG_TAG_UI, "AddSinhVienActivity created");

        setupToolbar();
        initViews();
        initService();
        setupSpinner();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        etMaSV = findViewById(R.id.etMaSV);
        etHoTen = findViewById(R.id.etHoTen);
        etNgaySinh = findViewById(R.id.etNgaySinh);
        etDiaChi = findViewById(R.id.etDiaChi);
        rgGioiTinh = findViewById(R.id.rgGioiTinh);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        spinnerLop = findViewById(R.id.spinnerLop);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void initService() {
        sinhVienService = SinhVienService.getInstance(this);
    }

    private void setupSpinner() {
        List<String> lopList = sinhVienService.getAllLopNames();

        // Remove "Tất cả" option for Add/Edit screen
        if (lopList != null && lopList.size() > 0 && lopList.get(0).equals(Constants.FILTER_ALL)) {
            lopList.remove(0);
        }

        // Check if list is empty
        if (lopList == null || lopList.isEmpty()) {
            AppLogger.e(Constants.LOG_TAG_UI, "Lop list is empty!");
            Toast.makeText(this, "Không có lớp nào. Vui lòng thêm lớp trước!", Toast.LENGTH_LONG).show();
            return;
        }

        AppLogger.d(Constants.LOG_TAG_UI, "Setting up spinner with " + lopList.size() + " items");
        for (String lop : lopList) {
            AppLogger.d(Constants.LOG_TAG_UI, "Lop: " + lop);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                lopList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(adapter);
    }

    private void setupListeners() {
        etNgaySinh.setFocusable(false);
        etNgaySinh.setClickable(true);
        etNgaySinh.setLongClickable(false);

        etNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLogger.d(Constants.LOG_TAG_UI, "etNgaySinh clicked");
                showDatePicker();
            }
        });

        btnSave.setOnClickListener(v -> saveSinhVien());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        AppLogger.d(Constants.LOG_TAG_UI, "showDatePicker started");

        Calendar calendar = Calendar.getInstance();

        String currentDate = etNgaySinh.getText().toString();
        if (!currentDate.isEmpty()) {
            try {
                String[] parts = currentDate.split("/");
                if (parts.length == 3) {
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
                    calendar.set(Calendar.YEAR, Integer.parseInt(parts[2]));
                }
            } catch (Exception e) {
                AppLogger.e(Constants.LOG_TAG_UI, "Error parsing date", e);
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddSinhVienActivity.this,  // Hoặc EditSinhVienActivity.this
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                        etNgaySinh.setText(date);
                        AppLogger.d(Constants.LOG_TAG_UI, "Date selected: " + date);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
        AppLogger.d(Constants.LOG_TAG_UI, "DatePickerDialog displayed");
    }

    private void saveSinhVien() {
        String maSV = etMaSV.getText().toString().trim();
        String hoTen = etHoTen.getText().toString().trim();
        String ngaySinh = etNgaySinh.getText().toString().trim();
        String diaChi = etDiaChi.getText().toString().trim();

        if (!validateInput(maSV, hoTen)) {
            return;
        }

        if (sinhVienService.exists(maSV)) {
            Toast.makeText(this, Constants.MSG_DUPLICATE_ID, Toast.LENGTH_SHORT).show();
            etMaSV.requestFocus();
            return;
        }

        String gioiTinh = rbNam.isChecked() ? Constants.GENDER_MALE : Constants.GENDER_FEMALE;
        String selectedLop = spinnerLop.getSelectedItem().toString();
        String maLop = selectedLop.split(" - ")[0];

        SinhVien sinhVien = new SinhVien();
        sinhVien.setMaSV(maSV);
        sinhVien.setHoTen(hoTen);
        sinhVien.setNgaySinh(ngaySinh);
        sinhVien.setGioiTinh(gioiTinh);
        sinhVien.setDiaChi(diaChi);
        sinhVien.setMaLop(maLop);

        if (sinhVienService.addSinhVien(sinhVien)) {
            AppLogger.d(Constants.LOG_TAG_UI, "Sinh vien added successfully");
            Toast.makeText(this, Constants.MSG_ADD_SUCCESS, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, Constants.MSG_ADD_FAILED, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String maSV, String hoTen) {
        if (maSV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã sinh viên", Toast.LENGTH_SHORT).show();
            etMaSV.requestFocus();
            return false;
        }

        if (hoTen.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
            etHoTen.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
