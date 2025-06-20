package entities;

import lombok.Data;

@Data
public class Genero implements Comparable<Genero>{


    private int id;
    private String genero;
    private Integer cantidadRatings;

    public Genero(int id, String genero){
        this.id = id;
        this.genero = genero;
    }

    @Override
    public int compareTo(Genero g) {
        return this.getCantidadRatings().compareTo(g.getCantidadRatings());
    }
}
