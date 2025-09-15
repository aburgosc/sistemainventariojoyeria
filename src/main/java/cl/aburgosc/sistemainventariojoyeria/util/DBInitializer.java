package cl.aburgosc.sistemainventariojoyeria.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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

/**
 *
 * @author aburgosc
 */
public class DBInitializer {

	public static void initializeDatabase() {
		try (Connection conn = DBConnection.getConnection()) {
			crearTabla(conn, Producto.class);
			crearTabla(conn, AtributoAdicional.class);
			crearTabla(conn, Cliente.class);
			crearTabla(conn, Categoria.class);
			crearTabla(conn, DetalleVenta.class);
			crearTabla(conn, Metal.class);
			crearTabla(conn, MovimientoStock.class);
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
			MetalService metalService = new MetalServiceImpl();
			ProductoService productoService = new ProductoServiceImpl();
			AtributoAdicionalService atributoService = new AtributoAdicionalServiceImpl();
			ClienteService clienteService = new ClienteServiceImpl();
			ProductoLoteService loteService = new ProductoLoteServiceImpl();

			if (!categoriaService.listar().isEmpty()) {
				System.out.println("Datos de prueba ya insertados, se omite.");
				return;
			}

			// ------------------- CATEGORÍAS -------------------
			categoriaService.insertar(new Categoria("Anillos", "Anillos variados"));
			categoriaService.insertar(new Categoria("Collares", "Collares de lujo"));
			categoriaService.insertar(new Categoria("Pulseras", "Pulseras de moda"));
			categoriaService.insertar(new Categoria("Aretes", "Aretes finos"));

			// ------------------- METALES -------------------
			metalService.insertar(new Metal("Oro Amarillo", "14k"));
			metalService.insertar(new Metal("Oro Amarillo", "18k"));
			metalService.insertar(new Metal("Plata", "925"));
			metalService.insertar(new Metal("Platino", "950"));

			// ------------------- CLIENTES -------------------
			clienteService.insertar(new Cliente("11111111-1", "Juan", "Perez", "987654321", "juan@ejemplo.cl",
					"Av. Siempre Viva #741"));
			clienteService.insertar(new Cliente("22222222-2", "Pedro", "Lagos", "912345678", "pedro@ejemplo.cl",
					"Av. Siempre Viva #742"));
			clienteService.insertar(
					new Cliente("33333333-3", "Ana", "Gonzalez", "987123456", "ana@ejemplo.cl", "Av. Las Flores #123"));
			clienteService.insertar(new Cliente("44444444-4", "Luis", "Martinez", "912987654", "luis@ejemplo.cl",
					"Av. Los Robles #56"));
			clienteService.insertar(new Cliente("55555555-5", "Maria", "Lopez", "912345678", "maria@ejemplo.cl",
					"Av. Siempre Viva #743"));

			// ------------------- PRODUCTOS -------------------
			Producto p1 = new Producto();
			p1.setNombre("Anillo Lisboa");
			p1.setDescripcion("Oro Amarillo 14k con esmeraldas y diamantes");
			p1.setIdCategoria(1);
			p1.setIdMetal(1);
			p1.setCodigoUnico("ANL001");
			productoService.insertar(p1);

			Producto p2 = new Producto();
			p2.setNombre("Collar Barcelona");
			p2.setDescripcion("Collar de plata con piedras preciosas");
			p2.setIdCategoria(2);
			p2.setIdMetal(3);
			p2.setCodigoUnico("COL001");
			productoService.insertar(p2);

			Producto p3 = new Producto();
			p3.setNombre("Pulsera Roma");
			p3.setDescripcion("Pulsera de oro 18k con diseño moderno");
			p3.setIdCategoria(3);
			p3.setIdMetal(2);
			p3.setCodigoUnico("PUL001");
			productoService.insertar(p3);

			Producto p4 = new Producto();
			p4.setNombre("Aretes Paris");
			p4.setDescripcion("Aretes de platino con diamantes pequeños");
			p4.setIdCategoria(4);
			p4.setIdMetal(4);
			p4.setCodigoUnico("ARE001");
			productoService.insertar(p4);

			// ------------------- LOTES DE PRODUCTOS -------------------
			ProductoLote lote1 = new ProductoLote();
			lote1.setIdProducto(1);
			lote1.setCostoUnitario(BigDecimal.valueOf(2500000));
			lote1.setPrecioVenta(BigDecimal.valueOf(3200000));
			lote1.setCantidad(5);
			lote1.setArtesano("Artesano 1");
			lote1.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
			loteService.insertar(lote1);

			ProductoLote lote2 = new ProductoLote();
			lote2.setIdProducto(2);
			lote2.setCostoUnitario(BigDecimal.valueOf(300000));
			lote2.setPrecioVenta(BigDecimal.valueOf(1200000));
			lote2.setCantidad(3);
			lote2.setArtesano("Artesano 2");
			lote2.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
			loteService.insertar(lote2);

			ProductoLote lote3 = new ProductoLote();
			lote3.setIdProducto(3);
			lote3.setCostoUnitario(BigDecimal.valueOf(1500000));
			lote3.setPrecioVenta(BigDecimal.valueOf(2100000));
			lote3.setCantidad(4);
			lote3.setArtesano("Artesano 3");
			lote3.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
			loteService.insertar(lote3);

			ProductoLote lote4 = new ProductoLote();
			lote4.setIdProducto(4);
			lote4.setCostoUnitario(BigDecimal.valueOf(5000000));
			lote4.setPrecioVenta(BigDecimal.valueOf(6500000));
			lote4.setCantidad(2);
			lote4.setArtesano("Artesano 4");
			lote4.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
			loteService.insertar(lote4);

			// ------------------- ATRIBUTOS ADICIONALES -------------------
			atributoService.insertar(new AtributoAdicional(1, "Talla", "13", "mm"));
			atributoService.insertar(new AtributoAdicional(1, "Talla", "15", "mm"));
			atributoService.insertar(new AtributoAdicional(2, "Largo", "45", "cm"));
			atributoService.insertar(new AtributoAdicional(3, "Tamaño", "M", null));
			atributoService.insertar(new AtributoAdicional(4, "Peso", "2.5", "g"));

			System.out.println("Datos de prueba insertados usando los servicios.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <T> void crearTabla(Connection conn, Class<T> clazz) throws SQLException {
		if (!clazz.isAnnotationPresent(DBTable.class)) {
			throw new IllegalArgumentException("La clase " + clazz.getSimpleName() + " no tiene @DBTable");
		}

		DBTable tableAnnotation = clazz.getAnnotation(DBTable.class);
		String tableName = tableAnnotation.nombre();

		StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (\n");

		StringBuilder columnas = new StringBuilder();

		Class<?> actual = clazz;
		while (actual != null && !actual.equals(Object.class)) {
			for (Field f : actual.getDeclaredFields()) {
				if (f.isAnnotationPresent(DBColumn.class)) {
					DBColumn col = f.getAnnotation(DBColumn.class);
					String nombreColumna = col.nombre();
					String tipoColumna = mapTipoJavaASQL(f.getType());

					if (col.primaryKey()) {
						columnas.append(nombreColumna).append(" SERIAL PRIMARY KEY");
					} else {
						columnas.append(nombreColumna).append(" ").append(tipoColumna);
					}
					columnas.append(",\n");
				}
			}
			actual = actual.getSuperclass();
		}

		if (columnas.length() > 0) {
			columnas.setLength(columnas.length() - 2);
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
		if (tipo == BigDecimal.class) {
			return "NUMERIC(15,2)";
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
