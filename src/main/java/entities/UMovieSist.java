package entities;

import lombok.Data;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.heap.MyHeap;
import uy.edu.um.tad.heap.MyHeapImpl;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;
import uy.edu.um.tad.stack.EmptyStackException;
import uy.edu.um.tad.stack.MyStack;


import java.util.Comparator;

@Data
public class UMovieSist {

    private final CargaDeDatos datos;


    public UMovieSist(CargaDeDatos datos) {

        this.datos = datos;

    }

    public void top5_peliculas_mas_calificadas_por_idioma_original() throws EmptyStackException {
        long inicioTotal = System.currentTimeMillis();

        Pelicula.comparator = Pelicula.RATING_COMPARATOR; // comparar por cantidad de evaluaciones
        //long inicioSetup = System.currentTimeMillis();
        MyHash<String, MyHeap<Pelicula>> peliculasPorIdioma = new MyHashImpl<>();
        peliculasPorIdioma.put("en", new MyHeapImpl<>());
        peliculasPorIdioma.put("es", new MyHeapImpl<>());
        peliculasPorIdioma.put("fr", new MyHeapImpl<>());
        peliculasPorIdioma.put("it", new MyHeapImpl<>());
        //long finSetup = System.currentTimeMillis();

        //long inicioCarga = System.currentTimeMillis();
        MyList<Pelicula> listaDePeliculas = getDatos().getTodasLasPeliculas().values();
        for (int i = 0; i < listaDePeliculas.size(); i++) {
            Pelicula p = listaDePeliculas.get(i);
            MyHeap<Pelicula> heap = peliculasPorIdioma.get(p.getIdiomaOriginal());
            if (heap != null) {
                int cantidadRatings = p.cantEvaluacionesPelicula();
                if (cantidadRatings > 200) {
                    if (heap.size() < 5) {
                        heap.insert(p);
                    } else {
                        Pelicula menor = heap.get(); // pelicula con menor cantidad de calificaciones actualmente en el heap
                        if (p.compareTo(menor) > 0) {
                            heap.delete();
                            heap.insert(p); //la remplazo por una con mayor cantidad
                        }
                    }
                }
            }
        }
        //long finCarga = System.currentTimeMillis();

        //long inicioPrint = System.currentTimeMillis();
        int largo = 5;
        String[] idiomas = {"en", "es", "fr", "it"};

        for (String idioma : idiomas) {
            System.out.println("\n----------| Top 5 para idioma: " + idioma + " |----------");
            MyHeap<Pelicula> heapImprimir = peliculasPorIdioma.get(idioma);
            int heapSize = heapImprimir.size();
            int toPrint = Math.min(largo, heapSize);
            MyStack<Pelicula> stackPeliculas = new MyLinkedListImpl<>(); //stack para invertir el orden (como usamos un heap min para mantener las menores arriba, al sacarlas salen de menor a mayor)
            for (int i = 0; i < toPrint; i++) {
                stackPeliculas.push(heapImprimir.delete());

            }
            while (!stackPeliculas.isEmpty()) {
                System.out.println(stackPeliculas.pop().toStringFunc1());
            }
        }

        //long finPrint = System.currentTimeMillis();

        long finTotal = System.currentTimeMillis();

        System.out.println("\n--- Tiempo de ejecución ---");
//            System.out.println("Inicialización de estructuras: " + (finSetup - inicioSetup) + " ms");
//            System.out.println("Carga y filtrado de películas: " + (finCarga - inicioCarga) + " ms");
//            System.out.println("Extracción e impresión: " + (finPrint - inicioPrint) + " ms");
        System.out.println("Tiempo total de ejecución: " + (finTotal - inicioTotal) + " ms");
    }


    public void top10_peliculas_con_mayor_calificacion () throws EmptyStackException{
        //Ordena el top 10 de peliculas con mayor calificacion y lo devuelve
        long inicio = System.currentTimeMillis();
        Pelicula.comparator = Pelicula.AVG_COMPARATOR; //seteamos el comparable de pelicula para que compare por nota promedio de evaluaciones.
        MyList<Pelicula> listaDePeliculas = getDatos().getTodasLasPeliculas().values(); //todas las peliculas
        MyHeap<Pelicula> ordenarPeliculas = new MyHeapImpl<>(); //heap donde guardo las peliculas para ordenarlas

        for (int i = 0; i < listaDePeliculas.size(); i++) {
            Pelicula p = listaDePeliculas.get(i);
            int cantidadEvaluaciones = p.cantEvaluacionesPelicula();
            if (cantidadEvaluaciones >= 1000) {
                double avg = p.getAvgRating();
                if (avg !=-1 &&ordenarPeliculas.size() < 10) {
                    ordenarPeliculas.insert(p);
                } else {
                    Pelicula menor = ordenarPeliculas.get(); // pelicula con menor avg de calificaciones actualmente en el heap
                    if (p.compareTo(menor) > 0) {
                        ordenarPeliculas.delete();
                        ordenarPeliculas.insert(p); //la remplazo por una con mayor avg
                    }
                }
            }
        }
        //IMPRIMIR PELICULAS
        int largo = ordenarPeliculas.size();  //set top 5
        MyStack<Pelicula> stackPeliculas = new MyLinkedListImpl<>();
        for (int i = 0; i < largo; i++) {
            stackPeliculas.push(ordenarPeliculas.delete());
        }
        while(!stackPeliculas.isEmpty()){
            System.out.println(stackPeliculas.pop().toStringFunc2());        }
        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución: " + (fin - inicio) + " ms");
    }

    public void top5_colecciones_con_mayores_ingresos () throws EmptyStackException {
        long inicio = System.currentTimeMillis();
        Coleccion.comparator = Coleccion.INCOME_COMPARATOR;
        MyHeap<Coleccion> heapColecciones = new MyHeapImpl<>();
        MyHash<Integer ,Coleccion> hashColecciones = getDatos().getColecciones();
        MyList<Coleccion> colecciones = hashColecciones.values();
        if (colecciones.isEmpty()){
            System.out.println("No existen colecciones");
        }
        else{
            for (int i = 0; i<colecciones.size(); i++){
                int idColeccion = colecciones.get(i).getId();
                Coleccion coleccion = hashColecciones.get(idColeccion);
                coleccion.calcIngresos();
                if(coleccion.getIncome() < 1000000){
                    continue;
                }
                if(heapColecciones.size()<5){
                    heapColecciones.insert(coleccion);
                }
                else{
                    Coleccion menorInc = heapColecciones.get();
                    if(coleccion.compareTo(menorInc) > 0){
                        heapColecciones.delete();
                        heapColecciones.insert(coleccion);
                    }
                }

            }
        }
        //imprimir colecciones

        int tamanio = heapColecciones.size();
        MyStack<Coleccion> stackColecciones = new MyLinkedListImpl<>();
        for(int i = 0; i<tamanio; i++){
            stackColecciones.push(heapColecciones.delete());
        }
        for(int i = 0; i<tamanio; i++){
            System.out.println(stackColecciones.pop().toStringC());
        }
        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución: " + (fin - inicio) + " ms");


        //Los ingresos se calculan a nivel de pelicula, para calcular los ingresos totales de una colección se suman estos.


        }

    }


