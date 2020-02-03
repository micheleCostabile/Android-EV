package it.porting.android_is.gestioneUtente;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import it.porting.android_is.R;
import it.porting.android_is.firebaseArchive.FireBaseArchive;
import it.porting.android_is.firebaseArchive.bean.UtenteBean;
import it.porting.android_is.gestioneStudente.MainActivityStudente;

public class EditActivityUtente extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FireBaseArchive fireBaseArchive = new FireBaseArchive();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,153,0)));
        actionBar.setTitle("Modifica Profilo");


        final Button bSalva;
        bSalva=findViewById(R.id.button);
        if (user != null) {
            String email;
            email=user.getEmail();
            TextView emailTv;
            emailTv=findViewById(R.id.textView8);
            emailTv.setText(email);
            fireBaseArchive.getUserByKey(email, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        final UtenteBean utenteBean = task.getResult().toObject(UtenteBean.class);
                        String cognome,nome,password;
                        nome=utenteBean.getNome();
                        cognome=utenteBean.getCognome();
                        password=utenteBean.getPassword();
                        final EditText nomeEt, cognomeEt, psswEt;
                        nomeEt= findViewById(R.id.editText);
                        nomeEt.setText(nome);
                        cognomeEt = findViewById(R.id.editText2);
                        cognomeEt.setText(cognome);
                        psswEt=findViewById(R.id.editText3);
                        psswEt.setText(password);
                        bSalva.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                salva(psswEt,nomeEt,cognomeEt,utenteBean);

                            }
                        });

                    }
                    else{
                        Log.d("Errore nella query","ERRORE");
                    }
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "Non sei loggato!", Toast.LENGTH_SHORT).show();
        }


    }



    public void salva(EditText psswEt,EditText nomeEt, EditText cognomeEt, UtenteBean utente){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        String password= String.valueOf(psswEt.getText());
        String nome= String.valueOf(nomeEt.getText());
        String cognome = String.valueOf(cognomeEt.getText());
        String email=user.getEmail();
        user.updatePassword(password);
        Map<String, Object> user = new HashMap<>();
        user.put("nome", nome);
        user.put("cognome", cognome);
        user.put("email", email);
        user.put("password", password);
        user.put("sesso", utente.getSesso());
        user.put("ruolo", utente.getRuolo());
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
        Intent intent =  new Intent(getApplicationContext(), MainActivityStudente.class);
        Toast.makeText(getApplicationContext(),"Modifica completata!", Toast.LENGTH_LONG);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.option1:  modpage();
                return true;
            case R.id.option2:  guida();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void modpage(){
        Intent intent = new Intent(getApplicationContext(), ViewActivityUtente.class);
        startActivity(intent);
    }

    public void guida(){
        Intent intent = new Intent(getApplicationContext(), Guida.class);
        startActivity(intent);
    }
}

