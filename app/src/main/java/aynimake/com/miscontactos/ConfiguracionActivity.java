package aynimake.com.miscontactos;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Toshiba on 03/03/2015.
 */
public class ConfiguracionActivity extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ConfiguracionFragment configFrag = new ConfiguracionFragment();
        transaction.replace(android.R.id.content, configFrag);
        transaction.commit();
    }
}
