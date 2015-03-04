package aynimake.com.miscontactos.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Toshiba on 04/03/2015.
 */
public class ApplicationContextProvider extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
