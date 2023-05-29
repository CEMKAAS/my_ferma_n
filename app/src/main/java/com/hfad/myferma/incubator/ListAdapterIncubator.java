package com.hfad.myferma.incubator;

import android.annotation.SuppressLint;
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

public class ListAdapterIncubator extends RecyclerView.Adapter<ListAdapterIncubator.MyViewHolder> {

    private String[] massId, massTempDamp, massOver, massAiring;
    private Listener listener;


    public static interface Listener {
        public void onClick(int position, int day);
    }

    public ListAdapterIncubator(String[] massId1, String[] massTempDam1, String[] massOver1, String[] massAiring1) {
        this.massId = massId1;
        this.massTempDamp = massTempDam1;
        this.massOver = massOver1;
        this.massAiring = massAiring1;
    }

    public void setListener(ListAdapterIncubator.Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_row_incubator, parent, false);
        return new ListAdapterIncubator.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final ListAdapterIncubator.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.idTxt.setText("День " + String.valueOf(position + 1));
        holder.titleTxt.setText(String.valueOf(massTempDamp[position + 1]));
        holder.discTxt.setText(String.valueOf(massTempDamp[position + 31]));
        holder.calendarTxt.setText(String.valueOf(massOver[position + 1]));
        holder.airingtxt.setText(String.valueOf(massAiring[position + 1]));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position, position + 1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (massId[2].equals("Курицы")) {
            return 21;
        } else if (massId[2].equals("Индюки")) {
            return 28;
        } else if (massId[2].equals("Гуси")) {
            return 30;
        } else if (massId[2].equals("Утки")) {
            return 28;
        } else if (massId[2].equals("Перепела")) {
            return 17;
        } else {
            return 30;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idTxt, titleTxt, discTxt, calendarTxt, airingtxt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idTxt = itemView.findViewById(R.id.id_txt);
            titleTxt = itemView.findViewById(R.id.title_txt);
            discTxt = itemView.findViewById(R.id.disc_txt);
            calendarTxt = itemView.findViewById(R.id.calendar_txt);
            airingtxt = itemView.findViewById(R.id.airing_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }

    }
}