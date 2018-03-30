package in.eapen.apps.earthquakeviewer.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import in.eapen.apps.earthquakeviewer.R;
import in.eapen.apps.earthquakeviewer.model.Earthquake;

/**
 * Created by geapen on 8/24/16.
 */
public class EarthquakesAdapter extends ArrayAdapter<Earthquake> {
    private Typeface roboto;

    // cache
    static class ViewHolderItem {
        TextView magnitude;
        TextView latitude;
        TextView longitude;
        TextView date;
    }

    // context data source
    public EarthquakesAdapter(Context context, List<Earthquake> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        roboto = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Bold.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;

        Earthquake earthquake = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_earthquake, parent, false);
            // setup viewholder which is especially helpful for longer lists
            viewHolder = new ViewHolderItem();
            viewHolder.magnitude = (TextView) convertView.findViewById(R.id.tvMagnitude);
            viewHolder.latitude = (TextView) convertView.findViewById(R.id.tvLatitude);
            viewHolder.longitude = (TextView) convertView.findViewById(R.id.tvLongitude);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);

            // set the holder with the view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        viewHolder.magnitude.setTypeface(roboto);

        DecimalFormat decimalFormatter = new DecimalFormat("#0.00");

        viewHolder.magnitude.setText(decimalFormatter.format(earthquake.magnitude));
        viewHolder.latitude.setText(decimalFormatter.format(earthquake.latitude));
        viewHolder.longitude.setText(decimalFormatter.format(earthquake.longitude));

        if (earthquake.magnitude >= 8.0) {
            viewHolder.magnitude.setTextColor(Color.RED);
        } else {
            viewHolder.magnitude.setTextColor(Color.BLACK);
        }

        // if the dates are more than a few days old, it shows the actual date instead
        // which happens to be the case for most areas I searched for
        viewHolder.date.setText(DateUtils.getRelativeTimeSpanString(earthquake.datetime));

        return convertView;
    }
}
