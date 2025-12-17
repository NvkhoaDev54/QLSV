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


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;

import java.util.List;

public class BuoiHocAdapter extends RecyclerView.Adapter<BuoiHocAdapter.ViewHolder> {

    private Context context;
    private List<BuoiHoc> buoiHocList;
    private OnBuoiHocClickListener listener;

    public interface OnBuoiHocClickListener {
        void onBuoiHocClick(BuoiHoc buoiHoc);
    }

    public BuoiHocAdapter(Context context, List<BuoiHoc> buoiHocList, OnBuoiHocClickListener listener) {
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

        // Hiển thị thông tin buổi học (không có giảng viên)
        holder.tvMonHoc.setText("Môn: " + buoiHoc.getTenMonHoc());
        holder.tvLop.setText("Lớp: " + buoiHoc.getTenLop());
        holder.tvNgayHoc.setText("Ngày: " + buoiHoc.getNgayHoc());
        holder.tvTiet.setText("Tiết: " + buoiHoc.getTietBatDau() + "-" + buoiHoc.getTietKetThuc());

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBuoiHocClick(buoiHoc);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buoiHocList != null ? buoiHocList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvMonHoc, tvLop, tvNgayHoc, tvTiet;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvMonHoc = itemView.findViewById(R.id.tvMonHoc);
            tvLop = itemView.findViewById(R.id.tvLop);
            tvNgayHoc = itemView.findViewById(R.id.tvNgayHoc);
            tvTiet = itemView.findViewById(R.id.tvTiet);
        }
    }
}