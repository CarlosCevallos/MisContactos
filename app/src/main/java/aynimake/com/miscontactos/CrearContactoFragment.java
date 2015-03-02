package aynimake.com.miscontactos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import aynimake.com.miscontactos.util.ContactReceiver;
import aynimake.com.miscontactos.entity.Contacto;
import aynimake.com.miscontactos.util.TextChangedListener;

/**
 * Created by Toshiba on 11/02/2015.
 */
public class CrearContactoFragment extends Fragment implements View.OnClickListener {
    private EditText txtNombre, txtTelefono, txtEmail, txtDireccion;
    private ImageView imgViewContacto;
    private Button btnGuardar, btnCancelar;
    private int request_code = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crear_contacto, container, false);
        inicializarComponentes(rootView);
        return rootView;
    }

    private void inicializarComponentes(final View view) {
        txtNombre = (EditText) view.findViewById(R.id.cmpNombre);
        txtTelefono = (EditText) view.findViewById(R.id.cmpTelefono);
        txtEmail = (EditText) view.findViewById(R.id.cmpEmail);
        txtDireccion = (EditText) view.findViewById(R.id.cmpDireccion);

        imgViewContacto = (ImageView) view.findViewById(R.id.imgContacto);
        imgViewContacto.setOnClickListener(this);

        //
        txtNombre.addTextChangedListener(new TextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnGuardar.setEnabled(!s.toString().trim().isEmpty());
            }
        });

        btnGuardar = (Button) view.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);

        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGuardar: guardarContacto(view); break;
            case R.id.btnCancelar: limpiarCampos(); break;
            case R.id.imgContacto: cargarImagen(); break;
        }

    }

    private void cargarImagen() {
        Intent intent = null;

        /* Verificamos la version de la plataforma
           ...a partir de la v4.4 Kikat (api 19) cambio la seguridad.
        */
        if (Build.VERSION.SDK_INT < 19) {
            // Android JellyBean 4.3 y anteriores
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            // Android Kitkat 4.4 y posteriores
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(intent, request_code);
    }

    private void guardarContacto(View view) {
        agregarContacto(
                txtNombre.getText().toString().trim(),
                txtTelefono.getText().toString().trim(),
                txtEmail.getText().toString().trim(),
                txtDireccion.getText().toString().trim(),
                String.valueOf(imgViewContacto.getTag())  // Obtenemos el atributo TAG con la Uri de la Imagen
        );

        String msj = String.format("%s ha sido agregado a la lista!", txtNombre.getText());
        Toast.makeText(view.getContext(), msj, Toast.LENGTH_SHORT).show();

        msj = "Imagen: "+ String.valueOf(imgViewContacto.getTag());
        Toast.makeText(view.getContext(), msj, Toast.LENGTH_LONG).show();

        btnGuardar.setEnabled(false);
        limpiarCampos();
    }

    private void agregarContacto(String nombre, String telefono, String email, String direccion, String imageUri) {
        Contacto nuevo = new Contacto(nombre, telefono, email, direccion, imageUri);

        Intent intent = new Intent(ContactReceiver.FILTER_NAME);
        intent.putExtra("operacion", ContactReceiver.CONTACTO_AGREGADO);
        intent.putExtra("datos", nuevo);

        getActivity().sendBroadcast(intent);
    }

    private void limpiarCampos() {
        txtNombre.getText().clear();
        txtTelefono.getText().clear();
        txtEmail.getText().clear();
        txtDireccion.getText().clear();
        //Reestablecemos la imagen predeterminada del contacto
        imgViewContacto.setImageResource(R.drawable.contacto);

        //Devolver el Foco al campo "txtNombre"
        txtNombre.requestFocus();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == request_code) {
            Uri uri = data.getData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                ContentResolver resolver = getActivity().getContentResolver();
                resolver.takePersistableUriPermission(uri, takeFlags);
            }

            imgViewContacto.setImageURI(uri);
            // Utilizamos el atributo TAG para almacenar la Uri al archivo seleccionado
            imgViewContacto.setTag(uri);
        }
    }


}
