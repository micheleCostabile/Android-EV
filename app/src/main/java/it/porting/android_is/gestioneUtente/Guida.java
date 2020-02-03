package it.porting.android_is.gestioneUtente;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import it.porting.android_is.R;

public class Guida extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guida);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 153, 0)));
        actionBar.setTitle("Guida introduttiva all'uso");
        LinearLayout lineaa=findViewById(R.id.linearG);


         /*HorizontalScrollView sv=findViewById(R.id.scroll);
        sv = findViewById(R.id.scroll);
// scorrere completamente la ScrollView verso il basso
        sv.fullScroll(ScrollView.FOCUS_DOWN);
// scorrere completamente la ScrollView verso l'alto
        sv.fullScroll(ScrollView.FOCUS_UP);


          */



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }

}








