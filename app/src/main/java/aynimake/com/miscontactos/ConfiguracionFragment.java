package aynimake.com.miscontactos;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Toshiba on 03/03/2015.
 */
public class ConfiguracionFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.configuracion);
    }
}
