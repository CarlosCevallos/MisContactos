package aynimake.com.miscontactos;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aynimake.com.miscontactos.util.Contacto;
import aynimake.com.miscontactos.util.TextChangedListener;
//import android.view.Menu;
//import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private EditText txtNombre, txtTelefono, txtEmail, txtDireccion;
    private List<Contacto> contactos = new ArrayList<Contacto>();
    private ListView contactsListView;
    private Button btnAgregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentesUI();
        inicializarTabs();
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
        inicializarListaContactos();
        limpiarCampos();
    }

    private void inicializarListaContactos() {

    }

    private void agregarContacto(String nombre, String telefono, String email, String direccion) {
        contactos.add(new Contacto(nombre, telefono, email, direccion) );
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


}
