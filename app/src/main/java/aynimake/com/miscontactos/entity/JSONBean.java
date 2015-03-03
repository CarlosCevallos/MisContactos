package aynimake.com.miscontactos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.j256.ormlite.field.DatabaseField;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by Toshiba on 03/03/2015.
 */
public class JSONBean implements PropertyChangeListener {

    /**
     * Propiedad para identificar cambios en el bean, para proceder a la sincronizacion
     */
    @JsonProperty
    @DatabaseField
    protected  String md5;

    protected JSONBean() {
        support.addPropertyChangeListener(this);
    }

    //<editor-fold desc="PROPERTY CHANGE SUPPORT">
    @JsonIgnore
    protected PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
    //</editor-fold>


    @Override
    public void propertyChange(PropertyChangeEvent event) {
        procesarHashMD5();
    }

    public String getMd5() {
        return md5;
    }

    public void procesarHashMD5() {
        HashFunction hf = Hashing.md5();
        HashCode code = hf.hashInt(hashCode());  // El hashcode del objeto se calcula a partir de todas sus propiedades
        md5 = code.toString();
    }

}
