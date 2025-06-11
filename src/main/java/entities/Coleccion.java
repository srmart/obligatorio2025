package entities;

import lombok.Data;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

@Data
public class Coleccion {

    private int id;
    private String nombre;
    private final MyList<Pelicula> peliculas = new MyLinkedListImpl<>();

    public Coleccion(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
    public long calcIngresos(){
        long ingresos = 0;
        //Toma la suma de ganancias de todas las peliculas y las retorna.
        for(int i = 0; i< peliculas.size(); i++){
            ingresos += peliculas.get(i).gananciasGeneradas();
        }
        return ingresos;
    }


}
