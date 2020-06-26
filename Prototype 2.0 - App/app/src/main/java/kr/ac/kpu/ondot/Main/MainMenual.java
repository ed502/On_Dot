package kr.ac.kpu.ondot.Main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.kpu.ondot.R;


public class MainMenual extends Fragment {

    public MainMenual() {
        // Required empty public constructor
    }

    public static MainMenual newInstance(){
        Bundle args = new Bundle();
        MainMenual fragment = new MainMenual();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_menual, container, false);
    }

}
