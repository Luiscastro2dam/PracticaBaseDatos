package com.example.clase.practicaacesodatos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Clase on 18/10/2015.
 */
public class Contacto implements Serializable,Comparable<Contacto>{

    private long id; //localizador
    private String nombre;
    private List<String> numeros;

    /*Constructores*/
    public Contacto() {
    }

    public Contacto(long id, String nombre, List<String> numeros) {
        this.id = id;
        this.nombre = nombre;
        this.numeros = numeros;
    }
    public String getNum(int pos) {
        return numeros.get(pos);
    }
    public int getSizeNumeros(){
        return numeros.size();
    }
    public String getNumtel(int pos) {
        return numeros.get(pos);
    }
    /*get y setter*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getNumeros() {
        return numeros;
    }

    public String untelefono(int pos){
        return   numeros.get(pos).toString();
    }

    public void setNumeros(List<String> numeros) {
        this.numeros = numeros;
    }
    /* to string para mostrar*/

    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", numeros=" + numeros +
                '}';
    }
    /* comparable para ordenar*/
    @Override
    public int compareTo(Contacto o) {
        int res = this.nombre.compareTo(o.nombre);
        if(res == 0){
            res = (int) (this.id-o.id);
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contacto contacto = (Contacto) o;

        if (id != contacto.id) return false;
        if (nombre != null ? !nombre.equals(contacto.nombre) : contacto.nombre != null)
            return false;
        return !(numeros != null ? !numeros.equals(contacto.numeros) : contacto.numeros != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (numeros != null ? numeros.hashCode() : 0);
        return result;
    }
}



