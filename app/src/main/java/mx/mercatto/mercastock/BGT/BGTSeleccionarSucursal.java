package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import mx.mercatto.mercastock.Configuracion;

import mx.mercatto.mercastock.ListaSucursal;
import mx.mercatto.mercastock.R;
import mx.mercatto.mercastock.SucursalAdapter;

public class BGTSeleccionarSucursal extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static InputStream is = null;
     JSONObject jObj = null;
    JSONObject postParams=null;
    static String json = "";
    Integer CodeResponse;
    public boolean transaccionCompleta=false;
    Activity activity;
    ProgressDialog asyncDialog;


    public BGTSeleccionarSucursal(String url, Activity activity,JSONObject jObj) {
        this.URL = url;
        this.activity = activity;
        this.postParams=jObj;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            HttpPost httpPost = new HttpPost(URL);
            StringEntity entity = new StringEntity(postParams.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            CodeResponse = response.getStatusLine().getStatusCode();
            is = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));

        } catch (Exception e) {
            showToast( e.getMessage());
        }
        return jObj;
    }



    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
            Log.d("kek",file_url.toString());

        }
        catch (Exception e) {
            //showToast(":C"+e.getMessage());
            showToast("El WiFi se encuentra apagado");

        }
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }



}
