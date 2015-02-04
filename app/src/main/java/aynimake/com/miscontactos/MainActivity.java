package aynimake.com.miscontactos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

import aynimake.com.miscontactos.util.ContactListAdapter;
import aynimake.com.miscontactos.util.Contacto;
import aynimake.com.miscontactos.util.TextChangedListener;
//import android.view.Menu;
//import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private EditText txtNombre, txtTelefono, txtEmail, txtDireccion;
    //-- La siguiente linea se reemplaza por la siguiente linea ("ArrayAdapter<Contacto>")
    //private List<Contacto> contactos = new ArrayList<Contacto>();
    private ArrayAdapter<Contacto> adapter;
    private ListView contactsListView;
    private ImageView imgViewContacto;
    private Button btnAgregar;
    private int request_code = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentesUI();
        inicializarListaContactos();
        inicializarTabs();
    }

    private void inicializarListaContactos() {
        adapter = new ContactListAdapter(this, new ArrayList<Contacto>());
        contactsListView.setAdapter(adapter);
    }

    private void inicializarComponentesUI() {
        txtNombre = (EditText) findViewById(R.id.cmpNombre);
        txtTelefono = (EditText) findViewById(R.id.cmpTelefono);
        txtEmail = (EditText) findViewById(R.id.cmpEmail);
        txtDireccion = (EditText) findViewById(R.id.cmpDireccion);

        contactsListView = (ListView) findViewById(R.id.lvContactos);

        //
        txtNombre.addTextChangedListener(new TextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnAgregar = (Button) findViewById(R.id.bAgregar);
                btnAgregar.setEnabled(!s.toString().trim().isEmpty());
            }
        });

    }

    public void onClick(View view) {
        agregarContacto(
                txtNombre.getText().toString().trim(),
                txtTelefono.getText().toString().trim(),
                txtEmail.getText().toString().trim(),
                txtDireccion.getText().toString().trim()
        );

        String msj = String.format("%s ha sido agregado a la lista!", txtNombre.getText());
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();

        btnAgregar.setEnabled(false);
        limpiarCampos();
    }

    private void agregarContacto(String nombre, String telefono, String email, String direccion) {
        Contacto nuevo = new Contacto(nombre, telefono, email, direccion);
        adapter.add(nuevo);
    }

    private void limpiarCampos() {
        txtNombre.getText().clear();
        txtTelefono.getText().clear();
        txtEmail.getText().clear();
        txtDireccion.getText().clear();

        //Devolver el Foco al campo "txtNombre"
        txtNombre.requestFocus();
    }


    private void inicializarTabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        // Tab1 :  Crear
        TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Crear");
        tabHost.addTab(spec);
        // Tab2 :  Lista
        spec = tabHost.newTabSpec("tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Lista");
        tabHost.addTab(spec);
    }


    public void onImgClick(View view) {
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
}
