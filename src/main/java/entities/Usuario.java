package entities;

import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;

public class Usuario {

    private int id;
    private MyHash<Integer, Genero> generos = new MyHashImpl<>(60000); //generos por id
    int cantidadRatings;

    public Usuario(int id){
        this.id = id;
    }


}
