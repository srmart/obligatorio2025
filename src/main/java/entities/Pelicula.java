package entities;

import lombok.Data;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.util.Comparator;

@Data
public class Pelicula implements Comparable<Pelicula> {

    private int id;
    private String titulo;
    private String idiomaOriginal;
    private long presupuesto;
    private long ingresos;
    private Coleccion coleccion;
    private final MyList<Rating> ratings = new MyLinkedListImpl<>();//todas las evaluaciones realizadas a una pelicula
    private double avgRating;
    private final MyList<String> generos = new MyLinkedListImpl<>();
    //private MyList<Actores> actores;
    public static Comparator<Pelicula> comparator;
    public static final  Comparator<Pelicula> RATING_COMPARATOR = (Pelicula p1, Pelicula p2) -> Integer.compare(p1.cantEvaluacionesPelicula(), p2.cantEvaluacionesPelicula());
    public static final Comparator<Pelicula> AVG_COMPARATOR = (Pelicula p1, Pelicula p2) -> Double.compare(p1.ratingPromedio(),p2.ratingPromedio());

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

    public double ratingPromedio() {
        double rating = 0.0;

        if (this.getRatings().isEmpty()) {
            this.setAvgRating(-1); // no existen evaluaciones
            return -1;
        } else if (this.avgRating > 0) {
            return this.getAvgRating(); // ya fue calculado previamente
        } else {
            int size = getRatings().size();
            for (int i = 0; i < size; i++) {
                rating += this.getRatings().get(i).getRating();
            }

            rating = rating / size;
            this.setAvgRating(rating); // guarda el valor para futuras llamadas
            return rating;
        }
    }


    public int cantEvaluacionesPelicula(){
        return getRatings().size();
    }

    public String toStringFunc1(){
        return "ID: "+this.id +" TITULO: "+ this.titulo +" IDIOMA ORIGINAL: "+this.getIdiomaOriginal()+ " TOTAL DE EVALUACIONES: "+this.cantEvaluacionesPelicula();
    }
    public String toStringFunc2(){
        double avgRating = (getAvgRating()==0) ? ratingPromedio() : getAvgRating();
        return "ID: "+this.id +" TITULO: "+ this.titulo +" PROMEDIO DE EVALUACIONES: "+avgRating;
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
