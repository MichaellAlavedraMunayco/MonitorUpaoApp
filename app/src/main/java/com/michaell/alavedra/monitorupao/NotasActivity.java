package com.michaell.alavedra.monitorupao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michaell.alavedra.monitorupao.models.Componente;
import com.michaell.alavedra.monitorupao.utils.NotasAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.michaell.alavedra.monitorupao.methods.Methods.getSemestre;
import static com.michaell.alavedra.monitorupao.methods.Methods.showSnackbar;

public class NotasActivity extends AppCompatActivity {

    private TabLayout tlCursos;
    private ListView lvNotas;
    private SwipeRefreshLayout srlNotas;
    private FirebaseDatabase firebaseDatabase;
    private List<Componente> listaComponentes;
    private TabLayout.Tab tabCurso;
    private String semestre;
    private NotasAdapter notasAdapter;
    private String id_alumno;
    private Toolbar mToolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);
        tlCursos = findViewById(R.id.tlCursos);
        lvNotas = findViewById(R.id.lvNotas);
        srlNotas = findViewById(R.id.srlNotas);
        mToolbar = findViewById(R.id.myToolBar);
        //
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        //
        notasAdapter = new NotasAdapter(this);
        lvNotas.setAdapter(notasAdapter);
        // Obtener datos del alumno
        id_alumno = getIntent().getStringExtra("id_alumno");
        // Obtener instancia de la base de datos
        firebaseDatabase = FirebaseDatabase.getInstance();
        // Validar si el alumno tiene notas en la base de datos
        final DatabaseReference refNotas = firebaseDatabase.getReference("notas");
        semestre = getSemestre();
        refNotas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapNot) {
                for (final DataSnapshot snapSem : dataSnapNot.getChildren()) {
                    if (Objects.requireNonNull(snapSem.child("id_alumno").getValue()).toString().equals(id_alumno)
                            && snapSem.hasChild(semestre)) {
                        // Llenado dinámico del menu
                        snapSem.child(semestre).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapSem) {
                                for (DataSnapshot snapCurso : dataSnapSem.getChildren()) {
                                    String id_curso = snapCurso.getKey();
                                    tlCursos.addTab(tlCursos.newTab().setText(id_curso));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        return;
                    } else {
                        // Vista a la activity de ayuda para que el alumno ingrese datos por la pagina web
                        showSnackbar(Snackbar.make(findViewById(android.R.id.content), "Datos inexistentes", Snackbar.LENGTH_LONG));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        srlNotas.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (tabCurso != null) {
                    loadData(tabCurso);
                }
            }
        });
        tlCursos.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                srlNotas.setRefreshing(true);
                tabCurso = tab;
                loadData(tabCurso);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_notas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itmActualizar:
                if (notasAdapter.getComponentes().size() > 0) {
                    final String id_curso = Objects.requireNonNull(tabCurso.getText()).toString();
                    final DatabaseReference refNotas = firebaseDatabase.getReference("notas");
                    refNotas.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapNotas) {
                            for (final DataSnapshot snapSem : dataSnapNotas.getChildren()) {
                                if (Objects.requireNonNull(snapSem.child("id_alumno").getValue()).toString().equals(id_alumno)
                                        && snapSem.hasChild(semestre)) {
                                    String prev_comp_id = "";
                                    for (int i = 0; i < notasAdapter.getComponentes().size(); i++) {
                                        Componente comp = notasAdapter.getComponentes().get(i);
                                        if (i == 0) {
                                            refNotas.child(snapSem.getKey() + "/" + semestre + "/" + id_curso + "/promocional").setValue(comp.getPromocional());
                                        } else {
                                            String nota = comp.getNota();
                                            if (comp.esComponente()) {
                                                prev_comp_id = comp.getCodigo();
                                                refNotas.child(snapSem.getKey() + "/" + semestre + "/" + id_curso + "/" + prev_comp_id + "/nota").setValue(nota);
                                            } else {
                                                String id_sub = comp.getCodigo();
                                                refNotas.child(snapSem.getKey() + "/" + semestre + "/" + id_curso + "/" + prev_comp_id + "/" + id_sub + "/nota").setValue(nota);
                                            }
                                        }
                                    }
                                    showSnackbar(Snackbar.make(findViewById(android.R.id.content), "Se actualizaron las notas del curso " + id_curso, Snackbar.LENGTH_LONG));
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                break;
            case R.id.itmInformacionNotas:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void loadData(TabLayout.Tab tabCurso) {
        final String id_curso = Objects.requireNonNull(tabCurso.getText()).toString();
        listaComponentes = new ArrayList<>();
        DatabaseReference refCursos = firebaseDatabase.getReference("cursos");
        refCursos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapCurso : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(snapCurso.child("id_curso").getValue()).toString().equals(id_curso)) {
                        // Nombre del curso
                        String nombreCurso = Objects.requireNonNull(snapCurso.child("nombre").getValue()).toString();
                        // Modificar el titulo del actionbar
                        actionBar.setTitle(nombreCurso);
                        int i = 0;
                        while (snapCurso.hasChild("componente_" + (++i))) {
                            String id_componente = Objects.requireNonNull(snapCurso.child("componente_" + i + "/id_componente").getValue()).toString();
                            String descripcionComp = Objects.requireNonNull(snapCurso.child("componente_" + i + "/descripcion").getValue()).toString();
                            String pesoComp = Objects.requireNonNull(snapCurso.child("componente_" + i + "/peso").getValue()).toString();
                            listaComponentes.add(new Componente(id_componente, descripcionComp, pesoComp, true));
                            int j = 1;
                            while (snapCurso.child("componente_" + i).hasChild("subcomponente_" + (j))) {
                                String id_subcomponente = Objects.requireNonNull(snapCurso.child("componente_" + i + "/subcomponente_" + j + "/id_subcomponente").getValue()).toString();
                                String descripcionSub = Objects.requireNonNull(snapCurso.child("componente_" + i + "/subcomponente_" + j + "/descripcion").getValue()).toString();
                                String pesoSub = Objects.requireNonNull(snapCurso.child("componente_" + i + "/subcomponente_" + j + "/peso").getValue()).toString();
                                listaComponentes.add(new Componente(id_subcomponente, descripcionSub, pesoSub, false));
                                j++;
                            }
                        }
                        // Obtener las notas respectivas
                        DatabaseReference refNotas = firebaseDatabase.getReference("notas");
                        refNotas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapNot) {
                                ArrayList<Componente> temp = new ArrayList<>();
                                for (final DataSnapshot snapSem : dataSnapNot.getChildren()) {
                                    if (Objects.requireNonNull(snapSem.child("id_alumno").getValue()).toString().equals(id_alumno) && snapSem.hasChild(semestre)) {
                                        Componente cabecera = new Componente(Objects.requireNonNull(snapSem.child(semestre + "/" + id_curso + "/promocional").getValue()).toString());
                                        temp.add(cabecera);
                                        temp.addAll(listaComponentes);
                                        String id_prev = "";
                                        for (int i = 1; i < temp.size(); i++) {
                                            Componente comp = temp.get(i);
                                            if (comp.esComponente()) {
                                                id_prev = comp.getCodigo();
                                                String notaComp = Objects.requireNonNull(snapSem.child(semestre + "/" + id_curso + "/" + comp.getCodigo() + "/nota").getValue()).toString();
                                                comp.setNota(notaComp);

                                            } else {
                                                String notaSub = Objects.requireNonNull(snapSem.child(semestre + "/" + id_curso + "/" + id_prev + "/" + comp.getCodigo() + "/nota").getValue()).toString();
                                                comp.setNota(notaSub);
                                            }
                                            temp.set(i, comp);
                                        }
                                        notasAdapter.clear();
                                        notasAdapter.setComponentes(temp);
                                        srlNotas.setRefreshing(false);
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
