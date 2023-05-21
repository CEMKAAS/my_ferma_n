package com.hfad.myferma.AddPackage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.myferma.R;
import com.hfad.myferma.incubator.ListAdapterIncubator;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterAdd extends RecyclerView.Adapter<CustomAdapterAdd.MyViewHolder> {

    private ArrayList id, title, disc, day,mount,year;
    private List<ProductDB> productDB;

    private Listener listener;
    public static interface Listener {
        public void onClick(int position);
    }
    public CustomAdapterAdd(ArrayList idAdd, ArrayList titleAdd, ArrayList discAdd,
                     ArrayList dayAdd, ArrayList mountAdd, ArrayList yearAdd){
        this.id = idAdd;
        this.title = titleAdd;
        this.disc = discAdd;
        this.day = dayAdd;
        this.mount = mountAdd;
        this.year = yearAdd;

    }

    public CustomAdapterAdd(List<ProductDB> productDBS){
        this.productDB = productDBS;
    }

    public void setListener(CustomAdapterAdd.Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        StringBuilder date = new StringBuilder();
//
//        holder.idTxt.setText(String.valueOf(id.get(position)));
//        holder.titleTxt.setText(String.valueOf(title.get(position)));
//        holder.discTxt.setText(String.valueOf(disc.get(position)));
//
//        String mount1 = String.valueOf(mount.get(position));
//        String year1 = String.valueOf(year.get(position));
//        String day1 = String.valueOf(day.get(position));
//        date.append(day1);
//        date.append(".");
//        date.append(mount1);
//        date.append(".");
//        date.append(year1);
//
//        holder.calendarTxt.setText(date);


        StringBuilder date = new StringBuilder();

        holder.idTxt.setText(String.valueOf(productDB.get(position).getId()));
        holder.titleTxt.setText(String.valueOf(productDB.get(position).getName()));
        holder.discTxt.setText(String.valueOf(productDB.get(position).getDisc()));
        holder.calendarTxt.setText(String.valueOf(productDB.get(position).getData()));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return productDB.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idTxt, titleTxt, discTxt,calendarTxt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idTxt = itemView.findViewById(R.id.id_txt);
            titleTxt = itemView.findViewById(R.id.title_txt);
            discTxt = itemView.findViewById(R.id.disc_txt);
            calendarTxt = itemView.findViewById(R.id.calendar_txt);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
//            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
//            mainLayout.setAnimation(translate_anim);
        }

    }

}
