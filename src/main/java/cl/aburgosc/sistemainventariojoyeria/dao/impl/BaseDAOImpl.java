package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.BaseDAO;
import cl.aburgosc.sistemainventariojoyeria.exception.DAOException;
import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

public abstract class BaseDAOImpl<T> implements BaseDAO<T> {

	private final Class<T> type;

	protected BaseDAOImpl(Class<T> type) {
		this.type = type;
	}

	@Override
	public int insertar(T obj) throws DAOException {
		List<Field> campos = obtenerCampos(obj.getClass());
		if (campos.isEmpty()) {
			return 0;
		}

		String nombreTabla = obtenerNombreTabla(obj.getClass());
		StringBuilder columnas = new StringBuilder();
		StringBuilder placeholders = new StringBuilder();

		for (Field f : campos) {
			DBColumn col = f.getAnnotation(DBColumn.class);
			if (!col.primaryKey()) {
				if (columnas.length() > 0) {
					columnas.append(", ");
				}
				columnas.append(col.nombre());
				if (placeholders.length() > 0) {
					placeholders.append(", ");
				}
				placeholders.append("?");
			}
		}

		String sql = "INSERT INTO " + nombreTabla + " (" + columnas + ") VALUES (" + placeholders + ")";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, new String[] { "id" })) {

			int index = 1;
			for (Field f : campos) {
				DBColumn col = f.getAnnotation(DBColumn.class);
				if (!col.primaryKey()) {
					f.setAccessible(true);
					ps.setObject(index++, f.get(obj));
				}
			}

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					int idGenerado = rs.getInt(1);
					asignarId(obj, campos, idGenerado);
					return idGenerado;
				}
			}
		} catch (Exception e) {
			throw new DAOException("Error insertando en tabla " + nombreTabla, e);
		}

		return 0;
	}

	@Override
	public void actualizar(T obj) throws DAOException {
		List<Field> campos = new ArrayList<>();
		Field pkField = obtenerPkCampo(obj.getClass(), campos);

		if (pkField == null || campos.isEmpty()) {
			return;
		}

		String nombreTabla = obtenerNombreTabla(obj.getClass());
		StringBuilder setPart = new StringBuilder();
		for (Field f : campos) {
			DBColumn col = f.getAnnotation(DBColumn.class);
			if (setPart.length() > 0) {
				setPart.append(", ");
			}
			setPart.append(col.nombre()).append("=?");
		}

		String sql = "UPDATE " + nombreTabla + " SET " + setPart + " WHERE "
				+ pkField.getAnnotation(DBColumn.class).nombre() + "=?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			int index = 1;
			for (Field f : campos) {
				f.setAccessible(true);
				ps.setObject(index++, f.get(obj));
			}
			pkField.setAccessible(true);
			ps.setObject(index, pkField.get(obj));

			ps.executeUpdate();
		} catch (Exception e) {
			throw new DAOException("Error actualizando en tabla " + nombreTabla, e);
		}
	}

	@Override
	public void eliminar(int pk) throws DAOException {
		Field pkField = obtenerPkCampo(type, null);
		if (pkField == null) {
			return;
		}

		String nombreTabla = obtenerNombreTabla(type);
		String sql = "DELETE FROM " + nombreTabla + " WHERE " + pkField.getAnnotation(DBColumn.class).nombre() + "=?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setObject(1, pk);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new DAOException("Error eliminando de tabla " + nombreTabla, e);
		}
	}

	@Override
	public T obtenerPorId(int pk) throws DAOException {
		List<Field> campos = new ArrayList<>();
		Field pkField = obtenerPkCampo(type, campos);
		if (pkField == null) {
			return null;
		}

		String nombreTabla = obtenerNombreTabla(type);
		StringBuilder columnas = new StringBuilder();
		for (Field f : campos) {
			if (columnas.length() > 0) {
				columnas.append(", ");
			}
			columnas.append(f.getAnnotation(DBColumn.class).nombre());
		}

		String sql = "SELECT " + columnas + ", " + pkField.getAnnotation(DBColumn.class).nombre() + " FROM "
				+ nombreTabla + " WHERE " + pkField.getAnnotation(DBColumn.class).nombre() + "=?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setObject(1, pk);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapearObjeto(rs, pkField, campos);
				}
			}
		} catch (Exception e) {
			throw new DAOException("Error obteniendo entidad por ID en tabla " + nombreTabla, e);
		}

		return null;
	}

	@Override
	public List<T> listar() throws DAOException {
		List<T> lista = new ArrayList<>();
		List<Field> campos = new ArrayList<>();
		Field pkField = obtenerPkCampo(type, campos);
		if (pkField == null) {
			return lista;
		}

		String nombreTabla = obtenerNombreTabla(type);
		StringBuilder columnas = new StringBuilder();
		for (Field f : campos) {
			if (columnas.length() > 0) {
				columnas.append(", ");
			}
			columnas.append(f.getAnnotation(DBColumn.class).nombre());
		}

		String sql = "SELECT " + columnas + ", " + pkField.getAnnotation(DBColumn.class).nombre() + " FROM "
				+ nombreTabla;

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(mapearObjeto(rs, pkField, campos));
			}
		} catch (Exception e) {
			throw new DAOException("Error listando entidades de tabla " + nombreTabla, e);
		}

		return lista;
	}

	protected String obtenerNombreTabla(Class<?> clazz) {
		if (clazz.isAnnotationPresent(DBTable.class)) {
			return clazz.getAnnotation(DBTable.class).nombre();
		}
		return clazz.getSimpleName().toLowerCase();
	}

	private List<Field> obtenerCampos(Class<?> clazz) {
		List<Field> campos = new ArrayList<>();
		Class<?> actual = clazz;
		while (actual != null && !actual.equals(Object.class)) {
			for (Field f : actual.getDeclaredFields()) {
				if (f.isAnnotationPresent(DBColumn.class)) {
					campos.add(f);
				}
			}
			actual = actual.getSuperclass();
		}
		return campos;
	}

	private Field obtenerPkCampo(Class<?> clazz, List<Field> campos) {
		Field pkField = null;
		Class<?> actual = clazz;
		while (actual != null && !actual.equals(Object.class)) {
			for (Field f : actual.getDeclaredFields()) {
				if (f.isAnnotationPresent(DBColumn.class)) {
					DBColumn col = f.getAnnotation(DBColumn.class);
					if (col.primaryKey()) {
						pkField = f;
					} else if (campos != null) {
						campos.add(f);
					}
				}
			}
			actual = actual.getSuperclass();
		}
		return pkField;
	}

	private void asignarId(T obj, List<Field> campos, int idGenerado) throws IllegalAccessException {
		for (Field f : campos) {
			DBColumn col = f.getAnnotation(DBColumn.class);
			if (col.primaryKey()) {
				f.setAccessible(true);
				f.set(obj, idGenerado);
				break;
			}
		}
	}

	private T mapearObjeto(ResultSet rs, Field pkField, List<Field> campos) throws Exception {
		T obj = type.getDeclaredConstructor().newInstance();
		pkField.setAccessible(true);
		pkField.set(obj, rs.getObject(pkField.getAnnotation(DBColumn.class).nombre()));
		for (Field f : campos) {
			f.setAccessible(true);
			f.set(obj, rs.getObject(f.getAnnotation(DBColumn.class).nombre()));
		}
		return obj;
	}
}
