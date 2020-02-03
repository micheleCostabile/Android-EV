package it.porting.android_is.gestioneAdmin;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import it.porting.android_is.R;
import it.porting.android_is.adapter.RequestAdapterAdmin;
import it.porting.android_is.firebaseArchive.FireBaseArchive;
import it.porting.android_is.firebaseArchive.bean.RequestBean;
import it.porting.android_is.gestioneUtente.Guida;
import it.porting.android_is.gestioneUtente.LoginActivity;
import it.porting.android_is.gestioneUtente.ViewActivityUtente;
import it.porting.android_is.network.RetrofitSingleton;
import it.porting.android_is.utility.LazyInitializedSingleton;
import it.porting.android_is.utility.MyDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivityAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RequestAdapterAdmin requestAdapterAdmin;
    private FireBaseArchive fireBaseArchive;
    private ArrayList<RequestBean> requestBeans = new ArrayList<>();
    private static SharedPreferences.Editor editor;
    private static SharedPreferences preferences;
    private static FirebaseStorage firebaseStorage;
    private static StorageReference storageReference;
    private static StorageReference ref;
    private String file;
    private ArrayList<String> idFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Home Admin");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 153, 0)));


        //Inizializzazione shared preferences ed editor, saranno utilizzate per verificare
        //se l'utente ha associato l'account all'impronta digitale
        preferences = this.getSharedPreferences(
                "myPref", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //Controlliamo se fingerSaved è uguale a 0, se è 0 vuol dire che l'utente non ha
        //associato la sua impronta e visualizziamo quindi il dialogFragment dove gli chiediamo se
        //vuole memorizzarla o meno
        if(preferences.getInt("fingerSaved", 0) == 0){
            MyDialogFragment dialogFragment = new MyDialogFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialogFragment.show(ft, "dialog");
        }

        //individuo la recyclerview
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        //inizializzo un riferimento all'oggetto che si interfaccia con firebase
        fireBaseArchive = new FireBaseArchive();
        //prelevo tutte le request da inserire nella recyclerview
        fireBaseArchive.getAllRequests(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //Se il task ha successo, salvo ogni "tupla" all'interno dell ArrayList
                    for (QueryDocumentSnapshot req : task.getResult()) {
                        RequestBean requestBean = req.toObject(RequestBean.class);
                        idFields.add(req.getId());
                        requestBeans.add(requestBean);
                    }

                    requestAdapterAdmin = new RequestAdapterAdmin(requestBeans, idFields, getApplicationContext());
                    recyclerView.setAdapter(requestAdapterAdmin);



                } else {
                    Log.d("Errore nella query", "ERRORE");
                }
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option1:
                modpage();
                return true;
            case R.id.option2:
                excelApproved();
                return true;
            case R.id.option3:
                guida();
                return true;
            case R.id.option4:
                excelRefused();
                return true;
            case R.id.logout:
                logout();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }


    //trasferisce il controllo alla pagina di visualizzazione del profilo
    public void modpage(){
        Intent intent = new Intent(getApplicationContext(), ViewActivityUtente.class);
        startActivity(intent);
    }

    //visualizza la guida utente
    public void guida(){
        Intent intent = new Intent(getApplicationContext(), Guida.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu_admin, menu);

        return true;
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


    public void excelApproved() {
        RetrofitSingleton.getInstance().performCreateApprovedExcel(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Errore in fase di creazione", Toast.LENGTH_SHORT).show();

                }

                Toast.makeText(getApplicationContext(), "File Excel creato", Toast.LENGTH_SHORT).show();
                downloadAccepted();


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Errore in fase di creazione", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void excelRefused() {
        RetrofitSingleton.getInstance().performCreateRefusedExcel(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Errore in fase di creazione", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "File Excel creato", Toast.LENGTH_SHORT).show();
                downloadRefused();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Errore in fase di creazione", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void downloadAccepted(){
        storageReference=firebaseStorage.getInstance().getReference();
        file = "Accettate.xlsx";
        ref=storageReference.child(file);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                 downloadFile(getApplicationContext(),file, DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"ERRORE", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void downloadRefused(){
        storageReference=firebaseStorage.getInstance().getReference();
        file = "Rifiutate.xlsx";
        ref=storageReference.child(file);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                Log.d("URL",url);

                downloadFile(getApplicationContext(),file,DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "ERRORE", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadFile(Context context, String fileName, String destinationDir, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDir,fileName);
        downloadManager.enqueue(request);
    }


}
