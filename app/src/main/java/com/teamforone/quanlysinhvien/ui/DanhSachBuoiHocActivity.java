package com.teamforone.quanlysinhvien.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;
import com.teamforone.quanlysinhvien.domain.uiadapters.BuoiHocAdapter;
import com.teamforone.quanlysinhvien.service.BuoiHocService;
import com.teamforone.quanlysinhvien.service.SinhVienService;

import java.util.ArrayList;
import java.util.List;

public class DanhSachBuoiHocActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private Spinner spinnerLop;
    private FloatingActionButton fabAdd, fabHome;

    private BuoiHocService buoiHocService;
    private SinhVienService sinhVienService;
    private BuoiHocAdapter adapter;
    private List<BuoiHoc> buoiHocList;
    private List<String> lopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_buoi_hoc);

        initViews();
        initServices();
        setupToolbar();
        setupRecyclerView();
        setupSpinner();
        setupButtons();
        loadData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);
        spinnerLop = findViewById(R.id.spinnerLop);
        fabAdd = findViewById(R.id.fabAdd);
        fabHome = findViewById(R.id.fabHome);
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

    private void setupRecyclerView() {
        buoiHocList = new ArrayList<>();
        adapter = new BuoiHocAdapter(this, buoiHocList, buoiHoc -> {
            Intent intent = new Intent(DanhSachBuoiHocActivity.this, DiemDanhActivity.class);
            intent.putExtra("BUOI_HOC_ID", buoiHoc.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupSpinner() {
        lopList = sinhVienService.getAllLopNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, lopList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(spinnerAdapter);

        spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByLop(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void setupButtons() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, TaoBuoiHocActivity.class);
            startActivity(intent);
        });

        fabHome.setOnClickListener(v -> finish());

        findViewById(R.id.btnResetFilter).setOnClickListener(v -> {
            spinnerLop.setSelection(0);
        });
    }

    private void loadData() {
        buoiHocList.clear();
        buoiHocList.addAll(buoiHocService.getAllBuoiHoc());
        updateUI();
    }

    private void filterByLop(int position) {
        buoiHocList.clear();

        if (position == 0) {
            buoiHocList.addAll(buoiHocService.getAllBuoiHoc());
        } else {
            String selected = lopList.get(position);
            String maLop = selected.split(" - ")[0];
            buoiHocList.addAll(buoiHocService.getBuoiHocByLop(maLop));
        }

        updateUI();
    }

    private void updateUI() {
        if (buoiHocList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}