package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "metal")
public class Metal extends ObjetoBase {

    @DBColumn(nombre = "nombre")
    private String nombre;

    @DBColumn(nombre = "pureza")
    private String pureza;

    public Metal() {

    }

    public Metal(String nombre, String pureza) {
        this.nombre = nombre;
        this.pureza = pureza;
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPureza() {
        return pureza;
    }

    public void setPureza(String pureza) {
        this.pureza = pureza;
    }

    @Override
    public String toString() {
        return nombre + " " + pureza;
    }
}
