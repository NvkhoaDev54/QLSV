package com.teamforone.quanlysinhvien.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;
import com.teamforone.quanlysinhvien.domain.model.DiemDanh;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;
import com.teamforone.quanlysinhvien.domain.uiadapters.DiemDanhAdapter;
import com.teamforone.quanlysinhvien.service.BuoiHocService;
import com.teamforone.quanlysinhvien.service.DiemDanhService;
import com.teamforone.quanlysinhvien.service.SinhVienService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiemDanhActivity extends AppCompatActivity {

    private TextView tvMonHoc, tvLop, tvNgayHoc, tvTiet;
    private TextView tvCoMat, tvVang;
    private RecyclerView recyclerView;
    private Button btnSave, btnDiemDanhTatCa, btnVangTatCa;

    private BuoiHocService buoiHocService;
    private DiemDanhService diemDanhService;
    private SinhVienService sinhVienService;

    private DiemDanhAdapter adapter;
    private List<SinhVien> sinhVienList;
    private Map<String, DiemDanh> diemDanhMap;
    private BuoiHoc buoiHoc;
    private int buoiHocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diem_danh);

        buoiHocId = getIntent().getIntExtra("BUOI_HOC_ID", -1);
        if (buoiHocId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy buổi học", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        initServices();
        setupToolbar();
        loadBuoiHoc();
        setupRecyclerView();
        setupButtons();
        loadDiemDanh();
    }

    private void initViews() {
        tvMonHoc = findViewById(R.id.tvMonHoc);
        tvLop = findViewById(R.id.tvLop);
        tvNgayHoc = findViewById(R.id.tvNgayHoc);
        tvTiet = findViewById(R.id.tvTiet);
        tvCoMat = findViewById(R.id.tvCoMat);
        tvVang = findViewById(R.id.tvVang);
        recyclerView = findViewById(R.id.recyclerView);
        btnSave = findViewById(R.id.btnSave);
        btnDiemDanhTatCa = findViewById(R.id.btnDiemDanhTatCa);
        btnVangTatCa = findViewById(R.id.btnVangTatCa);
    }

    private void initServices() {
        buoiHocService = BuoiHocService.getInstance(this);
        diemDanhService = DiemDanhService.getInstance(this);
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

    private void loadBuoiHoc() {
        buoiHoc = buoiHocService.getBuoiHocById(buoiHocId);
        if (buoiHoc == null) {
            Toast.makeText(this, "Không tìm thấy thông tin buổi học", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvMonHoc.setText("Môn học: " + (buoiHoc.getTenMonHoc() != null ?
                buoiHoc.getTenMonHoc() : buoiHoc.getMaMonHoc()));
        tvLop.setText("Lớp: " + (buoiHoc.getTenLop() != null ?
                buoiHoc.getTenLop() : buoiHoc.getMaLop()));
        tvNgayHoc.setText("Ngày: " + buoiHoc.getNgayHoc());
        tvTiet.setText("Tiết: " + buoiHoc.getTietBatDau() + "-" + buoiHoc.getTietKetThuc());
    }

    private void setupRecyclerView() {
        sinhVienList = sinhVienService.filterByLop(buoiHoc.getMaLop());
        diemDanhMap = new HashMap<>();

        adapter = new DiemDanhAdapter(this, sinhVienList, diemDanhMap, new DiemDanhAdapter.OnDiemDanhChangeListener() {
            @Override
            public void onStatusChanged(String maSV, boolean coMat) {
                updateStats();
            }

            @Override
            public void onNoteChanged(String maSV, String ghiChu) {
                if (diemDanhMap.containsKey(maSV)) {
                    diemDanhMap.get(maSV).setGhiChu(ghiChu);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupButtons() {
        btnDiemDanhTatCa.setOnClickListener(v -> {
            adapter.setAllStatus(true);
            updateStats();
        });

        btnVangTatCa.setOnClickListener(v -> {
            adapter.setAllStatus(false);
            updateStats();
        });

        btnSave.setOnClickListener(v -> saveDiemDanh());
    }

    private void loadDiemDanh() {
        List<DiemDanh> existingList = diemDanhService.getDiemDanhByBuoiHoc(buoiHocId);

        if (existingList != null && !existingList.isEmpty()) {
            for (DiemDanh dd : existingList) {
                diemDanhMap.put(dd.getMaSV(), dd);
            }
            adapter.notifyDataSetChanged();
        } else {
            // Initialize with default values (all present)
            for (SinhVien sv : sinhVienList) {
                DiemDanh dd = new DiemDanh();
                dd.setBuoiHocId(buoiHocId);
                dd.setMaSV(sv.getMaSV());
                dd.setHoTenSV(sv.getHoTen());
                dd.setCoMat(true);
                dd.setGhiChu("");
                diemDanhMap.put(sv.getMaSV(), dd);
            }
        }

        updateStats();
    }

    private void updateStats() {
        int coMat = 0;
        int vang = 0;

        for (DiemDanh dd : diemDanhMap.values()) {
            if (dd.isCoMat()) {
                coMat++;
            } else {
                vang++;
            }
        }

        tvCoMat.setText("Có mặt: " + coMat);
        tvVang.setText("Vắng: " + vang);
    }

    private void saveDiemDanh() {
        int successCount = 0;
        int totalCount = diemDanhMap.size();

        for (DiemDanh dd : diemDanhMap.values()) {
            if (diemDanhService.saveDiemDanh(dd)) {
                successCount++;
            }
        }

        if (successCount == totalCount) {
            Toast.makeText(this, "Lưu điểm danh thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lưu thành công " + successCount + "/" + totalCount + " sinh viên",
                    Toast.LENGTH_LONG).show();
        }
    }
}