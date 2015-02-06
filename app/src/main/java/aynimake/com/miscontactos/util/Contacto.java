package aynimake.com.miscontactos.util;

import android.net.Uri;

/**
 * Created by Toshiba on 03/02/2015.
 */
public class Contacto {
    private String nombre, telefono, email, direccion;
    private Uri imageUri;


    public Contacto(String nombre, String telefono, String email, String direccion, Uri imageUri) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.imageUri = imageUri;
    }

    //<editor-fold desc="GETTER METHODS">
    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public Uri getImageUri() { return imageUri; }
    //</editor-fold>

    //<editor-fold desc="SETTER METHODS">
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setImageUri(Uri imageUri) { this.imageUri = imageUri; }
    //</editor-fold>


}
