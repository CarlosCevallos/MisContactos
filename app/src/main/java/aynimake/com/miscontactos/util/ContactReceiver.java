package aynimake.com.miscontactos.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import aynimake.com.miscontactos.entity.Contacto;
import aynimake.com.miscontactos.entity.ContactoContract;

/**
 * Created by Toshiba on 11/02/2015.
 */
public class ContactReceiver extends BroadcastReceiver {

    public static final String FILTER_NAME = "listacontactos";
    public static final int CONTACTO_AGREGADO = 1;
    public static final int CONTACTO_ELIMINADO = 2;
    public static final int CONTACTO_ACTUALIZADO = 3;

    private Context context = ApplicationContextProvider.getContext();
    private ContentResolver resolver = context.getContentResolver();

    private final DataChangeTracker tracker;

    public ContactReceiver(Activity activity) {
        this.tracker = new DataChangeTracker(activity);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int operacion = intent.getIntExtra("operacion",-1);

        switch (operacion) {
            case CONTACTO_AGREGADO: agregarContacto(intent); break;
            case CONTACTO_ELIMINADO: eliminarContacto(intent); break;
            case CONTACTO_ACTUALIZADO: actualizarContacto(intent); break;
        }

    }

    private void agregarContacto(Intent intent) {
        Contacto contacto = (Contacto) intent.getParcelableExtra("datos");
        ContentValues values = contacto.getContentValues();
        values.remove(ContactoContract._ID);   // Evitar insercion de id en contactos nuevos
        Uri insertedUri = resolver.insert(ContactoContract.CONTENT_URI, values);

        // Obtenemos el id del nuevo registro insertado.
        // Se obtiene el Ultimo segmento de la url. este representa el "id" generado por el SqlLite
        contacto.setServerId(Integer.parseInt(insertedUri.getLastPathSegment()));
        contacto.setId(contacto.getServerId());  // TCUTT: Fuerzo a Actualizar el "Id" de SQLite

        tracker.recordCreateOp(contacto);
    }

    private void eliminarContacto(Intent intent) {
        ArrayList<Contacto> lista = intent.getParcelableArrayListExtra("datos");
        for (Contacto contacto : lista) {
            // Insertamos el "id" del SqlLite obtenido en "agregarContacto"
            Uri queryUri = ContentUris.withAppendedId(ContactoContract.CONTENT_URI, contacto.getId());
            Cursor cursor = resolver.query(queryUri, null, null, null, null, null);
            contacto = Contacto.crearInstanciaDeCursor(cursor);

            int eliminados = resolver.delete(queryUri, null, null);
            Log.d("eliminarContacto?", String.valueOf(eliminados));

            tracker.recordDeleteOp(contacto);

            cursor.close();
        }
    }

    private void actualizarContacto(Intent intent) {
        Contacto contacto = (Contacto) intent.getParcelableExtra("datos");
        ContentValues values = contacto.getContentValues();
        // Evitamos modificar el ID desde este metodo
        values.remove(ContactoContract._ID);
        Uri updateUri = ContentUris.withAppendedId(ContactoContract.CONTENT_URI, contacto.getId());

        int actualizados = resolver.update(updateUri, values, null, null);

        Log.d("actualizarContacto?", String.valueOf(actualizados));

        // Por el momento la Actualizacion SOLO implica el asignar el "serverId" regresado
        // por el servidor al insertar nuevos contactos, NO aplicaremos al tracker en esta ocasion.
        // tracker.recordUpdateOp(contacto);
    }


}
