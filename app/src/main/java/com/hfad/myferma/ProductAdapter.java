package com.hfad.myferma;

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

import com.hfad.myferma.incubator.ListAdapterIncubator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private Map<String, Double> produtsMap;
    private List<String> productsList;
    private List<Double> unitList;
    private String name, suffix;
    private DecimalFormat f, eggFormat;

    public ProductAdapter(Map<String, Double> productsMapList, String name, String suffix) {
        this.produtsMap = productsMapList;
        this.name = name;
        this.suffix = suffix;
        productsList = new ArrayList<>();
        unitList = new ArrayList();
        f = new DecimalFormat("0.00");
        for (Map.Entry<String, Double> entry :
                produtsMap.entrySet()) {
            productsList.add(entry.getKey());
            unitList.add(entry.getValue());
        }
    }

    public String unitString(String animals) {
        if (suffix.equals(" Шт.")) {
            if (animals.equals("Яйца")) {
                return " шт.";
            } else if (animals.equals("Молоко")) {
                return " л.";
            } else if (animals.equals("Мясо")) {
                return " кг.";
            } else {
                return " ед.";
            }
        } else {
            return " ₽";
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_row_products, parent, false);
        return new ProductAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        String nameProuct = productsList.get(position);
        holder.products.setText(name);
        holder.count.setText(nameProuct);

        holder.unit.setText(String.valueOf(f.format(unitList.get(position))) + unitString(nameProuct));

    }

    @Override
    public int getItemCount() {
        return produtsMap.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView products, unit, count;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            products = itemView.findViewById(R.id.products_text);
            unit = itemView.findViewById(R.id.unit_text);
            count = itemView.findViewById(R.id.count_text);
        }
    }


}
