package entities;

import lombok.Data;

@Data
public class Pelicula {

    private int id;
    private String titulo_original;
    private int presupuesto;
    private int ingresos;
    //private Coleccion perteneceA;
    //private final MyList<Rating> ratings;
    //private final MyList<Actores> actores;


    public Pelicula(int id, String titulo_original, int presupuesto, int ingresos) {
        this.id = id;
        this.titulo_original = titulo_original;
        this.presupuesto = presupuesto;
        this.ingresos = ingresos;
    }

    public int gananciasGeneradas(){
        int ganancias = 0;
        //Se debe hacer la diferencia entre los ingresos y el presupuesto.
        return ganancias;
    }

    public double ratingPromedio(){
        double rating = 0.0;
        //Va a recorrer la "lista" de ratings sumandolos, luego divide por el size de la lista.
        return rating;
    }

    public int cantEvaluaciones(int idPelicula){
        int result = 0;
        //Recorre la lista de ratings y para un mismo idPelicula suma 1 el resultado
        return result;
    }

    public int cantEvaluacionesUser(int idUsuario){
        int result = 0;
        //Recorre la lista de ratings y para un mismo idUsuario suma 1 el resultado
        return result;
    }

}
