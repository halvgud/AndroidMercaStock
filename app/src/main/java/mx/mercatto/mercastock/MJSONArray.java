package mx.mercatto.mercastock;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Juan Carlos De León on 01/06/2016.
 */
public class MJSONArray extends JSONArray {

    @Override
    public Object remove(int index) {

        JSONArray output = new JSONArray();
        int len = this.length();
        for (int i = 0; i < len; i++)   {
            if (i != index) {
                try {
                    output.put(this.get(i));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return output;
        //return this; If you need the input array in case of a failed attempt to remove an item.
    }
}