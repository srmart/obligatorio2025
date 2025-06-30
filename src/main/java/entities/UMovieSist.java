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


import java.time.LocalDate;

@Data
public class UMovieSist {

    private final CargaDeDatos datos;


    public UMovieSist(CargaDeDatos datos) {

        this.datos = datos;

    }

    public void top5_peliculas_mas_calificadas_por_idioma_original() throws EmptyStackException {
        long inicio = System.currentTimeMillis();

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

        long fin = System.currentTimeMillis();

        System.out.println("\n--- Tiempo de ejecución ---");
//            System.out.println("Inicialización de estructuras: " + (finSetup - inicioSetup) + " ms");
//            System.out.println("Carga y filtrado de películas: " + (finCarga - inicioCarga) + " ms");
//            System.out.println("Extracción e impresión: " + (finPrint - inicioPrint) + " ms");
        System.out.println("Tiempo de ejecución: "+(fin-inicio)+" ms"+"\n\n");
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
        System.out.println("Tiempo de ejecución: "+(fin-inicio)+" ms"+"\n\n");
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
        System.out.println("Tiempo de ejecución: "+(fin-inicio)+" ms"+"\n\n");
        //Los ingresos se calculan a nivel de pelicula, para calcular los ingresos totales de una colección se suman estos.
        }

        public void top_10_directores_mas_calificaciones() throws EmptyStackException {
            long inicio = System.currentTimeMillis();
            MyList<Director> directors = getDatos().getDirectores().values();
            long finCargaPDirector = System.currentTimeMillis();
            for(int i = 0; i<directors.size();i++){//set mediana para todos los directores
                directors.get(i).mediana();
            }
            Director.comparator = Director.MEDIANA;//comparator de director para comparar por mediana



            MyList<Director> listaDirectores = getDatos().getDirectores().values();

            for (int i = 0; i < listaDirectores.size(); i++) {
                Director d = listaDirectores.get(i);
                System.out.println(d.getNombre() + " -> Películas: " + d.getCantidadDePeliculas() + " -> Mediana: " + d.getMediana());
            }

            MyHeap<Director> directoresPorMediana = new MyHeapImpl<>();
            for(int i = 0; i<listaDirectores.size(); i++){
                Director directorActual = listaDirectores.get(i);
                if(directorActual.getCantidadDePeliculas()>1){
                    if(directoresPorMediana.size()<10){
                        directoresPorMediana.insert(directorActual);
                    }
                    else{
                        Director directorMenorMediana = directoresPorMediana.get();
                        if(directorActual.compareTo(directorMenorMediana)>0){
                            directoresPorMediana.delete();
                            directoresPorMediana.insert(directorActual);
                        }
                    }
                }
            }
            //imprimir resultado
            int size = directoresPorMediana.size();
            MyStack<Director> directores = new MyLinkedListImpl<>();
            for(int i = 0; i<size; i++){
                directores.push(directoresPorMediana.delete());
            }
            for(int i = 0; i<size; i++){
                System.out.println(directores.pop().toStringDir());
            }
            long fin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución: "+(fin-inicio)+" ms"+"\n\n");
        }


        public void mejor_actor_por_cada_mes(){
            long inicio = System.currentTimeMillis();
            MyHash<Integer, MyHash<String, MyList<Integer>>> peliculasPorActorPorMes = new MyHashImpl<>(12); // hash por mes donde cada mes contiene un hash con las peliculas por actor de ese mes
            MyHash<Integer, MyHash<String, Integer>> calificacionesPorActorPorMes = new MyHashImpl<>(12); // hash por mes donde cada mes contiene un hash con las calificaciones por actor de ese mes
            MyList<Pelicula> peliculas = getDatos().getTodasLasPeliculas().values();

            // Recorremos todas las películas y todos sus ratings
            for (int i = 0; i < peliculas.size(); i++) {
                Pelicula pelicula = peliculas.get(i);
                MyList<Rating> ratings = pelicula.getRatings();

                for (int j = 0; j < ratings.size(); j++) {
                    Rating rating = ratings.get(j);
                    LocalDate fechaRating = rating.getFecha();
                    int mes = fechaRating.getMonthValue() - 1; // enero = 1 por localdate => enero = 0

                    MyList<Actor> actores = pelicula.getActores();


                    if (!peliculasPorActorPorMes.contains(mes)) { //se inicializan hashes si no existen para el mes
                        peliculasPorActorPorMes.put(mes, new MyHashImpl<>());
                    }
                    if (!calificacionesPorActorPorMes.contains(mes)) { //se inicializan hashes si no existen para el mes
                        calificacionesPorActorPorMes.put(mes, new MyHashImpl<>());
                    }

                    MyHash<String, MyList<Integer>> peliculasPorActor = peliculasPorActorPorMes.get(mes);
                    MyHash<String, Integer> calificacionesPorActor = calificacionesPorActorPorMes.get(mes);


                    for (int k = 0; k < actores.size(); k++) { // para cada actor de la película
                        String nombreActor = actores.get(k).getNombre();


                        if (!calificacionesPorActor.contains(nombreActor)) { //actualizar cantidad de calificaciones
                            calificacionesPorActor.put(nombreActor, 1);
                        } else {
                            calificacionesPorActor.put(nombreActor, calificacionesPorActor.get(nombreActor) + 1);
                        }


                        if (!peliculasPorActor.contains(nombreActor)) { // actualizar películas únicas
                            MyList<Integer> listaPeliculas = new MyLinkedListImpl<>();
                            listaPeliculas.add(pelicula.getId());
                            peliculasPorActor.put(nombreActor, listaPeliculas);
                        } else {
                            MyList<Integer> listaPeliculas = peliculasPorActor.get(nombreActor);
                            boolean yaExiste = false;
                            for (int m = 0; m < listaPeliculas.size(); m++) {
                                if (listaPeliculas.get(m) == pelicula.getId()) {
                                    yaExiste = true;
                                    break;
                                }
                            }
                            if (!yaExiste) { //no existe, la agrego
                                listaPeliculas.add(pelicula.getId());
                            }
                        }
                    }
                }
            }

            String[] nombresMeses = {
                    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            };

            for (int mes = 0; mes < 12; mes++) {
                if (peliculasPorActorPorMes.contains(mes)) {
                    MyHash<String, MyList<Integer>> peliculasPorActor = peliculasPorActorPorMes.get(mes);
                    MyHash<String, Integer> calificacionesPorActor = calificacionesPorActorPorMes.get(mes);

                    String actorTop = null;
                    int maxPeliculas = 0;
                    int maxCalificaciones = 0;

                    MyList<String> actoresMes = peliculasPorActor.keys();
                    for (int i = 0; i < actoresMes.size(); i++) {
                        String nombreActor = actoresMes.get(i);
                        int peliculasUnicas = peliculasPorActor.get(nombreActor).size();
                        int calificaciones = calificacionesPorActor.get(nombreActor);

                        if (peliculasUnicas > maxPeliculas || (peliculasUnicas == maxPeliculas && calificaciones > maxCalificaciones)) {
                            actorTop = nombreActor;
                            maxPeliculas = peliculasUnicas;
                            maxCalificaciones = calificaciones;
                        }
                    }
                    if (actorTop != null) {
                        System.out.println("Mes: " + nombresMeses[mes]
                                + " | Actor: " + actorTop
                                + " | Películas: " + maxPeliculas
                                + " | Calificaciones: " + maxCalificaciones);
                    } else {
                        System.out.println("Mes: " + nombresMeses[mes]
                                + " | Sin datos para este mes.");
                    }
                } else {
                    System.out.println("Mes: " + nombresMeses[mes]
                            + " | Sin datos para este mes.");
                }
            }

            long fin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución: "+(fin-inicio)+" ms"+"\n\n");
        }


    public void topUsuarioPorGeneroEnTop10() {

        MyHash<Integer, Integer> cantidadPorGenero = new MyHashImpl<>();//hash para guardar cantidad de ratings por generos
        MyHash<Integer, Genero> generosPorId = new MyHashImpl<>();//hash para obtener genero a partir de id

        MyList<Pelicula> peliculas = getDatos().getTodasLasPeliculas().values();
        for (int i = 0; i < peliculas.size(); i++) {
            Pelicula peli = peliculas.get(i);
            MyHash<Integer, Genero> generos = peli.getGeneros();
            MyList<Rating> ratings = peli.getRatings();

            MyList<Integer> idsGenero = generos.keys();//obtengo las key de generos del hash dentro de pelicula
            for (int j = 0; j < idsGenero.size(); j++) {
                int idGen = idsGenero.get(j);
                Genero genero = generos.get(idGen);
                generosPorId.put(idGen, genero); //agrego el genero al hash creado

                int suma = cantidadPorGenero.contains(idGen) ? cantidadPorGenero.get(idGen) : 0; //si no está el genero de la pelicula no cuento sus evaluaciones
                cantidadPorGenero.put(idGen, suma + ratings.size()); //se suma 0 o la cantidad de evaluaciones del genero dependiendo de la linea anterior
            }
        }

        //setear la cantidad de ratings a los generos
        MyList<Integer> ids = cantidadPorGenero.keys();
        for (int i = 0; i < ids.size(); i++) {
            int idGen = ids.get(i);
            int cantidad = cantidadPorGenero.get(idGen);
            Genero genero = generosPorId.get(idGen);
            if (genero != null) {
                genero.setCantidadRatings(cantidad);
            }
        }

    }


}









