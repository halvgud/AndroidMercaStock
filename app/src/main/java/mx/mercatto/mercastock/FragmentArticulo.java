package mx.mercatto.mercastock;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mx.mercatto.mercastock.BGT.BGTAPI;
import mx.mercatto.mercastock.BGT.BGTCargarListadoArticulo;

public class FragmentArticulo extends Fragment {

    String cat_id="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_lista_articulo, container, false);
            Bundle args = getArguments();
            String articulo = args.getString("articulo");
            cat_id = args.getString(Configuracion.getIdCategoria());
            getActivity().setTitle("Lista de " + articulo);
            if (PushNotificationService.Xrray != null) {
                for (int j = 0; j < PushNotificationService.Xrray.size(); j++) {
                    if (PushNotificationService.Xrray.get(j).equals(cat_id)) {
                        PushNotificationService.Xrray.remove(j);
                        break;
                    }
                }
            }
            if (!Configuracion.settings.getString("claveGCM", "").equals(Main.idRegistro)) {
                BGTAPI.ClAp = 9;
            }
            cargarListadoArticulo();

            return rootView;

    }

    public void cargarListadoArticulo() {
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put(Configuracion.getIdCategoria(), cat_id);
            jsonObj1.put("claveApi", Configuracion.settings.getString("ClaveApi",""));
            BGTCargarListadoArticulo bgt = new BGTCargarListadoArticulo(Configuracion.getApiUrlArticulo(), getActivity(), jsonObj1);
            bgt.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
