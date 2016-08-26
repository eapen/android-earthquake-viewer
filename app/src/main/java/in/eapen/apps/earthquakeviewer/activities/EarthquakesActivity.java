package in.eapen.apps.earthquakeviewer.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

import in.eapen.apps.earthquakeviewer.R;
import in.eapen.apps.earthquakeviewer.adapters.EarthquakesAdapter;
import in.eapen.apps.earthquakeviewer.models.Earthquake;
import in.eapen.apps.earthquakeviewer.utils.DateHelper;
import in.eapen.apps.earthquakeviewer.utils.FetchUrlTask;
import in.eapen.apps.earthquakeviewer.utils.NetworkCheck;

public class EarthquakesActivity extends AppCompatActivity {


    private ArrayList<Earthquake> earthquakes;
    private EarthquakesAdapter earthquakesAdapter;
    // default bounds  => ((37.174703712988865, -122.81540517968756), (38.045005508057116, -120.89279775781256))east
    // TODO: remove formatted=true to reduce data transferred, allow modification of lat/long
    // private static final String API_URL = "http://api.geonames.org/earthquakesJSON?username=mkoppelman&north=43.3498&south=41.349837&west=13&east=15";
    // Top 100 Worst earthquakes on the planet
    private static final String API_URL = "http://api.geonames.org/earthquakesJSON?username=mkoppelman&north=90&south=-90&west=-180&east=180&maxRows=100";
    private static final String EARTHQUAKE_LIST = "earthquakes";

    private SwipeRefreshLayout swipeContainer;
    NetworkCheck nc = new NetworkCheck(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquakes);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        earthquakes = new ArrayList<>();
        earthquakesAdapter = new EarthquakesAdapter(this, earthquakes);
        ListView lv = (ListView) findViewById(R.id.lvEarthquakes);
        lv.setAdapter(earthquakesAdapter);

        if (!nc.isNetworkAvailable()) {
            Toast.makeText(this, "Closing application, network unavailable", Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            // fetch popular photos
            fetchEarthquakes();
        }

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!nc.isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                } else {
                    fetchEarthquakesAsync();
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void fetchEarthquakesAsync() {
        earthquakes.clear();
        fetchEarthquakes();
        Toast.makeText(getApplicationContext(), "Refreshed data", Toast.LENGTH_SHORT).show();
    }

    private void fetchEarthquakes() {

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new FetchUrlTask().execute(API_URL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Log.d("debug", jsonResponse.toString());


        JSONArray results = null;
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
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
        Log.d("debug", String.valueOf(earthquakes.size()) + " elements in array");
        earthquakesAdapter.notifyDataSetChanged();
    }


}
