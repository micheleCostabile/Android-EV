package it.porting.android_is.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

import it.porting.android_is.R;
import it.porting.android_is.firebaseArchive.bean.RequestBean;
import it.porting.android_is.utility.AttachmentsDownloader;
import it.porting.android_is.utility.LazyInitializedSingleton;

import static androidx.core.content.ContextCompat.startActivity;


/**
 * Classe Adapter che consente la gestione della recyclerView nell'activity della segreteria
 */
public class RequestAdapterStudente extends RecyclerView.Adapter <RequestAdapterStudente.ViewHolder> {

    ArrayList<RequestBean> arrayList;
    private ArrayList<String> idFields = new ArrayList<>();
    Context context;



    public RequestAdapterStudente(ArrayList<RequestBean> arrayList, ArrayList<String> idFields, Context context) {
        this.arrayList = arrayList;
        this.idFields = idFields;
        this.context = context;

    }




    /**
     * Inserisce nella recyclerview la formattazione di ogni riga
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_studente, parent, false);
        return new ViewHolder(v);


    }


    /**
     * Inserisce in ogni campo il rispettivo valore
     *
     * @param holder   la recyclerview
     * @param position posizione della tupla
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.idText.setText("ID richiesta: "  + idFields.get(position));
        holder.emailText.setText("Email: " + arrayList.get(position).getUser_key());
        holder.statoText.setText("Stato : " + arrayList.get(position).getStato());
        holder.bt_attachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachmentsDownloader.getInstance().downloadAttachments(arrayList.get(position).getUser_key(), context);
            }
        });

    }



    /**
     * conteggio elementi
     * @return
     */
    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    /**
     * classe "gestore" della recyclerview, in cui vengono inizializzati tutti i campi.
     */

    public class ViewHolder extends RecyclerView.ViewHolder {


        View root;
        TextView idText;
        TextView emailText;
        TextView statoText;
        Button bt_attachments;






        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            idText = root.findViewById(R.id.idText);
            emailText = root.findViewById(R.id.emailText);
            statoText = root.findViewById(R.id.statoText);
            bt_attachments = root.findViewById(R.id.btAttachmentsSt);


        }
    }





}
