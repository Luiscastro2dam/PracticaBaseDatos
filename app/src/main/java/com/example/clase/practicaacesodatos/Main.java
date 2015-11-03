package com.example.clase.practicaacesodatos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Main extends AppCompatActivity {

    private static List<Contacto> personas;
    private static Adaptador cl;
    private ListView lv;
    private SharedPreferences prefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.iniciar();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //para cargar el menu de la barra de arriba
        getMenuInflater().inflate(R.menu.menu_barra, menu);
        return true;
    }

    public int devolverid() {
        return personas.size() + 1;
    }
//////////////---------------------------------//////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Contacto contacto = (Contacto) data.getExtras().getSerializable("contacto");
                for (Contacto c : personas) {
                    if (c.getId() == contacto.getId()) {
                        c.setNumeros(contacto.getNumeros());
                        c.setNombre(contacto.getNombre());
                    }
                }
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Contacto contacto = (Contacto) data.getExtras().getSerializable("contacto");
                personas.add(contacto);
            }
        }
        cl.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.anadir: {
                Intent i = new Intent(this, Agregar.class);
                Bundle b = new Bundle();
                Contacto c = new Contacto(); //creacion de nuevo contacto
                b.putSerializable("contacto", c);
                i.putExtras(b);
                startActivityForResult(i, 2);//mirar
                return true;
            }
            case R.id.asc: {
                cl.asc();
                Toast.makeText(this, R.string.m2_OrdenarMayor, Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.descend: {
                cl.desc();
                Toast.makeText(this, R.string.m3_OrdenarMenor, Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.guardar: {
                try {
                    this.escribir();
                    sincro =  new GregorianCalendar().getTime().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, R.string.m4_guar, Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.sincronizar: {
                /////////////////Leer//////////////////////////////////
                try {
                    this.leerCopiaSeguridad();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, R.string.m5_sincro, Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.informacionSincronizar: {
                /////////////////Actualizar Cambios si quieres//////////////////////////////////
                Toast.makeText(this, "Ultima Sincronización " +sincro, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
        //menu de la barra de arriba
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opciones");
        menu.add(0, v.getId(), 0, "Editar");//opciones del menu
        menu.add(0, v.getId(), 0, "Borrar");
        menu.add(0, v.getId(), 0, "LLamar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position;
        if (item.getTitle() == "LLamar") {
            List<String> numeros;
            numeros = personas.get(info.position).getNumeros();
            String tele = numeros.get(0).toString();
            try {
                //  Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tele));
                // startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (item.getTitle() == "Borrar") {
            personas.remove(info.position);//borramos la persona selecionada
            cl.notifyDataSetChanged(); //reseteamos la lista
        } else if (item.getTitle() == "Editar") {
            Intent i = new Intent(this, Editar.class);
            Bundle b = new Bundle();
            Contacto contPaso = personas.get(pos);
            b.putSerializable("contPaso", contPaso);
            i.putExtras(b);
            startActivityForResult(i, 0);
        }
        return true;
    }
    boolean copi;
    int pos;
    String sincro;
    public void iniciar() throws IOException, XmlPullParserException {
        //---preferencias comparticas--------//
        prefer = getSharedPreferences("sincronizacion",this.MODE_PRIVATE);
        final SharedPreferences.Editor ed = prefer.edit();

       if(prefer.getString("tipo","").compareTo("automatica")==0){
            copi=true;
        }


        if(prefer.getString("tipo","").compareTo("")==0) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Sincronizacion Programada");
            alert.setMessage("¿Quieres activar la sincronizacíon automatica?");

            DialogInterface.OnClickListener si = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ed.putString("tipo", "automatica");
            ed.putString("ultima", new GregorianCalendar().getTime().toString());
                    sincro =  new GregorianCalendar().getTime().toString();
                    try {
                        copi=true;
                        escribir();
                    } catch (IOException e) {
                    }
                    ed.commit();
                }
            };
            DialogInterface.OnClickListener no = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ed.putString("tipo", "manual");
                    ed.commit();
                    copi = false;
                    //no hacemos nada por que lo dejamos a eleccion del usuario
                }
            };

            alert.setPositiveButton("si", si);
            alert.setNegativeButton("no", no);
            alert.show();

        }
        lv = (ListView) findViewById(R.id.lvMostrar);
        if(copi==true){
            this.leerCopiaSeguridad();
        }else{

        personas = this.getListaContactos(this);

        for (Contacto aux : personas)
            aux.setNumeros(this.getListaTelefonos(this, aux.getId()));

        cl = new Adaptador(this, R.layout.elementos_lista, personas);
        lv.setAdapter(cl);
        registerForContextMenu(lv);
       /*eventos list*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override //Cuando pulsas con el boton aparece el texto de abajo
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Manten pulsado para Opciones", Toast.LENGTH_LONG).show();
                pos = position;
            }
        });


    }}

    public void imgElTelefonos(View v) {
        int pos = this.pos;
        Contacto a = this.devolverContacto(pos);
        String s = "Nums de" + a.getNombre() + ":\n";
        s += a.getNumeros();
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(s);
        dialogo.setCancelable(true);
        dialogo.setPositiveButton("salir", null);
        AlertDialog muestra = dialogo.create();
        muestra.show();
    }

    public static Contacto devolverContacto(int pos) {
        return personas.get(pos);
    }

    public static void resetear() {
        cl.notifyDataSetChanged();
        cl.asc(); //ordenar
    }

    public static void anadirContactos(Contacto nuevo) {
        personas.add(nuevo);
    }

    public static void borrarContacto(long id) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getId() == id) {
                personas.remove(i);
            }
        }
    }


    //----------------metodos dados para el ejercicico--------------------------------------------------
    public static List<Contacto> getListaContactos(Context contexto) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1", "1"};
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = contexto.getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int indiceNombre = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        List<Contacto> lista = new ArrayList<>();
        Contacto contacto;
        while (cursor.moveToNext()) {
            contacto = new Contacto();
            contacto.setId(cursor.getLong(indiceId));
            contacto.setNombre(cursor.getString(indiceNombre));
            lista.add(contacto);
        }
        return lista;
    }

    /*Teléfonos de un contacto*/
    public static List<String> getListaTelefonos(Context contexto, long id) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String argumentos[] = new String[]{id + ""};
        String orden = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Cursor cursor = contexto.getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        List<String> lista = new ArrayList<>();
        String numero;
        while (cursor.moveToNext()) {
            numero = cursor.getString(indiceNumero);
            lista.add(numero);
        }
        return lista;
    }

    ////////////////////----------Copias de seguridad---------------//////////////////////////
    public void escribir() throws IOException {
        FileOutputStream fosxml = new FileOutputStream(new File(this.getExternalFilesDir(null), "CopiaSeguridad.xml"));
        XmlSerializer docxml = Xml.newSerializer();
        docxml.setOutput(fosxml, "UTF-8");
        docxml.startDocument(null, Boolean.valueOf(true));
        docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        docxml.startTag(null, "contactos");
        for (int i = 0; i < personas.size(); i++) {
            docxml.startTag(null, "contacto");
            docxml.attribute(null, "id", String.valueOf(personas.get(i).getId()));
            docxml.attribute(null, "nombre", String.valueOf(personas.get(i).getNombre()));
            for (int j = 0; j < personas.get(i).getNumeros().size(); j++) {
                docxml.startTag(null, "telefono");
                docxml.text(personas.get(i).getNum(j).toString());
                docxml.endTag(null, "telefono");
            }
            docxml.endTag(null, "contacto");
        }
        docxml.endDocument();
        docxml.flush();
        fosxml.close();
    }

    public void leerCopiaSeguridad() throws IOException, XmlPullParserException {
        List<Contacto> personasNuevas = new ArrayList();
        Contacto contac;
        List<String> nume = new ArrayList<>();
        String nombres = "";
        XmlPullParser lectorxml = Xml.newPullParser();
        lectorxml.setInput(new FileInputStream(new File(this.getExternalFilesDir(null), "CopiaSeguridad.xml")), "utf-8");
        int evento = lectorxml.getEventType(), id = 0;
        while (evento != XmlPullParser.END_DOCUMENT) {
            if (evento == XmlPullParser.START_TAG) {
                String etiqueta = lectorxml.getName();
                if (etiqueta.compareTo("contacto") == 0) {
                    nume = new ArrayList<>();
                    id = Integer.parseInt(lectorxml.getAttributeValue(null, "id"));
                    nombres = lectorxml.getAttributeValue(null, "nombre");
                } else if (etiqueta.compareTo("telefono") == 0) {
                    String texto = lectorxml.nextText();
                    nume.add(texto);
                }
            }
            if (evento == XmlPullParser.END_TAG) {
                String etiqueta = lectorxml.getName();
                if (etiqueta.compareTo("contacto") == 0) {
                    contac = new Contacto(id, nombres, nume);
                    personasNuevas.add(contac);
                }
            }
            evento = lectorxml.next();
        }
        //rompemos adaptador y abrimos otro nuevo
        cl = new Adaptador(this, R.layout.elementos_lista, personasNuevas);
        lv.setAdapter(cl);
        personas=personasNuevas; //igualamos los arrays
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                Toast.makeText(getApplicationContext(), "Manten pulsado para Opciones", Toast.LENGTH_LONG).show();
            }
        });
        registerForContextMenu(lv);//registramos nuestro menu contextual

    }

/////////-----------------Cuando cierra el Programa------------------------
//guardamos una copia total con todos los archivos
   @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(copi==true) {
                this.escribir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



