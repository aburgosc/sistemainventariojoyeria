package cl.aburgosc.sistemainventariojoyeria.model;

import java.sql.Timestamp;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

/**
 *
 * @author aburgosc
 * 
 */
@DBTable(nombre = "stock")
public class Stock extends ObjetoBase {

    @DBColumn(nombre = "id_lote")
    private int idLote;

    @DBColumn(nombre = "cantidad_disponible")
    private int cantidadDisponible;

    @DBColumn(nombre = "ultima_actualizacion")
    private Timestamp ultimaActualizacion;

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

    public Timestamp getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Timestamp ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}
