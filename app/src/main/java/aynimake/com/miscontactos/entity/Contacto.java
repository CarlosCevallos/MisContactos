package aynimake.com.miscontactos.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "contacto")
public class Contacto extends JSONBean implements Serializable {

    @JsonProperty
    private Integer serverId;

    @JsonProperty("androidId")
    @DatabaseField(generatedId = true)
    private int id;     // Primary Key

    @JsonProperty
    @DatabaseField(index = true, canBeNull = false)
    private String nombre;

    @JsonProperty
    @DatabaseField
    private String telefono;

    @JsonProperty
    @DatabaseField
    private String email;

    @JsonProperty
    @DatabaseField
    private String direccion;

    @JsonProperty
    @DatabaseField
    private String imageUri;

    @JsonProperty
    @DatabaseField
    private String propietario;


    /* El motor ORMLite requiere este constructor vacio para poder instanciar objetos de esta clase
       por medio del API de Reflection  */
    public Contacto() {
    }

    public Contacto(int id, String nombre, String telefono, String email, String direccion, String imageUri, String propietario) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.imageUri = imageUri;
        this.propietario = propietario;

        procesarHashMD5();
    }

    //<editor-fold desc="GETTER METHODS">
    public int getId() {
        return id;
    }

    public String getImageUri() { return imageUri; }

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

    public String getPropietario() {
        return propietario;
    }
    //</editor-fold>

    //<editor-fold desc="SETTER METHODS">
    public void setId(int id) {
        int oldVal = this.id;
        this.id = id;
        support.firePropertyChange("id", oldVal, id);
    }

    public void setNombre(String nombre) {
        String oldVal = this.nombre;
        this.nombre = nombre;
        support.firePropertyChange("nombre", oldVal, nombre);
    }

    public void setTelefono(String telefono) {
        String oldVal = this.telefono;
        this.telefono = telefono;
        support.firePropertyChange("telefono", oldVal, telefono);
    }

    public void setEmail(String email) {
        String oldVal = this.email;
        this.email = email;
        support.firePropertyChange("email", oldVal, email);
    }

    public void setDireccion(String direccion) {
        String oldVal = this.direccion;
        this.direccion = direccion;
        support.firePropertyChange("direccion", oldVal, direccion);
    }

    public void setImageUri(String imageUri) {
        String oldVal = this.imageUri;
        this.imageUri = imageUri;
        support.firePropertyChange("imageUri", oldVal, imageUri);
    }

    public void setPropietario(String propietario) {
        String oldVal = this.propietario;
        this.propietario = propietario;
        support.firePropertyChange("propietario", oldVal, propietario);
    }
    //</editor-fold>


    //<editor-fold desc="METODOS EQUALS Y HASHCODE">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contacto contacto = (Contacto) o;

        if (id != contacto.id) return false;
        if (direccion != null ? !direccion.equals(contacto.direccion) : contacto.direccion != null)
            return false;
        if (email != null ? !email.equals(contacto.email) : contacto.email != null) return false;
        if (imageUri != null ? !imageUri.equals(contacto.imageUri) : contacto.imageUri != null)
            return false;
        if (nombre != null ? !nombre.equals(contacto.nombre) : contacto.nombre != null)
            return false;
        if (propietario != null ? !propietario.equals(contacto.propietario) : contacto.propietario != null)
            return false;
        if (telefono != null ? !telefono.equals(contacto.telefono) : contacto.telefono != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (telefono != null ? telefono.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        result = 31 * result + (imageUri != null ? imageUri.hashCode() : 0);
        result = 31 * result + (propietario != null ? propietario.hashCode() : 0);
        return result;
    }
    //</editor-fold>

}
