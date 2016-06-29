package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import mx.mercatto.mercastock.BGT.BGTCambiarPIN;



public class FragmentPassword extends Fragment implements View.OnClickListener  {

    public EditText txtPinActual;
    public EditText txtPinNuevo;
    public EditText txtPinNuevoR;

    public FragmentPassword() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_password, container, false);
        getActivity().setTitle("Cambiar PIN");

        Button upButton = (Button) rootView.findViewById(R.id.button8);
        upButton.setOnClickListener(this);


        txtPinActual= (EditText)rootView.findViewById(R.id.editText10);
        txtPinNuevo= (EditText)rootView.findViewById(R.id.editText11);
        txtPinNuevoR= (EditText)rootView.findViewById(R.id.editText12);

        txtPinActual.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String value3 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtPinActual.getText().toString();
                value2=txtPinNuevo.getText().toString();
                value3=txtPinNuevoR.getText().toString();
                evaluar(value1,value2,value3,gg);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        txtPinNuevo.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String value3 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtPinActual.getText().toString();
                value2=txtPinNuevo.getText().toString();
                value3=txtPinNuevoR.getText().toString();
                evaluar(value1,value2,value3,gg);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        txtPinNuevoR.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String value3 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtPinActual.getText().toString();
                value2=txtPinNuevo.getText().toString();
                value3=txtPinNuevoR.getText().toString();
                evaluar(value1,value2,value3,gg);
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
    private void evaluar(String value1,String value2, String value3,String gg){
        if ((!value1.equals(gg) && !value2.equals(gg) && !value3.equals(gg)) && (value2.equals(value3)) && (value2.equals(value3))&&(value1.length()==4 && value2.length()==4 && value2.length()==4)) {
            if (getView()!=null) {
                getView().findViewById(R.id.button8).setEnabled(true);
            }
        } else {
            if (getView()!=null) {
                getView().findViewById(R.id.button8).setEnabled(false);
            }
        }
    }
    protected FragmentActivity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity)activity;
    }

    //@Override
    public void onClick(View v) {

        cambiarPin();
    }


    public void cambiarPin() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String auth_token_string = settings.getString("usuario", ""/*default value*/);
        try {
            JSONObject jsobj = new JSONObject();
            jsobj.put("usuario",auth_token_string);
            jsobj.put("pin_viejo", txtPinActual.getText().toString());
            jsobj.put("pin_nuevo",txtPinNuevo.getText().toString());
            jsobj.put("gcm",Configuracion.settings.getString("claveGCM",""));
            BGTCambiarPIN bgt = new BGTCambiarPIN(Configuracion.getApiUrlPin(), getActivity(), jsobj);
            bgt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        }

}

