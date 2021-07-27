package com.xampy.piigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.xampy.piigo.api.server.VolleyRequestQueueSingleton;
import com.xampy.piigo.controllers.registerController.RegisterController;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    //[START class variables ]
    private static final String REGISTER_URL = "http://192.168.137.199:8000/register";
    private static final String REGISTER_PARAMETER_PHONE_STRING = "phone";
    private static final String REGISTER_RESULT_STRING = "status";

    //Volley for handling register request on server
    private VolleyRequestQueueSingleton mVolleyRequestQueueSingleton;
    private LinearLayout mRegisterUserEntryLinearLayout;
    private ProgressBar mRegisterUserProgressBar;
    private Button mFinishRegisterButton;
    private EditText mRegisterPhoneNumberEditText;

    //[END class variables ]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hide the status bar
        getSupportActionBar().hide();

        mVolleyRequestQueueSingleton = VolleyRequestQueueSingleton.getInstance(getApplicationContext());

        mRegisterUserEntryLinearLayout = (LinearLayout) findViewById(R.id.register_user_entry);
        mRegisterUserProgressBar = (ProgressBar) findViewById(R.id.register_user_progress_bar);


        mFinishRegisterButton = (Button) findViewById(R.id.register_user_create_account_button);
        mRegisterPhoneNumberEditText = (EditText) findViewById(R.id.register_user_phone_edit_text);
        mFinishRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Call the controller to check the phone number
                boolean number_correct = RegisterController.checkPhoneNumber(
                        mRegisterPhoneNumberEditText.getText().toString()
                );
                
                if(!number_correct){
                    Toast.makeText(RegisterActivity.this, "Vérifier votre numéro", Toast.LENGTH_SHORT).show();
                }else {

                    mRegisterUserEntryLinearLayout.setVisibility(View.GONE);
                    mRegisterUserProgressBar.setVisibility(View.VISIBLE);

                    mFinishRegisterButton.setVisibility(View.GONE);

                    //[START registration request ]

                    //Create a json request on register link
                    JSONObject register_params = new JSONObject();
                    try {
                        register_params.put(REGISTER_PARAMETER_PHONE_STRING, "96735958");
                        Log.d("REGISTRATION PARAMS", register_params.toString());
                    } catch (JSONException e) {
                        //notify errors
                        e.printStackTrace();
                    }


                    StringRequest register = new StringRequest(
                            Request.Method.GET,
                            REGISTER_URL + "?phone=96735958",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Responses ", response);

                                    try {
                                        JSONObject json = new JSONObject(response);
                                        String status = json.getString(REGISTER_RESULT_STRING);
                                        Log.d("REGISTRATION RESULT", status + " " + response.toString());

                                        if (status.equals("success")) {

                                            //[START saving required data to database ]
                                            //[START saving required data to database ]

                                            Toast.makeText(RegisterActivity.this, "Création de compte réussi", Toast.LENGTH_SHORT).show();

                                            Intent home_activity = new Intent(RegisterActivity.this, PostActivity.class);
                                            startActivity(home_activity);
                                            finish();
                                        } else if (status.equals("failed")) {
                                            Toast.makeText(RegisterActivity.this, "Erreur survenu...", Toast.LENGTH_SHORT).show();

                                            //Reestablish the views
                                            mRegisterUserProgressBar.setVisibility(View.GONE);
                                            mRegisterUserEntryLinearLayout.setVisibility(View.VISIBLE);
                                            mFinishRegisterButton.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Erreur...", Toast.LENGTH_SHORT).show();
                            Log.d("ERROR REGISTER ", error.toString());


                            //Reestablish the views
                            mRegisterUserProgressBar.setVisibility(View.GONE);
                            mRegisterUserEntryLinearLayout.setVisibility(View.VISIBLE);
                            mFinishRegisterButton.setVisibility(View.VISIBLE);

                        }
                    });

                    //Add the request to the volley request queue
                    //mVolleyRequestQueueSingleton.getRequestQueue().add(registerRequest);
                    mVolleyRequestQueueSingleton.getRequestQueue().add(register);
                    //[END registration request ]
                }
            }
        });



    }
}
