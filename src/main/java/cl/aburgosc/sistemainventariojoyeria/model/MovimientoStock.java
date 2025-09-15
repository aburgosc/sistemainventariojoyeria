package cl.aburgosc.sistemainventariojoyeria.model;

import java.sql.Timestamp;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "movimiento_stock")
public class MovimientoStock extends ObjetoBase {

    @DBColumn(nombre = "id_lote")
    private int idLote;

    @DBColumn(nombre = "tipo_movimiento")
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE

    @DBColumn(nombre = "cantidad")
    private int cantidad;

    @DBColumn(nombre = "fecha")
    private Timestamp fecha;

    @DBColumn(nombre = "referencia")
    private String referencia;

    public MovimientoStock() {
    }
    
    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
