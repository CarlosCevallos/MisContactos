package aynimake.com.miscontactos;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aynimake.com.miscontactos.entity.Contacto;
import aynimake.com.miscontactos.entity.ContactoContract;
import aynimake.com.miscontactos.entity.JSONBean;
import aynimake.com.miscontactos.net.HttpServiceBroker;
import aynimake.com.miscontactos.util.AsyncTaskListener;
import aynimake.com.miscontactos.util.ContactArrayAdapter;
import aynimake.com.miscontactos.util.ContactReceiver;
import aynimake.com.miscontactos.util.DataChangeTracker;
import aynimake.com.miscontactos.util.DataChangeTracker.StoredRecord;
import aynimake.com.miscontactos.util.MenuBarActionReceiver;
import butterknife.ButterKnife;
import butterknife.InjectView;

import static aynimake.com.miscontactos.util.MenuBarActionReceiver.MenuBarActionListener;

/**
 * Created by Toshiba on 11/02/2015.
 */
public class ListaContactosFragment extends Fragment
        implements MenuBarActionListener, AsyncTaskListener<List<String>> {


    private static final String LOG_TAG = ListaContactosFragment.class.getSimpleName();

    @InjectView(R.id.fragment_listview)
    protected ListView contactsListView;

    private MenuBarActionReceiver receiver;
    private ContactArrayAdapter listAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_contactos, container, false);

        ButterKnife.inject(this, rootView);

        setHasOptionsMenu(true);  // Habilita el ActionBar de este Fragment para tener botones
        return rootView;
    }

    // Metodo que se ejecuta despues de la ejecucion de "onCreateView"
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        inicializarComponentes(getActivity(), savedInstanceState);
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

    private void inicializarComponentes( Context context, Bundle savedInstanceState ) {
        if (savedInstanceState == null) {
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(ContactoContract.CONTENT_URI, null, null, null, null);
            List<Contacto> contactos = Contacto.crearListaDeCursor(cursor);

            /*
            El adapter sera el encargado de ir creando los fragmentos de c/u de los contactos
            conforme de necesiten.
            De esta manera se estara "reciclando" la interfaz visual
            sin tener que crear un layout por c/contacto
             */
            listAdapter = new ContactArrayAdapter(context, R.layout.listview_item, contactos);
            contactsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            contactsListView.setAdapter(listAdapter);

            cursor.close();
        }
    }


    @Override
    public void contactoAgregado(Contacto contacto) {
        listAdapter.add(contacto);
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
            public void onClick(DialogInterface dialogInterface, int which) {
                SparseBooleanArray array = contactsListView.getCheckedItemPositions();
                ArrayList<Contacto> seleccion = new ArrayList<Contacto>();

                for (int j = 0; j < array.size(); j++) {
                    // Posición del contacto en el adaptador
                    int posicion = array.keyAt(j);
                    if (array.valueAt(j)) seleccion.add(listAdapter.getItem(posicion));
                }
                for (Contacto con : seleccion) listAdapter.remove(con);
                Intent intent = new Intent(ContactReceiver.FILTER_NAME);
                intent.putExtra("operacion", ContactReceiver.CONTACTO_ELIMINADO);
                intent.putParcelableArrayListExtra("datos", seleccion);
                getActivity().sendBroadcast(intent);

                contactsListView.clearChoices();
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
