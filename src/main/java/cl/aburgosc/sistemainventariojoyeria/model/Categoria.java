package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "categoria")
public class Categoria extends ObjetoBase {

    @DBColumn(nombre = "nombre")
    private String nombre;

    @DBColumn(nombre = "descripcion")
    private String descripcion;

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
