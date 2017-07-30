package in.eapen.apps.earthquakeviewer.main;

import java.util.List;

import in.eapen.apps.earthquakeviewer.models.Earthquake;

/**
 * Created by geapen on 9/10/16.
 */
public class MainPresenterImpl implements MainPresenter, FetchEarthquakesInteractor.OnFinishedListener {

    private MainView mainView;
    private FetchEarthquakesInteractor fetchEarthquakesInteractor;

    public MainPresenterImpl(MainView mainView, FetchEarthquakesInteractor fetchEarthquakesInteractor) {
        this.mainView = mainView;
        this.fetchEarthquakesInteractor = fetchEarthquakesInteractor;
    }

    @Override
    public void onResume() {
        fetchEarthquakesInteractor.getEarthquakes(this);
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onFinished(List<Earthquake> earthquakeList) {
        if (mainView != null) {
            mainView.setItems(earthquakeList);
        }
    }

    public MainView getMainView() {
        return mainView;
    }
}
