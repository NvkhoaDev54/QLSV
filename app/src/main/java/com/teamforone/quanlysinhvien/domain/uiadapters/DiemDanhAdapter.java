package com.teamforone.quanlysinhvien.domain.uiadapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.DiemDanh;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;

import java.util.List;
import java.util.Map;

public class DiemDanhAdapter extends RecyclerView.Adapter<DiemDanhAdapter.ViewHolder> {

    private Context context;
    private List<SinhVien> sinhVienList;
    private Map<String, DiemDanh> diemDanhMap;
    private OnDiemDanhChangeListener listener;

    public interface OnDiemDanhChangeListener {
        void onStatusChanged(String maSV, boolean coMat);
        void onNoteChanged(String maSV, String ghiChu);
    }

    public DiemDanhAdapter(Context context, List<SinhVien> sinhVienList,
                           Map<String, DiemDanh> diemDanhMap, OnDiemDanhChangeListener listener) {
        this.context = context;
        this.sinhVienList = sinhVienList;
        this.diemDanhMap = diemDanhMap;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sinh_vien_diem_danh, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SinhVien sv = sinhVienList.get(position);
        DiemDanh dd = diemDanhMap.get(sv.getMaSV());

        holder.tvMaSV.setText(sv.getMaSV());
        holder.tvHoTen.setText(sv.getHoTen());

        // Remove listener temporarily
        holder.cbCoMat.setOnCheckedChangeListener(null);
        holder.etGhiChu.removeTextChangedListener(holder.textWatcher);

        if (dd != null) {
            holder.cbCoMat.setChecked(dd.isCoMat());
            holder.etGhiChu.setText(dd.getGhiChu() != null ? dd.getGhiChu() : "");
        } else {
            holder.cbCoMat.setChecked(true);
            holder.etGhiChu.setText("");
        }

        // Set listener back
        holder.cbCoMat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (dd != null) {
                dd.setCoMat(isChecked);
                if (listener != null) {
                    listener.onStatusChanged(sv.getMaSV(), isChecked);
                }
            }
        });

        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (dd != null) {
                    dd.setGhiChu(s.toString());
                    if (listener != null) {
                        listener.onNoteChanged(sv.getMaSV(), s.toString());
                    }
                }
            }
        };

        holder.etGhiChu.addTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return sinhVienList.size();
    }

    public void setAllStatus(boolean coMat) {
        for (DiemDanh dd : diemDanhMap.values()) {
            dd.setCoMat(coMat);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaSV, tvHoTen;
        EditText etGhiChu;
        CheckBox cbCoMat;
        TextWatcher textWatcher;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaSV = itemView.findViewById(R.id.tvMaSV);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            etGhiChu = itemView.findViewById(R.id.etGhiChu);
            cbCoMat = itemView.findViewById(R.id.cbCoMat);
        }
    }
}