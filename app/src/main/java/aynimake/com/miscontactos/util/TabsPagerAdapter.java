package aynimake.com.miscontactos.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import aynimake.com.miscontactos.CrearContactoFragment;
import aynimake.com.miscontactos.ListaContactosFragment;

/**
 * Created by Toshiba on 12/02/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new CrearContactoFragment();
            case 1: return new ListaContactosFragment();
        }
        return null;
    }
}
