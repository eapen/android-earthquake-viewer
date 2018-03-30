package in.eapen.apps.earthquakeviewer.presenter;

import java.util.List;

import in.eapen.apps.earthquakeviewer.model.Earthquake;


public interface EarthquakesContract {

  interface View {

    void showToastNotification(String message, int ToastDuration);

    void showEarthquakes(List<Earthquake> earthquakes);

  }

  interface UserActionsListener {
    void fetchData();
  }
}
