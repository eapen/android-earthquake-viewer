package in.eapen.apps.earthquakeviewer.main;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import in.eapen.apps.earthquakeviewer.models.Earthquake;
import in.eapen.apps.earthquakeviewer.utils.DateHelper;
import in.eapen.apps.earthquakeviewer.utils.FetchUrlTask;

/**
 * Created by geapen on 9/10/16.
 */
public class FetchEarthquakesInteractorImpl implements FetchEarthquakesInteractor {

    // default bounds  => ((37.174703712988865, -122.81540517968756), (38.045005508057116, -120.89279775781256))east
    // TODO: remove formatted=true to reduce data transferred, allow modification of lat/long
    // private static final String API_URL = "http://api.geonames.org/earthquakesJSON?username=mkoppelman&north=43.3498&south=41.349837&west=13&east=15";

    // Top 100 Worst earthquakes on the planet
    private static final String API_URL = "http://api.geonames.org/earthquakesJSON?username=mkoppelman&north=90&south=-90&west=-180&east=180&maxRows=100";
    private static final String EARTHQUAKE_LIST = "earthquakes";
    private List<Earthquake> earthquakeList;

    @Override
    public List<Earthquake> getEarthquakes(OnFinishedListener listener) {

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new FetchUrlTask().execute(API_URL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Log.d("debug", jsonResponse.toString());


        JSONArray results;
        JSONObject eqObject = null;


        // For legibility let's sort by date rather than the default magnitude

        class DateIndexEq {
            private int index;
            private float date;

            public DateIndexEq (int index, long date) {
                this.index = index;
                this.date = date;
            }
        }

        class DepthIndexEqComparator implements Comparator<DateIndexEq> {
            public int compare(DateIndexEq a, DateIndexEq b) {
                return (b.date > a.date) ? 1 : -1;
            }
        }

        if (jsonResponse != null && jsonResponse.has(EARTHQUAKE_LIST)) {
            try {
                results = jsonResponse.getJSONArray(EARTHQUAKE_LIST);
                DepthIndexEqComparator comparator = new DepthIndexEqComparator();
                ArrayList<DateIndexEq> dateList = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    eqObject = results.getJSONObject(i);
                    dateList.add(new DateIndexEq(i, DateHelper.getEpoch(eqObject.getString("datetime"))));
                }

                Collections.sort(dateList, comparator);

                for (DateIndexEq die : dateList){
                    eqObject = results.getJSONObject(die.index);
                    Earthquake earthquake = new Earthquake();
                    earthquake.datetime = DateHelper.getEpoch(eqObject.getString("datetime"));
                    earthquake.depth = (float) eqObject.getDouble("depth");
                    earthquake.magnitude = (float) eqObject.getDouble("magnitude");
                    earthquake.latitude = eqObject.getDouble("lat");
                    earthquake.longitude = eqObject.getDouble("lng");
                    earthquake.source = eqObject.getString("src");

                    earthquakeList.add(earthquake);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            String errorMessage = "Sorry, no data was found. Please try another location.";
            if (jsonResponse != null && jsonResponse.has("status")) {
                try {
                    errorMessage = jsonResponse.getJSONObject("status").getString("message").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("debug", String.valueOf(earthquakeList.size()) + " elements in array");
        return earthquakeList;
    }

}
