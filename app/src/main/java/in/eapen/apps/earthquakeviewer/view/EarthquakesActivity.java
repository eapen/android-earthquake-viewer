package in.eapen.apps.earthquakeviewer.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.eapen.apps.earthquakeviewer.R;
import in.eapen.apps.earthquakeviewer.presenter.EarthquakesContract;
import in.eapen.apps.earthquakeviewer.presenter.EarthquakesPresenter;
import in.eapen.apps.earthquakeviewer.model.Earthquake;
import in.eapen.apps.earthquakeviewer.utils.NetworkCheck;


public class EarthquakesActivity extends AppCompatActivity implements EarthquakesContract.View {

  private EarthquakesPresenter presenter;
  private EarthquakesAdapter earthquakesAdapter;
  private List<Earthquake> earthquakes;
  // default bounds  => ((37.174703712988865, -122.81540517968756), (38.045005508057116, -120.89279775781256))east
  // TODO: remove formatted=true to reduce data transferred, allow modification of lat/long
  // private static final String API_URL = "http://api.geonames.org/earthquakesJSON?username=mkoppelman&north=43.3498&south=41.349837&west=13&east=15";
  // Top 100 Worst earthquakes on the planet

  private SwipeRefreshLayout swipeContainer;
  NetworkCheck nc = new NetworkCheck(this);
  private ProgressBar progressBar;


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
      showToastNotification("Closing application, network unavailable", Toast.LENGTH_LONG);
      this.finish();
    } else {
      fetchEarthquakes();
    }

    // Lookup the swipe container view
    swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        if (!nc.isNetworkAvailable()) {
          showToastNotification("Please check your network connection", Toast.LENGTH_LONG);
        } else {
          fetchEarthquakes();
        }
        swipeContainer.setRefreshing(false);
      }
    });

  }

  private void fetchEarthquakes() {
    presenter = new EarthquakesPresenter(this);
    presenter.fetchData();
  }

  @Override
  public void showToastNotification(String message, int ToastDuration) {
    Toast.makeText(getApplicationContext(), message, ToastDuration).show();
  }

  @Override
  public void showEarthquakes(List<Earthquake> earthquakeList) {
    earthquakes.clear();
    earthquakes.addAll(earthquakeList);
    earthquakesAdapter.notifyDataSetChanged();
    Log.d("debug", String.valueOf(earthquakeList.size()) + " elements in array");
  }
}
