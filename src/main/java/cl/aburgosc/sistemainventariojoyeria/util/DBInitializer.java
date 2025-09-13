package cl.aburgosc.sistemainventariojoyeria.util;

import cl.aburgosc.sistemainventariojoyeria.model.AtributoAdicional;
import cl.aburgosc.sistemainventariojoyeria.model.Categoria;
import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.model.DetalleVenta;
import cl.aburgosc.sistemainventariojoyeria.model.Metal;
import cl.aburgosc.sistemainventariojoyeria.model.MovimientoStock;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.model.ProductoLote;
import cl.aburgosc.sistemainventariojoyeria.model.Stock;
import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import cl.aburgosc.sistemainventariojoyeria.service.AtributoAdicionalService;
import cl.aburgosc.sistemainventariojoyeria.service.CategoriaService;
import cl.aburgosc.sistemainventariojoyeria.service.ClienteService;
import cl.aburgosc.sistemainventariojoyeria.service.MetalService;
import cl.aburgosc.sistemainventariojoyeria.service.ProductoLoteService;
import cl.aburgosc.sistemainventariojoyeria.service.ProductoService;
import cl.aburgosc.sistemainventariojoyeria.service.impl.AtributoAdicionalServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.CategoriaServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ClienteServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.MetalServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ProductoLoteServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ProductoServiceImpl;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author aburgosc
 */
public class DBInitializer {

    public static void initializeDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            crearTabla(conn, AtributoAdicional.class);
            crearTabla(conn, Cliente.class);
            crearTabla(conn, Categoria.class);
            crearTabla(conn, DetalleVenta.class);
            crearTabla(conn, Metal.class);
            crearTabla(conn, MovimientoStock.class);
            crearTabla(conn, Producto.class);
            crearTabla(conn, ProductoLote.class);
            crearTabla(conn, Stock.class);
            crearTabla(conn, Venta.class);
            System.out.println("Inicializacion de base de datos completada correctamente.");
            insertarDatosDePrueba();
        } catch (SQLException e) {
            System.err.println("Error inicializando la base de datos: " + e.getMessage());
        }
    }

    public static void insertarDatosDePrueba() {
        try {
            CategoriaService categoriaService = new CategoriaServiceImpl();
            if (!categoriaService.listar().isEmpty()) {
                System.out.println("Datos de prueba ya insertados, se omite.");
                return;
            }
            MetalService metalService = new MetalServiceImpl();
            ProductoService productoService = new ProductoServiceImpl();
            AtributoAdicionalService atributoService = new AtributoAdicionalServiceImpl();
            ClienteService clienteService = new ClienteServiceImpl();
            ProductoLoteService loteService = new ProductoLoteServiceImpl();

            // Categorías
            categoriaService.insertar(new Categoria("Anillos", "Anillos variados"));
            categoriaService.insertar(new Categoria("Collares", "Collares de lujo"));

            // Metales
            metalService.insertar(new Metal("Oro Amarillo", "14k"));
            metalService.insertar(new Metal("Oro Amarillo", "18k"));
            metalService.insertar(new Metal("Plata", "925"));

            // Clientes
            clienteService.insertar(new Cliente("Juan", "Perez", "987654321", "juan@example.com", "Av. Siempre Viva #742"));
            clienteService.insertar(new Cliente("Maria", "Lopez", "912345678", "maria@example.com", "Av. Siempre Viva #742"));

            // Productos
            Producto p1 = new Producto();
            p1.setNombre("Anillo Lisboa");
            p1.setDescripcion("Oro Amarillo 14k con esmeraldas y diamantes");
            p1.setIdCategoria(1); // si asumimos que Anillos = 1
            p1.setIdMetal(1); // Oro Amarillo 14k
            p1.setCodigoUnico("ANL001");
            productoService.insertar(p1);

            Producto p2 = new Producto();
            p2.setNombre("Collar Barcelona");
            p2.setDescripcion("Collar de plata con piedras preciosas");
            p2.setIdCategoria(2); // Collares
            p2.setIdMetal(2); // Plata 925
            p2.setCodigoUnico("COL001");
            productoService.insertar(p2);

            ProductoLote lote1 = new ProductoLote();
            lote1.setIdProducto(1);
            lote1.setCostoUnitario(BigDecimal.valueOf(2519000));
            lote1.setPrecioVenta(BigDecimal.valueOf(2519000));
            lote1.setCantidad(5);
            lote1.setArtesano("Artesano 1");
            lote1.setFechaIngreso(new java.sql.Timestamp(System.currentTimeMillis()));
            loteService.insertar(lote1);

            ProductoLote lote2 = new ProductoLote();
            lote2.setIdProducto(2);
            lote2.setCostoUnitario(BigDecimal.valueOf(300000));
            lote2.setPrecioVenta(BigDecimal.valueOf(1200000));
            lote2.setCantidad(3);
            lote2.setArtesano("Artesano 2");
            lote2.setFechaIngreso(new java.sql.Timestamp(System.currentTimeMillis()));
            loteService.insertar(lote2);

            // Atributos adicionales
            atributoService.insertar(new AtributoAdicional(1, "Talla", "13", "mm"));
            atributoService.insertar(new AtributoAdicional(2, "Tamaño", "M", null));

            System.out.println("Datos de prueba insertados usando los servicios.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void crearTabla(Connection conn, Class<T> clazz) throws SQLException {
        // Verificar que la clase tenga @DBTable
        if (!clazz.isAnnotationPresent(DBTable.class)) {
            throw new IllegalArgumentException("La clase " + clazz.getSimpleName() + " no tiene @DBTable");
        }

        DBTable tableAnno = clazz.getAnnotation(DBTable.class);
        String tableName = tableAnno.nombre();

        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (\n");

        StringBuilder columnas = new StringBuilder();

        // Recorrer campos y generar columnas
        Class<?> actual = clazz;
        while (actual != null && !actual.equals(Object.class)) {
            for (Field f : actual.getDeclaredFields()) {
                if (f.isAnnotationPresent(DBColumn.class)) {
                    DBColumn col = f.getAnnotation(DBColumn.class);
                    String nombreColumna = col.nombre();
                    String tipoColumna = mapTipoJavaASQL(f.getType());

                    if (col.primaryKey()) {
                        // Usar SERIAL para auto-incremento en PostgreSQL
                        columnas.append(nombreColumna)
                                .append(" SERIAL PRIMARY KEY");
                        // Si quieres autoincrement:
                        // columnas.append(nombreColumna).append(" ").append(tipoColumna).append(" PRIMARY KEY AUTOINCREMENT");
                    } else {
                        columnas.append(nombreColumna)
                                .append(" ").append(tipoColumna);
                    }
                    columnas.append(",\n");
                }
            }
            actual = actual.getSuperclass();
        }

        // Quitar la última coma
        if (columnas.length() > 0) {
            columnas.setLength(columnas.length() - 2); // eliminar ",\n"
        }

        sql.append(columnas).append("\n);");

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql.toString());
        }
    }

    private static String mapTipoJavaASQL(Class<?> tipo) {
        if (tipo == int.class || tipo == Integer.class) {
            return "INTEGER";
        }
        if (tipo == double.class || tipo == Double.class || tipo == float.class || tipo == Float.class) {
            return "NUMERIC";
        }
        if (tipo == String.class) {
            return "TEXT";
        }
        if (tipo == boolean.class || tipo == Boolean.class) {
            return "BOOLEAN";
        }
        if (tipo == Timestamp.class) {
            return "TIMESTAMP";
        }
        return "TEXT";
    }
}
