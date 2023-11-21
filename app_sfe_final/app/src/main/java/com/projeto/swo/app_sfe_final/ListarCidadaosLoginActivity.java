package com.projeto.swo.app_sfe_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ListarCidadaosLoginActivity extends AppCompatActivity {
    Button btVoltar;
    EditText idCidadao, nome, cpf, login, senha, sexo;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cidadaos_login);

        btVoltar = findViewById(R.id.btVoltar);
        int idCidadaoFromPreferences;  // Renomeada para evitar conflito
        idCidadao = findViewById(R.id.idCidadaoLista);
        nome = findViewById(R.id.nomeLista);
        cpf = findViewById(R.id.cpfLista);
        sexo = findViewById(R.id.sexoLista);
        login = findViewById(R.id.loginLista);
        senha = findViewById(R.id.senhaLista);

        // Tornar os campos de texto apenas leitura
        idCidadao.setEnabled(false);
        nome.setEnabled(false);
        cpf.setEnabled(false);
        sexo.setEnabled(false);
        login.setEnabled(false);
        senha.setEnabled(false);

        try {
            // Recuperar o ID do cidadão armazenado
            SharedPreferences preferences = getSharedPreferences("perfilCidadao", Context.MODE_PRIVATE);
            idCidadaoFromPreferences = preferences.getInt("idCidadao", -1);

            if (idCidadaoFromPreferences != -1) {
                // Criar uma instância do banco de dados
                db = openOrCreateDatabase("sfe_banco", Context.MODE_PRIVATE, null);

                // Consulta SQL para obter os dados do cidadão e do login
                String query = "SELECT idCidadao, nome, cpf, sexo, login, senha FROM Cidadao " +
                        "INNER JOIN Login ON Cidadao.Login_idlogin = Login.idlogin " +
                        "WHERE Cidadao.idCidadao = ?";
                String[] selectionArgs = {String.valueOf(idCidadaoFromPreferences)};

                // Executa a consulta
                Cursor cursor = db.rawQuery(query, selectionArgs);

                if (cursor.moveToFirst()) {
                    do {
                        // Exibir os valores
                        preencherCampo(cursor, "idCidadao", idCidadao);
                        preencherCampo(cursor, "nome", nome);
                        preencherCampo(cursor, "cpf", cpf);
                        preencherCampo(cursor, "sexo", sexo);
                        preencherCampo(cursor, "login", login);
                        preencherCampo(cursor, "senha", senha);
                    } while (cursor.moveToNext());
                } else {
                    exibirMensagem("Erro: Nenhum resultado encontrado.");
                }

                // Fechar o cursor
                cursor.close();
            } else {
                exibirMensagem("Erro: ID do cidadão não encontrado.");
            }
        } catch (Exception e) {
            MostrarMensagem("Erro :" + e.toString());
        }

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar uma Intent para iniciar a MainActivity
                Intent intent = new Intent(ListarCidadaosLoginActivity.this, HomeCidadaoActivity.class);

                // Iniciar a MainActivity
                startActivity(intent);

                finish();
            }
        });
    }


    private void preencherCampo(Cursor cursor, String nomeColuna, EditText campo) {
        // Verifica se a coluna existe no cursor
        int columnIndex = cursor.getColumnIndex(nomeColuna);
        if (columnIndex != -1) {
            // A coluna foi encontrada, então podemos acessar seus dados
            campo.setText(cursor.getString(columnIndex));
        } else {
            // A coluna não foi encontrada
            exibirMensagem("Erro: Coluna '" + nomeColuna + "' não encontrada no cursor.");
        }
    }

    private void exibirMensagem(String mensagem) {
        Toast.makeText(ListarCidadaosLoginActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void MostrarMensagem(String s) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ListarCidadaosLoginActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(s);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }
}
