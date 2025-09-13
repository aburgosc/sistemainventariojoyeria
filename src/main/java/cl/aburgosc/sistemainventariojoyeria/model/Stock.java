package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "stock")
public class Stock extends ObjetoBase {

    @DBColumn(nombre = "id_lote")
    private int idLote;

    @DBColumn(nombre = "cantidad_disponible")
    private int cantidadDisponible;

    @DBColumn(nombre = "ultima_actualizacion")
    private java.sql.Timestamp ultimaActualizacion;

    public Stock() {
    }

    
    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public java.sql.Timestamp getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(java.sql.Timestamp ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}
