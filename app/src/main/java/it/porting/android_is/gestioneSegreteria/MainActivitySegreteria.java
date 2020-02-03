package it.porting.android_is.gestioneSegreteria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.porting.android_is.R;
import it.porting.android_is.adapter.RequestAdapterSegreteria;
import it.porting.android_is.firebaseArchive.FireBaseArchive;
import it.porting.android_is.firebaseArchive.bean.RequestBean;
import it.porting.android_is.gestioneUtente.Guida;
import it.porting.android_is.gestioneUtente.LoginActivity;
import it.porting.android_is.gestioneUtente.ViewActivityUtente;
import it.porting.android_is.utility.LazyInitializedSingleton;
import it.porting.android_is.utility.MyDialogFragment;

public class MainActivitySegreteria extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RequestAdapterSegreteria requestAdapterSegreteria;
    private FireBaseArchive fireBaseArchive;
    private FireBaseArchive fireBaseArchive2;
    private ArrayList<RequestBean> requestBeans = new ArrayList<>();

    private static SharedPreferences.Editor editor;
    private static SharedPreferences preferences;
    private ArrayList<String> idFields = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_segreteria);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Home Segreteria");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,153,0)));
        /*requestBeans = new ArrayList<RequestBean>();
        */


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
        fireBaseArchive2 = new FireBaseArchive();
        //prelevo tutte le request da inserire nella recyclerview
        fireBaseArchive.getAllRequests(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //Se il task ha successo, salvo ogni "tupla" all'interno dell ArrayList
                    for(QueryDocumentSnapshot req : task.getResult()){
                        RequestBean requestBean = req.toObject(RequestBean.class);
                        if(requestBean.getStato().equals("Approvata")){
                            requestBeans.add(requestBean);
                            idFields.add(req.getId());
                        }
                    }

                    requestAdapterSegreteria = new RequestAdapterSegreteria(requestBeans, idFields, getApplicationContext());
                    recyclerView.setAdapter(requestAdapterSegreteria);
                }
                else{
                    Log.d("Errore nella query","ERRORE");
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.option1:  modpage();
                return true;
            case R.id.option2:  guida();
                return true;
            case R.id.logout: logout();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

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





}
