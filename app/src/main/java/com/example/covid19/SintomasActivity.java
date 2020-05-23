package com.example.covid19;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class SintomasActivity extends Fragment {

    View v;
    ListView listView;
    String[] arraySintomas = {"Fiebre/escalofrío","Malestar General","Tos","Dolor de garganta","Congestión Nasal","Dificultad Respiratoria","Diarrea","Náuseas/vómitos","","",""};
    public SintomasActivity(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sintomas, container, false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
