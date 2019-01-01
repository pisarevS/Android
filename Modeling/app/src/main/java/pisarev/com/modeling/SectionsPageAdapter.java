package pisarev.com.modeling;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pisarev.com.modeling.Fragments.ParameterFragment;
import pisarev.com.modeling.Fragments.ProgramFragment;

public class SectionsPageAdapter extends FragmentPagerAdapter {
    public SectionsPageAdapter (FragmentManager fm) {
        super( fm );
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            ProgramFragment programFragment=new ProgramFragment();
            return programFragment;
        }
        if(position==1){
            ParameterFragment parameterFragment= new ParameterFragment();
            return parameterFragment ;
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
