package mx.mercatto.mercastock;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import mx.mercatto.mercastock.BGT.BGTConfigurarServidorSucursal;


public class FragmentSucursal extends Fragment implements View.OnClickListener  {
    public static  String ip="";
    Spinner listaSucSpinner;
    EditText txtIp;
    InputMethodManager imm;
    View rootView;
    Button guardar;
    Button probar;
    public FragmentSucursal() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sucursal, container, false);
        getActivity().setTitle("Configurar Servidor Sucursal");

        txtIp= (EditText)rootView.findViewById(R.id.editText13);
        if(!Configuracion.settings.getString("ip","").equals("")){
            txtIp.setText(Configuracion.settings.getString("ip",""));
        }

        if(!Configuracion.settings.getString("sucursal","").equals("")){
            rootView.findViewById(R.id.button6).setEnabled(true);
        }
        Button upButton = (Button) rootView.findViewById(R.id.button6);
        upButton.setOnClickListener(this);
        Button upButton2 = (Button) rootView.findViewById(R.id.button9);
        upButton2.setOnClickListener(this);

        ip = txtIp.getText().toString();




        txtIp.addTextChangedListener(new TextWatcher() {
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                String valorIp = txtIp.getText().toString();
                listaSucSpinner = (Spinner) getActivity().findViewById(R.id.spinnerRegistroUsuario);
                listaSucSpinner.setAdapter(null);
                if (!valorIp.equals(gg) && valorIp.length()>=7) {
                    rootView.findViewById(R.id.button6).setEnabled(true);
                   //getView().findViewById(R.id.button9).setEnabled(true);
                } else {
                    rootView.findViewById(R.id.button6).setEnabled(false);
                    //getView().findViewById(R.id.button9).setEnabled(false);
                    rootView.findViewById(R.id.button6).setEnabled(true);
                }
                if(listaSucSpinner.getAdapter()==null){
                    rootView.findViewById(R.id.button9).setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

        switch(v.getId())
        {
            case R.id.button6: {
                //peticion();
                cargaHost();
                guardar = (Button) getActivity().findViewById(R.id.button9);
                guardar.setEnabled(false);
                probar = (Button) getActivity().findViewById(R.id.button6);
                probar.setEnabled(false);
            }
                break;

            case R.id.button9: {
                PushNotificationService.Xrray=null;
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("sucursal", BGTConfigurarServidorSucursal.sucursalSeleccionada);
                Log.d("DB PATH",BGTConfigurarServidorSucursal.dbPathSucursal);
                editor.putString("db", BGTConfigurarServidorSucursal.dbPathSucursal);
                 editor.putString("idSucursal",BGTConfigurarServidorSucursal.idSucursalInt);
               // editor.putString("idsucursal", BGTConfigurarServidorSucursal.dbPathSucursal);
                editor.putString("ip", BGTConfigurarServidorSucursal.hostSeleccionada);
                editor.apply();
                Configuracion.reiniciarValoresDefault();
                Configuracion.Inicializar(getActivity());
                //Configuracion.setDBNombre(BGTConfigurarServidorSucursal.1idSucursalSeleccionada);
                Log.d("DB PATH", BGTConfigurarServidorSucursal.dbPathSucursal);
              /*  SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("sucursal", BGTConfigurarServidorSucursal.sucursalSeleccionada);
                editor.putString("idSucursal", BGTConfigurarServidorSucursal.1idSucursalSeleccionada);
                //editor.putString("idsucursal", BGTConfigurarServidorSucursal.1idSucursalSeleccionada.toString());
                editor.putString("ip", txtIp.getText().toString());
                editor.apply();*/
                /*
>>>>>>> 99243102798b5987ba87f0c485800113033d234d
*/
                FragmentLogin fragment2 = new FragmentLogin();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();

            }
                break;
            // similarly for other buttons
        }
    }

    public void peticion(String host) {

        //String host= BGTConfigurarServidorSucursal.dbPathSucursal;
        try {
            //JSONObject jsobj = new JSONObject();
            //jsobj.put("idSucursal","");
            //jsobj.put("nombre","");



            //showToast("Se ha establecido la conexión");
        } catch (Exception e) {
            //showToast("No jala");
            e.printStackTrace();
        }
    }
    public  void cargaHost() {
       ip = txtIp.getText().toString();
        BGTConfigurarServidorSucursal bgt;
        try {
            JSONObject jsobj = new JSONObject();
            bgt = new BGTConfigurarServidorSucursal("http://"+ ip + Configuracion.getApiUrlConfigurarIp()+"/sucursalHost", getActivity(),jsobj,true);
            bgt.execute();

            //showToast("Se ha establecido la conexión");
        } catch (Exception e) {
            showToast("No jala");
            e.printStackTrace();
        }
    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}