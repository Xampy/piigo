package com.xampy.piigo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xampy.piigo.models.timer.SetIntervalTimer;
import com.xampy.piigo.views.dialogs.ChangePhoneNumberDialogFragment;

public class SettingsActivity extends AppCompatActivity {

    public static final String SETTINGS_AUTO_POSITION_UPDATE_STRING = "auto_position_update";

    private ConstraintLayout mAutoPositionConstraintLayout;
    private ConstraintLayout mChangePhoneNumberConstraintLayout;
    private TextView mAutoPositionValueTextView;
    private TextView mChangePhoneNmberCurrentValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);

        getSupportActionBar().hide();

        //Get view elements
        mAutoPositionConstraintLayout = (ConstraintLayout) findViewById(R.id.settings_auto_position_constraint_layout);
        mChangePhoneNumberConstraintLayout = (ConstraintLayout) findViewById(R.id.settings_current_phone_constraint_layout);
        mAutoPositionValueTextView = (TextView) findViewById(R.id.settings_auto_position_value_text_view);
        mChangePhoneNmberCurrentValueTextView = (TextView) findViewById(R.id.settings_current_phone_number_value_text_view);


        //[START view initialization]
        //Get the data provided by the activity
        //mAutoPositionValueTextView.setText(R.string.on_text);
        //[END view initialization]

        //[START Handling back button click ]
        ( findViewById(R.id.settings_go_back) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We finish the activity
                finish();
            }
        });
        //[END Handling back button click ]

        //[START Handling auto position click]
        mAutoPositionConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the text
                String value = mAutoPositionValueTextView.getText().toString();

                if(value.equals("On")){
                    mAutoPositionValueTextView.setText(R.string.off_text);
                }else if(value.equals("Off")){
                    mAutoPositionValueTextView.setText(R.string.on_text);
                }
            }
        });
        //[END Handling auto position click]

        //[START Handling change phone number click]
        mChangePhoneNumberConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open the text input dialog
                ChangePhoneNumberDialogFragment fragment = new ChangePhoneNumberDialogFragment();
                //Handle listener here
                fragment.show(getSupportFragmentManager(), "CHANGE NUMBER");
            }
        });
        //[END Handling auto position click]




        //i = 0;






        //[START testing the set interval class
         /*mtimer = new SetIntervalTimer(1000, new SetIntervalTimer.OnSetIntervalTimerListener() {
            @Override
            public void onTimeElapsed() {
                //Toast.makeText(SettingsActivity.this, i, Toast.LENGTH_SHORT).show();
                Log.d("SET INTERVAL", String.valueOf(i));
                i += 1;
            }
        });*/
        //[END testing the set interval class ]
    }
}
