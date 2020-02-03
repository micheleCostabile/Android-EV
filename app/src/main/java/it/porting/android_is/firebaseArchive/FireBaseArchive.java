package it.porting.android_is.firebaseArchive;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

import it.porting.android_is.firebaseArchive.bean.RequestBean;
import it.porting.android_is.firebaseArchive.bean.UtenteBean;


public class FireBaseArchive {

    //istanza di connessione al db
    FirebaseFirestore db;


    public FireBaseArchive() {

        db = FirebaseFirestore.getInstance();
    }


    /**
     * Consente di prelevare tutte le request dal db, restituisce un arraylist di request
     *
     * @param onCompleteListener
     */

    public void getAllRequests(OnCompleteListener<QuerySnapshot> onCompleteListener) {
        //Eseguo la "query", salvando la collection
        CollectionReference collectionReference = db.collection("request");

        //una volta completata, lancio onComplete
        collectionReference.get().addOnCompleteListener(onCompleteListener);
    }

    public void getRequestByKey(String email, OnCompleteListener<QuerySnapshot> listener) {

        //Abbiamo fatto il casting
        Query collectionReference = db.collection("request").whereEqualTo("user_key", email);
        collectionReference.get().addOnCompleteListener(listener);

    }
    public void getUserByKey(String email, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        DocumentReference doc = db.collection("utenti").document(email);
        doc.get().addOnCompleteListener(onCompleteListener);
    }



}

