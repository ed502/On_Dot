package kr.ac.kpu.ondot.MenualPagerAdapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import kr.ac.kpu.ondot.Main.MainEducate;
import kr.ac.kpu.ondot.Main.MainMenual;
import kr.ac.kpu.ondot.Main.MainQuiz;
import kr.ac.kpu.ondot.Main.MainTranslate;
import kr.ac.kpu.ondot.Menual.MainMenual1;
import kr.ac.kpu.ondot.Menual.MainMenual2;

public class MainMenualAdapter extends FragmentPagerAdapter {
    private static int MAX_PAGE = 2;

    public MainMenualAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MainMenual1.newInstance();
            case 1:
                return MainMenual2.newInstance();

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