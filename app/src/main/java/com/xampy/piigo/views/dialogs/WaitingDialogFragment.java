package com.xampy.piigo.views.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.piigo.R;

public class WaitingDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_waiting_progress, null);
        ProgressBar mProgressBar = (ProgressBar) mView.findViewById(R.id.make_search_progress_bar);
        TextView mProgressText = (TextView)  mView.findViewById(R.id.make_search_progress_text_view);


        builder.setView(mView);
        return builder.create();
    }
}
