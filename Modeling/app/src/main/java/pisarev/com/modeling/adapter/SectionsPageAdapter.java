package pisarev.com.modeling.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pisarev.com.modeling.mvp.view.fragments.ProgramFragment;

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private ProgramFragment programFragment;

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
        programFragment = new ProgramFragment();
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return programFragment;
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return 1;
    }
}
