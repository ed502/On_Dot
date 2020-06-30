package kr.ac.kpu.ondot.MenualPagerAdapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import kr.ac.kpu.ondot.Menual.MainMenual1;
import kr.ac.kpu.ondot.Menual.MainMenual2;
import kr.ac.kpu.ondot.Menual.QuizMenual1;
import kr.ac.kpu.ondot.Menual.QuizMenual2;

public class QuizMenualAdapter extends FragmentPagerAdapter {
    private static int MAX_PAGE = 2;

    public QuizMenualAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return QuizMenual1.newInstance();
            case 1:
                return QuizMenual2.newInstance();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return MAX_PAGE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}