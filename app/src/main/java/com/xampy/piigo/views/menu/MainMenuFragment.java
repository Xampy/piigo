package com.xampy.piigo.views.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xampy.piigo.R;
import com.xampy.piigo.models.UserTypeEnum;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_USER_TYPE = "user_type";
    private static final String ARG_PARAM_IDENTIFIER = "identifier";

    // TODO: Rename and change types of parameters
    private UserTypeEnum mUserType;
    private String mIdentifier;



    //[START interfaces definition]
    public interface OnMainMenuFragmentInteractionListener {
        /**
         * User just want to update its positions
         */
        void onUpdatePositionClicked();

        /**
         * Change town district and identifier
         */
        void onUpdateStatusClicked();

        /**
         * Just want to make a search
         */
        void onSearchClicked();
    }
    //[END interfaces definition]


    //[START class variables]
    private OnMainMenuFragmentInteractionListener mListener;
    //[END class variables]



    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userTypeEnum Parameter 1.
     * @param identifier Parameter 2.
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance(UserTypeEnum userTypeEnum, String identifier) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();

        args.putSerializable(ARG_PARAM_USER_TYPE, userTypeEnum);
        args.putString(ARG_PARAM_IDENTIFIER, identifier);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserType = (UserTypeEnum) getArguments().getSerializable(ARG_PARAM_USER_TYPE);
            mIdentifier = getArguments().getString(ARG_PARAM_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_menu, container, false);

        //[START icon and identifier set ]
        ImageView mUserTypeImageView = (ImageView) root.findViewById(R.id.main_menu_user_type_image_view);
        TextView mUserTypeIdentifierTextView = (TextView) root.findViewById(R.id.main_menu_user_type_identifier_text_view);

        if(mUserType != null){
            if(mUserType == UserTypeEnum.CLIENT){
                mUserTypeIdentifierTextView.setText("-");

                Context context = getContext();

                assert context != null;

                mUserTypeImageView.setImageDrawable(
                        context.getResources().getDrawable(
                                context.getResources().getIdentifier(
                                        "ic_client_black",
                                        "drawable",
                                        context.getPackageName()
                                )
                        )
                );

                //Next is to hide the update position
                //and status position and show only the search button

                //( (LinearLayout) root.findViewById(R.id.main_menu_position_set_linear_layout)).setVisibility(View.GONE);
                //( (LinearLayout) root.findViewById(R.id.main_menu_state_set_linear_layout)).setVisibility(View.GONE);

                ( (LinearLayout) root.findViewById(R.id.main_menu_search_set_linear_layout)).setVisibility(View.VISIBLE);


            }else if(mUserType == UserTypeEnum.BIKE){

                if(mIdentifier != null){
                    mUserTypeIdentifierTextView.setText(mIdentifier);

                    Context context = getContext();

                    assert context != null;

                   mUserTypeImageView.setImageDrawable(
                            context.getResources().getDrawable(
                                    context.getResources().getIdentifier(
                                            "ic_bike_black",
                                            "drawable",
                                            context.getPackageName()
                                    )
                            )
                    );
                }
            }else if(mUserType == UserTypeEnum.CAR){
                mUserTypeIdentifierTextView.setText("-");

                if(mIdentifier != null){
                    mUserTypeIdentifierTextView.setText(mIdentifier);

                    Context context = getContext();

                    assert context != null;

                    mUserTypeImageView.setImageDrawable(
                            context.getResources().getDrawable(
                                    context.getResources().getIdentifier(
                                            "ic_car_black",
                                            "drawable",
                                            context.getPackageName()
                                    )
                            )
                    );
                }
            }
        }
        //[END icon and identifier set ]


        //[START handling click on menu button ]
        ImageButton mPositionUpdateButton = (ImageButton) root.findViewById(R.id.main_menu_position_update_imageButton);
        ImageButton mStatusUpdaterButton = (ImageButton) root.findViewById(R.id.main_menu_status_update_imageButton);
        ImageButton mSearchButton = (ImageButton) root.findViewById(R.id.main_menu_search_image_button);

        mPositionUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onUpdatePositionClicked();
            }
        });

        mStatusUpdaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onUpdateStatusClicked();
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onSearchClicked();
            }
        });

        //[END handling click on menu button ]



        return root;
    }


    public void setListener(OnMainMenuFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }
}
