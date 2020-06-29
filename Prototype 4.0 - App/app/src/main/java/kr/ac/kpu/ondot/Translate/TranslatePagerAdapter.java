package kr.ac.kpu.ondot.Translate;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import kr.ac.kpu.ondot.Main.MainEducate;
import kr.ac.kpu.ondot.Main.MainQuiz;
import kr.ac.kpu.ondot.Main.MainTranslate;

public class TranslatePagerAdapter extends FragmentPagerAdapter {
    private static int MAX_PAGE = 3;

    public TranslatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MainEducate.newInstance();
            case 1:
                return MainQuiz.newInstance();
            case 2:
                return MainTranslate.newInstance();
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
