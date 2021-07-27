package com.xampy.piigo.views.search;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xampy.piigo.PiigoActivity;
import com.xampy.piigo.R;
import com.xampy.piigo.models.UserTypeEnum;
import com.xampy.piigo.models.search.SearchResultDummyContent;
import com.xampy.piigo.models.search.SearchResultsAdapter;
import com.xampy.piigo.models.userentity.PiigoUserClient;
import com.xampy.piigo.views.dialogs.SearchResultsItemViewDialogFragment;
import com.xampy.piigo.views.dialogs.WaitingDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.xampy.piigo.models.search.SearchResultDummyContent.SEARCH_RESULTS_ITEMS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_USER_SEARCH_TYPE = "search_user_type";

    private static final String SEARCH_GET_OFFERS_URL = "http://192.168.137.199:8000/offers";
    private static final String SERVER_REQUEST_RESULT_STRING = "status";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //[START class variables ]
    private UserTypeEnum mSearchUserType;
    private SearchResultsAdapter mSearchResultsAdapter;
    private TextView mResultsCountTextView;
    private ImageView mSearchUserTypeIconImageView;

    //[END class variables ]

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param search_user_type Parameter 2.
     * @return A new instance of fragment SearchResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultsFragment newInstance(UserTypeEnum search_user_type) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_USER_SEARCH_TYPE, search_user_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            mSearchUserType = (UserTypeEnum) getArguments().getSerializable(ARG_PARAM_USER_SEARCH_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_results, container, false);

        mResultsCountTextView = (TextView) root.findViewById(R.id.search_results_count_text_view);
        mSearchUserTypeIconImageView = (ImageView) root.findViewById(R.id.search_result_user_type_image_image_view);

        //Change the icon according to the user search type
        if(mSearchUserType != null ){
            //By default the user type is a car
            if(mSearchUserType == UserTypeEnum.CAR){

                Context context = getContext();
                String iconName = "ic_car_white";
                mSearchUserTypeIconImageView.setImageDrawable(
                        context.getResources().getDrawable(
                                context.getResources().getIdentifier(
                                        iconName,
                                        "drawable",
                                        context.getPackageName()
                                )
                        )
                );
            }
        }

        RecyclerView mSearchResultsItemsRecyclerView = (RecyclerView) root.findViewById(R.id.search_results_list_view);
        mSearchResultsItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        final WaitingDialogFragment wait = new WaitingDialogFragment();
        wait.show(getActivity().getSupportFragmentManager(), "WAIT_DIALOG");
        wait.setCancelable(false);

        //[START construct the adapter items here]
        //We drop all elements in the search results arrays
        SEARCH_RESULTS_ITEMS.clear();

        mSearchResultsAdapter = new SearchResultsAdapter(
                SEARCH_RESULTS_ITEMS,
                ((PiigoActivity) getActivity()).getPiigoUserLatitude(),
                ((PiigoActivity) getActivity()).getPiigoUserLongitude(),
                new SearchResultsAdapter.OnSearchResultsItemListener() {
                    @Override
                    public void OnItemClicked(SearchResultDummyContent.SearchResultDummyItem item) {

                        //Open the dialog
                        SearchResultsItemViewDialogFragment fragment = new SearchResultsItemViewDialogFragment();
                        fragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "SEARCH_RESULTS_ITEM");
                    }
                });

        mSearchResultsItemsRecyclerView.setAdapter(mSearchResultsAdapter);

        //Make a request on server
        //with arguments from the search data entry fragment
        StringRequest result_request = new StringRequest(
                Request.Method.GET,
                SEARCH_GET_OFFERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // convert into a json

                        Log.d("SEARCH RESULTS", response);

                        try {
                            JSONObject json_offers = new JSONObject(response);
                            json_offers = json_offers.getJSONObject("offers");

                            for (int i = 1; i < 500; i++) {

                                try {
                                    JSONObject offer = json_offers.getJSONObject(String.valueOf(i));
                                    String offer_identifier = offer.getString("identifier");
                                    double offer_long = offer.getDouble("long");
                                    double offer_lal = offer.getDouble("lal");
                                    String offer_minAgo = offer.getString("ago");
                                    int distance = offer.getInt("distance");

                                    //Add offer to adapter
                                    SearchResultDummyContent.addSearchResultItem(
                                            SearchResultDummyContent.createDummyItem(
                                                    String.valueOf(i),
                                                    offer_identifier,
                                                    "url",
                                                    offer_long,
                                                    offer_lal,
                                                    offer_minAgo,
                                                    distance)
                                    );

                                    //Notify data set change
                                    String c = mSearchResultsAdapter.getItemCount() + " résultat(s)";
                                    mResultsCountTextView.setText(c);
                                    mSearchResultsAdapter.notifyDataSetChanged();
                                }catch (Exception e) {
                                    //We pass

                                    wait.dismiss();

                                    break;
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        wait.dismiss();
                    }
                });

        //Add request to queue
        ( (PiigoActivity)getActivity() ).getmVolleyRequestQueueSingleton().getRequestQueue().add(result_request);

        //wait.dismiss();
        //[END construct the adapter items here]

        return root;
    }


    private void makeResearch(String town, String district){

    }

}
