package kr.ac.kpu.ondot.Main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import kr.ac.kpu.ondot.R;


public class MainEducate extends Fragment {

    public MainEducate() {
        // Required empty public constructor
    }

    public static MainEducate newInstance(){
        Bundle args = new Bundle();
        MainEducate fragment = new MainEducate();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_educate, container, false);
    }

}
