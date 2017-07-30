package in.eapen.apps.earthquakeviewer.main;

import java.util.List;

import in.eapen.apps.earthquakeviewer.models.Earthquake;

/**
 * Created by geapen on 9/10/16.
 */
public interface FetchEarthquakesInteractor {

    interface OnFinishedListener {
        void onFinished(List<Earthquake> earthquakeList);
    }

    List<Earthquake> getEarthquakes(OnFinishedListener listener);
}
