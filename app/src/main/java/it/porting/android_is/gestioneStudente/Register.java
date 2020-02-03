package it.porting.android_is.gestioneStudente;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.HashMap;
import java.util.Map;

import it.porting.android_is.R;
import it.porting.android_is.gestioneUtente.LoginActivity;

public class Register extends AppCompatActivity {

    private EditText etNome;
    private EditText etCognome;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etVPassword;
    private RadioGroup rdgroup_Sex;
    private RadioButton radioButtonSex;
    private ProgressBar progressBar;
    private Button btReg;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String sex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        etNome = findViewById(R.id.etNome);
        etCognome = findViewById(R.id.etCognome);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etVPassword = findViewById(R.id.etV_password);
        rdgroup_Sex = findViewById(R.id.radio_Sex);
        btReg = findViewById(R.id.btReg);
        progressBar = findViewById(R.id.progressBar);

        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    public EditText getEtNome() {
        return etNome;
    }

    public EditText getEtCognome() {
        return etCognome;
    }

    public EditText getEtEmail() {
        return etEmail;
    }

    public EditText getEtPassword() {
        return etPassword;
    }

    public EditText getEtVPassword() {
        return etVPassword;
    }

    public String getSex() {
        return sex;
    }

    public void register() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        final String nome = String.valueOf(etNome.getText());
        final String cognome = String.valueOf(etCognome.getText());
        final String email = String.valueOf(etEmail.getText());
        final String password = String.valueOf(etPassword.getText());
        final String vpassword = String.valueOf(etVPassword.getText());
        final Context context = this;
        final int cfu = 0;
        //La registrazione non va a buon fine in quanto uno dei campi è vuoto
        if (nome.equals("") || cognome.equals("") || email.equals("") || password.equals("") || vpassword.equals("")) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(this, "Non hai compilato tutti i campi", Toast.LENGTH_LONG).show();

        //La registrazione non va a buon fine in quanto il nome è in un formato non consentito
        }else if((nome.matches(".*\\W+.*")|| (nome.matches(".*\\d+.*"))) || nome.length() > 20 ){
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(context, "Il campo Nome non rispetta il formato desiderato", Toast.LENGTH_LONG).show();

        //La registrazione non va a buon fine in quanto il cognome è in un formato non consentito
        } else if((cognome.matches(".*\\W+.*")|| (nome.matches(".*\\d+.*"))) || cognome.length() > 20) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(context, "Il campo Nome non rispetta il formato desiderato", Toast.LENGTH_LONG).show();

        //La registrazione non va a buon fine in quanto il prefisso dell'email non raggiunge la lunghezza minima consentita
        }else if((email.substring(0, email.indexOf("@")).length() < 4)){
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(this, "Il campo email non rispetta la lunghezza minima consentita", Toast.LENGTH_LONG).show();

        //La registrazione non va a buon fine in quanto l'email non rispetta il formato consentito
        }else if (!email.endsWith("@studenti.unisa.it") || !(email.substring(0, email.indexOf("@")).contains("."))) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(this, "Il campo email non rispetta il formato consentito ", Toast.LENGTH_LONG).show();

         //La registrazione non va a buon fine in quanto non è stato inserito il sesso
        }else if (rdgroup_Sex.getCheckedRadioButtonId() == -1) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(this, "Non hai inserito il sesso", Toast.LENGTH_LONG).show();

        //La registrazione non va a buon fine in quanto la password non raggiunge la lunghezza minima consentita
        } else if (password.length() < 8) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(this, "La password non rispetta la lunghezza minima consentita", Toast.LENGTH_LONG).show();

        //La registrazione non va a buon fine in quanto la password non rispetta il formato consentito
        }else if(password.matches(".*\\W+.*")){
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(this, "La password non rispetta il formato consentito", Toast.LENGTH_LONG).show();

        //La registrazione non va a buon fine in quanto le due password non coincidono
        }else if (!password.equals(vpassword)) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(this, "I campi password non corrispondono", Toast.LENGTH_LONG).show();

        } else {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> user = new HashMap<>();
                                user.put("nome", nome);
                                user.put("cognome", cognome);
                                user.put("email", email);
                                user.put("password", password);
                                user.put("sesso", sex);
                                user.put("ruolo", "studente");
                                user.put("cfu", cfu);

                                db.collection("utenti").document(email)
                                        .set(user, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("DEBUG", "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("DEBUG", "Error writing document", e);
                                            }
                                        });
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Toast.makeText(context, "Registrazione avvenuta con successo !", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Toast.makeText(context, "Email già esistente", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
    }

    public void clickSex(View v){
        int radioSexM = rdgroup_Sex.getCheckedRadioButtonId();
        radioButtonSex = findViewById(radioSexM);
        sex = radioButtonSex.getText().toString();

    }


}



