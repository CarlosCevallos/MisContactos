package aynimake.com.miscontactos.net;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashSet;

import aynimake.com.miscontactos.util.AsyncTaskListener;

/**
 * Created by Toshiba on 21/03/2015.
 */
public class HttpGetWorker<T> extends AsyncTask<String, Void, T> {

    private HashSet<AsyncTaskListener<T>> listeners;
    private final ObjectMapper mapper;
    private Class<T> beanClass;

    public HttpGetWorker(ObjectMapper mapper, Class<T> beanClass) {
        this.mapper = mapper;
        this.beanClass = beanClass;
    }


    @Override
    protected T doInBackground(String... params) {
        return process(params[0]);
    }

    @Override
    protected void onPostExecute(T result) {
        for (AsyncTaskListener<T> listener : listeners) {
            listener.processResult(result);
        }
    }


    public void addAsyncTaskListener(AsyncTaskListener<T> listener) {
        if (listeners == null) listeners = new HashSet<AsyncTaskListener<T>>();
        listeners.add(listener);
    }


    private T process(String url) {
        T data = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json");
        try {
            HttpResponse resp = client.execute(httpGet);
            StatusLine statusLine = resp.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = resp.getEntity();
                String respStr = EntityUtils.toString(entity);
                data = mapper.readValue(respStr, beanClass);
            } else {
                // TODO: Mostrar alerta al usuario, notificando del error
                //Log.e("JSON","statusCode:"+String.valueOf(statusCode)+"  Error al cargar el documento json");
                Log.e("JSON","Error al cargar el documento json");
            }
        } catch (IOException ex) {
            Log.e("HttpGetWorker",ex.getLocalizedMessage(), ex);
        }

        return data;
    }

}
