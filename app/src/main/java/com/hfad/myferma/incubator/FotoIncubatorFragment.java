package com.hfad.myferma.incubator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.hfad.myferma.R;


public class FotoIncubatorFragment extends Fragment {
    private int day;

    private String type;

    private TextView name, description;

    private ImageView image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_foto_incubator, container, false);
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.getMenu().findItem(R.id.delete).setVisible(false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            day = bundle.getInt("data");
            type = bundle.getString("type");
        }

        name = layout.findViewById(R.id.name);
        description = layout.findViewById(R.id.textView2);
        image = layout.findViewById(R.id.imageView2);

        setFoto();

        return layout;
    }

    public void setDescription (String description1, int image1){
        name.setText("День " + day);
        description.setText(description1 + "\nЯйцо должно выглядеть так как указано на картинке");
        image.setImageResource(image1);
    }
    public void setFoto() {

        if (type.equals("Курицы")) {

            if (day < 7) {

                image.setImageResource(R.drawable.chiken1);
                name.setText("День " + day);
                description.setText("Овоскопировать еще рано, на " + 7 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора");

            } else if (day >= 7 && day < 11) {

                setDescription("Первое овоскопирование",R.drawable.chiken1);

            } else if (day >= 11 && day < 16) {
                setDescription("Второе овоскопирование",R.drawable.chiken2);

            } else if (day >= 16) {
                setDescription("Третье овоскопирование",R.drawable.chiken3);
            }

        } else if (type.equals("Индюки")) {
            if (day < 8) {

                image.setImageResource(R.drawable.turkeys1);
                name.setText("День " + day);
                description.setText("Овоскопировать еще рано, на " + 8 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора");

            } else if (day >= 8 && day < 14) {
                setDescription("Первое овоскопирование",R.drawable.turkeys1);
            } else if (day >= 14 && day < 25) {
                setDescription("Второе овоскопирование",R.drawable.turkeys2);
            } else if (day >= 25) {
                setDescription("Третье овоскопирование",R.drawable.turkeys3);
            }

        } else if (type.equals("Гуси")) {
            if (day < 9) {

                image.setImageResource(R.drawable.goose1);
                name.setText("День " + day);
                description.setText("Овоскопировать еще рано, на " + 9 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора");

            } else if (day >= 9 && day < 15) {
                setDescription("Первое овоскопирование",R.drawable.goose1);
            } else if (day >= 15 && day < 21) {
                setDescription("Второе овоскопирование",R.drawable.goose2);
            } else if (day >= 21) {
                setDescription("Третье овоскопирование",R.drawable.goose3);
            }

        } else if (type.equals("Утки")) {
            if (day < 8) {

                image.setImageResource(R.drawable.duck1);
                name.setText("День " + day);
                description.setText("Овоскопировать еще рано, на " + 8 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора");

            } else if (day >= 8 && day < 15) {
                setDescription("Первое овоскопирование",R.drawable.duck1);
            } else if (day >= 15 && day < 26) {
                setDescription("Второе овоскопирование",R.drawable.duck2);
            } else if (day >= 26) {
                setDescription("Третье овоскопирование",R.drawable.duck3);
            }

        } else if (type.equals("Перепела")) {
            if (day < 6) {

                image.setImageResource(R.drawable.quail1);
                name.setText("День " + day);
                description.setText("Овоскопировать еще рано, на " + 6 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора");

            } else if (day >= 6 && day < 13) {
                setDescription("Первое овоскопирование",R.drawable.quail1);
            } else if (day >= 13 && day < 16) {
                setDescription("Второе овоскопирование",R.drawable.quail2);
            }
        }

    }
}

