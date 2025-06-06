package entities;
import com.opencsv.CSVReader;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;

@Data

public class CargaDeDatos {
        MyHash<Integer, Pelicula> todasLasPeliculas = new MyHashImpl<>();

}
