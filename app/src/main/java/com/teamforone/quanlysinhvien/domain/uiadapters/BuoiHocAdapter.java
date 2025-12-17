package com.teamforone.quanlysinhvien.domain.uiadapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;

import java.util.List;

public class BuoiHocAdapter extends RecyclerView.Adapter<BuoiHocAdapter.ViewHolder> {

    private Context context;
    private List<BuoiHoc> buoiHocList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDiemDanhClick(BuoiHoc buoiHoc);
    }

    public BuoiHocAdapter(Context context, List<BuoiHoc> buoiHocList, OnItemClickListener listener) {
        this.context = context;
        this.buoiHocList = buoiHocList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_buoi_hoc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuoiHoc buoiHoc = buoiHocList.get(position);

        holder.tvMonHoc.setText(buoiHoc.getTenMonHoc() != null ? buoiHoc.getTenMonHoc() : buoiHoc.getMaMonHoc());
        holder.tvLop.setText("Lớp: " + (buoiHoc.getTenLop() != null ? buoiHoc.getTenLop() : buoiHoc.getMaLop()));
        holder.tvGiangVien.setText("GV: " + (buoiHoc.getHoTenGV() != null ? buoiHoc.getHoTenGV() : "Chưa phân công"));
        holder.tvNgayHoc.setText("Ngày: " + buoiHoc.getNgayHoc());
        holder.tvTiet.setText("Tiết: " + buoiHoc.getTietBatDau() + "-" + buoiHoc.getTietKetThuc());

        holder.btnDiemDanh.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDiemDanhClick(buoiHoc);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buoiHocList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonHoc, tvLop, tvGiangVien, tvNgayHoc, tvTiet;
        ImageButton btnDiemDanh;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonHoc = itemView.findViewById(R.id.tvMonHoc);
            tvLop = itemView.findViewById(R.id.tvLop);
            tvGiangVien = itemView.findViewById(R.id.tvGiangVien);
            tvNgayHoc = itemView.findViewById(R.id.tvNgayHoc);
            tvTiet = itemView.findViewById(R.id.tvTiet);
            btnDiemDanh = itemView.findViewById(R.id.btnDiemDanh);
        }
    }
}