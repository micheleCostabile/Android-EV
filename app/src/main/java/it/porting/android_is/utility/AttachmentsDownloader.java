package it.porting.android_is.utility;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class AttachmentsDownloader {

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;
    String fileToDownload;
    String fileToDownload2;
    private static AttachmentsDownloader instance;


    private AttachmentsDownloader(){}

    public static AttachmentsDownloader getInstance(){
        if(instance == null){
            instance = new AttachmentsDownloader();
        }
        return instance;
    }
    public void downloadAttachments(String email, final Context context){
        storageReference=firebaseStorage.getInstance().getReference();
        fileToDownload = "Certificato_"+email;
        ref = storageReference.getRoot().child(fileToDownload);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadFile(context.getApplicationContext(),fileToDownload+".pdf",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context.getApplicationContext(), fileToDownload + " non esistente sul db", Toast.LENGTH_LONG).show();

            }
        });

        fileToDownload2 = "Richiesta_Firmata_"+email;
        ref = storageReference.getRoot().child(fileToDownload2);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadFile(context.getApplicationContext(),fileToDownload2+".pdf",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context.getApplicationContext(), fileToDownload2 + "  non esistente sul db", Toast.LENGTH_LONG).show();
            }
        });




    }

    public void downloadFile(Context context, String fileName, String destinationDir, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDir,fileName);
        downloadManager.enqueue(request);
    }

}
