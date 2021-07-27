package com.xampy.piigo.views.search;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xampy.piigo.R;
import com.xampy.piigo.models.UserTypeEnum;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchDataEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchDataEntryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    //[START interfaces definition]
    public interface OnSearchDataEntryFragmentInteractionListener {
        void onSearchButtonClicked(UserTypeEnum searchUserType);
    }
    //[END interfaces definition]

    //[START class variables ]
    private ImageView IconImageView;
    private TextView IconTextView;
    private TextView mRadiusTextView;

    private ImageView mNextImageView;
    private ImageView mPrevImageView;
    private ImageView mIncreaseImageView;
    private ImageView mDecreaseImageView;

    private Button mNextActionButton;
    private Button mGoBackButton;

    private FrameLayout mRadiusSetFrameLayout;
    private LinearLayout mLocationSetLinearLayout;

    private int mCurrentChoice;
    private short mRadius;
    private UserTypeEnum mSearchUserType;


    private OnSearchDataEntryFragmentInteractionListener mListener;
    //[END class variables ]

    public SearchDataEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchDataEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchDataEntryFragment newInstance(String param1, String param2) {
        SearchDataEntryFragment fragment = new SearchDataEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mCurrentChoice = 0;
        mRadius = 100;
        mSearchUserType = UserTypeEnum.BIKE; //By default it's a bike
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_data_entry, container, false);

        IconImageView = (ImageView) root.findViewById(R.id.piigo_activity_user_type_icon_image_view);
        IconTextView = (TextView) root.findViewById(R.id.piigo_activity_user_type_name_text_view);

        //[START Manage prev and next Click]
        mNextImageView = (ImageView) root.findViewById(R.id.search_user_select_next_text_view);
        mNextImageView.setOnClickListener(mNextPrevClickListener);
        mPrevImageView = (ImageView) root.findViewById(R.id.search_user_select_prev_text_view);
        mPrevImageView.setOnClickListener(mNextPrevClickListener);
        //[END Manage prev and next Click]

        //[START Manage increase and decrease radius Click]
        mIncreaseImageView = (ImageView) root.findViewById(R.id.search_increase_radius_image_view);
        mIncreaseImageView.setOnClickListener(mUpDownClickListener);
        mDecreaseImageView = (ImageView) root.findViewById(R.id.search_decrease_radius_image_view);
        mDecreaseImageView.setOnClickListener(mUpDownClickListener);

        mRadiusTextView = (TextView) root.findViewById(R.id.search_radius_text_view);
        mRadiusTextView.setText("50 m");
        //[END Manage increase and decrease radius Click]


        //[START switching between the location chooser and the radius chooser]
        mRadiusSetFrameLayout = (FrameLayout) root.findViewById(R.id.search_first_data_entry_frame);
        mLocationSetLinearLayout = (LinearLayout) root.findViewById(R.id.search_second_data_entry_linear_frame);


        mNextActionButton = (Button) root.findViewById(R.id.piigo_user_search_action_button);
        mGoBackButton = (Button) root.findViewById(R.id.search_user_back_button);


        mNextActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("SEARCH FRAGMENT",  mNextActionButton.getText().toString());

                //Check if the text is "Search" in french
                if(mNextActionButton.getText().toString().equals("Rechercher")){


                    if(mListener != null)
                        mListener.onSearchButtonClicked(mSearchUserType);
                }else {
                    //Certainly we have the next text

                    //Hide the radius chooser
                    //and show the position chooser

                    mRadiusSetFrameLayout.setVisibility(View.GONE);
                    mLocationSetLinearLayout.setVisibility(View.VISIBLE);

                    mGoBackButton.setVisibility(View.VISIBLE);

                    //Change the text name to "Rechercher"
                    mNextActionButton.setText(R.string.search_verb);

                }
            }
        });



        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Switch between frame (location and radius)
                //and hide the gp back button
                mLocationSetLinearLayout.setVisibility(View.GONE);
                mRadiusSetFrameLayout.setVisibility(View.VISIBLE);


                mGoBackButton.setVisibility(View.GONE);

                //Change the text name to "Suivant"
                mNextActionButton.setText(R.string.go_next);
            }
        });
        //END switching between the location chooser and the radius chooser]


        return root;
    }


    public void setListener(OnSearchDataEntryFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }


    //[START the next and prev click handler ]
    private final View.OnClickListener mNextPrevClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Handle Click here

            if (v.getId() == R.id.search_user_select_next_text_view) {
                mCurrentChoice += 1;
                mCurrentChoice = (mCurrentChoice % 3);
            }else if (v.getId() == R.id.search_user_select_prev_text_view) {
                mCurrentChoice -= 1;

                if(mCurrentChoice < 0) { mCurrentChoice = 2 - ((-mCurrentChoice) % 2); }
            }

            String userType = "ZEDMAN";
            String iconName = "ic_bike_black";
            if (mCurrentChoice == 0) {
                userType = "ZEDMAN";
                iconName = "ic_bike_black";
                mSearchUserType = UserTypeEnum.BIKE;
            } else if (mCurrentChoice == 1) {
                userType = "TAXIMAN";
                iconName = "ic_car_black";
                mSearchUserType = UserTypeEnum.CAR;
            }

            Context context = getContext();

            assert context != null;

            IconImageView.setImageDrawable(
                    context.getResources().getDrawable(
                            context.getResources().getIdentifier(
                                    iconName,
                                    "drawable",
                                    context.getPackageName()
                            )
                    )
            );

            IconTextView.setText(userType);


            //Change data on view
            Toast.makeText(getActivity(), userType + mCurrentChoice, Toast.LENGTH_SHORT).show();
        }
    };
    //[END the next and prev click handler ]



    //[START the increase and decrease click handler ]
    private final View.OnClickListener mUpDownClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Handle Click here

            if (v.getId() == R.id.search_increase_radius_image_view) {
               mRadius += 50;

               if(mRadius > 1000) mRadius = 1000;
            }else if (v.getId() == R.id.search_decrease_radius_image_view) {
                mRadius -= 50;

                if(mRadius < 100) mRadius = 100;
            }

            String userRadius = mRadius + " m";
            mRadiusTextView.setText(userRadius);
        }
    };
    //[END the the increase and decrease click handler ]
}
