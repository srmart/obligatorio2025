package entities;

import lombok.Data;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.util.Comparator;

@Data
public class Pelicula implements Comparable<Pelicula>{

    private int id;
    private String titulo;
    private String idiomaOriginal;
    private long presupuesto;
    private long ingresos;
    private Coleccion coleccion;
    private final MyList<Rating> ratings = new MyLinkedListImpl<>(); //todas las evaluaciones realizadas a una pelicula
    //private MyList<Actores> actores;
    static Comparator<Pelicula> comparator;


    public Pelicula(int id, String titulo, String idiomaOriginal, long presupuesto, long ingresos) {
        this.id = id;
        this.titulo = titulo;
        this.idiomaOriginal = idiomaOriginal;
        this.presupuesto = presupuesto;
        this.ingresos = ingresos;
    }

    public long gananciasGeneradas(){
        long ganancias = 0;
        //Se debe hacer la diferencia entre los ingresos y el presupuesto.
        ganancias = ingresos - presupuesto;
        return ganancias;
    }

    public double ratingPromedio(){
        double rating = 0.0;
        //Va a recorrer la "lista" de ratings sumandolos, luego divide por el size de la lista.
        for(int i = 0; i < this.getRatings().size() ; i++){
            rating += this.getRatings().get(i).getRating();
        }
        rating = rating / this.getRatings().size();
        return rating;
    }

    public int cantEvaluacionesPelicula(int idPelicula){
        int result = 0;
        //Recorre la lista de ratings y para un mismo idPelicula suma 1 el resultado
        for(int i = 0; i<getRatings().size(); i++){
            result += 1;
        }
        return result;
    }

    public static Comparator<Pelicula> porCantRating(){
        return (Pelicula p1, Pelicula p2) -> Integer.compare(p2.cantEvaluacionesPelicula(p2.getId()), p1.cantEvaluacionesPelicula(p1.getId()));
    }
    public Comparator<Pelicula> avgRating() {
        return (Pelicula p1, Pelicula p2) -> Double.compare(p2.ratingPromedio(),p1.ratingPromedio());
    }

    public String toStringFunc1(){
        return "ID: "+this.id +" TITULO: "+ this.titulo +" IDIOMA ORIGINAL: "+this.getIdiomaOriginal()+ " TOTAL DE EVALUACIONES: "+this.cantEvaluacionesPelicula(this.id);
    }



    @Override
    public int compareTo(Pelicula p) {
        return comparator.compare(this, p);
    }





    //este metodo se debería implementar en el sistema, donde hay un hash de calificaciones por usuario.
//    public int cantEvaluacionesUser(int idUsuario){
//        int result = 0;
//        //Recorre la lista de ratings y para un mismo idUsuario suma 1 el resultado
//        return result;
//    }



}
