package com.projeto.swo.app_sfe_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HomeCidadaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cidadao);


    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_cidadao, menu);
        return  true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item_perfil:
                Intent intent = new Intent(HomeCidadaoActivity.this, PerfilCidActivity.class);
                startActivity(intent);
                break;
            case R.id.item_listar:

                Intent intent1 = new Intent(HomeCidadaoActivity.this, ListarCidadaosLoginActivity.class);
                startActivity(intent1);
                break;
                   }
        return super.onOptionsItemSelected(item);

    }
}