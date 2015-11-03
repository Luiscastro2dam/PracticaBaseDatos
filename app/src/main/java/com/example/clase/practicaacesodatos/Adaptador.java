package com.example.clase.practicaacesodatos;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Clase on 18/10/2015.
 */
public class Adaptador extends ArrayAdapter<Contacto> {
    private Context contexto;
    private int res;
    private LayoutInflater inflador;
    private List<Contacto> personas;//donde metemos todos los contactos
    /*constructor*/
    public Adaptador(Context contexto, int res, List<Contacto> personas) {
        super(contexto, res, personas);
        this.contexto = contexto;
        this.personas = personas;
        this.res = res;
        inflador= (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class guardaLista{
        public TextView tv1;
        public TextView tv2;
        public ImageView ivFoto;
        public ImageView ivNum;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        guardaLista gv = new guardaLista();
        if(convertView==null){
            convertView = inflador.inflate(res,null);
            TextView tv1 = (TextView) convertView.findViewById(R.id.tvNom);
            gv.tv1 = tv1;
            TextView tv2 = (TextView) convertView.findViewById(R.id.tvTelefono);
            gv.tv2 = tv2;
            ImageView ivFoto = (ImageView) convertView.findViewById(R.id.imgFoto);
            gv.ivFoto=ivFoto;
            ImageView ivNum = (ImageView) convertView.findViewById(R.id.imgTelefonos);
            gv.ivFoto=ivNum;
            convertView.setTag(gv);
        } else {
            gv = (guardaLista) convertView.getTag();
        }
        gv.tv1.setText(personas.get(position).getNombre());
        gv.tv2.setText(personas.get(position).untelefono(0));//coje el primero

        return convertView;
    }
    /*public void img(View v){
        int pos= v.getId();
        Contacto a = Main.devolverContacto(pos);
        String s="Nums de"+a.getNombre()+":\n";
        s+=a.getNumeros();
        AlertDialog.Builder dialogo = new AlertDialog.Builder(contexto);
        dialogo.setMessage(s);
        dialogo.setCancelable(true);
        dialogo.setPositiveButton("salir", null);
        AlertDialog muestra = dialogo.create();
        muestra.show();
    }
     */
    //--------ordenaciones---------------------------
    public void desc() {
        Collections.reverse(personas);
        this.notifyDataSetChanged();
    }

    public void asc() {
        Collections.sort(personas);
        this.notifyDataSetChanged();
    }

      }




