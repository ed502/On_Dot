package kr.ac.kpu.ondot.Educate;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EduPagerAdapter extends FragmentPagerAdapter {
    private static int MAX_PAGE = 6;

    public EduPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EduFirstFrag.newInstance();
            case 1:
                return EduSecondFrag.newInstance();
            case 2:
                return EduThirdFrag.newInstance();
            case 3:
                return EduFourthFrag.newInstance();
            case 4:
                return EduFifthFrag.newInstance();
            case 5:
                return EduSixthFrag.newInstance();
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
