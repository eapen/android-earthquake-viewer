package in.eapen.apps.earthquakeviewer.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by geapen on 8/25/16.
 */

public final class DateHelper {
    public static long getEpoch(String rawJsonDate) {
        String geonamesFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sf = new SimpleDateFormat(geonamesFormat, Locale.ENGLISH);
        sf.setLenient(true);

        long dateMillis = 0l;
        try {
            dateMillis = sf.parse(rawJsonDate).getTime();
            Log.d("debug", Long.toString(dateMillis));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateMillis;

    }
}
