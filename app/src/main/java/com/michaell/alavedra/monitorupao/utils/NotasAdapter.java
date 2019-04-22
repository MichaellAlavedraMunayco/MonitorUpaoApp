package com.michaell.alavedra.monitorupao.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.michaell.alavedra.monitorupao.R;
import com.michaell.alavedra.monitorupao.models.Componente;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

public class NotasAdapter extends BaseAdapter {

    private List<Componente> componentes = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final Context context;

    public NotasAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return componentes.size();
    }

    @Override
    public Componente getItem(int i) {
        return componentes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void clear() {
        this.componentes.clear();
    }

    public void setComponentes(List<Componente> componentes) {
        this.componentes = componentes;
        calcularNotas();
        notifyDataSetChanged();
    }

    public List<Componente> getComponentes() {
        return this.componentes;
    }

    private int getColorPorNota(String nota) {
        // 0 - 10.4 rojo
        // 10.5 - 14 amarillo
        // 15 - 17 verde
        // 18-20 azul
        double Nota = Double.parseDouble(nota);
        int color;
        if (Nota < 10.5) {
            color = context.getResources().getColor(R.color.option_analisis);
        } else if (Nota < 15) {
            color = context.getResources().getColor(R.color.option_notas);
        } else if (Nota < 18) {
            color = context.getResources().getColor(R.color.option_ranking);
        } else {
            color = context.getResources().getColor(R.color.option_historial);
        }
        return color;
    }

    @SuppressLint("DefaultLocale")
    private void calcularNotas() {
        double promocional = 0;
        double notaComp = 0;
        for (int i = getCount() - 1; i > 0; i--) {
            if (getItem(i).esComponente()) {
                if (notaComp > 0)
                    getItem(i).setNota(String.format("%.2f", notaComp));
                notaComp = 0;
                promocional += Double.parseDouble(getItem(i).getNota()) * (Double.parseDouble(getItem(i).getPeso()) / 100);
            } else {
                notaComp += Double.parseDouble(getItem(i).getNota()) * (Double.parseDouble(getItem(i).getPeso()) / 100);
            }
        }
        getItem(0).setPromocional(String.format("%.2f", promocional));
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"});
        final Componente componente = componentes.get(i);
        if (i == 0) {
            view = layoutInflater.inflate(R.layout.promocional_view, null);
            TextView txvNotaPromocional = view.findViewById(R.id.txvNotaPromocional);
            txvNotaPromocional.setText(componente.getPromocional());
            txvNotaPromocional.setTextColor(getColorPorNota(componente.getPromocional()));
        } else {
            if (componente.esComponente()) {
                view = layoutInflater.inflate(R.layout.componente_view, null);
                TextView txvCodigoComp = view.findViewById(R.id.txvCodigoComponente);
                TextView txvDescripcionComp = view.findViewById(R.id.txvDescripcionComponente);
                TextView txvPesoComp = view.findViewById(R.id.txvPesoComponente);
                final MaterialBetterSpinner mbsNotaComp = view.findViewById(R.id.mbsNotaComp);
                //
                txvCodigoComp.setText(componente.getCodigo());
                txvDescripcionComp.setText(componente.getDescripcion());
                txvPesoComp.setText(componente.getPeso().concat("%"));
                mbsNotaComp.setAdapter(adapter);
                mbsNotaComp.setText(componente.getNota());
                mbsNotaComp.setTextColor(getColorPorNota(componente.getNota()));
                mbsNotaComp.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        double nota = (TextUtils.isEmpty(s.toString())) ? 0 : Double.parseDouble(s.toString());
                        componente.setNota(String.valueOf(nota));
                        calcularNotas();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else {
                view = layoutInflater.inflate(R.layout.subcomponente_view, null);
                TextView txvCodigoSub = view.findViewById(R.id.txvCodigoSubcomponente);
                TextView txvDescripcionSub = view.findViewById(R.id.txvDescripcionSubcomponente);
                TextView txvPesoSub = view.findViewById(R.id.txvPesoSubcomponente);
                final MaterialBetterSpinner mbsNotaSub = view.findViewById(R.id.mbsNotaSub);
                //
                txvCodigoSub.setText(componente.getCodigo());
                txvDescripcionSub.setText(componente.getDescripcion());
                txvPesoSub.setText(componente.getPeso().concat("%"));
                mbsNotaSub.setAdapter(adapter);
                mbsNotaSub.setText(componente.getNota());
                mbsNotaSub.setTextColor(getColorPorNota(componente.getNota()));
                mbsNotaSub.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        double nota = (TextUtils.isEmpty(s.toString())) ? 0 : Double.parseDouble(s.toString());
                        componente.setNota(String.valueOf(nota));
                        calcularNotas();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
        return view;
    }
}
