package it.porting.android_is.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class MyDialogFragment extends DialogFragment {

    static int saved = 0;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences preferences;
    @Nullable
    private Bundle savedInstanceState;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        preferences = getActivity().getSharedPreferences(
                "myPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        this.savedInstanceState = savedInstanceState;
        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Fingerprint Authentication");
        builder.setMessage("Associare l'impronta digitale all'account?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               editor.putString("email", LazyInitializedSingleton.getInstance().getUser().get("email").toString());
               editor.putString("password", LazyInitializedSingleton.getInstance().getUser().get("password").toString());
               editor.putInt("fingerSaved", 1);
               editor.commit();
               Toast.makeText(getActivity(), "Impronta Associata all'account", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO nothing

            }
        });

        return builder.create();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
