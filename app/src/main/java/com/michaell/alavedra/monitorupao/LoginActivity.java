package com.michaell.alavedra.monitorupao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michaell.alavedra.monitorupao.models.Alumno;

import java.util.Objects;

import static com.michaell.alavedra.monitorupao.methods.Methods.showSnackbar;

public class LoginActivity extends AppCompatActivity {
    // Declaración de controles
    private TextInputLayout til_Id_Alumno;
    private TextInputEditText tiet_ID_Alumno;
    private TextInputLayout til_Password;
    private TextInputEditText tiet_Password;
    // Variables Firebase Database
    private FirebaseDatabase firebaseDatabase;
    private Alumno alumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        til_Id_Alumno = findViewById(R.id.til_ID_Alumno);
        tiet_ID_Alumno = findViewById(R.id.tiet_ID_Alumno);
        til_Password = findViewById(R.id.til_Contraseña);
        tiet_Password = findViewById(R.id.tiet_Contraseña);
        MaterialButton mbtn_Ingresar = findViewById(R.id.mbtn_Ingresar);
        Toolbar mToolBar = findViewById(R.id.myToolBar);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Autenticación");
        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        // Inicio de sesión
        mbtn_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Captura de los datos de entrada
                final String idAlumno = Objects.requireNonNull(tiet_ID_Alumno.getText()).toString();
                final String password = Objects.requireNonNull(tiet_Password.getText()).toString();
                // Validación inicial de campos
                if (TextUtils.isEmpty(idAlumno) || TextUtils.isEmpty(password)) {
                    til_Id_Alumno.setError("Ingresa ID UPAO");
                    til_Password.setError("Ingresa contraseña");
                    return;
                }
                if (idAlumno.length() != 9) {
                    til_Id_Alumno.setError("Longitud de ID UPAO incorrecto");
                    return;
                }
                // Referencia a la base de datos de Alumnos
                DatabaseReference databaseReference = firebaseDatabase.getReference("alumnos");
                // Autenticación de alumno
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        alumno = null;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String ext_id_alumno = Objects.requireNonNull(snapshot.child("id_alumno").getValue()).toString();
                            String ext_password = Objects.requireNonNull(snapshot.child("password").getValue()).toString();
                            if (ext_id_alumno.equals(idAlumno) && ext_password.equals(password)) {
                                alumno = snapshot.getValue(Alumno.class);
                                break;
                            }
                        }
                        if (alumno != null) {
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            intent.putExtra("id_alumno", alumno.getId_alumno());
                            startActivity(intent);
                        } else {
                            showSnackbar(Snackbar.make(findViewById(android.R.id.content), "Datos incorrectos", Snackbar.LENGTH_LONG));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}
