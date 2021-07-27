package com.xampy.piigo.views.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.piigo.R;

import org.w3c.dom.Text;

import java.util.Objects;

public abstract class AbstractTextInputDialogFragment extends DialogFragment {


    private final String mTitle;

    public AbstractTextInputDialogFragment(String title) {
        mTitle = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()),
                R.style.CustomDialogChangePhoneNumber);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_text_input, null);


        //Get views elements
        TextView mDialogFragmentTitleTextView = (TextView) mView.findViewById(R.id.dialog_text_input_title);
        mDialogFragmentTitleTextView.setText(mTitle);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Pass
                //Set listener later
            }
        });



        builder.setView(mView);
        return builder.create();
    }
}
