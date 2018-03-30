package in.eapen.apps.earthquakeviewer.model;

import android.support.annotation.NonNull;

import java.util.List;


public interface EarthquakeRepository {

  String API_URL = "http://api.geonames.org/earthquakesJSON?username=mkoppelman&north=90&south=-90&west=-180&east=180&maxRows=100";
  String EARTHQUAKE_LIST = "earthquakes";

  interface LoadEarthquakesCallback {
    void onEarthquakesLoaded(List<Earthquake> earthquakeList);
    void onEarthquakesLoadFailure(String error);
  }

  void getEarthquakes(@NonNull LoadEarthquakesCallback callback);
}
