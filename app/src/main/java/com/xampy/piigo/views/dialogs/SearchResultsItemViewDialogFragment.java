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

import java.util.Objects;

public class SearchResultsItemViewDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()),
                R.style.CustomDialogChangePhoneNumber);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_search_results_one_item, null);


        builder.setView(mView);
        return builder.create();
    }
}
