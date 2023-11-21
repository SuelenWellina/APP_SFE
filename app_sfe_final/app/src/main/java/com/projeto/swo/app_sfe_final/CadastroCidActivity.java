package com.projeto.swo.app_sfe_final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class CadastroCidActivity extends AppCompatActivity {

    Button btCadastrar , btVoltar;
    EditText nome, cpf,logradouro,numero,complemento,estado,cidade,bairro,cep, login, senha;
    RadioButton sexoF,sexoM;
    SQLiteDatabase db;
    String tipo_user="cidadão";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cid);

        btCadastrar =findViewById(R.id.btCadastrar);
        btVoltar =findViewById(R.id.btVoltar);

        nome =findViewById(R.id.nomeLista);
        cpf =findViewById(R.id.cpfLista);
        sexoF =findViewById(R.id.radioF);
        sexoM =findViewById(R.id.radioM);
        logradouro =findViewById(R.id.logradouro);
        numero =findViewById(R.id.numero);
        complemento =findViewById(R.id.complemento);
        estado =findViewById(R.id.estado);
        cidade =findViewById(R.id.cidade);
        bairro =findViewById(R.id.bairro);
        cep =findViewById(R.id.cep);
        login =findViewById(R.id.loginLista);
        senha =findViewById(R.id.senhaLista);
        try {
            // Inicializar o banco de dados
            db = openOrCreateDatabase("sfe_banco", Context.MODE_PRIVATE, null);
        }catch (Exception e){
            MostrarMensagem("Erro :" +e.toString());
        }


        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeC = nome.getText().toString();
                String cpfC = cpf.getText().toString();
                String sexoC =Sexo();
                String logradouroC =logradouro.getText().toString();
                String  numeroC =numero.getText().toString();
                String  complementoC =complemento.getText().toString();
                String  estadoC =estado.getText().toString();
                String  cidadeC =cidade.getText().toString();
                String  bairroC =bairro.getText().toString();
                String  cepC =cep.getText().toString();
                String senhaC =senha.getText().toString();
                String loginC =login.getText().toString();



                try {

                    // Inserir dados na tabela Login
                    String insertLoginQuery = "INSERT INTO Login (login, senha) VALUES ('" + loginC + "', '" + senhaC + "')";
                    db.execSQL(insertLoginQuery);

                    // Obter o ID do último Login inserido
                    Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
                    int loginId = -1;
                    if (cursor.moveToFirst()) {
                        loginId = cursor.getInt(0);
                    }
                    cursor.close();

                    if (loginId != -1) {
                        // Inserir dados na tabela Cidadao
                        String insertCidadaoQuery = "INSERT INTO Cidadao " +
                                "(nome, tipo_user, cpf, sexo, logradouro, numero, complemento, estado, cidade, bairro, cep, Login_idlogin) " +
                                "VALUES ('" + nomeC + "','" + tipo_user + "', '" + cpfC + "', '" + sexoC + "', '" + logradouroC + "', " +
                                "'" + numeroC + "', '" + complementoC + "', '" + estadoC + "', '" + cidadeC + "', '" + bairroC + "', '" + cepC + "', '" + loginId + "')"; // Substitua 'tipo_usuario_aqui' pelo valor correto

                        db.execSQL(insertCidadaoQuery);
                        MostrarMensagem("Dados cadastrados com sucesso!");
                        Intent intent = new Intent(CadastroCidActivity.this, MainActivity.class);

                        // Iniciar a MainActivity
                        startActivity(intent);
                    } else {
                        MostrarMensagem("Erro ao obter o ID do Login.");
                    }
                } catch (Exception e) {
                    MostrarMensagem("Erro: " + e.toString());
                    Long.getLong(e.toString());
                }
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar uma Intent para iniciar a MainActivity
                Intent intent = new Intent(CadastroCidActivity.this, MainActivity.class);

                // Iniciar a MainActivity
                startActivity(intent);

                finish();
            }
        });

    }

    private String Sexo() {
        if (sexoF.isChecked()) {
            return "Feminino";
        } else if (sexoM.isChecked()) {
            return "Masculino";
        } else {
            // Caso nenhum RadioButton esteja selecionado
            return null;
        }
    }

    private void MostrarMensagem(String s) {
        AlertDialog.Builder dialogo = new
                AlertDialog.Builder(CadastroCidActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(s);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }









}