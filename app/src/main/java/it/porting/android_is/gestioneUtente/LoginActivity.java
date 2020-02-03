package it.porting.android_is.gestioneUtente;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import it.porting.android_is.R;
import it.porting.android_is.adapter.SpinnerAdapter;
import it.porting.android_is.gestioneAdmin.MainActivityAdmin;
import it.porting.android_is.gestioneSegreteria.MainActivitySegreteria;
import it.porting.android_is.gestioneStudente.MainActivityStudente;
import it.porting.android_is.gestioneStudente.Register;
import it.porting.android_is.utility.LazyInitializedSingleton;


public class LoginActivity extends AppCompatActivity {

    private Button bLogin;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvRegisterNow;
    private ProgressBar progressBar;
    private String email;
    private String password;
    private Toast toast;
    private Boolean exit = false;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private Context context;
    private Activity activity;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    private SharedPreferences.Editor editor;
    private static SharedPreferences preferences;
    private Spinner spinner;
    private boolean isSpinnerTouched = false;

    String[] countryNames={"IT","ES","EN"};
    int flags[] = {R.drawable.italy, R.drawable.spain, R.drawable.uk};


    // START FIRESTORE DECLARATION
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    //END FIRESTORE DECLARATION

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        preferences = this.getSharedPreferences(
                "myPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        mAuth = FirebaseAuth.getInstance();
        bLogin = findViewById(R.id.bLogin);
        etEmail = findViewById(R.id.etEmail);
        etEmail.setText("");
        etPassword = findViewById(R.id.etPassword);
        etPassword.setText("");
        tvRegisterNow = findViewById(R.id.register_now);
        progressBar = findViewById(R.id.progressBar);
        spinner = findViewById(R.id.spinner_lingua);




        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerTouched) return;
                    changeLanguage(parent, view, position, id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //doNothing
            }
        });
        SpinnerAdapter customAdapter=new SpinnerAdapter(getApplicationContext(),flags,countryNames);
        spinner.setAdapter(customAdapter);

        context = this;




        //Controlliamo se è stata associata un'impronta digitale all'account
        //In caso positivo chiamiamo il metodo startFingerAuth() che inizierà l'autenticazione con impronta
        if (preferences.getInt("fingerSaved", 0) == 1) {
            startFingerAuth();
        }

                tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    public void login() {
        progressBar.setVisibility(View.VISIBLE);

        //Se l'utente non ha compilato tutti i campi
        if (String.valueOf(etEmail.getText()).equals("") && String.valueOf(etPassword.getText()).equals("")) {

            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            toast = Toast.makeText(getApplicationContext(), "Dati non inseriti", Toast.LENGTH_LONG);
            toast.show();

        }

        //Se l'utente non ha compilato il campo dell'email
        else if (String.valueOf(etEmail.getText()).equals("")) {

            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            toast = Toast.makeText(getApplicationContext(), "Email non inserita", Toast.LENGTH_LONG);
            toast.show();

        }

        //Se l'utente non ha compilato il campo della password
        else if (String.valueOf(etPassword.getText()).equals("")) {

            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            toast = Toast.makeText(getApplicationContext(), "Password non inserita", Toast.LENGTH_LONG);
            toast.show();

        } else {
            //Se l'utente ha compilato tutti i campi

            email = String.valueOf(etEmail.getText());
            password = String.valueOf(etPassword.getText());

            //modulo autenticazione firebase
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                     /* Se task.isSuccessful() ritorna true significa che l'utente è riuscito a loggare con successo
                        A questo punto quindi prendiamo dal CloudFirestore dalla collezione 'utenti' il documento che
                        ha come id l'email dell'utente appena loggato, così da poter avere più informazioni riguardanti l'utente
                        tra le quali il ruolo che ha all'interno del sistema
                      */
                        DocumentReference docRef = db.collection("utenti").document(email);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    // Facciamo il retrieve del documento e lo salviamo nel singleton, N.B: sarà salvato sottoforma di HASHMAP
                                    LazyInitializedSingleton.getInstance().setUser(document.getData());
                                    progressBar.setVisibility(View.GONE);
                                    redirect();


                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    toast = Toast.makeText(getApplicationContext(), "I dati inseriti non sono stati caricati in sessione", Toast.LENGTH_LONG);
                                    toast.show();

                                }
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        toast = Toast.makeText(getApplicationContext(), "I dati inseriti non sono corretti", Toast.LENGTH_LONG);
                        toast.show();
                    }


                }

            });
        }
    }


    public void register() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }


    public void redirect() {
        // INZIO A VERIFICARE QUALE ACTIVITY LANCIARE DOPO IL LOGIN

                            /*
                            PRENDO IL DOCUMENTO DAL SINGLETON INSTANZIATO AL MOMENTO DEL LOGIN

                            CASO 1 : lazyInizializedSingleton.getInstance().getUser().get("ruolo") restituisce un utente con ruolo "studente"
                            CASO 2 : lazyInizializedSingleton.getInstance().getUser().get("ruolo") restituisce un utente con ruolo "segretario"
                            CASO 3 : lazyInizializedSingleton.getInstance().getUser().get("ruolo") restituisce un utente con ruolo "admin"

                            */


        // START CASO 1
        if (String.valueOf(LazyInitializedSingleton.getInstance().getUser().get("ruolo")).equals("studente")) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            Intent intent = new Intent(getApplicationContext(), MainActivityStudente.class);
            startActivity(intent);
            this.finish();

        }
        // END CASO 1

        // START CASO 2
        else if (String.valueOf(LazyInitializedSingleton.getInstance().getUser().get("ruolo")).equals("segretario")) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            Intent intent = new Intent(getApplicationContext(), MainActivitySegreteria.class);

            startActivity(intent);
            this.finish();
        }
        //END CASO 2

        // START CASO 3
        else if (String.valueOf(LazyInitializedSingleton.getInstance().getUser().get("ruolo")).equals("admin")) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            Intent intent = new Intent(getApplicationContext(), MainActivityAdmin.class);
            startActivity(intent);
            this.finish();
        }
        //END CASO 3
    }


    @Override
    public void onBackPressed() {

        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        moveTaskToBack(false);

    }


    //crash
    public void fingerprintLogin(Context activity) {
        EditText etEmail = ((Activity) activity).findViewById(R.id.etEmail);
        EditText etPassword = ((Activity) activity).findViewById(R.id.etPassword);
        etEmail.setText(preferences.getString("email", ""));
        etPassword.setText(preferences.getString("password", ""));
    }

    public void startFingerAuth(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {

                Toast.makeText(getApplicationContext(), "Non è stato rilevato uno scanner di impronte", Toast.LENGTH_LONG);

            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "Permesso non autorizzato per l'uso dello scanner di impronte", Toast.LENGTH_LONG);


            } else if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(getApplicationContext(), "Aggiungi un blocco al tuo telefono", Toast.LENGTH_LONG);


            } else if (!fingerprintManager.hasEnrolledFingerprints()) {

                Toast.makeText(getApplicationContext(), "È necessario aggiungere almeno 1 impronta digitale per utilizzare questa funzione", Toast.LENGTH_LONG);


            } else {

                Toast.makeText(getApplicationContext(), "\n" + "Posiziona il dito sullo scanner per accedere all'app", Toast.LENGTH_LONG);


                generateKey();


            }

            if (cipherInit()) {

                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                Fingerprint fingerprintHandler = new Fingerprint(this);
                fingerprintHandler.startAuth(fingerprintManager, cryptoObject, context);


            }

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();


        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }


    public void changeLanguage(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), countryNames[position], Toast.LENGTH_LONG).show();

        setAppLocale(countryNames[position].toLowerCase());
        Intent refresh = new Intent(this, LoginActivity.class);
        spinner.setSelection(position);
        finish();
        recreate();
        startActivity(refresh);



    }

    public void setAppLocale(String language){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        conf.setLocale(new Locale(language.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }



}
