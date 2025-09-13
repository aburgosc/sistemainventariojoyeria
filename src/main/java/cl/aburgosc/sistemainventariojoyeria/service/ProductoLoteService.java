/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.aburgosc.sistemainventariojoyeria.service;

import cl.aburgosc.sistemainventariojoyeria.model.ProductoLote;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author aburgosc
 */
public interface ProductoLoteService extends BaseService<ProductoLote> {

    List<ProductoLote> ObtenerLotesPorIdProducto(int idProducto) throws Exception;

    ProductoLote asignarLoteDisponible(int idProducto, int cantidadSolicitada) throws Exception;

    ProductoLote reservarLoteParaVenta(int idProducto, int cantidadSolicitada, int idVenta) throws Exception;

    int obtenerStockTotal(int idProducto) throws Exception;

    BigDecimal obtenerPrecioPromedio(int idProducto) throws Exception;
}
