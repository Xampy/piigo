package com.xampy.piigo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xampy.piigo.api.server.VolleyRequestQueueSingleton;
import com.xampy.piigo.models.UserTypeEnum;
import com.xampy.piigo.views.UserDataSetFragment;
import com.xampy.piigo.views.UserLocationSetFragment;
import com.xampy.piigo.views.UserTypeSelectionFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class PostActivity extends AppCompatActivity {

    private static final String POST_ACTIVITY_UPDATE_USER_TYPE_URL = "http://192.168.137.199:8000/update_user_type";
    private static final String POST_ACTIVITY_UPDATE_USER_IMAGE_URL = "http://192.168.137.199:8000/update_photo";
    private static final String POST_ACTIVITY_UPDATE_USER_POSITIONS_URL = "http://192.168.137.199:8000/update_positions";

    private static final String SERVER_REQUEST_RESULT_STRING = "status";

    private static final String POST_ACTIVITY_RESULT_STRING = "status";

    public static final String FROM_POST_ACTIVITY_USER_DATA_TYPE_PARAM = "user_type";
    public static final String FROM_POST_ACTIVITY_USER_IDENTIFIER_PARAM = "identifier";

    private FrameLayout mAllFragmentFrameLayout;

    private UserTypeEnum mUserType;
    private String mIdentifier;
    private String mTownString;
    private String mDistrictString;


    private ProgressBar mWaitingProgressBar;
    private VolleyRequestQueueSingleton mVolleyRequestQueueSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Hide the status bar
        getSupportActionBar().hide();


        //request handler
        mVolleyRequestQueueSingleton = VolleyRequestQueueSingleton.getInstance(getApplicationContext());




        //The progress bar
        mWaitingProgressBar = (ProgressBar) findViewById(R.id.post_activity_user_progress_bar);



        //Get the main fragment frame layout container
        mAllFragmentFrameLayout = (FrameLayout) findViewById(R.id.post_activity_main_all_fragment_frame);

        //Open the fragment
        openUserSelectionFragment();
    }




    //[START Functions for open fragments ]
    public void openUserSelectionFragment(){
        UserTypeSelectionFragment fragment = UserTypeSelectionFragment.newInstance("", "");
        fragment.setUserTypeSelectionListener(new UserTypeSelectionFragment.
                OnUserTypeSelectionFragmentInteractionListener() {
                    @Override
                    public void onNexTButtonClicked(UserTypeEnum userTypeEnum) {

                        //Set the userType
                        mUserType = userTypeEnum;

                        //[START updating the user type ]
                        StringRequest update_user_type = new StringRequest(
                                Request.Method.GET,
                                POST_ACTIVITY_UPDATE_USER_TYPE_URL +
                                        "?user_type=" + mUserType.toString().toLowerCase() +
                                        "&phone=96735958",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //handle the response here

                                        //Convert the response to JSON Object
                                        try {
                                            JSONObject json = new JSONObject(response);

                                            if(json.getString(SERVER_REQUEST_RESULT_STRING).equals("success")){
                                                //Open the location set fragment
                                                openUserLocationSetFragment();
                                            }else {
                                                //We've got an error
                                                Toast.makeText(PostActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(PostActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(PostActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        mVolleyRequestQueueSingleton.getRequestQueue().add(update_user_type);
                        //[END updating the user type ]

                    }
                });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        ft.add(R.id.post_activity_main_all_fragment_frame, fragment, "SELECT USER TPE").commit();
    }

    public void openUserLocationSetFragment(){
        UserLocationSetFragment fragment = UserLocationSetFragment.newInstance("", "");
        fragment.setUserLocationSetListener(new UserLocationSetFragment.OnUserLocationSetFragmentInteractionListener() {

            @Override
            public void onNexTButtonClicked(String town, String district) {

                //Set parameters
                mTownString = town;
                mDistrictString = district;

                //User is a client then we pass directly to the
                //map activity
                if(mUserType != UserTypeEnum.CLIENT) {
                    openUserDataSetFragment();
                }else{
                    mIdentifier = "-";
                    openNextActivity();
                }
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        ft.replace(R.id.post_activity_main_all_fragment_frame, fragment, "SET USER LOCATION").commit();
    }

    public void openUserDataSetFragment(){
        UserDataSetFragment fragment = UserDataSetFragment.newInstance("", "");
        fragment.setUserDataSetListener(new UserDataSetFragment.OnUserDataSetFragmentInteractionListener() {
            @Override
            public void onGoButtonClicked(String identifier) {

                mIdentifier = identifier;

                //[START updating the user type ]
                StringRequest update_user_identifier = new StringRequest(
                        Request.Method.GET,
                        POST_ACTIVITY_UPDATE_USER_IMAGE_URL +
                                "?identifier=" + mIdentifier +
                                "&phone=96735958",
                        new Response.Listener<String>() {
                            @Override 
                            public void onResponse(String response) {
                                //handle the response here

                                //Convert the response to JSON Object
                                try {
                                    JSONObject json = new JSONObject(response);

                                    if(json.getString(SERVER_REQUEST_RESULT_STRING).equals("success")){

                                        Toast.makeText(PostActivity.this, "Identifiant mis à jour", Toast.LENGTH_SHORT).show();
                                        openNextActivity();


                                    }else {
                                        //We've got an error
                                        Toast.makeText(PostActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(PostActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(PostActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                mVolleyRequestQueueSingleton.getRequestQueue().add(update_user_identifier);
                //[END updating the user type ]


            }

        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        ft.replace(R.id.post_activity_main_all_fragment_frame, fragment, "SET USER Data").commit();
    }
    //[END Functions for open fragments ]


    //[START required functions ]
    private  void openNextActivity(){
        Intent map_activity = new Intent(PostActivity.this, PiigoActivity.class);

        map_activity.putExtra(FROM_POST_ACTIVITY_USER_DATA_TYPE_PARAM, mUserType);
        map_activity.putExtra(FROM_POST_ACTIVITY_USER_IDENTIFIER_PARAM, mIdentifier);

        startActivity(map_activity);
        finish();
    }
    //[END required functions ]


    @Override
    public void onBackPressed() {



        super.onBackPressed();

        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
            this.finish();
    }
}
