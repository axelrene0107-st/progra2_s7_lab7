package progra2_s7_lab7;

import java.util.List;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Steam steam = new Steam();

            // Verificar si existe un administrador
            boolean existeAdmin = false;
            List<Player> jugadores = steam.getAllPlayers();

            for (Player p : jugadores) {
                if (p.getTipoUsuario().equalsIgnoreCase("admin")) {
                    existeAdmin = true;
                    break;
                }
            }

            // Si NO existe, crear uno por defecto
            if (!existeAdmin) {
                long nacimiento = System.currentTimeMillis() - (30L * 365 * 24 * 60 * 60 * 1000); // edad 30 simbólica
                steam.addPlayer("admin", "admin", "Administrador Principal", nacimiento, "", "admin");

                System.out.println("Administrador creado por defecto:");
                System.out.println("Usuario: admin  |  Contraseña: admin");
            }

            // Abrir ventana de Login
            new LoginFrame(steam).setVisible(true);
        });
    }
}
