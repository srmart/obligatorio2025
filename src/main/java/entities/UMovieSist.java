package entities;

import lombok.Data;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.heap.MyHeap;
import uy.edu.um.tad.heap.MyHeapImpl;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

@Data
public class UMovieSist {

    private final CargaDeDatos datos;

    public UMovieSist (CargaDeDatos datos){

        this.datos = datos;
    }

    public void top5_peliculas_mas_calificadas_por_idioma_original(){
        Pelicula.comparator = Pelicula.RATING_COMPARATOR; //seteamos el comparable de pelicula para que compare por cantidad de evaluaciones.
        MyHash<String, MyHeap<Pelicula>> peliculasPorIdioma = new MyHashImpl<>(); //hash donde guardamos por idioma original un heap con las peliculas
        MyList<Pelicula> listaDePeliculas = getDatos().getTodasLasPeliculas().values(); //todas las peliculas
        for(int i = 0; i<listaDePeliculas.size(); i++){
            switch (listaDePeliculas.get(i).getIdiomaOriginal()){ //switch case sobre el idioma de la pelicula en cada iteraciòn
                case "en", "es", "fr", "it":
                    if(peliculasPorIdioma.contains(listaDePeliculas.get(i).getIdiomaOriginal())){//existe el heap de peliculas en este idioma, agrego la pelicula.
                        peliculasPorIdioma.get(listaDePeliculas.get(i).getIdiomaOriginal()).insert(listaDePeliculas.get(i));
                    }else{ //no existe el heap, la creo y agrego la pelicula
                        MyHeap<Pelicula> peliculas = new MyHeapImpl<>();
                        peliculas.insert(listaDePeliculas.get(i));
                        peliculasPorIdioma.put(listaDePeliculas.get(i).getIdiomaOriginal(), peliculas);
                    }

                    break;

                default:
                    break;
            }
        }
        //IMPRIMIR PELICULAS
        int largo = 5;  //set top 5
        for(int i=0; i<largo; i++){
            System.out.println(peliculasPorIdioma.get("en").delete().toStringFunc1());
        }
        for(int i=0; i<largo; i++){
            System.out.println(peliculasPorIdioma.get("es").delete().toStringFunc1());
        }
        for(int i=0; i<largo; i++){
            System.out.println(peliculasPorIdioma.get("fr").delete().toStringFunc1());
        }
        for(int i=0; i<largo; i++){
            System.out.println(peliculasPorIdioma.get("it").delete().toStringFunc1());
        }
    }

    public MyList<String> top10_peliculas_con_mayor_calificacion(){
        MyList<String> resultado = new MyLinkedListImpl<>();
        //Ordena el top 10 de peliculas con mayor calificacion y lo devuelve
        return resultado;
    }

    public MyList<String> top5_colecciones_con_mayores_ingresos(){
        MyList<String> resultado = new MyLinkedListImpl<>();
        //Los ingresos se calculan a nivel de pelicula, para calcular los ingresos totales de una colección se suman estos.
        //
        return resultado;
    }

}
