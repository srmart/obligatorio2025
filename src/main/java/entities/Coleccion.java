package entities;

import lombok.Data;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

@Data
public class Coleccion {

    private int id;
    private String nombre;
    private int PRUEBA;
    private MyList<Pelicula> peliculas = new MyLinkedListImpl<>();

    public int calcIngresos(){
        int ingresos = 0;
        //Toma la suma de ganancias de todas las peliculas y las retorna.
        return ingresos;
    }


}
