package kr.ac.kpu.ondot.Menual;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.kpu.ondot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EducateMenual1 extends Fragment {
    public EducateMenual1() {
        // Required empty public constructor
    }

    public static EducateMenual1 newInstance() {
        Bundle args = new Bundle();
        EducateMenual1 fragment = new EducateMenual1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menual_educate1, container, false);
    }

}
