package entities;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Data
public class Rating {

    private int idUsuario;
    private int idPelicula;
    private double rating;
    private long timestamp;
    private LocalDate fecha;

    public Rating(int idUsuario, int idPelicula, double rating, long timestamp) {
        this.idUsuario = idUsuario;
        this.idPelicula = idPelicula;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public LocalDate timestampComoDate(){
        long timestamp = this.getTimestamp();
        Instant instant = Instant.ofEpochSecond(timestamp);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate fecha = instant.atZone(zoneId).toLocalDate();
        setFecha(fecha);
        return fecha;
    }


}
