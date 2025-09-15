package cl.aburgosc.sistemainventariojoyeria.ui.dto;


import cl.aburgosc.sistemainventariojoyeria.ui.util.UIColumn;

public class ClienteDTO {

	@UIColumn(header = "ID")
    private int id;
	@UIColumn(header = "RUT")
    private String rut;
	@UIColumn(header = "Nombres")
    private String nombre;
	@UIColumn(header = "Apellidos")
    private String apellido;
	@UIColumn(header = "E-mail")
    private String email;
	@UIColumn(header = "Teléfono")
    private String telefono;

    public ClienteDTO() {
    }

    public ClienteDTO(int id, String rut, String nombre, String apellido, String email, String telefono) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
