package aynimake.com.miscontactos.net;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import aynimake.com.miscontactos.util.AsyncTaskListener;

/**
 * Created by Toshiba on 21/03/2015.
 */
public class HttpDeleteWorker extends AsyncTask<String, Void, List<String>> {

    private HashSet<AsyncTaskListener<List<String>>> listeners;
    private final ObjectMapper mapper;

    public HttpDeleteWorker(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        ArrayList<String> list = new ArrayList<String>();
        for (String url : params) list.add( process(url) );

        return list;
    }

    @Override
    protected void onPostExecute(List<String> result) {
        for (AsyncTaskListener<List<String>> listener : listeners) {
            listener.processResult(result);
        }
    }


    public void addAsyncTaskListener(AsyncTaskListener<List<String>> listener) {
        if (listeners == null) listeners = new HashSet<AsyncTaskListener<List<String>>>();
        listeners.add(listener);
    }


    public String process(String url) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.addHeader("Content-Type", "application/json");
        try {
            HttpResponse resp = client.execute(httpDelete);
            String respStr = EntityUtils.toString(resp.getEntity());
            // TODO: Eliminar el LOG al finalizar las pruebas e Implementar una notificacion al usuario
            Log.i("DELETE RESPONSE JSON", respStr);
        } catch (IOException ex) {
            Log.e("HttpDeleteWorker", ex.getLocalizedMessage(), ex);
        }

        return builder.toString();
    }


}
