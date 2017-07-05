package uxarmy.uidemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uxarmy.uidemo.R;
import uxarmy.uidemo.dao.ColorPalette;

/**
 * Created by user on 05/07/17.
 */

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ColorPalette> colors;

    public ColorAdapter(Context context, ArrayList<ColorPalette> colors) {
        this.context = context;
        this.colors = colors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.palette_row, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ColorPalette colorPalette = colors.get(position);
        if (colorPalette.paletteSwatch != null) {

            holder.viewPalette.setBackgroundColor(colorPalette.paletteSwatch.getRgb());
        } else {
            holder.viewPalette.setBackgroundColor(colorPalette.colorCode);
        }
        holder.viewPalette.setTag(colorPalette);
        holder.viewPalette.setOnClickListener(((View.OnClickListener) context));
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View viewPalette;

        ViewHolder(View itemView) {
            super(itemView);
            viewPalette = itemView.findViewById(R.id.viewPalette);
        }
    }
}
