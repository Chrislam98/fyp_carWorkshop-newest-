package com.example.carworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkshopImageAdapter extends RecyclerView.Adapter<WorkshopImageAdapter.WorkshopImageViewHolder> {

    ArrayList<Icon> icon;
    Context c;

    public WorkshopImageAdapter(ArrayList<Icon> icon, Context c) {
        this.icon = icon;
        this.c = c;
    }

    @NonNull
    @Override
    public WorkshopImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.workshop_image,parent,false);
        return new WorkshopImageAdapter.WorkshopImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopImageViewHolder holder, int position) {
        Icon icon1 = icon.get(position);
        Picasso.get().load(icon1.getiUrl()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.workshopImage);
    }

    @Override
    public int getItemCount() {
        return icon.size();
    }

    public static class WorkshopImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView workshopImage;

        public WorkshopImageViewHolder(@NonNull View itemView) {
            super(itemView);
            workshopImage = itemView.findViewById(R.id.WSImage);

        }
    }
}
