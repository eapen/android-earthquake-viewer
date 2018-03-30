package in.eapen.apps.earthquakeviewer.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import in.eapen.apps.earthquakeviewer.model.Earthquake;
import in.eapen.apps.earthquakeviewer.model.EarthquakeRepository;
import in.eapen.apps.earthquakeviewer.model.EarthquakeRepositoryImpl;


public class EarthquakesPresenter implements EarthquakesContract.UserActionsListener {

  private final EarthquakesContract.View earthquakeView;
  private EarthquakeRepository repository;

  public EarthquakesPresenter(@NonNull EarthquakesContract.View earthquakeView) {
    this.earthquakeView = earthquakeView;
    repository = new EarthquakeRepositoryImpl();
  }

  @Override
  public void fetchData() {
    repository.getEarthquakes(new EarthquakeRepository.LoadEarthquakesCallback() {
      @Override
      public void onEarthquakesLoaded(List<Earthquake> earthquakeList) {
        earthquakeView.showEarthquakes(earthquakeList);
      }
      @Override
      public void onEarthquakesLoadFailure(String error) {
        Log.d("debug", "An error occurred");
        earthquakeView.showToastNotification(error, Toast.LENGTH_LONG);
      }
    });
  }
}
