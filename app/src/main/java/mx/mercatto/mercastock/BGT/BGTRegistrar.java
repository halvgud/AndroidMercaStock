package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import mx.mercatto.mercastock.*;


public class BGTRegistrar extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    JSONObject postparams = null;
    Activity activity;
    ProgressDialog asyncDialog;
    public static String ClaveApi = "Default";
    Boolean bandera=true;
    EditText txtusuario ;
    EditText txtpassword;
    EditText txtpassword2;
    EditText txtnombre ;
    Spinner txtsexo;
    EditText txtapellido ;

    static Integer CodeResponse;
    public BGTRegistrar(String url, Activity activity, JSONObject postparams) {
        this.URL = url;
        this.activity = activity;
        this.postparams = postparams;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    protected void onPreExecute() {
        super.onPreExecute();
        if(activity!=null) {
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("Guardando Usuario");

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

        }  catch (Exception e) {
            bandera=false;
          //  showToast(e.getMessage());
        }
        return jObj;

    }


    @Override
    protected void onPostExecute(JSONObject file_url) {
        super.onPostExecute(file_url);
        txtusuario = (EditText) activity.findViewById(R.id.editText5);
        txtpassword = (EditText) activity.findViewById(R.id.editText6);
        txtpassword2= (EditText) activity.findViewById(R.id.editText7);
        txtnombre = (EditText) activity.findViewById(R.id.editText8);
        txtapellido = (EditText) activity.findViewById(R.id.editText9);
        String[] arraySpinner;
        txtsexo = (Spinner) activity.findViewById(R.id.spinnerSexo);
        try {

            if(bandera) {
                switch (BGTRegistrar.CodeResponse) {

                    case 200: {
                        showToast("Se registró correctamente el usuario");
                        //FragmentCategoria fragment = new FragmentCategoria();
                        //FragmentManager fragmentManager = getActivity().getFragmentManager();
                        //fragmentManager.beginTransaction().replace(R.id.content_main,fragment).addToBackStack(null).commit();
                        txtusuario.requestFocus();
                        txtusuario.setText("");
                        txtpassword.setText("");
                        txtpassword2.setText("");
                        txtnombre.setText("");
                        txtapellido.setText("");
                        arraySpinner =new String[]{
                                "Masculino","Femenino"
                        };
                        txtsexo = (Spinner) activity.findViewById(R.id.spinnerSexo);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,
                                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
                        txtsexo.setAdapter(adapter);

                    }
                    break;
                    case 401:{
                        showToast(file_url.getString("mensaje"));
                    }break;
                    default:
                        showToast(Integer.toString(CodeResponse));
                        break;
                }
            }else{
                FragmentConexionPerdida fragment = new FragmentConexionPerdida();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
            }
                    jObj=null;
               }
        catch (JSONException e){
            Log.d("",e.getMessage());
        }catch (Exception e) {
            Log.d("",e.getMessage());
            throw e;
        }
        finally{
            if(activity!=null){
                asyncDialog.dismiss();
            }
        }
    }

    }


