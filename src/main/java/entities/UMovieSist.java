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
    private final MyHash<String, MyHeap<Pelicula>> peliculasPorIdioma = new MyHashImpl<>(); //hash donde guardamos por idioma original un heap con las peliculas(para funcion 1)
    private final MyHeap<Pelicula> ordenarPeliculas = new MyHeapImpl<>(); //heap donde guardo las peliculas para ordenarlas

    public UMovieSist (CargaDeDatos datos){

        this.datos = datos;
        peliculasPorIdioma.put("en", new MyHeapImpl<>());
        peliculasPorIdioma.put("es", new MyHeapImpl<>());
        peliculasPorIdioma.put("fr", new MyHeapImpl<>());
        peliculasPorIdioma.put("it", new MyHeapImpl<>());
    }

    public void top5_peliculas_mas_calificadas_por_idioma_original(){
        long inicio = System.currentTimeMillis();
        Pelicula.comparator = Pelicula.RATING_COMPARATOR; //seteamos el comparable de pelicula para que compare por cantidad de evaluaciones.

        // vacíar los heaps antes de reutilizarlos
        for (String idioma : new String[]{"en", "es", "fr", "it"}){
            MyHeap<Pelicula> heap = peliculasPorIdioma.get(idioma);
            while (heap.size()!=0) {
                heap.delete();  // vaciar
            }
        }

        MyList<Pelicula> listaDePeliculas = getDatos().getTodasLasPeliculas().values(); //todas las peliculas
        for(int i = 0; i<listaDePeliculas.size(); i++){
            Pelicula p = listaDePeliculas.get(i);
            String idioma = p.getIdiomaOriginal();
            switch (idioma){ //switch case sobre el idioma de la pelicula en cada iteraciòn
                case "en", "es", "fr", "it":
                    peliculasPorIdioma.get(idioma).insert(p); //se agrega la pelicula a su respectivo heap para ordenarse
                default: //el idioma de la pelicula no corresponde con ninguno del hash
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
        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución: "+(fin - inicio)+" ms");
    }



    public void top10_peliculas_con_mayor_calificacion(){
        //Ordena el top 10 de peliculas con mayor calificacion y lo devuelve
        long inicio = System.currentTimeMillis();
        Pelicula.comparator = Pelicula.AVG_COMPARATOR; //seteamos el comparable de pelicula para que compare por nota promedio de evaluaciones.
        MyList<Pelicula> listaDePeliculas = getDatos().getTodasLasPeliculas().values(); //todas las peliculas
        //limpiar el heap para ordenar
        while (ordenarPeliculas.size()!=0) {
            ordenarPeliculas.delete();
        }

        for(int i = 0; i<listaDePeliculas.size(); i++){
            Pelicula p =listaDePeliculas.get(i);
            if(p.getAvgRating()!=-1 && listaDePeliculas.get(i).getRatings().size()>=50) { //se filtran las peliculas cuya cantidad de evaluaciones es menor a 50 para que una pelicula con pocas evaluaciones no opaque otra pelicula muy evaluada
                ordenarPeliculas.insert(p);
            }
        }
        //IMPRIMIR PELICULAS
        int largo = 10;  //set top 5
        for(int i=0; i<largo; i++){
            System.out.println(ordenarPeliculas.delete().toStringFunc2());
        }
        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución: "+(fin - inicio)+" ms");
    }

    public MyList<String> top5_colecciones_con_mayores_ingresos(){
        MyList<String> resultado = new MyLinkedListImpl<>();
        //Los ingresos se calculan a nivel de pelicula, para calcular los ingresos totales de una colección se suman estos.
        //
        return resultado;
    }

}
