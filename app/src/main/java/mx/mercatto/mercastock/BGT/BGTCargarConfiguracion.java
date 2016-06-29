package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import mx.mercatto.mercastock.Configuracion;
import mx.mercatto.mercastock.FragmentConexionPerdida;
import mx.mercatto.mercastock.R;

public class BGTCargarConfiguracion extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";


    Activity activity;
    ProgressDialog asyncDialog;


    public BGTCargarConfiguracion(String url,Activity activity) {
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
           // DefaultHttpClient httpClient = new DefaultHttpClient();
          //  HttpGet httpGet = new HttpGet(URL);
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //HttpResponse httpResponse = httpClient.execute(httpGet);
            //HttpEntity httpEntity = httpResponse.getEntity();
            is = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));

        } catch (Exception e) {
            if (activity!= null){
                FragmentConexionPerdida fragment = new FragmentConexionPerdida();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
            }
        }
        return jObj;

    }
    public void showToast(String msg) {
        Toast.makeText(this.activity, msg, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
                    CargarConfiguraciones(file_url);
               } catch (Exception e) {
            throw e;
        }
    }

    private void CargarConfiguraciones(JSONObject file_url){
        try {
            if(file_url!= null) {
                JSONArray countries = file_url.getJSONArray(Configuracion.getDatos());

                // looping through All countries
                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);
                    Configuracion.setDatos(c.getString("parametro").equals("TAG_DATOS") ? c.getString("valor") : Configuracion.getDatos());
                    Configuracion.setIdLogin(c.getString("parametro").equals("TAG_ID_LOGIN") ? c.getString("valor") : Configuracion.getIdLogin());
                    Configuracion.setDescripcionLogin(c.getString("parametro").equals("TAG_DESCRIPCION_LOGIN") ? c.getString("valor") : Configuracion.getDescripcionLogin());
                    Configuracion.setApiUrlLogIn(c.getString("parametro").equals("API_URL_LOGIN") ? c.getString("valor") : Configuracion.getApiUrlLogIn());
                    Configuracion.setApiUrlSucursal(c.getString("parametro").equals("API_URL_SUCURSAL") ? c.getString("valor") : Configuracion.getApiUrlSucursal());
                    Configuracion.setIdCategoria(c.getString("parametro").equals("TAG_ID_CATEGORIA") ? c.getString("valor") : Configuracion.getIdCategoria());
                    Configuracion.setDescripcionCategoria(c.getString("parametro").equals("TAG_DESCRIPCION_CATEGORIA") ? c.getString("valor") : Configuracion.getDescripcionCategoria());
                    Configuracion.setCantidadCategoria(c.getString("parametro").equals("TAG_CANTIDAD_CATEGORIA") ? c.getString("valor") : Configuracion.getCantidadCategoria());
                    Configuracion.setApiUrlCategoria(c.getString("parametro").equals("API_URL_CATEGORIA") ? c.getString("valor") : Configuracion.getApiUrlCategoria());
                    Configuracion.setIdArticulo(c.getString("parametro").equals("TAG_ID_ARTICULO") ? c.getString("valor") : Configuracion.getIdArticulo());
                    Configuracion.setDescripcioArticulo(c.getString("parametro").equals("TAG_DESCRIPCION_ARTICULO") ? c.getString("valor") : Configuracion.getDescripcioArticulo());
                    Configuracion.setUnidadArticulo(c.getString("parametro").equals("TAG_UNIDAD_ARTICULO") ? c.getString("valor") : Configuracion.getUnidadArticulo());
                    Configuracion.setExistenciaArticulo(c.getString("parametro").equals("TAG_EXISTENCIA_ARTICULO") ? c.getString("valor") : Configuracion.getExistenciaArticulo());
                    Configuracion.setIdInventarioArticulo(c.getString("parametro").equals("TAG_CANTIDAD_ARTICULO") ? c.getString("valor") : Configuracion.getIdInventarioArticulo());
                    Configuracion.setApiUrlArticulo(c.getString("parametro").equals("API_URL_ARTICULO") ? c.getString("valor") : Configuracion.getApiUrlArticulo());
                    Configuracion.setIdInventario(c.getString("parametro").equals("TAG_ID_INVENTARIO") ? c.getString("valor") : Configuracion.getIdInventario());
                    Configuracion.setValorInventario(c.getString("parametro").equals("TAG_VALOR_ID_INVENTARIO") ? c.getString("valor") : Configuracion.getValorInventario());
                    Configuracion.setApiUrlInventario(c.getString("parametro").equals("API_URL_INVENTARIO") ? c.getString("valor") : Configuracion.getApiUrlInventario());
                    Configuracion.setIdRegistro(c.getString("parametro").equals("TAG_ID_REGISTRO") ? c.getString("valor") : Configuracion.getIdRegistro());
                    Configuracion.setDescripcionRegistro(c.getString("parametro").equals("TAG_DESCRIPCION_REGISTRO") ? c.getString("valor") : Configuracion.getDescripcionRegistro());
                    Configuracion.setApiUrlRegistro(c.getString("parametro").equals("API_URL_REGISTRO") ? c.getString("valor") : Configuracion.getApiUrlRegistro());
                    //if(getApiUrl().length() == 0) {
                    Configuracion.setApiUrl(c.getString("parametro").equals("API_URL") ? c.getString("valor") : "http://"+Configuracion.settings.getString("ip","192.168.1.17")+"/");
                    //}
                    Configuracion.setConfirmacion_Mensaje_Gurdado(c.getString("parametro").equals("CONFIRMACION_MENSAJE_GUARDADO") ? c.getString("valor") : Configuracion.getConfirmacion_Mensaje_Gurdado());
                    Configuracion.setConfirmacion_Habilitar_Decimales(c.getString("parametro").equals("CONFIRMACION_HABILITAR_DECIMALES") ? c.getString("valor") : Configuracion.getConfirmacion_Habilitar_Decimales());
                    Configuracion.setGranelArticulo(c.getString("parametro").equals("TAG_GRANEL_ARTICULO") ? c.getString("valor") : Configuracion.getGranelArticulo());
                    Configuracion.setClaveArticulo(c.getString("parametro").equals("TAG_CLAVE_ARTICULO") ? c.getString("valor") : Configuracion.getClaveArticulo());
                    Configuracion.setFlagBloqueoPorIntentos(c.getString("parametro").equals("FLAG_BLOQUEO_POR_INTENTOS") ? c.getString("valor") : Configuracion.getFlagBloqueoPorIntentos());
                    Configuracion.setFlagBloqueoCantidad(c.getString("parametro").equals("FLAG_BLOQUEO_CANTIDAD") ? c.getString("valor") : Configuracion.getFlagBloqueoCantidad());
                    Configuracion.setFlagBloqueoTiempo(c.getString("parametro").equals("FLAG_BLOQUEO_TIEMPO") ? c.getString("valor") : Configuracion.getFlagBloqueoTiempo());
                    Configuracion.setApiUrlBloqueo(c.getString("parametro").equals("API_URL_BLOQUEO") ? c.getString("valor") : Configuracion.getApiUrlBloqueo());

                    Configuracion.setidSucursal(c.getString("parametro").equals("TAG_ID_SUCURSAL") ? c.getString("valor") : Configuracion.getIdSucursal());
                    Configuracion.setDescripcionSucursal(c.getString("parametro").equals("TAG_DESCRIPCION_SUCURSAL") ? c.getString("valor") : Configuracion.getDescripcionSucursal());
                    Configuracion.setApiUrlPin(c.getString("parametro").equals("TAG_API_URL_PIN") ? c.getString("valor") : Configuracion.getApiUrlPin());
                    Configuracion.setProcesadoCategoria(c.getString("parametro").equals("TAG_PROCESADO_CATEGORIA") ? c.getString("valor") : Configuracion.getProcesadoCategoria());
                    Configuracion.setApiUrlConfiguracion(c.getString("parametro").equals("TAG_API_URL_CONFIGURACION") ? c.getString("valor") : Configuracion.getApiUrlConfiguracion());
                    Configuracion.setApiUrlConfigurarIp(c.getString("parametro").equals("TAG_API_URL_CONFIGURAR_IP") ? c.getString("valor") : Configuracion.getApiUrlConfigurarIp());
                    Configuracion.setApiUrlRevisarApi(c.getString("parametro").equals("TAG_API_URL_REVISAR_API") ? c.getString("valor") : Configuracion.getApiUrlRevisarApi());
                }
            }
            Configuracion.Finalizado=true;
        }catch(JSONException e)
        {
            if (activity!= null)
            showToast(e.getMessage());
        }
    }

}
