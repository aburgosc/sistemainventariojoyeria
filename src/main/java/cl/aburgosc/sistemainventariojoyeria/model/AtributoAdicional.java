package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "atributo_adicional")
public class AtributoAdicional extends ObjetoBase {

    @DBColumn(nombre = "id_lote")
    private int idLote;

    @DBColumn(nombre = "nombre_atributo")
    private String nombreAtributo;

    @DBColumn(nombre = "valor")
    private String valor;

    @DBColumn(nombre = "unidad")
    private String unidad; // opcional

    public AtributoAdicional(int idLote, String nombreAtributo, String valor, String unidad) {
        this.idLote = idLote;
        this.nombreAtributo = nombreAtributo;
        this.valor = valor;
        this.unidad = unidad;
    }

    public AtributoAdicional() {
    }

    
    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public String getNombreAtributo() {
        return nombreAtributo;
    }

    public void setNombreAtributo(String nombreAtributo) {
        this.nombreAtributo = nombreAtributo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
