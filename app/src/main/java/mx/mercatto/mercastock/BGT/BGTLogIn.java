package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.preference.PreferenceManager;
//import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import mx.mercatto.mercastock.Configuracion;


import mx.mercatto.mercastock.FragmentCategoria;
import mx.mercatto.mercastock.FragmentConexionPerdida;
import mx.mercatto.mercastock.Main;
import mx.mercatto.mercastock.R;


public class BGTLogIn extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    JSONObject postparams = null;
    Activity activity;
    ProgressDialog asyncDialog;
    public static String ClaveApi = "Default";
    public static String User = "Default";

    static Integer CodeResponse;
    public BGTLogIn(String url, Activity activity,JSONObject postparams) {
        this.URL = url;
        Log.d("urllogin", URL);
        this.activity = activity;
        this.postparams = postparams;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
    }
    protected void onPreExecute() {
        super.onPreExecute();
        if(activity!=null) {
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("Cargando Usuario");
            asyncDialog.show();
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
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

        } catch (Exception e) {
        //    showToast(e.getMessage());
            bandera=false;
        }
        return jObj;

    }
boolean bandera=true;
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
                if(bandera){
                    Login(file_url);
                }else{

                    FragmentConexionPerdida fragment = new FragmentConexionPerdida();
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                }
                    jObj=null;
               }
       /* catch (Exception e) {
            throw e;
        }*/
        finally{
            if(activity!=null){
                asyncDialog.dismiss();
            }
        }
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    private void Login(JSONObject file_url){
       /* EditText txtusuario;
        EditText txtpassword;
        txtusuario   = (EditText)activity.findViewById(R.id.editText);
        txtpassword   = (EditText)activity.findViewById(R.id.editText2);
        String usuario = txtusuario.getText().toString();
        String password = txtpassword.getText().toString();*/
        try{
            //TextView txtusuario = (TextView) activity.findViewById(R.id.editText);
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
           // String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);
            SharedPreferences.Editor editor = settings.edit();
            switch (CodeResponse) {
                case 200: {
                    BGTCargarListadoCategoria.banderaTest=true;
                    JSONObject datos = file_url.getJSONObject("datos");
                    ClaveApi = datos.getString("claveApi");
                    User = datos.getString("usuario");
                    editor.putString("ClaveApi", ClaveApi);
                    editor.putString("usuario", datos.getString("usuario"));
                    editor.putString("idUsuario",datos.getString("idUsuario"));
                    editor.putString("nombre", datos.getString("nombre"));
                    editor.putString("idNivelAutorizacion",datos.getString("idNivelAutorizacion"));
                    editor.putInt("controlusuario", Integer.parseInt(datos.getString("idNivelAutorizacion")));
                    editor.putString("login", "true");
                    editor.putString("claveGCM", Main.idRegistro);

                    Main.idSesion=1;
                    // Main.ClAp=Integer.parseInt(ClaveApi);
                    editor.apply();
                    Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(300);
                    editor.putBoolean("FLAG_DESTROY",true);
                    editor.apply();
                    Intent intent = activity.getIntent();
                    activity.finish();
                    activity.startActivity(intent);

                    FragmentCategoria fragment = new FragmentCategoria();
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                    // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }
                break;
                case 400:{

                    String subEstado=file_url.getString("estado");
                    switch (subEstado){
                        case "11":
                            showToast(file_url.getString("mensaje"));
                            break;
                        default:
                            showToast("Usuario o contraseña incorrectas");
                    }

                }break;

                case 401:{
                    showToast("Usuario y/o contraseña incorrectos");
                }
                break;
                default:
                    showToast(Integer.toString(BGTLogIn.CodeResponse));
            }
        }catch(JSONException e){
            showToast(e.getMessage());
        }
    }
    }


