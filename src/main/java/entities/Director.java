package entities;

import lombok.Data;

import uy.edu.um.tad.binarytree.MySearchBinaryTreeImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.util.Comparator;

@Data
public class Director implements Comparable<Director>{

    private int id;
    private String nombre;
    private int cantidadDePeliculas = 0;
    private double mediana = 0;
    private final MySearchBinaryTreeImpl<Double, Pelicula> peliculasPorCalificacion = new MySearchBinaryTreeImpl<>();
    public static Comparator<Director> comparator;
    public static final Comparator<Director> MEDIANA = Comparator.comparingDouble(Director::getMediana);
//    public static final Comparator<Director> MEDIANA = (Director d1, Director d2) -> Double.compare(d1.mediana(),d2.mediana());


    public Director(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public void agregarPelicula(Pelicula pelicula) {//metodo para agregar peliculas al arbol del director
        double avg = pelicula.getAvgRating();
        if (avg >= 0) {
            peliculasPorCalificacion.add(avg, pelicula);
        }
    }


    public void mediana() {
        MyList<Pelicula> pelisOrdenadas = peliculasPorCalificacion.inOrderValues();
        int size = pelisOrdenadas.size();
        if (size == 0) {
            setMediana(0);
        } else if (size % 2 == 1) {
            setMediana(pelisOrdenadas.get(size / 2).getAvgRating());
        } else {
            double avg1 = pelisOrdenadas.get(size / 2 - 1).getAvgRating();
            double avg2 = pelisOrdenadas.get(size / 2).getAvgRating();
            setMediana((avg1 + avg2) / 2.0);
        }
    }



    public String toStringDir(){
        return "NOMBRE DEL DIRECTOR: "+this.nombre +" CANTIDAD DE PELICULAS: "+ this.cantidadDePeliculas +" MEDIANA DE SU CALIFICACIÓN: "+this.mediana;
    }




    @Override
    public int compareTo(Director d) {
        return comparator.compare(this, d);
    }
}
