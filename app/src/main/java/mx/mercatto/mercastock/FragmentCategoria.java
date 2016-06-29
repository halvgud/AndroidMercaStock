package mx.mercatto.mercastock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import mx.mercatto.mercastock.BGT.BGTCargarListadoCategoria;



public class FragmentCategoria extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_departamento, container, false);

           // if (PushNotificationService.Xrray != null) {
          //      getActivity().setTitle("Lista de Categorias" + PushNotificationService.Xrray.get(0));
          //  } else {
                getActivity().setTitle("Lista de Categorias");
          //  }

            cargarListadoCategoria();

        //Main.setContentView(R.layout.activity_main_logged);
        return rootView;
    }

    public void cargarListadoCategoria() {
        try {
        BGTCargarListadoCategoria bgt;
        JSONObject jsonObj1 = new JSONObject();
        jsonObj1.put("claveApi", Configuracion.settings.getString("ClaveApi",""));
        // Create the POST object and add the parameters
            Log.d("Clave Api ",Configuracion.settings.getString("ClaveApi",""));
            Log.d("CATEGORIA URL",Configuracion.getApiUrlCategoria());
        bgt = new BGTCargarListadoCategoria(Configuracion.getApiUrlCategoria(),getActivity(),jsonObj1);

           bgt.execute();
        } catch (JSONException e) {
           // throw e;
        }
    }

    public void onResume() {
        super.onResume();  // Always call the superclass method first

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
