package entities;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
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
        //private MyHash<String, MyList<Pelicula>> peliculasPorDirector;  //peliculas por nombre director
        private int erroresDeParseoPelis = 0;
        private int erroresDeParseoRatings = 0;
        private int lineasIncorrectas = 0;
        private int peliculasCargadas = 0;
        private int ratingsCargados = 0;
        private int cantidadDeColecciones = 0;


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
                                                //campoColeccion = campoColeccion.replace('\'', '"');// remplazar '' por "" para que sea un json válido.
                                                campoColeccion = campoColeccion.replaceAll("(?<=\\{|, )'(\\w+)':", "\"$1\":");  // claves
                                                campoColeccion = campoColeccion.replaceAll(":\\s*'(.*?)'(?=[,}])", ": \"$1\""); // valores
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
                                                        userRatings.add(unRating);
                                                        ratingsCargados++;
                                                }
                                        } else { //no existe aún este idusuario en el hash -> creo la lista de rating, agrego el rating y lo meto en el hash.
                                                unRating = new Rating(idUsuario, idPelicula, rating, fechaRating);
                                                MyList<Rating> ratingsUsuario = new MyLinkedListImpl<>();
                                                ratingsUsuario.add(unRating);
                                                ratingsPorUsuario.put(idUsuario, ratingsUsuario);
                                                ratingsCargados++;
                                        }
                                        if(todasLasPeliculas.contains(idPelicula)){ //agregar la calificación a la pelicula con el mismo id que el id calificacion
                                                todasLasPeliculas.get(idPelicula).getRatings().add(unRating); //saco la lista de ratings de la pelicula y agrego el rating.
                                        }

                                }catch (NumberFormatException e){
                                        erroresDeParseoRatings++;
                                }

                        }
                        long fin = System.currentTimeMillis();
                        //----------------FIN CARGA DE EVALUACIONES----------------//
                        System.out.println("Errores de parseo en carga de peliculas: "+getErroresDeParseoPelis());
                        System.out.println("Errores de parseo en carga de ratings: "+getErroresDeParseoRatings());
                        System.out.println("Cantidad de peliculas cargadas: "+getPeliculasCargadas());
                        System.out.println("Cantidad de colecciones guardadas: "+getCantidadDeColecciones());
                        System.out.println("Cantidad de ratings cargados: "+getRatingsCargados());
//
                        System.out.println("Tiempo de carga: "+(fin - comienzo)+" ms");




                        //----------------COMIENZA CARGA DE CREDITOS----------------//



                        //----------------FIN CARGA DE CREDITOS----------------//


                } catch (FileNotFoundException e) {
                    throw new RuntimeException("El archivo no existe "+e.getMessage(), e);
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }

        }
}
