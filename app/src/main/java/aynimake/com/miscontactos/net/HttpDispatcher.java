package aynimake.com.miscontactos.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import aynimake.com.miscontactos.entity.JSONBean;
import aynimake.com.miscontactos.util.AsyncTaskListener;

/**
 * Created by Toshiba on 04/03/2015.
 */
public class HttpDispatcher {

    private final Context context;
    private ObjectMapper mapper;

    // URL del proyecto Desarrollo en tu Idioma - Servicios Rest con NetBeans
    private final String BASE_URL_ADDRESS = "http://%s:%s/jsonweb/rest/contacto";
    private final String SERVER_ADDRESS;
    private final String SERVER_PORT;
    private final String REGISTRY_OWNER;

    public HttpDispatcher(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        SERVER_ADDRESS = prefs.getString("server_address", null);
        SERVER_PORT = prefs.getString("server_port", null);
        REGISTRY_OWNER = prefs.getString("username", null);

        mapper = new ObjectMapper();

        // Desactivar la autodeteccion y obligar al uso de atributos y NO de Getter / Setter
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.context = context;
    }

    // REST : GET  (muestra registro de BD)
    public <T> void doGet(Class<T> resulType, AsyncTaskListener<T> listener) {
        StringBuilder builder = new StringBuilder(String.format(BASE_URL_ADDRESS, SERVER_ADDRESS, SERVER_PORT));
        String url = builder.append("/owner/").append(REGISTRY_OWNER).toString();

        if (wifiEnabled()) {
            // TODO: Implementar HttpGetWorker
        } else {
            Toast.makeText(context, "WIFI no disponible !!", Toast.LENGTH_SHORT).show();
        }
    }

    // REST : POST  (inserta registro de BD)
    public void doPost(JSONBean bean, AsyncTaskListener<List<String>> listener) {
        StringBuilder builder = new StringBuilder(String.format(BASE_URL_ADDRESS, SERVER_ADDRESS, SERVER_PORT));
        String url = builder.toString();

        if (wifiEnabled()) {
            // TODO: Implementar HttpPostWorker
        } else {
            Toast.makeText(context, "WIFI no disponible !!", Toast.LENGTH_SHORT).show();
        }
    }

    // REST : PUT  (actualiza registro de BD)
    public void doPut(JSONBean bean, AsyncTaskListener<List<String>> listener) {
        StringBuilder builder = new StringBuilder(String.format(BASE_URL_ADDRESS, SERVER_ADDRESS, SERVER_PORT));
        String url = builder.append("/").append(bean.getServerId()).toString();

        if (wifiEnabled()) {
            // TODO: Implementar HttpPutWorker
        } else {
            Toast.makeText(context, "WIFI no disponible !!", Toast.LENGTH_SHORT).show();
        }
    }

    // REST : DELETE  (boorra registro de BD)
    public void doDelete(JSONBean bean, AsyncTaskListener<List<String>> listener) {
        StringBuilder builder = new StringBuilder(String.format(BASE_URL_ADDRESS, SERVER_ADDRESS, SERVER_PORT));
        String url = builder.append("/").append(bean.getServerId()).toString();

        if (wifiEnabled()) {
            // TODO: Implementar HttpDeleteWorker
        } else {
            Toast.makeText(context, "WIFI no disponible !!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean wifiEnabled() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return info != null && info.isConnected();
    }
}
