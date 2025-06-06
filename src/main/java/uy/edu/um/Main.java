package uy.edu.um;
import com.opencsv.CSVReader;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

@Data
public class Main {
    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        int opcion;
        do{
            System.out.println("Selecciona la opción que desee:");
            System.out.println("1. Carga de datos");
            System.out.println("2. Ejecutar consultas");
            System.out.println("3. Salir");
            opcion=leer.nextInt();
        } while(opcion != 3);

        switch (opcion){
            case 1:
                //Carga de Datos
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
                    consulta=leer.nextInt();
                    switch (consulta){
                        case 1:
                            // Consulta 1
                            break;
                        case 2:
                            // Consulta 2
                            break;
                        case 3:
                            // Consulta 3
                            break;
                        case 4:
                            // Consulta 4
                            break;
                        case 5:
                            // Consulta 5
                            break;
                        case 6:
                            // Consulta 6
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

    }
}
