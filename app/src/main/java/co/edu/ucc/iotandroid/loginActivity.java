package co.edu.ucc.iotandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class loginActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        txtEmail= (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btnIngresar)
    public void ClickInbresar(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(loginActivity.this, "Cargando...", "Por Favor Espera...", true);

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
      //  if (TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {


        (firebaseAuth.signInWithEmailAndPassword(email,password))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "¡Inicio Exitoso!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(loginActivity.this, ControlActivity.class);
                            intent.putExtra("Emailcc", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(intent);
                        } else {
                            Log.e("ERRORcc", task.getException().toString());
                            Toast.makeText(loginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            //Toast.makeText(this, "Has dejado campos vacios",
                //    Toast.LENGTH_LONG).show();
       // }
    }



    @OnClick(R.id.btnRegistrar)
    public void clickRegistrar() {
        Intent intent= new Intent(this,RegistroActivity.class);
        startActivity(intent);

    }

}

