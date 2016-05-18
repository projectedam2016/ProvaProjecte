package com.example.alumne.provaprojecte;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alumne on 12/05/16.
 */
//Aquesta classe no es troba funcional
public class AdapterMarked extends ArrayAdapter<Marked> {

    int groupid;

    ArrayList<Marked> marcats;

    Context context;
    public AdapterMarked(Context context, int resource) {
        super(context, resource);
    }



    public AdapterMarked(Context context, int vg, int id, ArrayList<Marked>
            marcats) {

        super(context, vg, id, marcats);

        this.context = context;

        groupid = vg;

        this.marcats = marcats;



    }



    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);
        TextView textName = (TextView) itemView.findViewById(R.id.marked_llibre);
        textName.setText(marcats.get(position).getTitol());
        TextView textUser = (TextView) itemView.findViewById(R.id.marked_user);
        textUser.setText(marcats.get(position).getUsuari());
        TextView textAceptat = (TextView) itemView.findViewById(R.id.marked_aceptat);
        textAceptat.setText(marcats.get(position).getAceptat());
        ImageView image=(ImageView)itemView.findViewById(R.id.marked_imatge);
        if(!marcats.get(position).getImatge().equals(""))
            image.setImageBitmap(BitmapFactory
                    .decodeByteArray(marcats.get(position).getImatge(), 0, marcats.get(position).getImatge().length));
        return itemView;

    }
}
