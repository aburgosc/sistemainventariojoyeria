package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;

/**
 *
 * @author aburgosc
 */
public abstract class ObjetoBase {

    @DBColumn(nombre = "id", primaryKey = true)
    int id;

    @DBColumn(nombre = "us_creacion")
    private String usCreacion;

    @DBColumn(nombre = "fec_creacion")
    private String fecCreacion;

    @DBColumn(nombre = "hora_creacion")
    private String horaCreacion;

    @DBColumn(nombre = "us_modif")
    private String usModif;

    @DBColumn(nombre = "fec_modif")
    private String fecModif;

    @DBColumn(nombre = "hora_modif")
    private String horaModif;

    public ObjetoBase() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsCreacion() {
        return usCreacion;
    }

    public void setUsCreacion(String usCreacion) {
        this.usCreacion = usCreacion;
    }

    public String getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(String fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(String horaCreacion) {
        this.horaCreacion = horaCreacion;
    }

    public String getUsModif() {
        return usModif;
    }

    public void setUsModif(String usModif) {
        this.usModif = usModif;
    }

    public String getFecModif() {
        return fecModif;
    }

    public void setFecModif(String fecModif) {
        this.fecModif = fecModif;
    }

    public String getHoraModif() {
        return horaModif;
    }

    public void setHoraModif(String horaModif) {
        this.horaModif = horaModif;
    }

}
