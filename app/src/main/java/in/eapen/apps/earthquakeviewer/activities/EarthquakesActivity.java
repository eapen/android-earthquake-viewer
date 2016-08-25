package in.eapen.apps.earthquakeviewer.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import in.eapen.apps.earthquakeviewer.R;
import in.eapen.apps.earthquakeviewer.adapters.EarthquakesAdapter;
import in.eapen.apps.earthquakeviewer.models.Earthquake;

public class EarthquakesActivity extends AppCompatActivity {


    private ArrayList<Earthquake> earthquakes;
    private EarthquakesAdapter earthquakesAdapter;
    private static final String API_URL = "http://api.geonames.org/earthquakesJSON?username=mkoppelman";
    private SwipeRefreshLayout swipeContainer;

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
        // fetch popular photos
        fetchEarthquakes();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchEarthquakesAsync();
            }
        });
    }

    private void fetchEarthquakesAsync() {
        earthquakesAdapter.clear();
        fetchEarthquakes();
        swipeContainer.setRefreshing(false);
        Toast.makeText(getApplicationContext(), "Refreshed data", Toast.LENGTH_SHORT).show();
    }

    private void fetchEarthquakes() {

        Random rand = new Random();
        for (int i=0; i< 10; i++) {
            Earthquake earthquake = new Earthquake();
            earthquake.datetime = (long) i;
            earthquake.depth = (long) (i * 10);
            earthquake.magnitude = rand.nextInt(12);
            earthquake.latitude = rand.nextFloat() * (180) + -90;
            earthquake.longitude = rand.nextFloat() * (360) + -180;

            earthquakes.add(earthquake);
        }

        Log.d("debug", String.valueOf(earthquakes.size()) + " elements in array");
        earthquakesAdapter.notifyDataSetChanged();
    }
}
