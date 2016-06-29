package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mx.mercatto.mercastock.Configuracion;

import mx.mercatto.mercastock.FragmentConexionPerdida;
import mx.mercatto.mercastock.ListaSucursal;
import mx.mercatto.mercastock.R;

public class BGTCargarSucursal extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    Activity activity;
    ProgressDialog asyncDialog;


    public BGTCargarSucursal(String url, Activity activity) {
        this.URL = url;
        this.activity = activity;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            is = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");

            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
        } catch (JSONException  |IOException e ) {
           Log.d("ERROR",e.getMessage());
            bandera=false;
        }

        return jObj;

    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

    }
    boolean bandera=true;
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
            if(bandera) {
                spinnerSucursal(file_url);
            }else{
                FragmentConexionPerdida fragment = new FragmentConexionPerdida();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
            }

               }
        catch (Exception e) {
            showToast(e.getMessage());
        }
    }
    ArrayList<ListaSucursal> _listaSucursal = new ArrayList<>();
    private void spinnerSucursal(JSONObject file_url){
        try {
            JSONArray countries = file_url.getJSONArray(Configuracion.getDatos());
            for (int i = 0; i < countries.length(); i++) {
                JSONObject c = countries.getJSONObject(i);
                String id = c.getString(Configuracion.getIdSucursal());
                String name = c.getString(Configuracion.getDescripcionSucursal());
                _listaSucursal.add(new ListaSucursal(id, name.toUpperCase()));
            }
            TextView txtSucursal = (TextView) activity.findViewById(R.id.textView13);
            txtSucursal.setText(_listaSucursal.get(0).toString());
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            if(!settings.getString("sucursal","").equals("")){
                txtSucursal.setText(settings.getString("sucursal",""));
            }

        }catch(JSONException e)
        {
            showToast(e.getMessage());
        }
    }


}
