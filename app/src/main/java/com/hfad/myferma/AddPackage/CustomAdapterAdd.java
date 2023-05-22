package com.hfad.myferma.AddPackage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private List<ProductDB> productDB;

    private int myRow;
    private Listener listener;
    public static interface Listener {
        public void onClick(int position);
    }

    public CustomAdapterAdd(List<ProductDB> productDBS, int myRow){
        this.productDB = productDBS;
        this.myRow = myRow;
    }

    public void setListener(CustomAdapterAdd.Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(myRow, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.idTxt.setText(String.valueOf(productDB.get(position).getId()));
        holder.titleTxt.setText(String.valueOf(productDB.get(position).getName()));
        holder.discTxt.setText(String.valueOf(productDB.get(position).getDisc()));
        holder.calendarTxt.setText(String.valueOf(productDB.get(position).getData()));
        if (R.layout.my_row_write_off==myRow) {
            holder.photo.setImageResource(productDB.get(position).getPrice());
        }else if (R.layout.my_row_sale==myRow) {
            holder.sixColum.setText(String.valueOf(productDB.get(position).getPrice()));
        }

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

        TextView idTxt, titleTxt, discTxt,calendarTxt, sixColum ;
        LinearLayout mainLayout;

        ImageView photo;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idTxt = itemView.findViewById(R.id.id_txt);
            titleTxt = itemView.findViewById(R.id.title_txt);
            discTxt = itemView.findViewById(R.id.disc_txt);
            calendarTxt = itemView.findViewById(R.id.calendar_txt);

            if (R.layout.my_row_write_off==myRow) {
                photo = (ImageView) itemView.findViewById(R.id.status_txt);
            }else if (R.layout.my_row_sale==myRow) {
                sixColum = itemView.findViewById(R.id.six_column);
            }

            mainLayout = itemView.findViewById(R.id.mainLayout);

            //Animate Recyclerview
//            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
//            mainLayout.setAnimation(translate_anim);
        }

    }

}
