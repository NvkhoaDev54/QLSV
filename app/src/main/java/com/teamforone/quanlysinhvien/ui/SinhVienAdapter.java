package com.teamforone.quanlysinhvien.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;

import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.ViewHolder>{
    private List<SinhVien> sinhVienList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(SinhVien sinhVien);
        void onDeleteClick(SinhVien sinhVien);
    }

    public SinhVienAdapter(List<SinhVien> sinhVienList, OnItemClickListener listener) {
        this.sinhVienList = sinhVienList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sinhvien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SinhVien sv = sinhVienList.get(position);
        holder.bind(sv, listener);
    }

    @Override
    public int getItemCount() {
        return sinhVienList.size();
    }

    public void updateList(List<SinhVien> newList) {
        this.sinhVienList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaSV, tvHoTen, tvNgaySinh, tvGioiTinh, tvLop;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvMaSV = itemView.findViewById(R.id.tvMaSV);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvNgaySinh = itemView.findViewById(R.id.tvNgaySinh);
            tvGioiTinh = itemView.findViewById(R.id.tvGioiTinh);
            tvLop = itemView.findViewById(R.id.tvLop);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(SinhVien sv, OnItemClickListener listener) {
            tvMaSV.setText(sv.getMaSV());
            tvHoTen.setText(sv.getHoTen());
            tvNgaySinh.setText("Ngày sinh: " + (sv.getNgaySinh() != null ? sv.getNgaySinh() : "N/A"));
            tvGioiTinh.setText("Giới tính: " + (sv.getGioiTinh() != null ? sv.getGioiTinh() : "N/A"));
            tvLop.setText("Lớp: " + (sv.getTenLop() != null ? sv.getTenLop() : "N/A"));

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(sv);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(sv);
                }
            });
        }
    }
}
