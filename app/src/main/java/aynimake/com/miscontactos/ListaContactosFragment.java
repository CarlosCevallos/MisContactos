package aynimake.com.miscontactos;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aynimake.com.miscontactos.entity.Contacto;
import aynimake.com.miscontactos.entity.ContactoContract;
import aynimake.com.miscontactos.entity.JSONBean;
import aynimake.com.miscontactos.net.HttpServiceBroker;
import aynimake.com.miscontactos.util.ApplicationContextProvider;
import aynimake.com.miscontactos.util.AsyncTaskListener;
import aynimake.com.miscontactos.util.ContactReceiver;
import aynimake.com.miscontactos.util.DataChangeTracker;
import aynimake.com.miscontactos.util.DataChangeTracker.StoredRecord;
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

        Context context = ApplicationContextProvider.getContext();
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(ContactoContract.CONTENT_URI, null, null, null, null);
        List<Contacto> contactos = Contacto.crearListaDeCursor(cursor);
        for (Contacto contacto: contactos) {
            ContactoFragment cfrag = ContactoFragment.crearInstancia(contacto, this);
            transaction.add(R.id.lista_fragmentos_contactos, cfrag);
        }
        cursor.close();
        transaction.commit();
    }


    @Override
    public void eliminarContactos() {
        //String mensaje = "¿Esta seguro de eliminar los contactos seleccionados?";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_action_warning);
        builder.setTitle(i18n(R.string.title_alertdialog_confirm));
        builder.setMessage(i18n(R.string.mesg_confirm_delete));
        builder.setPositiveButton(i18n(R.string.mesg_positive_dialog_option), new DialogInterface.OnClickListener() {
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
                intent.putParcelableArrayListExtra("datos", seleccion);

                getActivity().sendBroadcast(intent);
            }
        });
        builder.setNegativeButton(i18n(R.string.mesg_negative_dialog_option), null);
        builder.show();

    }

    @Override
    public void sincronizarDatos() {
        DataChangeTracker tracker = new DataChangeTracker(getActivity());

        ArrayList<StoredRecord> allRecords = tracker.retrieveRecords();
        ArrayList<StoredRecord> createList = new ArrayList<StoredRecord>();
        ArrayList<StoredRecord> updateList = new ArrayList<StoredRecord>();
        ArrayList<StoredRecord> deleteList = new ArrayList<StoredRecord>();

        for ( StoredRecord record : allRecords ) {
            switch ( record.getType() ) {
                case StoredRecord.TYPE_CREATE: createList.add(record); break;
                case StoredRecord.TYPE_UPDATE: updateList.add(record); break;
                case StoredRecord.TYPE_DELETE: deleteList.add(record); break;
            }
        }

        doPost(createList);
        doPut(updateList);
        doDelete(deleteList);

        tracker.clearRecords();
    }

    private void doPost(ArrayList<StoredRecord> createList) {
        Intent intent = new Intent(HttpServiceBroker.FILTER_NAME);
        intent.putExtra("metodo_http", HttpServiceBroker.HTTP_POST_METHOD);
        ArrayList<JSONBean> datos = new ArrayList<JSONBean>();
        for (StoredRecord record : createList) datos.add(record.getData());
        intent.putParcelableArrayListExtra("datos", datos);

        getActivity().sendBroadcast(intent);
    }

    private void doPut(ArrayList<StoredRecord> updateList) {
        Intent intent = new Intent(HttpServiceBroker.FILTER_NAME);
        intent.putExtra("metodo_http", HttpServiceBroker.HTTP_PUT_METHOD);
        ArrayList<JSONBean> datos = new ArrayList<JSONBean>();
        for (StoredRecord record : updateList) datos.add(record.getData());
        intent.putParcelableArrayListExtra("datos", datos);

        getActivity().sendBroadcast(intent);
    }

    private void doDelete(ArrayList<StoredRecord> deleteList) {
        Intent intent = new Intent(HttpServiceBroker.FILTER_NAME);
        intent.putExtra("metodo_http", HttpServiceBroker.HTTP_DELETE_METHOD);
        ArrayList<JSONBean> datos = new ArrayList<JSONBean>();
        for (StoredRecord record : deleteList) datos.add(record.getData());
        intent.putParcelableArrayListExtra("datos", datos);

        getActivity().sendBroadcast(intent);
    }

    @Override
    public void fragmentChecked(ContactoFragment cfrag, boolean isChecked) {
        if (isChecked) fragmentosSeleccionados.add(cfrag);
        else fragmentosSeleccionados.remove(cfrag);
    }


    @Override
    public void processResult(List<String> result) {
        for (String cad : result) {
            Toast.makeText(getActivity(), i18n(R.string.mesg_toast_sync_confirm, cad),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private String i18n(int resourceId, Object ... formatArgs) {
        return getResources().getString(resourceId, formatArgs);
    }
}
