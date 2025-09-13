package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "cliente")
public class Cliente extends ObjetoBase {

    @DBColumn(nombre = "nombre")
    private String nombre;

    @DBColumn(nombre = "apellido")
    private String apellido;

    @DBColumn(nombre = "telefono")
    private String telefono;

    @DBColumn(nombre = "email")
    private String email;

    @DBColumn(nombre = "direccion")
    private String direccion;

    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String telefono, String email, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
