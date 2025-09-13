package cl.aburgosc.sistemainventariojoyeria.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * Utilidades para mapear ResultSet a objetos usando anotaciones @DBColumn
 */
public class ResultSetMapper {

    /**
     * Mapea un ResultSet a una instancia de T usando reflexión y @DBColumn
     * Recorre también superclases para campos heredados.
     *
     * @param <T>
     * @param rs
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T map(ResultSet rs, Class<T> clazz) throws Exception {
        T obj = clazz.getDeclaredConstructor().newInstance();

        Class<?> current = clazz;
        while (current != null) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(DBColumn.class)) {
                    String colName = field.getAnnotation(DBColumn.class).nombre();
                    field.setAccessible(true);

                    switch (field.getType().getSimpleName()) {
                        case "int", "Integer" ->
                            field.set(obj, rs.getInt(colName));
                        case "double", "Double" ->
                            field.set(obj, rs.getDouble(colName));
                        case "BigDecimal" ->
                            field.set(obj, rs.getBigDecimal(colName));
                        case "String" ->
                            field.set(obj, rs.getString(colName));
                        case "Timestamp" ->
                            field.set(obj, rs.getTimestamp(colName));
                        case "long", "Long" ->
                            field.set(obj, rs.getLong(colName));
                        case "boolean", "Boolean" ->
                            field.set(obj, rs.getBoolean(colName));
                        default ->
                            throw new RuntimeException("Tipo no soportado: " + field.getType().getSimpleName());
                    }
                }
            }
            current = current.getSuperclass();
        }

        return obj;
    }
}
