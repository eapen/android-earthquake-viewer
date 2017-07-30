package in.eapen.apps.earthquakeviewer.main;

import java.util.List;

import in.eapen.apps.earthquakeviewer.models.Earthquake;

/**
 * Created by geapen on 9/10/16.
 */
public interface MainView {

    void setItems(List<Earthquake> earthquakeList);

    void showMessage(String message);
}
