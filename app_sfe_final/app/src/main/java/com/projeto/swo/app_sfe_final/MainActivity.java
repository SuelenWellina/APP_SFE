package com.projeto.swo.app_sfe_final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton cidadao, agente, admin;
    EditText usuario, senha;
    Button btLogar, btBanco;
    TextView cadastrar;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.tipo_user);
        cidadao = findViewById(R.id.cidadao);
        agente = findViewById(R.id.agente);
        admin = findViewById(R.id.admin);

        usuario = findViewById(R.id.usuario);
        senha = findViewById(R.id.senhaLista);

        btLogar = findViewById(R.id.btCadastrar);
        btBanco = findViewById(R.id.btBanco);
        //Para vizualizar o botão do banco so substituir GONE por VISIBELE
        btBanco.setVisibility(View.GONE);

        cadastrar = findViewById(R.id.cadastrar);

        try {
            db = openOrCreateDatabase("sfe_banco", Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            MostrarMensagem("Erro :" + e.toString());
        }

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroCidActivity.class);
                startActivity(intent);
            }
        });

        btBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db = openOrCreateDatabase("sfe_banco", Context.MODE_PRIVATE, null);
                    // Criação das tabelas...

                    AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                    dialogo.setTitle("Aviso")
                            .setMessage("Banco de Dados criado com sucesso!")
                            .setNeutralButton("OK", null)
                            .show();
                } catch (Exception e) {
                    MostrarMensagem("Erro ao criar o banco de dados: " + e.toString());
                }
            }
        });

        btLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = usuario.getText().toString();
                String senhaDigitada = senha.getText().toString();
                String tipoUsuario = tipodeUsuaario();

                if (tipoUsuario != null) {
                    String query = "SELECT idCidadao FROM Cidadao INNER JOIN Login ON Cidadao.Login_idlogin = Login.idlogin WHERE login = ? AND senha = ? AND tipo_user = ?";
                    String[] selectionArgs = {login, senhaDigitada, tipoUsuario};

                    Cursor cursor = db.rawQuery(query, selectionArgs);

                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex("idCidadao");
                        if (columnIndex != -1) {
                            int idCidadao = cursor.getInt(columnIndex);

                            SharedPreferences preferences = getSharedPreferences("perfilCidadao", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("idCidadao", idCidadao);
                            editor.apply();

                            Intent intent = new Intent(MainActivity.this, HomeCidadaoActivity.class);
                            startActivity(intent);
                        } else {
                            exibirMensagem("Erro: Coluna 'idCidadao' não encontrada no cursor.");
                        }
                    } else {
                        if (tipoUsuario.equals("admin") || tipoUsuario.equals("agente")) {
                            exibirMensagem("Páginas em Desenvolvimento!!");
                        } else {
                            exibirMensagem("Login ou senha incorretos");
                        }
                    }

                    cursor.close();
                } else {
                    exibirMensagem("Opção não selecionada!");
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.cidadao:
                if (checked) {
                    agente.setChecked(false);
                    admin.setChecked(false);
                }
                break;
            case R.id.agente:
                if (checked) {
                    cidadao.setChecked(false);
                    admin.setChecked(false);
                }
                break;
            case R.id.admin:
                if (checked) {
                    cidadao.setChecked(false);
                    agente.setChecked(false);
                }
                break;
        }
    }

    private String tipodeUsuaario() {
        if (cidadao.isChecked()) {
            return "cidadão";
        } else if (agente.isChecked()) {
            return "agente";
        } else if (admin.isChecked()) {
            return "admin";
        } else {
            return null;
        }
    }

    private void exibirMensagem(String mensagem) {
        Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void MostrarMensagem(String s) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(s);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }
}
