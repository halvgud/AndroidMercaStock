package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import java.lang.reflect.Field;

import mx.mercatto.mercastock.BGT.BGTSesion;

public class FragmentSesion extends Fragment implements View.OnClickListener {

    TextView txtNombre;
    EditText txtPin;

    BGTSesion bgt;
    TextView txtSucursal;
    DrawerLayout drawer;
    public static  int contador2=0;
    public FragmentSesion() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sesion, container, false);
        getActivity().setTitle("Sesión");

        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        Button upButton = (Button) rootView.findViewById(R.id.button5);
        upButton.setOnClickListener(this);
        Button upButton2 = (Button) rootView.findViewById(R.id.button4);
        upButton2.setOnClickListener(this);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Login", "false");
        editor.apply();

        String nombre= Configuracion.settings.getString("nombre", "");
        String sucursal =Configuracion.settings.getString("sucursal","");
        txtNombre=(TextView)rootView.findViewById(R.id.textView18);
        txtNombre.setText(String.format("Usuario: %s", nombre));
        txtSucursal=(TextView)rootView.findViewById(R.id.textView14);
        txtSucursal.setText(String.format("Sucursal: %s", sucursal));
        txtPin= (EditText)rootView.findViewById(R.id.editText4);

        txtPin.addTextChangedListener(new TextWatcher() {
            String value1 = "";

            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtPin.getText().toString();

                if ((!value1.equals(gg) && (value1.length() == 4))) {
                    if(getView()!=null)
                        getView().findViewById(R.id.button5).setEnabled(true);
                } else {
                    if(getView()!=null)
                        getView().findViewById(R.id.button5).setEnabled(false);
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
        switch(v.getId())
        {
            case R.id.button5 :
                String usuario = Configuracion.settings.getString("usuario","");
                String password = txtPin.getText().toString();
                try {
                    JSONObject jsonObj1 = new JSONObject();
                    jsonObj1.put("usuario", usuario);
                    jsonObj1.put("contrasena", password);
                    jsonObj1.put("claveGCM",Configuracion.settings.getString("claveGCM",""));
                    bgt = new BGTSesion(Configuracion.getApiUrlLogIn(),getActivity(),jsonObj1);
                    bgt.execute();
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } catch (Exception e){
                    showToast(e.getMessage());
                }
                break;
            case R.id.button4: {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                //if (Main.idSesion == 1) {
                    editor.putString("usuario", "");
                    editor.putString("ClaveApi", "");
                    //editor.putString("sucursal", "");
                    //editor.putString("ip", "");
                    //editor.putString("idSucursal", "");
                    editor.apply();
                    Main.idSesion=0;
                    Main.inicio=0;
                    Main.bandera=0;

               // }
                Main.controlUsuario =-1;
                editor.putBoolean("FLAG_DESTROY",true);
                editor.apply();
                getActivity().finish();
                Intent intent = getActivity().getIntent();
                startActivity(intent);
                FragmentLogin fragment2 = new FragmentLogin();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();
            }
            break;
        }
    }

    protected FragmentActivity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity)activity;
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
