package entities;

import lombok.Data;

@Data
public class Genero implements Comparable<Genero>{


    private int id;
    private String nombre;
    private Integer cantidadRatings;

    public Genero(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public int compareTo(Genero g) {
        return this.getCantidadRatings().compareTo(g.getCantidadRatings());
    }
}
