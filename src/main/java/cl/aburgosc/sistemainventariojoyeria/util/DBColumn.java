package cl.aburgosc.sistemainventariojoyeria.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author aburgosc
 */
@Target(ElementType.FIELD)  // Solo se puede aplicar a atributos de clase
@Retention(RetentionPolicy.RUNTIME) // Disponible en tiempo de ejecución
public @interface DBColumn {

    String nombre();                 // Nombre de la columna en la DB

    boolean primaryKey() default false; // Si es clave primaria
}
