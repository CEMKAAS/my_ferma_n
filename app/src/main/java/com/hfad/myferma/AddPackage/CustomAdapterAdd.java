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

import java.util.ArrayList;

public class CustomAdapterAdd extends RecyclerView.Adapter<CustomAdapterAdd.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList id, title, disc, day,mount,year;

    public CustomAdapterAdd(Activity activity, Context context, ArrayList idAdd, ArrayList titleAdd, ArrayList discAdd,
                     ArrayList dayAdd, ArrayList mountAdd, ArrayList yearAdd){
        this.activity = activity;
        this.context = context;
        this.id = idAdd;
        this.title = titleAdd;
        this.disc = discAdd;
        this.day = dayAdd;
        this.mount = mountAdd;
        this.year = yearAdd;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
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

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivityAdd.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("title", String.valueOf(title.get(position)));
                intent.putExtra("disc", String.valueOf(disc.get(position)));
                intent.putExtra("day", String.valueOf(day.get(position)));
                intent.putExtra("mount", String.valueOf(mount.get(position)));
                intent.putExtra("year", String.valueOf(year.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return id.size();
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
