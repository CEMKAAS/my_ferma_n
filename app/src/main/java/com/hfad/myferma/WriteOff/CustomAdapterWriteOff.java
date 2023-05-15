package com.hfad.myferma.WriteOff;

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

import com.hfad.myferma.AddPackage.CustomAdapterAdd;
import com.hfad.myferma.AddPackage.UpdateActivityAdd;
import com.hfad.myferma.R;

import java.util.ArrayList;

public class CustomAdapterWriteOff extends RecyclerView.Adapter<CustomAdapterWriteOff.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList id, title, disc, day,mount,year, status;

    public CustomAdapterWriteOff(Activity activity, Context context, ArrayList idWriteOff, ArrayList titleWriteOff, ArrayList discWriteOff,
                            ArrayList dayWriteOff, ArrayList mountWriteOff, ArrayList yearWriteOff, ArrayList statusWriteOff){
        this.activity = activity;
        this.context = context;
        this.id = idWriteOff;
        this.title = titleWriteOff;
        this.disc = discWriteOff;
        this.day = dayWriteOff;
        this.mount = mountWriteOff;
        this.year = yearWriteOff;
        this.status = statusWriteOff;

    }

    @NonNull
    @Override
    public CustomAdapterWriteOff.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_write_off, parent, false);

        return new CustomAdapterWriteOff.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final CustomAdapterWriteOff.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        StringBuilder date = new StringBuilder();

        holder.idTxt.setText(String.valueOf(id.get(position)));
        holder.titleTxt.setText(String.valueOf(title.get(position)));
        holder.discTxt.setText(String.valueOf(disc.get(position)));

        String mount1 = String.valueOf(mount.get(position));
        String year1 = String.valueOf(year.get(position));
        String day1 = String.valueOf(day.get(position));
        date.append(day1);
        date.append(".");
        date.append(mount1);
        date.append(".");
        date.append(year1);

        holder.calendarTxt.setText(date);
        holder.photo.setImageResource(Integer.valueOf(String.valueOf(status.get(position))));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateWriteOffActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("title", String.valueOf(title.get(position)));
                intent.putExtra("disc", String.valueOf(disc.get(position)));
                intent.putExtra("day", String.valueOf(day.get(position)));
                intent.putExtra("mount", String.valueOf(mount.get(position)));
                intent.putExtra("year", String.valueOf(year.get(position)));
                intent.putExtra("status", String.valueOf(status.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idTxt, titleTxt, discTxt,calendarTxt, statusTxt;
        ImageView photo;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

           photo = (ImageView)itemView.findViewById(R.id.status_txt);


            idTxt = itemView.findViewById(R.id.id_txt);
            titleTxt = itemView.findViewById(R.id.title_txt);
            discTxt = itemView.findViewById(R.id.disc_txt);
            calendarTxt = itemView.findViewById(R.id.calendar_txt);
//            statusTxt = itemView.findViewById(R.id.status_txt);



            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
//            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
//            mainLayout.setAnimation(translate_anim);
        }

    }

}
