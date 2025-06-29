package entities;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uy.edu.um.tad.binarytree.MySearchBinaryTreeImpl;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.heap.MyHeap;
import uy.edu.um.tad.heap.MyHeapImpl;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
@Data

public class CargaDeDatos {
        private MyHash<Integer, Pelicula> todasLasPeliculas = new MyHashImpl<>(65536); //peliculas por id pelicula
        private MyHash<Integer, MyList<Rating>> ratingsPorUsuario = new MyHashImpl<>(65536); //todas las evaluaciones realizadas por usuario.
        private MyHash<Integer, Coleccion> colecciones = new MyHashImpl<>(65536); //colecciones por su id
        private MyHash<String, Director> directores = new MyHashImpl<>(40000);
        private int erroresDeParseoPelis = 0;
        private int erroresDeParseoRatings = 0;
        private int lineasIncorrectas = 0;
        private int peliculasCargadas = 0;
        private int ratingsCargados = 0;
        private int cantidadDeColecciones = 0;
        private int cantGeneros = 0;
        int linea = 0;
        int actorCargado = 0;
        int errorActores = 0;
        int errorListaActores = 0;
        int errorDirectores = 0;


        public void cargaDatos() {
                long comienzo = System.currentTimeMillis();
                try {

                        //----------------COMIENZA CARGA DE PELICULAS----------------//
                        File archivoPelis = new File("movies_metadata.csv");
                        CSVReader leerPelis = new CSVReader(new FileReader(archivoPelis));
                        String[] peliculas;
                        leerPelis.readNext(); //saltar encabezado
                        //pos 1 = coleccion
                        //pos 2 = presupuesto
                        //pos 3 = generos
                        //pos 5 = idPeli
                        //pos 7 = original Lang
                        //pos 13 = ingresos
                        //pos 18 = titulo
//                        String[] nextLine = leerPelis.readNext();
//                        System.out.println("PRUEBA IMPRIMIR LINEA");
//                        System.out.println(Arrays.toString(nextLine));
//                        System.out.println("FIN PRUEBA");

                        while((peliculas =leerPelis.readNext())!=null){
                                if(peliculas.length!=19){
                                        System.out.println("Linea Incorrecta");
                                        lineasIncorrectas++;
                                        continue;
                                }

                                try {
                                        if((peliculas[5]==null || peliculas[5].equals("null") || peliculas[5].isEmpty()||peliculas[18]==null || peliculas[18].equals("null") || peliculas[18].isEmpty() || peliculas[2]==null || peliculas[2].equals("null") || peliculas[2].isEmpty() || peliculas[13]==null || peliculas[13].equals("null") || peliculas[13].isEmpty() || peliculas[7]==null || peliculas[7].equals("null") || peliculas[7].isEmpty())){
                                                continue;
                                        }
                                        int idPelicula = Integer.parseInt(peliculas[5]);
                                        String titulo = peliculas[18];
                                        long presupuesto = Long.parseLong(peliculas[2]);
                                        long ingresos = Long.parseLong(peliculas[13]);
                                        String originalLang = peliculas[7];

                                        Pelicula unaPelicula = null;
                                        if(todasLasPeliculas.contains(idPelicula)) { //evaluamos duplicados
                                                unaPelicula = todasLasPeliculas.get(idPelicula);
                                        }
                                        else {
                                                unaPelicula = new Pelicula(idPelicula, titulo, originalLang,presupuesto, ingresos);
                                                todasLasPeliculas.put(idPelicula, unaPelicula);
                                                peliculasCargadas++;
                                        }

                                        //coleccion
                                        if(peliculas[1]==null|| peliculas[1].isEmpty()||peliculas[1].equals("null") && !colecciones.contains(idPelicula)){ //coleccion unitaria (solo 1 pelicula)
                                                Coleccion coleccion = new Coleccion(idPelicula, titulo+" Colection");
                                                coleccion.getPeliculas().add(unaPelicula);
                                                cantidadDeColecciones++;
                                        }else {
                                                String campoColeccion = peliculas[1];

                                                campoColeccion = campoColeccion.replaceAll("(?<=\\{|, )'(\\w+)':", "\"$1\":");  // reemplazar comillas simples por dobles en claves
                                                campoColeccion = campoColeccion.replaceAll(":\\s*'(.*?)'(?=[,}])", ": \"$1\""); // reemplazar comillas simples por dobles en valores
                                                campoColeccion = campoColeccion.replace("None", "null");// remplazar None por null para evitar errores.
                                                //System.out.println("JSON a parsear: " + campoColeccion);
                                                JSONObject jsonColeccion = new JSONObject(campoColeccion);
                                                int idColeccion = jsonColeccion.getInt("id");
                                                String nombreColeccion = jsonColeccion.getString("name");

                                                Coleccion unaColeccion = null;
                                                if(colecciones.contains(idColeccion)){
                                                        unaColeccion = colecciones.get(idColeccion);
                                                        unaColeccion.getPeliculas().add(unaPelicula);
                                                }else{
                                                        unaColeccion = new Coleccion(idColeccion, nombreColeccion);
                                                        unaColeccion.getPeliculas().add(unaPelicula);
                                                        colecciones.put(idColeccion, unaColeccion);
                                                        cantidadDeColecciones++;
                                                }
                                        }
                                        //fin coleccion

                                        //carga generos
                                        if(peliculas[3].isEmpty() || peliculas[3].equals("null") || peliculas[3].equals("None")){
                                                System.out.println("La pelicula con id: "+idPelicula+" no tiene generos cargados.");
                                                continue;
                                        }
                                        String campoGeneros = peliculas[3].replace("'", "\""); //reemplazar comillas simples por dobles
                                        campoGeneros = campoGeneros.replace("\"[", "["); //reemplazar "[ por [
                                        campoGeneros = campoGeneros.replace("]\"", "]"); //reemplazar ]" por ]
                                        campoGeneros = campoGeneros.replace("\\\"", "\""); //manejar comillas escapadas \"
                                        JSONArray jsonGeneros = new JSONArray(campoGeneros);
                                        for(int i = 0; i<jsonGeneros.length(); i++) {
                                                JSONObject esteObjeto = jsonGeneros.getJSONObject(i);
                                                int idGen = esteObjeto.getInt("id");
                                                String nombreGen = esteObjeto.getString("name");
                                                Genero genero = new Genero(idGen, nombreGen);
                                                MyHash<Integer, Genero> generosPelicula = todasLasPeliculas.get(idPelicula).getGeneros();
                                                if (generosPelicula.contains(idGen)) {
                                                        continue;
                                                } else {
                                                        generosPelicula.put(idGen, genero);
                                                        cantGeneros++;
                                                }

                                        }

                                }catch (NumberFormatException e){
                                        System.out.println("Error de parseo con pelicula: "+peliculas[5]);
                                        erroresDeParseoPelis ++;
                                }

                        }
                        //----------------FIN CARGA DE PELICULAS----------------//


                        //----------------COMIENZA CARGA DE EVALUACIONES----------------//
                        File archivoRatings = new File("ratings_1mm.csv");
                        Scanner leerRatings = new Scanner(archivoRatings);
                        leerRatings.nextLine();//saltear encabezado
                        while (leerRatings.hasNextLine()) {
                                String line = leerRatings.nextLine();
                                String[] camposRating = line.split(",");
                                if (camposRating.length != 4) {
                                        System.out.println("Línea incorrecta");
                                        lineasIncorrectas++;
                                        continue;
                                }
                                //pos 0 = userId
                                //pos 1 = peliId
                                //pos 2 = rating
                                //pos 3 = fechaRating
                                try {
                                        int idUsuario = Integer.parseInt(camposRating[0]);
                                        int idPelicula = Integer.parseInt(camposRating[1]);
                                        double rating = Double.parseDouble(camposRating[2]);
                                        long fechaRating = Long.parseLong(camposRating[3]);
                                        Rating unRating = null;
                                        if (ratingsPorUsuario.contains(idUsuario)) { //si el hash sa tiene los ratings de este usuario, busco si contiene el rating para esta pelicula, si lo contiene ya queda.
                                                boolean existeRating = false;
                                                MyList<Rating> userRatings = ratingsPorUsuario.get(idUsuario);
                                                for (int i = 0; i < userRatings.size(); i++) {
                                                        if (userRatings.get(i).getIdPelicula() == idPelicula) {
                                                                existeRating = true;
                                                                break;
                                                        }
                                                }
                                                if (!existeRating) { //existe la lista de ratings pero el rating actual no -> lo agrego.
                                                        unRating = new Rating(idUsuario, idPelicula, rating, fechaRating);
                                                        unRating.timestampComoDate();//set fecha
                                                        userRatings.add(unRating);
                                                        ratingsCargados++;
                                                }
                                        } else { //no existe aún este idusuario en el hash -> creo la lista de rating, agrego el rating y lo meto en el hash.
                                                unRating = new Rating(idUsuario, idPelicula, rating, fechaRating);
                                                MyList<Rating> ratingsUsuario = new MyLinkedListImpl<>();
                                                ratingsUsuario.add(unRating);
                                                unRating.timestampComoDate(); //setear fecha
                                                ratingsPorUsuario.put(idUsuario, ratingsUsuario);
                                                ratingsCargados++;
                                        }
                                        if(todasLasPeliculas.contains(idPelicula)){ //agregar la calificación a la pelicula con el mismo id que el id calificacion
                                                todasLasPeliculas.get(idPelicula).getRatings().add(unRating); //saco la lista de ratings de la pelicula y agrego el rating.
//                                                MyList<Genero> generos = todasLasPeliculas.get(idPelicula).getGeneros().values();
//                                                int tamanio = generos.size();
//                                                for(int i = 0; i<tamanio; i++){
//                                                        Genero esteGen = generos.get(i);
//                                                        int cant = esteGen.getCantidadRatings();
//                                                        cant++;
//                                                        esteGen.setCantidadRatings(cant);
//                                                }
                                        }

                                }catch (NumberFormatException e){
                                        erroresDeParseoRatings++;
                                }

                        }


                        //----------------FIN CARGA DE EVALUACIONES----------------//






                        //----------------COMIENZA CARGA DE CREDITOS----------------//


                        File archivoCreditos = new File("credits.csv");
                        CSVReader leerCreditos = new CSVReader(new FileReader(archivoCreditos));
                        String[] creditos;
                        leerCreditos.readNext(); //saltar encabezado
                        //pos 0 = JsonArray de cast (actores)
                        //pos 1 = JsonArray de crew (pos 0 = director)
                        //pos 2 = id de pelicula


                        while((creditos = leerCreditos.readNext()) != null){
                                linea ++;
                                if(creditos.length!=3){
                                        System.out.println("linea incorrecta en "+linea);
                                        continue;
                                }
                                String stringCast = creditos[0];
                                String stringCrew = creditos[1];
                                int idPeliculaCreditos = Integer.parseInt(creditos[2]);

                                if(todasLasPeliculas.contains(idPeliculaCreditos)){
                                        boolean tieneDirector = false;
                                        Pelicula estaPelicula = todasLasPeliculas.get(idPeliculaCreditos);
                                        MyList<Actor> actoresPelicula = estaPelicula.getActores();
                                        if(actoresPelicula.isEmpty()){
                                                //Reemplazar caracteres para obtener json valido
                                                stringCast = stringCast.replaceAll("(?<=\\w)\"(?=\\w)", "'"); //busca comillas dobles dentro de los campos y las reemplaza por comillas simples.
                                                stringCast = stringCast.replace("\"[", "["); //reemplaza "[ por [
                                                stringCast = stringCast.replace("]\"", "]"); //reemplaza ]" por ]
                                                stringCast = stringCast.replace("None", "null");// remplazar None por null para evitar errores.
                                                try{
                                                        //Comienzo a sacar los actores.
                                                        JSONArray actores = new JSONArray(stringCast);
                                                        try {
                                                                for (int i = 0; i < actores.length(); i++) {
                                                                        JSONObject actor = actores.getJSONObject(i);
                                                                        int idActor = actor.getInt("id");
                                                                        String nombreActor = actor.getString("name");
                                                                        Actor nuevoActor = new Actor(idActor, nombreActor);
                                                                        actoresPelicula.add(nuevoActor);
                                                                }
                                                        } catch (JSONException e) {
                                                                errorActores++;
                                                        }
                                                } catch (JSONException e) {
                                                        //System.out.println("error parseo de actores de la linea: "+linea);
                                                        //System.out.println(stringCast);
                                                        errorListaActores++;
                                                }

                                        }
                                        if(todasLasPeliculas.get(idPeliculaCreditos).getDirector()!=null){
                                                tieneDirector = true;
                                        }

                                        try {
                                                if (!tieneDirector) {
                                                        stringCrew = stringCrew.replaceAll("(?<=\\w)\"(?=\\w)", "'"); //busca comillas dobles dentro de los campos y las reemplaza por comillas simples.
                                                        stringCrew = stringCrew.replace("\"[", "["); //reemplaza "[ por [
                                                        stringCrew = stringCrew.replace("]\"", "]"); //reemplaza ]" por ]
                                                        stringCrew = stringCrew.replace("None", "null");// remplazar None por null para evitar errores.
                                                        JSONArray crewArray = new JSONArray(stringCrew);
                                                        //Check si el director está en el primer lugar
                                                        Director directorEncontrado = null;
                                                        if (crewArray.getJSONObject(0).get("job") == "Director") {
                                                                if(!directores.contains(crewArray.getJSONObject(0).getString("name"))){
                                                                        directorEncontrado = new Director(crewArray.getJSONObject(0).getInt("id"), crewArray.getJSONObject(0).getString("name"));
                                                                        directores.put(directorEncontrado.getNombre(), directorEncontrado);
                                                                        break;
                                                                }
                                                                else{
                                                                        directorEncontrado = directores.get(crewArray.getJSONObject(0).getString("name"));
                                                                }
                                                        }
                                                        else{
                                                                for(int i = 0; i<crewArray.length(); i++){
                                                                        JSONObject director = crewArray.getJSONObject(i);
                                                                        if (director.get("job") == "Director" || director.get("job").equals("Director")){
                                                                                if(!directores.contains(director.getString("name"))){
                                                                                        directorEncontrado = new Director(director.getInt("id"), director.getString("name"));
                                                                                        directores.put(directorEncontrado.getNombre(), directorEncontrado);
                                                                                        break;
                                                                                }
                                                                                else{directorEncontrado=directores.get(director.getString("name"));}

                                                                        }
                                                                }
                                                        }
                                                        if(directorEncontrado!=null){
                                                                MyList<Pelicula> peliculasDirigidas = directorEncontrado.getPeliculasDirigidas();
                                                                if(!peliculasDirigidas.contains(estaPelicula)){
                                                                        peliculasDirigidas.add(estaPelicula);
                                                                        int cantPeliculas = directorEncontrado.getCantidadDePeliculas() + 1;
                                                                        directorEncontrado.setCantidadDePeliculas(cantPeliculas);
                                                                }
                                                        }


                                                }
                                        } catch (JSONException e) {
                                                //System.out.println("error parseo de director en linea: "+linea);
                                                //System.out.println(stringCrew);
                                                errorDirectores++;
                                        }

                                }
                                else{System.out.println("Pelicula no existe");}

                        }


                        //----------------FIN CARGA DE CREDITOS----------------//
                        long fin = System.currentTimeMillis();
                        System.out.println("Errores de parseo en carga de peliculas: "+getErroresDeParseoPelis());
                        System.out.println("Errores de parseo en carga de ratings: "+getErroresDeParseoRatings());
                        System.out.println("Cantidad de peliculas cargadas: "+getPeliculasCargadas());
                        System.out.println("Cantidad de colecciones guardadas: "+getCantidadDeColecciones());
                        System.out.println("Cantidad de ratings cargados: "+getRatingsCargados());
                        System.out.println("Cantidad de generos: "+getCantGeneros());
                        System.out.println(actorCargado);
                        System.out.println(errorActores);
                        System.out.println(errorListaActores);
                        System.out.println(errorDirectores);
                        System.out.println("Tiempo de carga: "+(fin - comienzo)+" ms");
                        Pelicula.comparator = Pelicula.AVG_COMPARATOR;
                        MyList<Director> directoresLista = directores.values();
                        for(int i = 0; i<directoresLista.size();i++){
                                MyList<Pelicula> pDirector = directoresLista.get(i).getPeliculasDirigidas();
                                for(int j = 0; j<pDirector.size();j++){
                                        if (pDirector.get(j).getAvgRating()!=(-1)){
                                                System.out.println(pDirector.get(j).toStringFunc2());
                                        }

                                }
                        }



                } catch (FileNotFoundException e) {
                    throw new RuntimeException("El archivo no existe "+e.getMessage(), e);
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }



        }




}
