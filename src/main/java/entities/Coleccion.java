package entities;

import lombok.Data;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.util.Comparator;

@Data
public class Coleccion implements Comparable<Coleccion> {

    private int id;
    private String nombre;
    private final MyList<Pelicula> peliculas = new MyLinkedListImpl<>();
    static Comparator<Coleccion> comparator;
    public static final Comparator<Coleccion> INCOME_COMPARATOR = (Coleccion c1, Coleccion c2) -> Long.compare(c1.calcIngresos(),c2.calcIngresos());
    private long income;

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
        setIncome(ingresos);
        return ingresos;
    }


    @Override
    public int compareTo(Coleccion c) {
        return comparator.compare(this, c);
    }
}
