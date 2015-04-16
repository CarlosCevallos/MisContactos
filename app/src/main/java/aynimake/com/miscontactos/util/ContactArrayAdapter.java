package aynimake.com.miscontactos.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aynimake.com.miscontactos.R;
import aynimake.com.miscontactos.entity.Contacto;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Toshiba on 16/04/2015.
 */
public class ContactArrayAdapter extends ArrayAdapter<Contacto> {

    private static final String LOG_TAG = ContactArrayAdapter.class.getSimpleName();

    private Context context;
    private List<Contacto> contactos;

    public ContactArrayAdapter(Context context, int resource, List<Contacto> contactos) {
        super(context, resource, contactos);
        this.contactos = contactos;
        this.context = context;
        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.listview_item, parent, false);

            holder = new ViewHolder();
            ButterKnife.inject(holder, row);

            /*
            Cada "row" tendra su unico tag, dado que el layout es compartido por cada
            fila que aparezca en el LinearLayout, esto optimiza recursos enormemente.
             */
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        inicializarContenido(row, holder, contactos.get(position));
        return row;
    }

    private void inicializarContenido(View view, ViewHolder holder, Contacto contacto) {
        holder.viewNombre.setText(contacto.getNombre());
        holder.viewTelefono.setText(contacto.getTelefono());
        holder.viewEmail.setText(contacto.getEmail());
        holder.viewDireccion.setText(contacto.getDireccion());

        Picasso.with(context).load(contacto.getImageUri())
                .config(Bitmap.Config.ARGB_8888).resize(800,800).centerCrop().placeholder(R.drawable.contacto)
                .error(R.drawable.contacto).into(holder.ivContactImage);
    }

    //<editor-fold desc="ViewHolder Class">
    /**
     * Calse para contener una referencia al contenido del layout definido en el xml
     * (Mantener una referencia a los datos que se encontraban en un leyout especifico para un contacto)
     */
    public class ViewHolder {

        @InjectView(R.id.rootLayout)
        protected LinearLayout rootView;

        @InjectView(R.id.viewNombre)
        protected TextView viewNombre;

        @InjectView(R.id.viewTelefono)
        protected TextView viewTelefono;

        @InjectView(R.id.viewEmail)
        protected TextView viewEmail;

        @InjectView(R.id.viewDireccion)
        protected TextView viewDireccion;

        @InjectView(R.id.ivFotoContacto)
        protected ImageView ivContactImage;
    }
    //</editor-fold>
}
