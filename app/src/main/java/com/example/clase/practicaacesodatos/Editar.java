package com.example.clase.practicaacesodatos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Clase on 01/11/2015.
 */
public class Editar extends AppCompatActivity {
    private Contacto contacto;
    private EditText etEdnom,etEdtel,etEdtel2,etEdtel3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_contactos);
        etEdnom = (EditText)findViewById(R.id.etEdnom);
        etEdtel = (EditText)findViewById(R.id.etEdtel);
        etEdtel2 = (EditText)findViewById(R.id.etEdtel2);
        etEdtel3 = (EditText)findViewById(R.id.etEdtel3);

        Intent i=this.getIntent();
        Bundle b=i.getExtras();
        contacto= (Contacto) b.getSerializable("contPaso");
        etEdnom.setText(contacto.getNombre());
        List<String> nume = contacto.getNumeros();
        for(int t =0;t<nume.size();t++){
            System.out.println(nume.get(t).toString()+" ----"+t);
            if(t==0)
            etEdtel.setText(nume.get(t).toString());
            if(t==1)
                etEdtel2.setText(nume.get(t).toString());
                if(t==2)
                    etEdtel3.setText(nume.get(t).toString());
        }


    }
    public  void btEtModificar(View v){
        ArrayList<String> numeros=new ArrayList();//para meter los numeros

        String nombre=etEdnom.getText().toString();
        String telefono = etEdtel.getText().toString();
        String telefono2 = etEdtel2.getText().toString();
        String telefono3 = etEdtel3.getText().toString();
        if(!nombre.isEmpty() && !telefono.isEmpty()){
            contacto.setNombre(nombre);
            numeros.add(telefono);
            if(telefono2.isEmpty()){
                numeros.add(" ");
            }else{
                numeros.add(telefono2);
            }
            if(telefono3.isEmpty()){
                numeros.add(" ");
            }else{
                numeros.add(telefono3);
            }
            contacto.setNumeros(numeros);

            long id=contacto.getId();
            long id2=id;
            Main.borrarContacto(id);
            Contacto nuevo=new Contacto((long) id2, nombre, numeros);
            Main.anadirContactos(nuevo);
            Main.resetear();
            Toast.makeText(this, "Contacto Modificado", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this,"Campos vacios rellenalos",Toast.LENGTH_LONG).show();
        }

    }
    public void btEtSubirImagen(View v){

    }
    public void btEtsalir(View v){
        finish();
    }
}
