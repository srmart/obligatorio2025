package entities;

import lombok.Data;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

@Data
public class Pelicula {

    private int id;
    private String titulo;
    private long presupuesto;
    private long ingresos;
    private Coleccion coleccion;
    private MyList<Rating> ratings = new MyLinkedListImpl<>(); //todas las evaluaciones realizadas a una pelicula
    //private MyList<Actores> actores;


    public Pelicula(int id, String titulo, long presupuesto, long ingresos) {
        this.id = id;
        this.titulo = titulo;
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

    //este metodo se debería implementar en el sistema, donde hay un hash de calificaciones por usuario.
    public int cantEvaluacionesUser(int idUsuario){
        int result = 0;
        //Recorre la lista de ratings y para un mismo idUsuario suma 1 el resultado
        return result;
    }

}
