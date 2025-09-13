package cl.aburgosc.sistemainventariojoyeria.exception;

/**
 *
 * @author aburgosc
 */
public class ServiceException extends Exception {

    public ServiceException(String mensaje) {
        super(mensaje);
    }

    public ServiceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
