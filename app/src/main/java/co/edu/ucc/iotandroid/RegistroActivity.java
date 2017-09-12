package co.edu.ucc.iotandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.ucc.iotandroid.entidades.Usuarios;

public class RegistroActivity extends AppCompatActivity {

    @BindView(R.id.txtNombres)
    EditText txtNombres;

    @BindView(R.id.txtEmail)
    EditText txtEmail;

    @BindView(R.id.txtPassword)
    EditText txtPassword;

    @BindView(R.id.btnRegistrar)
    Button btnRegistrar;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("usuarios");
    }


    @OnClick(R.id.btnRegistrar)
    public void clickRegistrar() {

        String nombres = txtNombres.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final String uid = task.getResult().getUser().getUid();

                            task.getResult().getUser().getToken(true)
                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {

                                            if (task.isSuccessful()) {

                                                final String nomUsuario = txtNombres.getText().toString();

                                                Usuarios objUsuario = new Usuarios();
                                                objUsuario.setNombre(nomUsuario);
                                                objUsuario.setToken(task.getResult().getToken());

                                                reference.child(uid).setValue(objUsuario)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    Intent intent = new Intent(RegistroActivity.this,
                                                                            ControlActivity.class);

                                                                    intent.putExtra("nomUsuario", nomUsuario);

                                                                    startActivity(intent);

                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(RegistroActivity.this,
                                                                            "Error " + task.getException().getMessage(),
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            } else {

                                                Toast.makeText(RegistroActivity.this,
                                                        "Error " + task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                        } else {

                            Toast.makeText(RegistroActivity.this,
                                    "Error " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @OnClick(R.id.tienesUsuario)
    public void clickTienesUsuario() {
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);

    }
}
