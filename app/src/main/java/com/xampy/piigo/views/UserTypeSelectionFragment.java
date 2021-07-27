package com.xampy.piigo.views;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xampy.piigo.R;
import com.xampy.piigo.models.UserTypeEnum;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserTypeSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserTypeSelectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    //[START interfaces definition]
    public interface OnUserTypeSelectionFragmentInteractionListener {

        /**
         * Notify the next fragment
         * @param userType choosed by user
         */
        void onNexTButtonClicked(UserTypeEnum userType);
    }
    //[END interfaces definition]





    //[START class variables]
    /**
     * The variable @mCurrentChoice is for representing the type
     * of user (MOTO = 1, CAR = 2; CLIENT = 3
     * default is a BIKE
     */
    private int mCurrentChoice;
    private UserTypeEnum mUserType = UserTypeEnum.BIKE;


    private ImageView mNextImageView;
    private ImageView mPrevImageView;

    private ImageView IconImageView;

    private TextView IconTextView;


    private OnUserTypeSelectionFragmentInteractionListener mListener;

    //[END class variables]





    public UserTypeSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserTypeSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserTypeSelectionFragment newInstance(String param1, String param2) {
        UserTypeSelectionFragment fragment = new UserTypeSelectionFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_user_type_selection, container, false);

        IconImageView = (ImageView) root.findViewById(R.id.user_type_icon_image_view);
        IconTextView = (TextView) root.findViewById(R.id.user_type_name_text_view);

        //[START Manage prev and next Click]
        mNextImageView = (ImageView) root.findViewById(R.id.user_select_next_text_view);
        mNextImageView.setOnClickListener(mNextPrevClickListener);
        mPrevImageView = (ImageView) root.findViewById(R.id.user_select_prev_text_view);
        mPrevImageView.setOnClickListener(mNextPrevClickListener);
        //[END Manage prev and next Click]

        Button NextButton = (Button) root.findViewById(R.id.user_type_next_button);
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){

                    //Notify the next fragment with the user type
                    mListener.onNexTButtonClicked(mUserType);
                }
            }
        });




        return root;
    }

    public void setUserTypeSelectionListener(OnUserTypeSelectionFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    //[START the next and prev click handler ]
    private final View.OnClickListener mNextPrevClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Handle Click here

            if (v.getId() == R.id.user_select_next_text_view) {
                mCurrentChoice += 1;
                mCurrentChoice = (mCurrentChoice % 3);
            }else if (v.getId() == R.id.user_select_prev_text_view) {
                mCurrentChoice -= 1;

                if(mCurrentChoice < 0) { mCurrentChoice = 3 - ((-mCurrentChoice) % 3); }
            }

            String userType = "ZEDMAN";
            String iconName = "ic_bike_black";
            if (mCurrentChoice == 0) {
                userType = "ZEDMAN";
                iconName = "ic_bike_white";
                mUserType = UserTypeEnum.BIKE;
            } else if (mCurrentChoice == 1) {
                userType = "TAXIMAN";
                iconName = "ic_car_white";
                mUserType = UserTypeEnum.CAR;
            } else if (mCurrentChoice == 2) {
                userType = "CLIENT";
                iconName = "ic_client_white";
                mUserType = UserTypeEnum.CLIENT;
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
}
