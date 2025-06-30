package entities;

import lombok.Data;

@Data
public class Actor {

    private int id;
    private String nombre;

    public Actor(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
}
