package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
//import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
//import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import mx.mercatto.mercastock.Configuracion;
import mx.mercatto.mercastock.FragmentConexionPerdida;
import mx.mercatto.mercastock.FragmentFormularioArticulo;

import mx.mercatto.mercastock.ListaAdaptador;
import mx.mercatto.mercastock.R;


public class BGTCargarListadoArticulo extends AsyncTask<String, String, JSONObject> {
    String sURL = null;

    String URL = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    JSONObject postparams = null;
    Activity activity;
    ProgressDialog asyncDialog;
    public static String ClaveApi = "Default";
    //public static String User = "Default";

    //static Integer CodeResponse;
    public BGTCargarListadoArticulo(String url, Activity activity, JSONObject postparams) {
        this.sURL = url;
        this.activity = activity;
        this.postparams = postparams;
        _Listado = new ArrayList<>();
        _JsonGenerico = null;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
    }
    protected void onPreExecute() {
        super.onPreExecute();
        if(activity!=null) {
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("Cargando Lista Artículo");
            asyncDialog.show();
        }

    }
boolean bandera=true;
    @Override
    protected JSONObject doInBackground(String... params) {
        try {/*
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
                sb.append(line + "\n");
            }
            is.close();*/

            java.net.URL url = new URL(sURL);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setUseCaches(false);
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestMethod("POST");
            httpCon.setConnectTimeout(5000);
            httpCon.setReadTimeout(5000);
            httpCon.connect(); // Note the connect() here

            OutputStream os = httpCon.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(postparams.toString());
            osw.flush();
            osw.close();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
/*
        } catch (UnsupportedEncodingException e) {
         bandera=false;
          //  showToast(e.getMessage());
        } catch (Exception e) {
            bandera=false;
           // showToast(e.getMessage());

        }
        return jObj;
*/
        } catch (Exception e) {
            // showToast(e.getMessage());
            bandera = false;
        }
        return jObj;
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    public static int devolverConteo(){
        return list.getCount();
    }
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
                if(bandera) {
                    ListViewArticulos(file_url);

                }else{

                    FragmentConexionPerdida fragment = new FragmentConexionPerdida();
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                }
               } finally{
            if(activity!=null){
                asyncDialog.dismiss();
            }
        }
    }
    public static JSONArray _JsonGenerico = null;
    public static ArrayList<HashMap<String, String>> _Listado = new ArrayList<>();
    public static ListView list;
    ListAdapter adapter;
    private void ListViewArticulos(JSONObject file_url){
        try {
            _JsonGenerico = file_url.getJSONArray(Configuracion.getDatos());
            for (int i = 0; i < _JsonGenerico.length(); i++) {
                JSONObject jsonTemporal = _JsonGenerico.getJSONObject(i);
                String nombreArticulo = jsonTemporal.getString(Configuracion.getDescripcioArticulo());
                String idCategoria = jsonTemporal.getString(Configuracion.getIdCategoria());
                String idArticulo = jsonTemporal.getString(Configuracion.getIdArticulo());
                String idInventario = jsonTemporal.getString(Configuracion.getIdInventario());
                String Unidad = jsonTemporal.getString(Configuracion.getUnidadArticulo());
                String exitencia = jsonTemporal.getString(Configuracion.getExistenciaArticulo());
                String granel = jsonTemporal.getString(Configuracion.getGranelArticulo());
                String clave = jsonTemporal.getString(Configuracion.getClaveArticulo());
                HashMap<String, String> mappeo = new HashMap<>();
                mappeo.put(Configuracion.getDescripcioArticulo(), nombreArticulo);
                mappeo.put(Configuracion.getIdArticulo(), idArticulo);
                mappeo.put(Configuracion.getIdCategoria(),idCategoria);
                mappeo.put(Configuracion.getIdInventario(),idInventario);
                mappeo.put(Configuracion.getUnidadArticulo(),Unidad);
                mappeo.put(Configuracion.getExistenciaArticulo(), exitencia);
                mappeo.put(Configuracion.getGranelArticulo(),granel);
                mappeo.put(Configuracion.getClaveArticulo(),clave);
               _Listado.add(mappeo);

            }
            list = (ListView) activity.findViewById(R.id.ListView1);

            adapter = new ListaAdaptador(activity, _Listado,
                    R.layout.list_v,
                    new String[]{Configuracion.getDescripcioArticulo()}, new int[]{R.id.descripcionColumna});
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(activity, "Se ha seleccionado " + _Listado.get(+position).get(Configuracion.getDescripcioArticulo()), Toast.LENGTH_SHORT).show();
                    String descripcionArticulo = _Listado.get(+position).get(Configuracion.getDescripcioArticulo());
                    String art_id = _Listado.get(+position).get(Configuracion.getIdArticulo());
                    String unidad = _Listado.get(+position).get(Configuracion.getUnidadArticulo());
                    String idInventario = _Listado.get(+position).get(Configuracion.getIdInventario());
                    FragmentFormularioArticulo fragment = new FragmentFormularioArticulo();
                    String existencia = _Listado.get(+position).get(Configuracion.getExistenciaArticulo());
                    String granel=_Listado.get(+position).get(Configuracion.getGranelArticulo());
                    String clave=_Listado.get(+position).get(Configuracion.getClaveArticulo());
                    //int restante=_Listado.get(+position).get("restante");
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    Bundle args = Bundle.EMPTY;
                    if (args == null) {
                        args = new Bundle();
                    } else {
                        args = new Bundle(args);
                    }
                    args.putString(Configuracion.getIdArticulo(), art_id);
                    args.putString(Configuracion.getIdInventario(), idInventario);
                    args.putString(Configuracion.getUnidadArticulo(), unidad);
                    args.putString(Configuracion.getDescripcioArticulo(), descripcionArticulo);
                    args.putString(Configuracion.getGranelArticulo(),granel);
                    args.putString(Configuracion.getClaveArticulo(),clave);
                    args.putString(Configuracion.getExistenciaArticulo(),existencia);
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                }
            });
        }catch (JSONException e){
            showToast(e.getMessage());
        }
    }

}


