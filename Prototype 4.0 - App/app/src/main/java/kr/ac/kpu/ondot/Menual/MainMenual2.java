package kr.ac.kpu.ondot.Menual;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.kpu.ondot.R;

public class MainMenual2 extends Fragment {

    public MainMenual2() {
        // Required empty public constructor
    }

    public static MainMenual2 newInstance() {
        Bundle args = new Bundle();
        MainMenual2 fragment = new MainMenual2();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menual_main2, container, false);
    }

}
