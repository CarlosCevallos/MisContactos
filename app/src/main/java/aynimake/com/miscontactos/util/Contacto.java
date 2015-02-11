package aynimake.com.miscontactos.util;

import java.io.Serializable;

/**
 * Created by Toshiba on 03/02/2015.
 */
public class Contacto implements Serializable {
    private String nombre, telefono, email, direccion;
    private String imageUri;    // No es posible Serializar objetos Uri


    public Contacto(String nombre, String telefono, String email, String direccion, String imageUri) {
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

    public String getImageUri() { return imageUri; }
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

    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contacto contacto = (Contacto) o;

        if (direccion != null ? !direccion.equals(contacto.direccion) : contacto.direccion != null)
            return false;
        if (email != null ? !email.equals(contacto.email) : contacto.email != null) return false;
        if (imageUri != null ? !imageUri.equals(contacto.imageUri) : contacto.imageUri != null)
            return false;
        if (!nombre.equals(contacto.nombre)) return false;
        if (telefono != null ? !telefono.equals(contacto.telefono) : contacto.telefono != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + (telefono != null ? telefono.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        result = 31 * result + (imageUri != null ? imageUri.hashCode() : 0);
        return result;
    }
}
