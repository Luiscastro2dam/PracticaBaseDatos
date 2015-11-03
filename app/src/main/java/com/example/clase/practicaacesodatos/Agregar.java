package com.example.clase.practicaacesodatos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Clase on 01/11/2015.
 */
public class Agregar extends AppCompatActivity{
    Contacto contacto;
    Main a;
    private EditText etAgnom,etAgtel,etAgtel2,etAgtel3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_contactos);
        contacto = (Contacto) this.getIntent().getExtras().getSerializable("contacto");

        etAgnom = (EditText)findViewById(R.id.etAgnom);
        etAgtel = (EditText)findViewById(R.id.etAgtel);
        etAgtel2 = (EditText)findViewById(R.id.etAgtel2);
        etAgtel3 = (EditText)findViewById(R.id.etAgtel3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_barra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    //--------------metedo del boton de agregar----------------------//
    public void agregarContactos(View v){
        ArrayList<String> numeros=new ArrayList();//para meter los numeros

        String nombre=etAgnom.getText().toString();
        String telefono = etAgtel.getText().toString();
        String telefono2 = etAgtel2.getText().toString();
        String telefono3 = etAgtel3.getText().toString();
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
            Random r=new Random();
            long id=r.nextInt(100)+1;
            Contacto nuevo=new Contacto((long) id, nombre, numeros);
            Main.anadirContactos(nuevo);
            Main.resetear();
            Toast.makeText(this,"Contacto añadido",Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this,"Campos vacios rellenalos",Toast.LENGTH_LONG).show();
        }

    }
    //--------------metodo del boton de cancelar---------------------//
    public void salirAgregar(View v){
        finish();
    }
}
