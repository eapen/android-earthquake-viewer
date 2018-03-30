package in.eapen.apps.earthquakeviewer.model;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

import in.eapen.apps.earthquakeviewer.utils.DateHelper;
import in.eapen.apps.earthquakeviewer.utils.FetchUrlTask;


public class EarthquakeRepositoryImpl implements EarthquakeRepository {

  List<Earthquake> earthquakes = new ArrayList<>();

  @Override
  public void getEarthquakes(@NonNull final LoadEarthquakesCallback callback) {
    JSONObject jsonResponse = null;
    try {
      jsonResponse = new FetchUrlTask().execute(API_URL).get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    JSONArray results;
    JSONObject eqObject;


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

    String errorMessage = "Sorry, no data was found. Please try another location.";

    if (jsonResponse != null && jsonResponse.has(EARTHQUAKE_LIST)) {
      try {
        results = jsonResponse.getJSONArray(EARTHQUAKE_LIST);
        DepthIndexEqComparator comparator = new DepthIndexEqComparator();
        // Let PQ take care of sorting by Date and we can use Index to populate adapter
        PriorityQueue<DateIndexEq> queue = new PriorityQueue<>(results.length(), comparator);
        for (int i = 0; i < results.length(); i++) {
          eqObject = results.getJSONObject(i);
          queue.add(new DateIndexEq(i, DateHelper.getEpoch(eqObject.getString("datetime"))));
        }

        DateIndexEq die;  // pun not intended

        while ((die = queue.poll()) != null) {
          eqObject = results.getJSONObject(die.index);
          Earthquake earthquake = new Earthquake();
          earthquake.datetime = DateHelper.getEpoch(eqObject.getString("datetime"));
          earthquake.depth = (float) eqObject.getDouble("depth");
          earthquake.magnitude = (float) eqObject.getDouble("magnitude");
          earthquake.latitude = eqObject.getDouble("lat");
          earthquake.longitude = eqObject.getDouble("lng");
          earthquake.source = eqObject.getString("src");

          earthquakes.add(earthquake);
        }
        callback.onEarthquakesLoaded(earthquakes);
      } catch (JSONException e) {
        errorMessage = e.toString();
        Log.d("debug", errorMessage);
        callback.onEarthquakesLoadFailure(errorMessage);
      }

    } else {
      if (jsonResponse != null && jsonResponse.has("status")) {
        try {
          errorMessage = jsonResponse.getJSONObject("status").getString("message").toString();
        } catch (JSONException e) {
          errorMessage = e.toString();
          Log.d("debug", errorMessage);
        }
      }
      callback.onEarthquakesLoadFailure(errorMessage);
    }
  }

}
