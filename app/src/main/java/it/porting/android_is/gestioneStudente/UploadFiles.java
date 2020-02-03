package it.porting.android_is.gestioneStudente;

import android.app.ProgressDialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import it.porting.android_is.R;
import it.porting.android_is.firebaseArchive.bean.RequestBean;
import it.porting.android_is.gestioneUtente.Guida;
import it.porting.android_is.gestioneUtente.LoginActivity;
import it.porting.android_is.gestioneUtente.ViewActivityUtente;
import it.porting.android_is.network.RetrofitSingleton;
import it.porting.android_is.utility.LazyInitializedSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFiles extends AppCompatActivity implements View.OnClickListener {

    private static final int PICKFILE_REQUEST_CODE = 1024;
    private static final int PICKFILE_REQUEST_CODE2 = 1025;
    private Button scegli,scegli2,upload;
    private TextView text,text2;
    private Uri filepath,filepath2;
    private StorageReference storageReference;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploads);
        storageReference= FirebaseStorage.getInstance().getReference();
        scegli=findViewById(R.id.chose);
        scegli2= findViewById(R.id.chose2);
        text=findViewById(R.id.testo1);
        text2=findViewById(R.id.testo2);
        upload=findViewById(R.id.butt);

        scegli.setOnClickListener(this);

        scegli2.setOnClickListener(this);



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
                Intent intent = new Intent(getApplicationContext(), MainActivityStudente.class);
                startActivity(intent);

            }
        });




    }


    @Override
    public void onClick(View view) {
        if(view==scegli){
            filecho();
        } else if(view==scegli2){
            filecho2();
        }

    }


    public void upload(){
        if(filepath!=null && filepath2!=null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Caricamento in corso...");
            progressDialog.show();

            String nomeF = "Certificato_"+user.getEmail() ;
            StorageReference Sr = storageReference.child(nomeF);
            Sr.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Caricamento completato", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress= (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage((int) progress + "% Caricati..");
                        }
                    })
            ;

            String nomeF2 = "Richiesta_Firmata_"+user.getEmail() ;
            StorageReference Sr2 = storageReference.child(nomeF2);
            Sr2.putFile(filepath2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Caricamento completato", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress= (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage((int) progress + "% Caricati..");
                        }
                    })
            ;
        }else{

        }

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option1:
                modpage();
                return true;

            case R.id.option2:
                reqForm();
                return true;

            case R.id.option3:
                guida();
                return true;

            case R.id.option4:
                downl();
                return true;

            case R.id.option5:
                upl();
                return true;

            case R.id.logout:
                logout();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    public void downl(){
        Intent intent = new Intent(getApplicationContext(), DownloadPDF.class);
        startActivity(intent);
    }

    public void upl(){
        Intent intent = new Intent(getApplicationContext(), UploadFiles.class);
        startActivity(intent);
    }

    public void modpage() {
        Intent intent = new Intent(getApplicationContext(), ViewActivityUtente.class);
        startActivity(intent);
    }

    public void reqForm() {
        Intent intent = new Intent(getApplicationContext(), it.porting.android_is.gestioneStudente.RequestForm.class);
        startActivity(intent);
    }

    public void guida() {
        Intent intent = new Intent(getApplicationContext(), Guida.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu_studente, menu);

        return true;

    }


    public void logout() {
        //Logout dal modulo di autenticazione di firebase
        FirebaseAuth.getInstance().signOut();
        //elimino la "sessione"
        LazyInitializedSingleton lazyInitializedSingleton = LazyInitializedSingleton.getInstance();
        lazyInitializedSingleton.clearInstance();
        //ritorno alla pagina di login
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
    public void filecho(){
        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleziona il pdf"),PICKFILE_REQUEST_CODE);
    }

    public void filecho2(){
        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleziona il pdf"),PICKFILE_REQUEST_CODE2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICKFILE_REQUEST_CODE && resultCode==RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            text.setText(filepath.toString());
        }
        else{
            filepath2=data.getData();
            text2.setText(filepath2.toString());
        }
    }


    public static class RequestForm extends AppCompatActivity {
        private EditText et1;
        private EditText et2;
        private EditText et3;
        private EditText et4;
        private EditText et5;
        private EditText et6;
        private EditText et7;
        private EditText et8;
        private Button button;
        private Spinner spinner_ente;
        private Spinner spinner_cfu;
        private TextView textView;
        private static FirebaseFirestore db = FirebaseFirestore.getInstance();


        RequestBean requestBean = new RequestBean();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.request_form);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 153, 0)));
            actionBar.setTitle("Inserisci richiesta");


            et1=findViewById(R.id.etAnno);
            et2=findViewById(R.id.etMatricola);
            spinner_ente = findViewById(R.id.spinner_ente);
            et4=findViewById(R.id.etRelease);
            et5=findViewById(R.id.et_scadenza);
            et6=findViewById(R.id.et_seriale);
            et7=findViewById(R.id.et_livello);
            spinner_cfu = findViewById(R.id.spinner_cfu);
            textView=findViewById(R.id.boxContainer2);



        button=findViewById(R.id.btSendForm);
            button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {

                 int seriale=Integer.parseInt(et6.getText().toString());
                 int cfu=Integer.parseInt(spinner_cfu.getSelectedItem().toString());
                 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                 Date parsedDate = null;
                 try {
                     parsedDate = dateFormat.parse(et4.getText().toString());
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }

                 Date parsedDate2 = null;
                 try {
                     parsedDate2 = dateFormat.parse(et5.getText().toString());
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }


                 Timestamp timestamp1 = new Timestamp(parsedDate);
                 Timestamp timestamp2 = new Timestamp(parsedDate2);
                 requestBean.setYear(et1.getText().toString());
                 requestBean.setMatricola(et2.getText().toString());
                 requestBean.setEnte(spinner_ente.getSelectedItem().toString());
                 requestBean.setRelease_date(timestamp1);
                 requestBean.setExpiry_date(timestamp2);
                 requestBean.setSerial(seriale);
                 requestBean.setLevel(et7.getText().toString());
                 requestBean.setValidated_cfu(cfu);
                 requestBean.setStato("Inviato");
                 requestBean.setUser_name(LazyInitializedSingleton.getInstance().getUser().get("nome").toString());
                 requestBean.setUser_surname(LazyInitializedSingleton.getInstance().getUser().get("cognome").toString());
                 requestBean.setUser_key(LazyInitializedSingleton.getInstance().getUser().get("email").toString());

                 db.collection("request").add(requestBean).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                     @Override
                     public void onSuccess(DocumentReference documentReference) {
                         Toast.makeText(getApplicationContext(), "Richiesta caricata con successo", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivityStudente.class);
                        startActivity(intent);


                     }
                 })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Log.w("DEBUG", "Error writing document", e);
                                 Toast.makeText(getApplicationContext(), "Richiesta non caricata", Toast.LENGTH_SHORT).show();

                             }
                         });


                 RetrofitSingleton.getInstance().performCreatePDF(requestBean,new Callback<Void>() {
                     @Override
                     public void onResponse(Call<Void> call, Response<Void> response) {
                         if (!response.isSuccessful()) {
                             textView.setText(("Code: " + response.code()));

                         }
                         textView.setText("ok");
                     }

                     @Override
                     public void onFailure(Call<Void> call, Throwable t) {
                         textView.setText((t.getMessage()));
                     }
                 });

             }
            }
     );



        }





        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.option1:
                    modpage();
                    return true;
                case R.id.option2:
                    guida();
                    return true;
                case R.id.logout:
                    logout();
                    return true;

            }
            return super.onOptionsItemSelected(item);

        }

        public void modpage() {
            Intent intent = new Intent(getApplicationContext(), ViewActivityUtente.class);
            startActivity(intent);
        }

        public void guida() {
            Intent intent = new Intent(getApplicationContext(), Guida.class);
            startActivity(intent);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.home_menu, menu);
            return true;
        }


        public void logout() {
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
}
