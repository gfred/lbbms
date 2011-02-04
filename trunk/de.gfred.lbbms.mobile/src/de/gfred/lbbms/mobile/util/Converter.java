package de.gfred.lbbms.mobile.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.gfred.lbbms.mobile.services.LocationData;

public class Converter {
    private static final String TAG = "de.gfred.lbbms.mobile.util.Converter";

    public static JSONObject convertLocationDataIntoJSONObject(LocationData data) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(Values.JSON_LOCATION_LONGITUDE, data.longitude);
            obj.put(Values.JSON_LOCATION_LATITUDE, data.latitude);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return obj;
    }
}
