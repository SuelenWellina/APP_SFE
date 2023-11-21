package com.projeto.swo.app_sfe_final;


import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PerfilCidActivity extends AppCompatActivity {

    Button btEditar , btVoltar, btExcluir;
    EditText nome, cpf,logradouro,numero,complemento,estado,cidade,bairro,cep, login, senha,sexo;
    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cid);

        btEditar =findViewById(R.id.btEditar);
        btVoltar =findViewById(R.id.btVoltar);
        btExcluir =findViewById(R.id.btExcluir);

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

        // Tornar os campos de texto apenas leitura
        nome.setEnabled(false);
        cpf.setEnabled(false);
        sexo.setEnabled(false);
        logradouro.setEnabled(false);
        numero.setEnabled(false);
        complemento.setEnabled(false);
        estado.setEnabled(false);
        cidade.setEnabled(false);
        bairro.setEnabled(false);
        cep.setEnabled(false);
        login.setEnabled(false);
        senha.setEnabled(false);
        try {
            // Recuperar o ID do cidadão armazenado
            SharedPreferences preferences = getSharedPreferences("perfilCidadao", Context.MODE_PRIVATE);
            int idCidadao = preferences.getInt("idCidadao", -1);

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

        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar uma Intent para iniciar a MainActivity
                Intent intent = new Intent(PerfilCidActivity.this, AlterarPerfilCidActivity.class);

                // Iniciar a MainActivity
                startActivity(intent);

                finish();
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar uma Intent para iniciar a MainActivity
                Intent intent = new Intent(PerfilCidActivity.this, HomeCidadaoActivity.class);

                // Iniciar a MainActivity
                startActivity(intent);

                finish();
            }
        });

        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar um AlertDialog para confirmar a exclusão
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilCidActivity.this);
                builder.setMessage("Tem certeza de que deseja excluir? Esta ação é irreversível.");

                // Configurar botões "Sim" e "Não" no AlertDialog
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // Recuperar o ID do cidadão armazenado
                            SharedPreferences preferences = getSharedPreferences("perfilCidadao", Context.MODE_PRIVATE);
                            int idCidadao = preferences.getInt("idCidadao", -1);

                            if (idCidadao != -1) {
                                // Criar uma instância do banco de dados
                                db = openOrCreateDatabase("sfe_banco", Context.MODE_PRIVATE, null);

                                // Excluir dados de login e cidadão usando subconsulta
                                String deleteQuery = "DELETE FROM Cidadao WHERE idCidadao = ? AND Login_idlogin IN (SELECT idlogin FROM Login)";
                                String[] cidadaoArgs = {String.valueOf(idCidadao)};
                                db.execSQL(deleteQuery, cidadaoArgs);

                                // Informar ao usuário que a exclusão foi bem-sucedida
                                exibirMensagem("Cidadão e dados de login excluídos com sucesso.");

                                // Voltar para a tela de login ou outra tela apropriada
                                Intent intent = new Intent(PerfilCidActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                exibirMensagem("Erro: ID do cidadão não encontrado.");
                            }

                        } catch (Exception e) {
                            MostrarMensagem("Erro :" + e.toString());
                        }
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Código para lidar com o caso em que o usuário clicou em "Não"
                        dialog.dismiss(); // Fecha o AlertDialog
                    }
                });

                // Mostrar o AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
        Toast.makeText(PerfilCidActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void MostrarMensagem(String s) {
        AlertDialog.Builder dialogo = new
                AlertDialog.Builder(PerfilCidActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(s);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }
}