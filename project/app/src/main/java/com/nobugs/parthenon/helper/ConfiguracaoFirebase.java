package com.nobugs.parthenon.helper;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.nobugs.parthenon.model.Atividades.Atividade;
import com.nobugs.parthenon.model.Atividades.AtividadesAux;
import com.nobugs.parthenon.model.Evento;
import com.nobugs.parthenon.model.EventoAux;
import com.nobugs.parthenon.model.Perguntas.Pergunta;
import com.nobugs.parthenon.model.Perguntas.PerguntaAux;

import java.util.List;

import io.realm.Realm;
import io.realm.internal.IOException;

public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth referenciaAutenticacao;
    private static FirebaseDatabase referenciaDatabase;

    public static FirebaseDatabase getDatabase(){
        if ( referenciaDatabase == null){
            referenciaDatabase = FirebaseDatabase.getInstance();
        }
        return referenciaDatabase;
    }

    //retorna a referencia do database
    public static DatabaseReference getFirebase(){
        if( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    //retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if( referenciaAutenticacao == null ){
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }

    public static void updateValues(String reference, Context ctx){
        final Realm realm = RealmHelper.getRealm(ctx);
        FirebaseDatabase db = getDatabase();
        DatabaseReference myRef = db.getReference(reference);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switch (dataSnapshot.getRef().getKey()){
                    case "atividades":
                        Log.v("rgk", dataSnapshot.getChildrenCount() + " atv");
                        for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
                            try {
                                RealmHelper.startTransaction();
                                AtividadesAux atvAux = dataValues.getValue(AtividadesAux.class);
                                Atividade atv = new Atividade(atvAux, dataValues.getKey());
                                realm.copyToRealmOrUpdate(atv);
                                RealmHelper.endTransaction();
                            } catch (IOException e) {
                                Log.v("rgk", e.getMessage());
                            }
                        }
                        break;
                    case "perguntas":
                        Log.v("rgk", dataSnapshot.getChildrenCount() + " perg");
                        for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
                            try {
                                RealmHelper.startTransaction();
                                PerguntaAux pergAux = dataValues.getValue(PerguntaAux.class);
                                Pergunta perg = new Pergunta(pergAux, dataValues.getKey());
                                realm.copyToRealmOrUpdate(perg);
                                RealmHelper.endTransaction();
                            } catch (IOException e) {
                                Log.v("rgk", e.getMessage());
                            }
                        }
                        break;
                    case "evento":
                        Log.v("rgk", dataSnapshot.getChildrenCount() + " evt");
                        try {
                            RealmHelper.startTransaction();
                            EventoAux evtAux = dataSnapshot.getValue(EventoAux.class);
                            Evento evt = new Evento(evtAux, dataSnapshot.getKey());
                            realm.copyToRealmOrUpdate(evt);
                            RealmHelper.endTransaction();
                        } catch (IOException e) {
                            Log.v("rgk", e.getMessage());
                        }
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.v("rgk", "possivelmente deu erro");
            }
        });
    }

}
