package com.xampy.piigo.models.search;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.xampy.piigo.R;
import com.xampy.piigo.models.piigomaps.MapsCalculator;
import com.xampy.piigo.models.timer.SetIntervalTimer;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private final OnSearchResultsItemListener mListener;
    private List<SearchResultDummyContent.SearchResultDummyItem> mValues;
    private LatLng mCurrentUserLalLong;


    public interface OnTimerMinuteAgoListener {
        void OnMinutePassed(TextView text_view, int new_min);
    }

    private OnTimerMinuteAgoListener mMinuteListener = new OnTimerMinuteAgoListener() {
        @Override
        public void OnMinutePassed(TextView text_view, int new_min) {
            text_view.setText(String.format("ily a %s min", new_min));
        }
    };


    public interface OnSearchResultsItemListener {
        void OnItemClicked(SearchResultDummyContent.SearchResultDummyItem item);
    }


    public SearchResultsAdapter(
            List<SearchResultDummyContent.SearchResultDummyItem> items,
            double currentUserLal, double currentUserLong, OnSearchResultsItemListener listener){

        mValues = items;
        mCurrentUserLalLong = new LatLng(currentUserLal, currentUserLong);
        mListener = listener;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_search_results_custom_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        //Set data values
        holder.mIdentifierTextView.setText(holder.mItem.mIdentifier);
        holder.mMinAgoTextView.setText(String.format("il y a %s min", holder.mItem.mMinAgo));
        holder.mDistanceTextView.setText(String.format("%d m", holder.mItem.mDistane));

        //Now add a time interval to increase minutes
        new SetIntervalTimer(
                10000, //for testing we use 10s
                new SetIntervalTimer.OnSetIntervalTimerListener() {
                    @Override
                    public void onTimeElapsed() {
                        int new_min = Integer.parseInt(holder.mItem.mMinAgo + "1");

                        //mMinuteListener.OnMinutePassed(holder.mMinAgoTextView, new_min);
                        //holder.mMinAgoTextView.setText(String.format("ily a %s min", new_min));
                    }
                });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if the listener is not null
                if (mListener != null)
                    mListener.OnItemClicked(holder.mItem);
            }
        });

        //Calculate the distance

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                float[] results = new float[1]; //MapsCalculator.getDistanceBetween(null, null);


                LatLng toLocation = new LatLng(holder.mItem.mLal, holder.mItem.mLong);

                //Get the distance to user location
                Location.distanceBetween(
                        mCurrentUserLalLong.latitude, mCurrentUserLalLong.longitude,
                        toLocation.latitude, toLocation.longitude,
                        results);

                double distance =  (( Math.random() % 100 ) + 1);

                holder.mDistanceTextView.setText(String.format("%s km", distance));
            }
        }).start();*/

        Log.d("SEARCH_DATA", holder.mItem.mIdentifier);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public final View mView;
        public final CircleImageView mImage;
        public final TextView mIdentifierTextView;
        public final TextView mDistanceTextView;
        public final TextView mMinAgoTextView;
        public  SearchResultDummyContent.SearchResultDummyItem mItem;

        public ViewHolder(View view){
            super(view);

            mView = view;
            mImage = (CircleImageView) view.findViewById(R.id.search_results_item_photo_image_view);
            mIdentifierTextView = (TextView) view.findViewById(R.id.search_results_item_identifier_text_view) ;
            mDistanceTextView = (TextView) view.findViewById(R.id.search_results_item_distance_text_view);
            mMinAgoTextView = (TextView) view.findViewById(R.id.search_results_item_min_ago_text_view);
        }
    }
}
