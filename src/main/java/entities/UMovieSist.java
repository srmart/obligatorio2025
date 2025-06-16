package entities;

import lombok.Data;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

@Data
public class UMovieSist {

    private final CargaDeDatos datos;

    public UMovieSist (CargaDeDatos datos){
        this.datos = datos;
    }

    public MyList<String> top5_peliculas_mas_calificadas_por_idioma_original(){
        MyList<String> resultado = new MyLinkedListImpl<>();
        MyHash<String, MyList<Pelicula>> peliculasPorIdioma = new MyHashImpl<>();
        //Calcula la cantidad de calificaciones de todas las peliculas, luego las diferencia por idioma original y se queda con las 5 con mayor
        //cantidad de calificaciones de todos los idiomas (Ingles, frances, italiano, español y portugués)
        MyList<Pelicula> listaDePeliculas = getDatos().getTodasLasPeliculas().values();

        for(int i = 0; i<listaDePeliculas.size(); i++){
            switch (listaDePeliculas.get(i).getIdiomaOriginal()){
                case "en", "es", "fr", "it":
                    if(peliculasPorIdioma.contains(listaDePeliculas.get(i).getIdiomaOriginal())){
                        peliculasPorIdioma.get(listaDePeliculas.get(i).getIdiomaOriginal()).add(listaDePeliculas.get(i));
                    }else{
                        MyList<Pelicula> pelisIngles = new MyLinkedListImpl<>();
                        pelisIngles.add(listaDePeliculas.get(i));
                        peliculasPorIdioma.put(listaDePeliculas.get(i).getIdiomaOriginal(), pelisIngles);
                    }
                    break;

                default:
                    break;
            }
        }



        return resultado;
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
