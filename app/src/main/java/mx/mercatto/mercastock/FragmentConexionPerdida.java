package mx.mercatto.mercastock;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Field;

import mx.mercatto.mercastock.BGT.BGTAPI;


public class FragmentConexionPerdida extends Fragment implements View.OnClickListener {
    public static Boolean FLAG_CONEXION_PERDIDA=false;
    EditText txtIp;
    View rootView;
    InputMethodManager imm;
    public  static  boolean conexionPerdida = false;
    public FragmentConexionPerdida() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conexion_perdida, container, false);
        getActivity().setTitle("Error al intentar conectar"+Configuracion.settings.getString("usuario",""));
        //showToast("Error en la conexión");
        Main.controlUsuario=-1;
        if(!Configuracion.settings.getString("sucursal","").equals("")){
            rootView.findViewById(R.id.button6).setEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        txtIp= (EditText)rootView.findViewById(R.id.editText13);
        if(!Configuracion.settings.getString("ip","").equals("")){
            txtIp.setText(Configuracion.settings.getString("ip", ""));
        }
        FLAG_CONEXION_PERDIDA=true;
        conexionPerdida=true;
        Button upButton = (Button) rootView.findViewById(R.id.button6);
        upButton.setOnClickListener(this);
        Button upButton2 = (Button) rootView.findViewById(R.id.button);
        upButton2.setOnClickListener(this);
        txtIp.addTextChangedListener(new TextWatcher() {
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                String valorIp = txtIp.getText().toString();
                if (!valorIp.equals(gg) && valorIp.length()>=7) {
                    rootView.findViewById(R.id.button6).setEnabled(true);
                    //getView().findViewById(R.id.button9).setEnabled(true);
                } else {
                    rootView.findViewById(R.id.button6).setEnabled(false);
                    //getView().findViewById(R.id.button9).setEnabled(false);
                    //getView().findViewById(R.id.button6).setEnabled(true);
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

                BGTAPI bgt;
                try {
                    rootView.findViewById(R.id.button6).setEnabled(false);

                    JSONObject jsonObj1 = new JSONObject();
                    jsonObj1.put("claveApi",Configuracion.settings.getString("ClaveApi",""));
                    bgt = new BGTAPI(Configuracion.getApiUrlRevisarApi(), getActivity(),jsonObj1 ,true);
                    //showToast("Intentando Reconexión");
                    bgt.execute();
                } catch (Exception e){
                    this.showToast(e.getMessage());
                }
                rootView.findViewById(R.id.button6).setEnabled(true);
            }
            break;
            case R.id.button: {
                FragmentSucursal fragment = new FragmentSucursal();
                FragmentManager fragmentManager = this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                FLAG_CONEXION_PERDIDA=false;
            }
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
