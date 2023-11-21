package com.projeto.swo.app_sfe_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlterarPerfilCidActivity extends AppCompatActivity {
    Button btSalvar , btVoltar;
    EditText nome, cpf,logradouro,numero,complemento,estado,cidade,bairro,cep, login, senha,sexo;
    SQLiteDatabase db;
    int idCidadao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_perfil_cid);

        btSalvar =findViewById(R.id.btEditar);
        btVoltar =findViewById(R.id.btVoltar);


        nome =findViewById(R.id.nomeLista);
        cpf =findViewById(R.id.cpfLista);
        sexo =findViewById(R.id.sexoLista);
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
            // Recuperar o ID do cidadão armazenado
            SharedPreferences preferences = getSharedPreferences("perfilCidadao", Context.MODE_PRIVATE);
            idCidadao = preferences.getInt("idCidadao", -1);

            if (idCidadao != -1) {
                // Criar uma instância do banco de dados
                db = openOrCreateDatabase("sfe_banco", Context.MODE_PRIVATE, null);

                // Consulta SQL para obter os dados do cidadão e do login
                String query = "SELECT Cidadao.*, Login.login, Login.senha FROM Cidadao " +
                        "INNER JOIN Login ON Cidadao.Login_idlogin = Login.idlogin " +
                        "WHERE Cidadao.idCidadao = ?";
                String[] selectionArgs = {String.valueOf(idCidadao)};

                // Executa a consulta
                Cursor cursor = db.rawQuery(query, selectionArgs);

                if (cursor.moveToFirst()) {
                    // Preencher os campos de texto com os dados recuperados
                    preencherCampo(cursor, "nome", nome);
                    preencherCampo(cursor, "cpf", cpf);
                    preencherCampo(cursor, "sexo", sexo);
                    preencherCampo(cursor, "logradouro", logradouro);
                    preencherCampo(cursor, "numero", numero);
                    preencherCampo(cursor, "complemento", complemento);
                    preencherCampo(cursor, "estado", estado);
                    preencherCampo(cursor, "cidade", cidade);
                    preencherCampo(cursor, "bairro", bairro);
                    preencherCampo(cursor, "cep", cep);

                    // Dados do Login
                    preencherCampo(cursor, "login", login);
                    preencherCampo(cursor, "senha", senha);
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

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recuperar os dados inseridos nos campos de texto
                String novoNome = nome.getText().toString();
                String novoCpf = cpf.getText().toString();
                String novoSexo = sexo.getText().toString();
                String novoLogradouro = logradouro.getText().toString();
                String novoNumero = numero.getText().toString();
                String novoComplemento = complemento.getText().toString();
                String novoEstado = estado.getText().toString();
                String novaCidade = cidade.getText().toString();
                String novoBairro = bairro.getText().toString();
                String novoCep = cep.getText().toString();
                String novoLogin = login.getText().toString();
                String novaSenha = senha.getText().toString();

                // Atualizar os dados no banco de dados
                try {
                    // Atualizar os dados do Cidadao
                    String updateCidadaoQuery = "UPDATE Cidadao SET nome=?, cpf=?, sexo=?, logradouro=?, numero=?, complemento=?, estado=?, cidade=?, bairro=?, cep=? WHERE idCidadao=?";
                    String[] updateCidadaoArgs = {novoNome, novoCpf, novoSexo, novoLogradouro, novoNumero, novoComplemento, novoEstado, novaCidade, novoBairro, novoCep, String.valueOf(idCidadao)};
                    db.execSQL(updateCidadaoQuery, updateCidadaoArgs);

                    // Atualizar os dados do Login
                    String updateLoginQuery = "UPDATE Login SET login=?, senha=? WHERE idlogin=?";
                    String[] updateLoginArgs = {novoLogin, novaSenha, String.valueOf(idCidadao)};
                    db.execSQL(updateLoginQuery, updateLoginArgs);

                    exibirMensagem("Dados atualizados com sucesso!");
                    // Criar uma Intent para iniciar a MainActivity
                    Intent intent = new Intent(AlterarPerfilCidActivity.this, PerfilCidActivity.class);

                    // Iniciar a MainActivity
                    startActivity(intent);

                    finish();

                } catch (Exception e) {
                    MostrarMensagem("Erro ao atualizar os dados: " + e.toString());
                }
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar uma Intent para iniciar a MainActivity
                Intent intent = new Intent(AlterarPerfilCidActivity.this, PerfilCidActivity.class);

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
        Toast.makeText(AlterarPerfilCidActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void MostrarMensagem(String s) {
        AlertDialog.Builder dialogo = new
                AlertDialog.Builder(AlterarPerfilCidActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(s);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }

}