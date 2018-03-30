package in.eapen.apps.earthquakeviewer.model;


public class Earthquake {
    public long datetime;
    public float depth;
    public double longitude;
    public double latitude;
    public float magnitude;
    public String source;
}

/*

{"earthquakes": [
  {
    "datetime": "2011-03-11 04:46:23",
    "depth": 24.4,
    "lng": 142.369,
    "src": "us",
    "eqid": "c0001xgp",
    "magnitude": 8.8,
    "lat": 38.322
  },
  {
    "datetime": "2012-04-11 06:38:37",
    "depth": 22.9,
    "lng": 93.0632,
    "src": "us",
    "eqid": "c000905e",
    "magnitude": 8.6,
    "lat": 2.311
  }
 ]
}
*/

