package cl.aburgosc.sistemainventariojoyeria;

import cl.aburgosc.sistemainventariojoyeria.ui.SistemaPrincipal;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.DBInitializer;
import javax.swing.SwingUtilities;

/**
 *
 * @author aburgosc
 */
public class SistemaInventarioJoyeria {

    public static void main(String[] args) {
        DBInitializer.initializeDatabase();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DBConnection.closePool();
            System.out.println("Pool de conexiones cerrado.");
        }));

        SwingUtilities.invokeLater(() -> new SistemaPrincipal().setVisible(true));
    }
}
