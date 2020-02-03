package it.porting.android_is.gestioneStudente;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import it.porting.android_is.R;
import it.porting.android_is.firebaseArchive.FireBaseArchive;
import it.porting.android_is.firebaseArchive.bean.RequestBean;
import it.porting.android_is.gestioneUtente.Guida;
import it.porting.android_is.gestioneUtente.LoginActivity;
import it.porting.android_is.gestioneUtente.ViewActivityUtente;
import it.porting.android_is.network.RetrofitSingleton;
import it.porting.android_is.utility.LazyInitializedSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestForm extends AppCompatActivity {
    private EditText et1;
    private EditText et2;
    private Spinner et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private Spinner et8;
    private Button button;
    private TextView textView;
    private FrameLayout frameLayout;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private FireBaseArchive fireBaseArchive;
    private ArrayList<RequestBean> requestBeans = new ArrayList<>();
    private DatePickerDialog dataPicker;

    private Random random;


    RequestBean requestBean = new RequestBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_form);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 153, 0)));
        actionBar.setTitle("Inserisci richiesta");


        et1 = findViewById(R.id.etAnno);
        et2 = findViewById(R.id.etMatricola);
        et3 = findViewById(R.id.spinner_ente);
        et4 = findViewById(R.id.etRelease);
        et5 = findViewById(R.id.et_scadenza);
        et6 = findViewById(R.id.et_seriale);
        et7 = findViewById(R.id.et_livello);
        et8 = findViewById(R.id.spinner_cfu);
        textView = findViewById(R.id.boxContainer2);
        frameLayout = findViewById(R.id.frame);




        fireBaseArchive = new FireBaseArchive();



        //prelevo tutte le request per  emailda inserire nella recyclerview
        fireBaseArchive.getRequestByKey(email, new OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //Se il task ha successo, salvo ogni "tupla" all'interno dell ArrayList
                    for (QueryDocumentSnapshot req : task.getResult()) {
                        RequestBean requestBean = req.toObject(RequestBean.class);

                        if(requestBean.getUser_key().equals(email)){
                            if(!requestBean.getStato().equals("Rifiutata")) {

                                frameLayout.setVisibility(View.VISIBLE);

                                button.setVisibility(View.GONE);
                                et1.setVisibility(View.GONE);
                                et2.setVisibility(View.GONE);
                                et3.setVisibility(View.GONE);
                                et4.setVisibility(View.GONE);
                                et5.setVisibility(View.GONE);
                                et6.setVisibility(View.GONE);
                                et7.setVisibility(View.GONE);
                                et8.setVisibility(View.GONE);

                            }
                        }



                    }


                }
                else{
                    Log.d("Errore nella query","ERRORE");
                }
            }
        });




        button = findViewById(R.id.btSendForm);
        button.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          if (et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty() || et4.getText().toString().isEmpty() || et5.getText().toString().isEmpty() || et6.getText().toString().isEmpty() || et7.getText().toString().isEmpty()) {
                                              Toast.makeText(getApplicationContext(), "compila tutti i dati", Toast.LENGTH_SHORT).show();
                                          }

                                          else if(et2.getText().length() < 10 || et2.getText().length() > 10)
                                          {
                                              Toast.makeText(getApplicationContext(), "matricola non valida", Toast.LENGTH_SHORT).show();

                                          }
                                          else {


                                              int seriale = Integer.parseInt(et6.getText().toString());
                                              int cfu = Integer.parseInt(et8.getSelectedItem().toString());
                                              SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
                                              requestBean.setEnte(et3.getSelectedItem().toString());
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
                                              });


                                              RetrofitSingleton.getInstance().performCreatePDF(requestBean, new Callback<Void>() {
                                                  @Override
                                                  public void onResponse(Call<Void> call, Response<Void> response) {
                                                      if (!response.isSuccessful()) {
                                                          textView.setText(("Code: " + response.code()));

                                                      }

                                                   }

                                                  @Override
                                                  public void onFailure(Call<Void> call, Throwable t) {
                                                      textView.setText((t.getMessage()));
                                                  }
                                              });

                                          }
                                      }
                                  }
        );


        et4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                dataPicker = new DatePickerDialog(RequestForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et4.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                dataPicker.show();
            }
        });
        et5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                dataPicker = new DatePickerDialog(RequestForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et5.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                dataPicker.show();
            }
        });
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
        Intent intent = new Intent(getApplicationContext(),RequestForm.class);
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


}




