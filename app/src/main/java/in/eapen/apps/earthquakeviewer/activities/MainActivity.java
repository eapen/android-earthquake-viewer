package in.eapen.apps.earthquakeviewer.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import in.eapen.apps.earthquakeviewer.R;
import in.eapen.apps.earthquakeviewer.main.FetchEarthquakesInteractor;
import in.eapen.apps.earthquakeviewer.main.MainPresenter;
import in.eapen.apps.earthquakeviewer.main.MainView;
import in.eapen.apps.earthquakeviewer.models.Earthquake;
import in.eapen.apps.earthquakeviewer.utils.NetworkCheck;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter mainPresenter;
    private ListView listView;

    private SwipeRefreshLayout swipeContainer;
    NetworkCheck nc = new NetworkCheck(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.lvEarthquakes);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!nc.isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT);
                } else {
                   listView.setAdapter(new ArrayAdapter<Earthquake>(this, ));
                }
            }
        }
        if (!nc.isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
        } else {
            fetchEarthquakesAsync();
        }
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void setItems(List<Earthquake> earthquakeList) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onFinished(List<Earthquake> earthquakeList) {

    }
}
