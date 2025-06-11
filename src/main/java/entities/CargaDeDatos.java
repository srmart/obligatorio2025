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
        private MyHash<Integer, Pelicula> todasLasPeliculas; //peliculas por id pelicula
        private MyHash<Integer, MyList<Rating>> ratingsPorUsuario; //todas las evaluaciones realizadas por usuario.
        private MyHash<Integer, Coleccion> colecciones; //colecciones por su id
        private MyHash<String, MyList<Pelicula>> peliculasPorDirector;  //peliculas por nombre director

        public void cargaDatos() {
                try {
                        //----------------COMIENZA CARGA DE EVALUACIONES----------------//
                        File archivoRatings = new File("ratings_1mm.csv");
                        Scanner leerRatings = new Scanner(archivoRatings);
                        leerRatings.nextLine();//saltear encabezado
                        while (leerRatings.hasNextLine()) {
                                String line = leerRatings.nextLine();
                                String[] camposRating = line.split(",");
                                if (camposRating.length != 4) {
                                        System.out.println("Línea incorrecta");
                                        continue;
                                }
                                //pos 0 = userId
                                //pos 1 = peliId
                                //pos 2 = rating
                                //pos 3 = fechaRating
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
                                        }
                                } else { //no existe aún este idusuario en el hash -> creo la lista de rating, agrego el rating y lo meto en el hash.
                                        unRating = new Rating(idUsuario, idPelicula, rating, fechaRating);
                                        MyList<Rating> ratingsUsuario = new MyLinkedListImpl<>();
                                        ratingsUsuario.add(unRating);
                                        ratingsPorUsuario.put(idUsuario, ratingsUsuario);
                                }
                                //falta agregar ratings a la lista de la pelicula.
                        }
                        //----------------FIN CARGA DE EVALUACIONES----------------//


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

                        while(leerPelis.readNext()!=null){
                                peliculas = leerPelis.readNext();
                                if(peliculas.length!=19){
                                        System.out.println("Linea Incorrecta");
                                        continue;
                                }

                                int idPelicula = Integer.parseInt(peliculas[5]);
                                String titulo = peliculas[18];
                                long presupuesto = Long.parseLong(peliculas[2]);
                                long ingresos = Long.parseLong(peliculas[12]);
                                String originalLang = peliculas[7];

                                Pelicula unaPelicula = null;
                                if(todasLasPeliculas.contains(idPelicula)){
                                        unaPelicula = todasLasPeliculas.get(idPelicula);
                                }
                                else {
                                        unaPelicula = new Pelicula(idPelicula, titulo, presupuesto, ingresos);
                                        todasLasPeliculas.put(idPelicula, unaPelicula);
                                }

                                //coleccion
                                if(peliculas[1]==null|| peliculas[1].isEmpty()||peliculas[1].equals("null")){
                                        Coleccion coleccion = new Coleccion(idPelicula, titulo+" Colection");
                                        coleccion.getPeliculas().add(unaPelicula);
                                }else {
                                        String campoColeccion = peliculas[1];
                                        campoColeccion = campoColeccion.replace('\'', '"'); // remplazar '' por "" para que sea un json válido

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
                                        }
                                }
                                //fin coleccion




                        }
                        //----------------FIN CARGA DE PELICULAS----------------//


                        //----------------COMIENZA CARGA DE CREDITOS----------------//



                        //----------------FIN CARGA DE CREDITOS----------------//


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

        }
}
