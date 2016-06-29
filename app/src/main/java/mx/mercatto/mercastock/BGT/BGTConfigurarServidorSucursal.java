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
import java.util.HashMap;

import mx.mercatto.mercastock.Configuracion;

import mx.mercatto.mercastock.FragmentSucursal;
import mx.mercatto.mercastock.ListaSucursal;
import mx.mercatto.mercastock.R;
import mx.mercatto.mercastock.SucursalAdapter;

public class BGTConfigurarServidorSucursal extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static Integer CodeResponse;
    static InputStream is = null;
    static JSONObject jObj = null;
    JSONObject postparams = null;
    JSONObject jObj2 = null;
    static String json = "";
    Boolean banderaExecucion;
    public boolean transaccionCompleta=false;
    Activity activity;
    ProgressDialog asyncDialog;


    public BGTConfigurarServidorSucursal(String url, Activity activity, JSONObject postparams, Boolean bandera) {

        this.URL = url;
        this.activity = activity;
        this.banderaExecucion=bandera;
        if(postparams!=null){
            this.postparams=postparams;
        }
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            Log.d("url",URL);
            HttpPost httpPost = new HttpPost(URL);
            StringEntity entity = new StringEntity(postparams.toString(), HTTP.UTF_8);
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
            transaccionCompleta=true;
        } catch (Exception e) {
            //bandera=false;
            //  showToast(e.getMessage());
        }
        return jObj;
    }
        /*
        try {
            java.net.URL url = new URL(URL);
            Log.d("url",URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getResponseCode();
            conn.setConnectTimeout(5000);
             is = conn.getErrorStream();
            if (is == null) {
               is  = conn.getInputStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line ).append("\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            transaccionCompleta=true;
        } catch (UnknownHostException  |JSONException|MalformedURLException|UnsupportedEncodingException|IllegalArgumentException|ProtocolException e) {
            e.printStackTrace();
            transaccionCompleta=false;
        }catch(IOException e){
            transaccionCompleta=false;
        }
        return jObj;
        }
        */



    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
            if(banderaExecucion){
                cargarListadoSucursalHost(file_url);
            }
            else{
                cargarListadoSucursal(file_url);
            }
            //cargarListadoSucursal(file_url);
                    jObj=null;
        }
        catch (Exception e) {
            //showToast(":C"+e.getMessage());
            showToast("El WiFi se encuentra apagado");

        }
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    ArrayList<ListaSucursal> _listaSucursal = new ArrayList<>();
    ArrayList<ListaSucursal> _listaSucursal2 = new ArrayList<>();
    Spinner listaSucSpinner;
    Spinner listaSucSpinner2;
    Button guardar;
    Button probar;
    public static String sucursalSeleccionada="";
    public static String hostSeleccionada="";
    public static String dbPathSucursal ="";
    public static String idSucursalInt="";
    public static ArrayList<HashMap<String, String>>_Listado;
    public static ArrayList<HashMap<String, String>>_Listado2;

    public void cargarListadoSucursal(JSONObject file_url) {
        _Listado = new ArrayList<>();
        //if(file_url.has("bandera")) {
            if (transaccionCompleta) {
                try {
                    JSONArray countries = file_url.getJSONArray(Configuracion.getDatos());
                    Log.d("prueba º","try");

                    if (countries.length() > 0) {
                        Log.d("prueba 2","if");
                        for (int i = 0; i < countries.length(); i++) {
                            HashMap<String, String> map = new HashMap<>();
                            JSONObject c = countries.getJSONObject(i);
                            String id = c.getString(Configuracion.getIdSucursal());
                            String name = c.getString(Configuracion.getDescripcionSucursal());
                            String dburl = c.getString("claveApi");
                            map.put("db", dburl);
                            map.put("idSucursal", id);
                            _listaSucursal.add(new ListaSucursal(dburl, name.toUpperCase()));
                            _Listado.add(map);
                        }

                    } else {
                        showToast(":(");
                        _listaSucursal.add(new ListaSucursal("", "".toUpperCase()));
                    }
                    listaSucSpinner = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario);
                    SucursalAdapter cAdapter = new SucursalAdapter(activity, android.R.layout.simple_spinner_item, _listaSucursal);
                    listaSucSpinner.setAdapter(cAdapter);
                    showToast("Se han cargado las sucursales");
                    listaSucSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                ListaSucursal selectedCountry = _listaSucursal.get(position);
                                sucursalSeleccionada = selectedCountry.toString();
                                dbPathSucursal = selectedCountry.getId();
                                idSucursalInt = _Listado.get(+position).get("idSucursal");
                                Log.d("o.o", position + "");

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    guardar = (Button) activity.findViewById(R.id.button9);
                    guardar.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                guardar = (Button) activity.findViewById(R.id.button9);
                guardar.setEnabled(false);
                probar = (Button) activity.findViewById(R.id.button6);
                probar.setEnabled(false);
                showToast("No se ha podido establecer la conexión");
                probar = (Button) activity.findViewById(R.id.button6);
                probar.setEnabled(true);
                listaSucSpinner = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario);
                listaSucSpinner.setAdapter(null);
            }
        //}
    }
    public void cargarListadoSucursalHost(JSONObject file_url) {
        //SON.stringify(file_url).indexOf('m');
        _Listado2 = new ArrayList<>();
        if (transaccionCompleta) {
            try {
                JSONArray countries = file_url.getJSONArray(Configuracion.getDatos());

                if (countries.length() > 0) {

                    for (int i = 0; i < countries.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        JSONObject c = countries.getJSONObject(i);
                        String id = c.getString("direccion");
                        String name = c.getString("direccion");
                        String dburl = c.getString("direccion");
                        map.put("direccion", dburl);
                        map.put("direccion",id);
                        _listaSucursal2.add(new ListaSucursal(dburl, name.toUpperCase()));
                        _Listado2.add(map);
                    }

                } else {
                    showToast(":(");
                    _listaSucursal2.add(new ListaSucursal("", "".toUpperCase()));
                }

                listaSucSpinner2 = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario2);
                SucursalAdapter cAdapter = new SucursalAdapter(activity, android.R.layout.simple_spinner_item, _listaSucursal2);
                listaSucSpinner2.setAdapter(cAdapter);
                showToast("Se han cargado las sucursales");
                listaSucSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ListaSucursal selectedCountry = _listaSucursal2.get(position);
                        hostSeleccionada=selectedCountry.toString();
                        try {
                            BGTConfigurarServidorSucursal bgt;
                            JSONObject jsobj = new JSONObject();
                            jsobj.put("direccion",hostSeleccionada);
                            Log.d("host2", hostSeleccionada);
                            bgt = new BGTConfigurarServidorSucursal("http://"+FragmentSucursal.ip+ Configuracion.getApiUrlConfigurarIp(),activity,jsobj, false);
                            bgt.execute() ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //String ip=FragmentSucursal.ip;
                        //String path="http://" + ip + Configuracion.getApiUrlConfigurarIp() + "/sucursalHost";

                        //dbPathSucursal =selectedCountry.getId();
                        //idSucursalInt = _Listado.get(+position).get("idSucursal");
                        //Log.d("o.o",position+"");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                probar = (Button) activity.findViewById(R.id.button6);
                probar.setEnabled(true);

            }catch(JSONException e){
               Log.d("exc",e.getMessage());
            }

        }
        else {
            guardar = (Button) activity.findViewById(R.id.button9);
            guardar.setEnabled(false);
            probar = (Button) activity.findViewById(R.id.button6);
            probar.setEnabled(false);
            showToast("No se ha podido establecer la conexión");
            probar = (Button) activity.findViewById(R.id.button6);
            probar.setEnabled(true);
            listaSucSpinner2 = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario2);
            listaSucSpinner2.setAdapter(null);
        }
    }


}
