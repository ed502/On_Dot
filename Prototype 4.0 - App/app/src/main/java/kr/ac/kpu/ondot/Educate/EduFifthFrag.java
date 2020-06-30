package kr.ac.kpu.ondot.Educate;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import kr.ac.kpu.ondot.R;

public class EduFifthFrag extends Fragment {

    public EduFifthFrag() {
        // Required empty public constructor
    }

    public static EduFifthFrag newInstance(){
        Bundle args = new Bundle();
        EduFifthFrag fragment = new EduFifthFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.edu_fifth_frag, container, false);
    }

}
