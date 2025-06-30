package uy.edu.um;
import entities.CargaDeDatos;
import entities.UMovieSist;
import lombok.Data;
import uy.edu.um.tad.stack.EmptyStackException;

import java.util.Scanner;

@Data
public class Main extends CargaDeDatos {
    public static void main(String[] args) throws EmptyStackException {

        CargaDeDatos cargaDeDatos = new CargaDeDatos();


        UMovieSist sistemaPrueba = new UMovieSist(cargaDeDatos);

        Scanner leer = new Scanner(System.in);
        int opcion=0;
        do{
            System.out.println("Selecciona la opción que desee:");
            System.out.println("1. Carga de datos");
            System.out.println("2. Ejecutar consultas");
            System.out.println("3. Salir");

            if (!leer.hasNextInt()) {
                System.out.println("Error: Debe ingresar un número válido.");
                leer.nextLine();
                continue;
            }
            opcion = leer.nextInt();
            leer.nextLine();

            if (opcion < 1 || opcion > 3) {
                System.out.println("Opción inválida. Intente de nuevo.");
                continue;
            }

            switch (opcion){
                case 1:
                    //Carga de Datos
                    System.out.println("||----------------Inicia la carga de datos----------------||");
                    cargaDeDatos.cargaDatos();
                    break;
                case 2:
                    int consulta=0;
                    do{
                        System.out.println("1. Top 5 de las películas que más calificaciones por idioma.");
                        System.out.println("2. Top 10 de las películas que mejor calificación media tienen por parte de los usuarios.");
                        System.out.println("3. Top 5 de las colecciones que más ingresos generaron.");
                        System.out.println("4. Top 10 de los directores que mejor calificación tienen.");
                        System.out.println("5. Actor con más calificaciones recibidas en cada mes del año.");
                        System.out.println("6. Usuarios con más calificaciones por género");
                        System.out.println("7. Salir");
                        if (!leer.hasNextInt()) {
                            System.out.println("Error: Debe ingresar un número válido.");
                            leer.nextLine();
                            continue;
                        }
                        consulta = leer.nextInt();
                        leer.nextLine();

                        if (consulta < 1 || consulta > 7) {
                            System.out.println("Opción inválida. Intente de nuevo.");
                            continue;
                        }

                        UMovieSist sistemaPrueb = new UMovieSist(cargaDeDatos);
                        switch (consulta){
                            case 1:
                                // Consulta 1
                                System.out.println("Ejecutando la consulta...");
                                sistemaPrueb.top5_peliculas_mas_calificadas_por_idioma_original();
                                break;
                            case 2:
                                // Consulta 2
                                System.out.println("Ejecutando la consulta...");
                                sistemaPrueb.top10_peliculas_con_mayor_calificacion();
                                break;
                            case 3:
                                // Consulta 3
                                System.out.println("Ejecutando la consulta...");
                                sistemaPrueb.top5_colecciones_con_mayores_ingresos();
                                break;
                            case 4:
                                // Consulta 4
                                System.out.println("Ejecutando la consulta...");
                                sistemaPrueb.   top_10_directores_mas_calificaciones();
                                break;
                            case 5:
                                // Consulta 5
                                System.out.println("Ejecutando la consulta...");
                                sistemaPrueb.mejor_actor_por_cada_mes();
                                break;
                            case 6:
                                // Consulta 6
                                System.out.println("Ejecutando la consulta...");
                                break;
                            case 7:
                                System.out.println("Volviendo al menú principal...");
                                break;
                            default:
                                System.out.println("Opción inválida. Intente de nuevo.");
                        }

                    }while(consulta !=7);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");


            }
        } while(opcion != 3);
    }
}
