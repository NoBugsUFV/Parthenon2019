package com.nobugs.parthenon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.nobugs.parthenon.R;
import com.nobugs.parthenon.helper.ConfiguracaoFirebase;
import com.nobugs.parthenon.model.Perguntas.PerguntaAux;
import java.util.UUID;

public class SubmitDuvidasActivity extends AppCompatActivity {
    private EditText tituloDuvida;
    private TextInputEditText conteudoDuvida;
    private Button enviarDuvida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_duvidas);
        tituloDuvida = findViewById(R.id.tituloDuvida);
        conteudoDuvida = findViewById(R.id.conteudoDuvida);
        enviarDuvida = findViewById(R.id.enviarDuvida);

        enviarDuvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PerguntaAux pergunta = new PerguntaAux();

                if(tituloDuvida.getText() != null){
                    if(conteudoDuvida.getText() != null){

                        pergunta.setRespondida("0");
                        pergunta.setTitulo(tituloDuvida.getText().toString());
                        pergunta.setPergunta(conteudoDuvida.getText().toString());
                        pergunta.setNomeFirebase(UUID.randomUUID().toString());
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user != null){
                            pergunta.setEmail(user.getEmail());
                        }

                        savePergunta(pergunta);
                        finish();   }
                    else{ Toast.makeText(SubmitDuvidasActivity.this, "Preencha o conteúdo da sua dúvida.", Toast.LENGTH_SHORT).show(); } }
                else{ Toast.makeText(SubmitDuvidasActivity.this, "Adicione um título a sua dúvida.", Toast.LENGTH_SHORT).show(); }
            }});


    }


    private void savePergunta( PerguntaAux perguntaAux){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
        DatabaseReference database = ConfiguracaoFirebase.getFirebase();
        DatabaseReference mensagemRef = database.child("perguntas").child(perguntaAux.getNomeFirebase());
        mensagemRef.setValue(perguntaAux);
        finish();   }
        else{ Toast.makeText(this, "Erro ao enviar pergunta. Tente novamente.", Toast.LENGTH_SHORT).show(); }


    }

}
