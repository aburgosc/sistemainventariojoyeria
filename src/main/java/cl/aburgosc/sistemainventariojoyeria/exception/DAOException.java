package cl.aburgosc.sistemainventariojoyeria.exception;

/**
 *
 * @author aburgosc
 */
public class DAOException extends Exception {

    public DAOException(String mensaje) {
        super(mensaje);
    }

    public DAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
