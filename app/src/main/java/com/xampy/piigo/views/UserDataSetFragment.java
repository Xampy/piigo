package com.xampy.piigo.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xampy.piigo.R;
import com.xampy.piigo.controllers.userdataset.VehicueIdentifierController;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserDataSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDataSetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_CHOOSE_PROFILE_IMAGE = 2000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    //[START interfaces definition]
    public interface OnUserDataSetFragmentInteractionListener {
        void onGoButtonClicked(String identifier);
    }
    //[END interfaces definition]


    //[START class variables]
    private OnUserDataSetFragmentInteractionListener mListener;

    //Uri for the image choosed
    private Uri mUriImageSelected;
    private CircleImageView mUserPhoto;
    //[END class variables]

    public UserDataSetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserDataSetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDataSetFragment newInstance(String param1, String param2) {
        UserDataSetFragment fragment = new UserDataSetFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_user_data_set, container, false);

        Button NextButton = (Button) root.findViewById(R.id.user_data_set_piigo_button);
        final EditText mUserIdentifierEditText = (EditText) root.findViewById(R.id.user_data_set_piigo_identifier_edit_text);

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code =  mUserIdentifierEditText.getText().toString();

                boolean is_correct = VehicueIdentifierController.checkVehicleIdentifier(code);

                if(!is_correct){
                    Toast.makeText(getActivity(), "Votre plaque est incorrect", Toast.LENGTH_SHORT).show();
                }else {

                    if (mListener != null) {
                        mListener.onGoButtonClicked(
                                code
                        );
                    }
                }
            }
        });


        //[START choosing image ]
        mUserPhoto = (CircleImageView) root.findViewById(R.id.user_data_set_photo_image_view);
        ImageView mUserPhotoChooser = (ImageView) root.findViewById(R.id.user_data_set_photo_choose_image_view);
        mUserPhotoChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open the file chooser dialog ! Activity for result
                Intent image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(image, REQUEST_CODE_CHOOSE_PROFILE_IMAGE);
            }
        });
        //[END choosing image ]

        return root;
    }

    public void setUserDataSetListener(OnUserDataSetFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }




    //[START handling activity result]

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //If the request code is for choosing image for user
        if(requestCode == REQUEST_CODE_CHOOSE_PROFILE_IMAGE){

            mUriImageSelected = data.getData();
            mUserPhoto.setImageURI(mUriImageSelected);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //[END handling activity result]
}
