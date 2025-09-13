package cl.aburgosc.sistemainventariojoyeria.util;

/**
 *
 * @author aburgosc
 */
public class StringUtils {

    /**
     * Convierte un objeto a String, evitando NullPointerException. Si es null,
     * retorna cadena vacía.
     */
    public static String safe(Object obj) {
        return obj != null ? obj.toString() : "";
    }
}
