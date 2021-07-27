package com.xampy.piigo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xampy.piigo.api.server.VolleyRequestQueueSingleton;
import com.xampy.piigo.models.UserTypeEnum;
import com.xampy.piigo.models.timer.SetIntervalTimer;
import com.xampy.piigo.models.userentity.AbstractPiigoUser;
import com.xampy.piigo.models.userentity.PiigoUserClient;
import com.xampy.piigo.views.menu.MainMenuFragment;
import com.xampy.piigo.views.search.SearchDataEntryFragment;
import com.xampy.piigo.views.search.SearchResultsFragment;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.xampy.piigo.PostActivity.FROM_POST_ACTIVITY_USER_DATA_TYPE_PARAM;
import static com.xampy.piigo.PostActivity.FROM_POST_ACTIVITY_USER_IDENTIFIER_PARAM;


public class PiigoActivity extends AppCompatActivity   implements
        EasyPermissions.PermissionCallbacks,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    //[START request code ]
    private static final int REQUEST_CODE_USE_LOCATION_COARSE = 123;
    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 100;
    //[END request code ]

    //Requests url
    private static final String UPDATE_POSITION_URL = "http://192.168.137.199:8000/update_position/";

    //[START class variables]
    private FrameLayout mMainMenuFrameLayout;

    private boolean mDoNotShowMenu;
    private boolean mSearchFragmentShown;


    private UserTypeEnum mUserType;
    private String mIdentifier;


    //Position update interval timer
    private SetIntervalTimer mAutoPositionUpdaterSetIntervalTimer;


    //For handling server requests
    private VolleyRequestQueueSingleton mVolleyRequestQueueSingleton;


    //Location manager
    private LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;

    public PiigoUserClient mPiigoUserClient;

    //[START permissions handling
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    //[END permissions handling


    //[END class variables]

    //[START interfaces definition]
    public interface OnPiigoGestureListenerCallbacks {
        void onSwipeLeft();
        void onSwipeRight();
    }

    //[END interfaces definition]

    //[START Gesture handling for the main menu ]
    public class PiigoGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        private OnPiigoGestureListenerCallbacks mGestureListener;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) {
                    if(mGestureListener != null)
                        mGestureListener.onSwipeRight();
                    //Toast.makeText(PiigoActivity.this, "Right swipe", Toast.LENGTH_SHORT).show();
                }else {
                    if(mGestureListener != null)
                        mGestureListener.onSwipeLeft();

                    //Toast.makeText(PiigoActivity.this, "Left swipe", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        }

        public void setGestureListenerCallbacks (OnPiigoGestureListenerCallbacks mGestureListener) {
            this.mGestureListener = mGestureListener;
        }
    }
    //[END Gesture handling for the main menu ]


    //[START class variables]
    private GestureDetectorCompat mDetector;



    private GoogleMap mMap;


    private short mMenuOpened = 0;
    //[END class variables]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piigo);

        //Hide the status bar
        getSupportActionBar().hide();

        //[START Retrieve argument passed ]
        Intent fromPostActivity = getIntent();

        mUserType = (UserTypeEnum) fromPostActivity.getSerializableExtra(FROM_POST_ACTIVITY_USER_DATA_TYPE_PARAM);
        mIdentifier = fromPostActivity.getStringExtra(FROM_POST_ACTIVITY_USER_IDENTIFIER_PARAM);

        //[START Retrieve argument passed ]


        //[START maps initialization ]
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //The volley request handler
        mVolleyRequestQueueSingleton = VolleyRequestQueueSingleton.getInstance(getApplicationContext());

        //[END maps initialization ]

        //[START required variable initialization ]
        mDoNotShowMenu = false;
        mSearchFragmentShown = false;
        mPiigoUserClient = new PiigoUserClient(this); //The user maps client

        //Position updating
        mAutoPositionUpdaterSetIntervalTimer = new SetIntervalTimer(
                30000, //30s
                new SetIntervalTimer.OnSetIntervalTimerListener() {
                    @Override
                    public void onTimeElapsed() {
                        //Make a controller in order to avoid conflict
                        //between the location updater and the access
                        //in this scope

                        Log.d("POSITION UPDATER",
                                "lal :" + mPiigoUserClient.getAltitude() +
                                        " - long: " + mPiigoUserClient.getLongitude());
                        //We send position update request

                        StringRequest update_position = new StringRequest(
                                Request.Method.GET,
                                UPDATE_POSITION_URL +
                                        "?phone=96735958" +
                                        "&lal=" + mPiigoUserClient.getAltitude() +
                                        "&long=" + mPiigoUserClient.getLongitude(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        //Nothing to do²

                                    }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                        //Add position to request
                        mVolleyRequestQueueSingleton.getRequestQueue().add(update_position);
                    }
                });
        //[END required variable initialization ]

        //[START gesture detector ]
        PiigoGestureListener mGestureListener = new PiigoGestureListener();
        mGestureListener.setGestureListenerCallbacks(new OnPiigoGestureListenerCallbacks() {
            @Override
            public void onSwipeLeft() {
                if(mMenuOpened == 1 ){

                    PiigoActivity.this.onBackPressed();

                    mMenuOpened = 0;
                    mDoNotShowMenu = false;
                    Toast.makeText(PiigoActivity.this, "Opened " + mMenuOpened, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwipeRight() {
                if(mMenuOpened == 0 && !mDoNotShowMenu) {
                    openMainMenuFragment();
                    Toast.makeText(PiigoActivity.this, "Opened " + mMenuOpened, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDetector = new GestureDetectorCompat(this,  mGestureListener);
        //[END gesture detector ]

        mMainMenuFrameLayout = (FrameLayout) findViewById(R.id.piigo_activity_main_menu_fragment_frame);


        //[START Menu Click handler ]
        ImageButton mMenuButton = (ImageButton) findViewById(R.id.piigo_activity_menu_image_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //We open the menu if no other fragment is diplayed
               if(mMenuOpened == 0 && !mDoNotShowMenu) {
                    openMainMenuFragment();
                    Toast.makeText(PiigoActivity.this, "Opened " + mMenuOpened, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //[END Menu Click handler ]


        //[START Menu Click handler ]
        ImageButton mSearchButton = (ImageButton) findViewById(R.id.piigo_activity_search_image_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //No menu frame opened ?
                if(mMenuOpened == 0)
                    openSearchFragment(false); //Not from the main menu search button
            }
        });
        //[END Menu Click handler ]
    }

    //[START getters functions ]
    public VolleyRequestQueueSingleton getmVolleyRequestQueueSingleton(){
        return mVolleyRequestQueueSingleton;
    }
    //[START getters functions ]



    //[START Functions for open fragments ]
    public void openMainMenuFragment(){
        MainMenuFragment fragment = MainMenuFragment.newInstance(
                mUserType,
                mIdentifier
        );

        //[START main menu listener
        fragment.setListener(new MainMenuFragment.OnMainMenuFragmentInteractionListener() {

            @Override
            public void onUpdatePositionClicked() {

                //Reopen the post activity for choosing
                //user type and so on

                Intent update_activity = new Intent(PiigoActivity.this, PostActivity.class);

                //Passing parameters for notify the activity
                //that we will only update
                startActivity(update_activity);


                //finish();
            }

            @Override
            public void onUpdateStatusClicked() {

                //Back press for closing the menu
                onBackPressed();

                //Open the settings activity
                Intent settings_activity = new Intent(PiigoActivity.this, SettingsActivity.class);

                /*map_activity.putExtra(FROM_POST_ACTIVITY_USER_DATA_TYPE_PARAM, mUserType);
                map_activity.putExtra(FROM_POST_ACTIVITY_USER_IDENTIFIER_PARAM, mIdentifier);*/

                //startActivity(settings_activity);

                startActivityForResult(settings_activity, SETTINGS_ACTIVITY_REQUEST_CODE);

            }

            @Override
            public void onSearchClicked() {
                openSearchFragment(true);
            }
        });
        //[END main menu listener




        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(
                R.anim.main_menu_enter_from_left, R.anim.main_menu_exit_to_left,
                R.anim.main_menu_enter_from_left, R.anim.main_menu_exit_to_left
                );

        ft.addToBackStack(null);
        ft.replace(R.id.piigo_activity_main_menu_fragment_frame, fragment, "Menu").commit();

        //Then the menu is opened
        mMenuOpened = 1;
    }

    public void openSearchFragment(boolean fromMenu){

        if(!fromMenu) {
            //Restrict the show of the menu
            mDoNotShowMenu = true;
            mSearchFragmentShown = true;

            SearchDataEntryFragment fragment = SearchDataEntryFragment.newInstance("", "");
            fragment.setListener(new SearchDataEntryFragment.OnSearchDataEntryFragmentInteractionListener() {
                @Override
                public void onSearchButtonClicked(UserTypeEnum searchUserType) {
                    openSearchResultsFragment(searchUserType);
                }
            });

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.setCustomAnimations(
                    R.anim.search_fragment_fade_in, R.anim.search_fragment_fade_out,
                    R.anim.search_fragment_fade_in, R.anim.search_fragment_fade_out
            );

            ft.addToBackStack(null);
            ft.replace(R.id.piigo_activity_main_all_fragment_frame, fragment, "Search Data").commit();
        }else {
            //We are from menu
            //we close the menu and open the search fragment

            //We do a back press for closing the main menu
            onBackPressed();

            //Now open th search
            if(mMenuOpened == 0)
                openSearchFragment(false);
        }
    }

    public void openSearchResultsFragment(UserTypeEnum searchUserType){

        //Restrict the show of the menu
        mDoNotShowMenu = true;
        mSearchFragmentShown = true;

        SearchResultsFragment fragment = SearchResultsFragment.newInstance(searchUserType);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(
                R.anim.search_fragment_fade_in, R.anim.search_fragment_fade_out,
                R.anim.search_fragment_fade_in, R.anim.search_fragment_fade_out
        );

        ft.addToBackStack(null);
        ft.replace(R.id.piigo_activity_main_all_fragment_frame, fragment, "Search Data Result").commit();
    }
    //[END Functions for open fragments ]




    //[START handling gesture event ]

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //[END handling gesture event ]

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){

            }
        }
    }


    //[START google maps callbacks ]


    protected synchronized  void buildGoogleApiClient(){

        this.googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        this.googleApiClient.connect();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {

           buildGoogleApiClient();
            mMap.setMyLocationEnabled(false);

        }else {
            EasyPermissions.requestPermissions(this, "Nous avons besoin de votre permission",
                    REQUEST_CODE_USE_LOCATION_COARSE, perms);
        }

    }


    //Connection and position handling
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private com.google.android.gms.location.LocationListener mLocationListener = new com.google.android.gms.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lastLocation = location;


            //Check if the user has a already a marker on the map
            if(mPiigoUserClient.getMarker() != null){
                mPiigoUserClient.getMarker().remove();
            }
            Log.d("Location info", String.valueOf(location.getLatitude()) +" " + String.valueOf(location.getLongitude()));
            mPiigoUserClient.setAltitude(location.getLatitude());
            mPiigoUserClient.setLongitude(location.getLongitude());

            mPiigoUserClient.drawMarker(mMap);

            if (googleApiClient != null){
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        googleApiClient, mLocationListener);
            }
        }


    };



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(1100);
        this.locationRequest.setFastestInterval(1100);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    this.googleApiClient,
                    this.locationRequest,
                    mLocationListener);
        } else {
            EasyPermissions.requestPermissions(this, "Nous avons besoin de votre permission",
                    123, perms);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //[END google maps callbacks ]




    //[START getters]
    public double getPiigoUserLatitude(){
        return mPiigoUserClient.getAltitude();
    }

    public double getPiigoUserLongitude(){
        return mPiigoUserClient.getLongitude();
    }
    //[END getters]


    @Override
    public void onBackPressed() {

        if(mMenuOpened == 1){
            mMenuOpened = 0;
        }

        if(mSearchFragmentShown){
            mDoNotShowMenu = false;
            mMenuOpened = 0;
            mSearchFragmentShown = false;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        //Release memory here
        mAutoPositionUpdaterSetIntervalTimer.stop();
        mAutoPositionUpdaterSetIntervalTimer = null;

        super.onDestroy();
    }
}
