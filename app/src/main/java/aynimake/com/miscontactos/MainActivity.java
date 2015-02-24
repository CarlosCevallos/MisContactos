package aynimake.com.miscontactos;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import aynimake.com.miscontactos.util.DatabaseHelper;
import aynimake.com.miscontactos.util.TabsPagerAdapter;



public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper>
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    // Control de Fichas (tabs)
    private ViewPager viewPager;
    private TabsPagerAdapter adapter;
    private ActionBar actionBar;

    // Titulos de las fichas
    private String[] titulos = {"Crear Contacto","Lista de Contactos"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarTabs();
    }

    private void inicializarTabs() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        adapter = new TabsPagerAdapter(getFragmentManager());

        viewPager.setAdapter(adapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Agregar las Fichas (tabs)
        for (String nombre: titulos) {
            ActionBar.Tab tab = actionBar.newTab().setText(nombre);
            tab.setTabListener(this);
            actionBar.addTab(tab);
        }
        viewPager.setOnPageChangeListener(this);
    }


    //<editor-fold desc="Metodos TAB CHANGE LISTENER">
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
    //</editor-fold>

    //<editor-fold desc="Metodos VIEW CHANGE LISTENER">
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //</editor-fold>

}
