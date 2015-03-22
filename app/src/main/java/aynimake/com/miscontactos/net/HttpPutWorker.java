package aynimake.com.miscontactos.net;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import aynimake.com.miscontactos.entity.JSONBean;
import aynimake.com.miscontactos.util.AsyncTaskListener;

/**
 * Created by Toshiba on 21/03/2015.
 */
public class HttpPutWorker extends AsyncTask<JSONBean, Void, List<String>> {
    private HashSet<AsyncTaskListener<List<String>>> listeners;
    private final ObjectMapper mapper;
    private final  String url;

    public HttpPutWorker(ObjectMapper mapper, String url) {
        this.mapper = mapper;
        this.url = url;
    }

    @Override
    protected List<String> doInBackground(JSONBean... params) {
        ArrayList<String> list = new ArrayList<String>();
        for (JSONBean bean : params) list.add( process(bean) );

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


    public String process(JSONBean bean) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(url);
        httpPut.addHeader("Content-Type", "application/json");
        try {
            HttpResponse resp = client.execute(httpPut);
            StatusLine statusLine = resp.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = resp.getEntity();
                String respStr = EntityUtils.toString(entity);
                builder.append(respStr);
            } else {
                // TODO: Mostrar alerta al usuario, notificando del error
                Log.e("JSON","statusCode:"+String.valueOf(statusCode)+"  Error al leer la respuesta");
            }
        } catch (IOException ex) {
            Log.e("HttpPutWorker", ex.getLocalizedMessage(), ex);
        }

        return builder.toString();
    }


}
