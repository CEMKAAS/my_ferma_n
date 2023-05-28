package com.hfad.myferma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

public class bottomFragment extends BottomSheetDialogFragment {

    public String TAG  = "bottom";

    private Button buttonSheet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom, container, false);
        AutoCompleteTextView animalsSpinerSheet = layout.findViewById(R.id.animals_spiner_sheet);
        TextInputLayout dataSheet = layout.findViewById(R.id.data_sheet);
        Button buttonSheet = layout.findViewById(R.id.button_sheet);


        return layout;
    }

}