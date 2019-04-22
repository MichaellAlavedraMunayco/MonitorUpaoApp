package com.michaell.alavedra.monitorupao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.michaell.alavedra.monitorupao.NotasActivity;
import com.michaell.alavedra.monitorupao.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewMenuAdapter extends RecyclerView.Adapter<RecyclerViewMenuAdapter.MenuViewHolder> {

    private final List<MenuItem> menuItems;
    private final Context context;
    private final String id_alumno;

    public RecyclerViewMenuAdapter(Context context, String id_alumno) {
        this.context = context;
        this.id_alumno = id_alumno;
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Notas", "Visualiza tus notas", R.color.option_notas, R.drawable.ic_format_list_numbered_white_24dp, new NotasActivity()));
        menuItems.add(new MenuItem("Análisis", "Visualizas las gráficas e indicadores de tu rendimiento", R.color.option_analisis, R.drawable.ic_assignment_ind_white_24dp, new NotasActivity()));
        menuItems.add(new MenuItem("Historial", "Visualizas tu histórico de rendimiento", R.color.option_historial, R.drawable.ic_library_books_white_24dp, new NotasActivity()));
        menuItems.add(new MenuItem("Ranking", "Visualiza la posición en la que te encuentras en relación a tus compañeros", R.color.option_ranking, R.drawable.ic_assessment_white_24dp, new NotasActivity()));
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int i) {
        final MenuItem menuItem = menuItems.get(i);
        final int color = context.getResources().getColor(menuItem.color);
        menuViewHolder.imvIcon.setImageResource(menuItem.icon);
        menuViewHolder.imvIcon.setBackgroundColor(color);
        menuViewHolder.txvTitulo.setText(menuItem.titulo);
        menuViewHolder.lyBackDescripcion.setBackgroundColor(color);
        menuViewHolder.txvDescripcion.setText(menuItem.descripcion);
        menuViewHolder.mcvItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, menuItem.activity.getClass());
                intent.putExtra("id_alumno", id_alumno);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        final MaterialCardView mcvItemMenu;
        final ImageView imvIcon;
        final TextView txvTitulo;
        final LinearLayout lyBackDescripcion;
        final TextView txvDescripcion;

        MenuViewHolder(View view) {
            super(view);
            this.mcvItemMenu = view.findViewById(R.id.mcvItemMenu);
            this.imvIcon = view.findViewById(R.id.imvIcono);
            this.txvTitulo = view.findViewById(R.id.txvTitulo);
            this.lyBackDescripcion = view.findViewById(R.id.lyBackDescripcion);
            this.txvDescripcion = view.findViewById(R.id.txvDescripcion);
        }
    }

    class MenuItem {
        final String titulo;
        final String descripcion;
        final int color;
        final int icon;
        final Activity activity;

        MenuItem(String titulo, String descripcion, int color, int icon, Activity activity) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.color = color;
            this.icon = icon;
            this.activity = activity;
        }
    }

}
