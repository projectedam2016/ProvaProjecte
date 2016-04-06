package com.example.alumne.provaprojecte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alumne on 06/04/16.
 */
public class NewAdapter extends ArrayAdapter<Llibre>{

    int groupid;

    ArrayList<Llibre> records;

    Context context;
    public NewAdapter(Context context, int resource) {
        super(context, resource);
    }



    public NewAdapter(Context context, int vg, int id, ArrayList<Llibre>
            records) {

        super(context, vg, id, records);

        this.context = context;

        groupid = vg;

        this.records = records;



    }



    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);
        TextView textName = (TextView) itemView.findViewById(R.id.llibre_name);
        textName.setText(records.get(position).getNom());
        TextView textPrice = (TextView) itemView.findViewById(R.id.llibre_autor);
        textPrice.setText(records.get(position).getAutor());

        return itemView;

    }
}
