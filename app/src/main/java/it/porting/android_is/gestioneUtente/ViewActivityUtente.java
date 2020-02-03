package it.porting.android_is.gestioneUtente;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import it.porting.android_is.R;
import it.porting.android_is.firebaseArchive.FireBaseArchive;
import it.porting.android_is.firebaseArchive.bean.UtenteBean;
import it.porting.android_is.utility.LazyInitializedSingleton;

public class ViewActivityUtente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        ActionBar actionBar = getSupportActionBar();
        Button btmod = findViewById(R.id.button);
        btmod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modpage();
            }
        });
        actionBar.setBackgroundDrawable(new ColorDrawable (Color.rgb(255,153,0)));
        actionBar.setTitle("Il Mio Profilo");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String email;
            TextView emailTv;
            String password;
            Button modifica;
            email= user.getEmail();
            //inizializzo un riferimento all'oggetto che si interfaccia con firebase
            FireBaseArchive fireBaseArchive = new FireBaseArchive();

            //prelevo tutti i dati da inserire nei campi
            fireBaseArchive.getUserByKey(email, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        UtenteBean utenteBean = task.getResult().toObject(UtenteBean.class);
                        String cognome,nome,password;
                        TextView nomeTv,cognomeTv;
                        EditText psswEt;
                        cognome=utenteBean.getCognome();
                        nome=utenteBean.getNome();
                        password=utenteBean.getPassword();
                        nomeTv= findViewById(R.id.textView6);
                        nomeTv.setText(nome);
                        cognomeTv= findViewById(R.id.textView7);
                        cognomeTv.setText(cognome);
                        psswEt=findViewById(R.id.editText4);
                        psswEt.setText(password);
                        psswEt.setEnabled(false);
                    }
                    else{
                        Log.d("Errore nella query","ERRORE");
                    }
                }
            });


            emailTv= findViewById(R.id.textView8);
            emailTv.setText(email);



        } else {
            Toast.makeText(this, "Non sei loggato!", Toast.LENGTH_SHORT).show();
        }
    }

    public void modpage(){
        Intent intent = new Intent(getApplicationContext(), EditActivityUtente.class);
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
            case R.id.option1:  modpage2();
                return true;
            case R.id.option2:  guida();
                return true;

            case R.id.logout:  logout();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void modpage2(){
        Intent intent = new Intent(getApplicationContext(), ViewActivityUtente.class);
        startActivity(intent);
    }

    public void guida(){
        Intent intent = new Intent(getApplicationContext(), Guida.class);
        startActivity(intent);
    }

    public void logout(){
        //Logout dal modulo di autenticazione di firebase
        FirebaseAuth.getInstance().signOut();
        //elimino la "sessione"
        LazyInitializedSingleton lazyInitializedSingleton = LazyInitializedSingleton.getInstance();
        lazyInitializedSingleton.clearInstance();
        //ritorno alla pagina di login
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}



