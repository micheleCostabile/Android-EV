package it.porting.android_is.gestioneUtente;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import it.porting.android_is.gestioneStudente.MainActivityStudente;



@TargetApi(Build.VERSION_CODES.M)
public class Fingerprint extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private Context activity;

    public Fingerprint(Context context) {

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject, Context activity) {

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        this.activity = activity;

    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {


        /*Intent intent = new Intent(context.getApplicationContext(), MainActivityStudente.class);
        context.startActivity(intent)*/;
        LoginActivity log = new LoginActivity();
        log.fingerprintLogin(activity);


    }

    public void onAuthenticationFailed() {

        this.update("Auth Failed. ", false);

    }

    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);

    }

    private void update(String s, boolean b) {

        if (b == false) {

            Toast toast = Toast.makeText(context, "Accesso non autorizzato", Toast.LENGTH_LONG);
            toast.show();
        }

    }
}




