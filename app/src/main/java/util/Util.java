package util;

import android.content.Context;
import android.location.LocationManager;

import java.util.List;

public class Util {

    public static boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


    public static String leftTrim(String str, String replace) {
        return str.replaceAll("^\\s+",replace);
    }
    public static String rightTrim(String str, String replace) {
        return str.replaceAll("\\s+$",replace);
    }
    public static String allTrim(String str, String replace) {
        return str.replaceAll("\\s+",replace);
    }

}
