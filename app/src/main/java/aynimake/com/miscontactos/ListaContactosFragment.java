package aynimake.com.miscontactos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

import aynimake.com.miscontactos.entity.Contacto;
import aynimake.com.miscontactos.net.HttpDispatcher;
import aynimake.com.miscontactos.util.AsyncTaskListener;
import aynimake.com.miscontactos.util.ContactReceiver;
import aynimake.com.miscontactos.util.DataChangeTracker;
import aynimake.com.miscontactos.util.DataChangeTracker.StoredRecord;
import aynimake.com.miscontactos.util.DatabaseHelper;
import aynimake.com.miscontactos.util.MenuBarActionReceiver;

import static aynimake.com.miscontactos.ContactoFragment.FragmentCheckedListener;
import static aynimake.com.miscontactos.util.MenuBarActionReceiver.MenuBarActionListener;

/**
 * Created by Toshiba on 11/02/2015.
 */
public class ListaContactosFragment extends Fragment
        implements MenuBarActionListener, FragmentCheckedListener, AsyncTaskListener<List<String>> {

    private MenuBarActionReceiver receiver;
    private List<ContactoFragment> fragmentosSeleccionados = new ArrayList<ContactoFragment>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_contactos, container, false);
        inicializarComponentes(rootView);
        setHasOptionsMenu(true);  // Habilita el ActionBar de este Fragment para tener botones
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver = new MenuBarActionReceiver(this);
        // Solo recibira notificaciones mientras se encuentre mostrando en pantalla
        getActivity().registerReceiver(receiver, new IntentFilter(MenuBarActionReceiver.FILTER_NAME));

        //Log.d("ON RESUME", "BROADCASTERRECEIVER REGISTERED");
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);

        //Log.d("ON PAUSE", "BROADCASTERRECEIVER UNREGISTERED");
    }

    private void inicializarComponentes(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        OrmLiteBaseActivity<DatabaseHelper> activity = getOrmLiteBaseActivity();
        if (activity != null) {
            DatabaseHelper helper = activity.getHelper();
            RuntimeExceptionDao<Contacto, Integer> dao = helper.getContactoRuntimeDAO();
            List<Contacto> contactos = dao.queryForAll();

            for (Contacto contacto: contactos) {
                ContactoFragment cfrag = ContactoFragment.crearInstancia(contacto, this);
                transaction.add(R.id.lista_fragmentos_contactos, cfrag);
            }
        }

        transaction.commit();
    }

    private OrmLiteBaseActivity<DatabaseHelper> getOrmLiteBaseActivity(){
        Activity activity = getActivity();
        if ( activity instanceof OrmLiteBaseActivity)
            return (OrmLiteBaseActivity<DatabaseHelper>) activity;
        return null;
    }

    @Override
    public void eliminarContactos() {
        String mensaje = "¿Esta seguro de eliminar los contactos seleccionados?";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_action_warning).setTitle("Confirmar Operación");
        builder.setMessage(mensaje);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                ArrayList<Contacto> seleccion = new ArrayList<Contacto>();

                for (ContactoFragment cfrag : fragmentosSeleccionados) {
                    seleccion.add(cfrag.getContactoActual());
                    transaction.remove(cfrag);
                }
                fragmentosSeleccionados.clear();
                transaction.commit();

                Intent intent = new Intent(ContactReceiver.FILTER_NAME);
                intent.putExtra("operacion", ContactReceiver.CONTACTO_ELIMINADO);
                intent.putExtra("datos", seleccion);

                getActivity().sendBroadcast(intent);
            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();

    }

    @Override
    public void sincronizarDatos() {
        HttpDispatcher dispatcher = new HttpDispatcher(getActivity());
        DataChangeTracker tracker = new DataChangeTracker(getActivity());
        ArrayList<StoredRecord> cambios = tracker.retrieveRecords();
        for (StoredRecord record : cambios) {
            switch (record.getType()) {
                case StoredRecord.TYPE_CREATE:
                    dispatcher.doPost(record.getData(), this); break;
                case StoredRecord.TYPE_UPDATE:
                    dispatcher.doPut(record.getData(), this); break;
                case StoredRecord.TYPE_DELETE:
                    dispatcher.doDelete(record.getData(), this); break;
            }
        }
        tracker.clearRecords();
        //TODO: Reemplazar boton de Sincronizacion por un Progress Bar
    }

    @Override
    public void fragmentChecked(ContactoFragment cfrag, boolean isChecked) {
        if (isChecked) fragmentosSeleccionados.add(cfrag);
        else fragmentosSeleccionados.remove(cfrag);
    }


    @Override
    public void processResult(List<String> result) {
        for (String cad : result) {
            Toast.makeText(getActivity(), String.format("Sincronizado %s", cad),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
