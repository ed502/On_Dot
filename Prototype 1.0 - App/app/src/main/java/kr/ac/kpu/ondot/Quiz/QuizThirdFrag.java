package kr.ac.kpu.ondot.Quiz;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import kr.ac.kpu.ondot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizThirdFrag extends Fragment {


    public QuizThirdFrag() {
        // Required empty public constructor
    }

    public static QuizThirdFrag newInstance(){
        Bundle args = new Bundle();
        QuizThirdFrag fragment = new QuizThirdFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.quiz_third_frag, container, false);
    }

}