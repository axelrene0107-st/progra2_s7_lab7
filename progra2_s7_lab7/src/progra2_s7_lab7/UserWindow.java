package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;

public class UserWindow extends JFrame {

    private Steam steam;
    private int codigoUsuario;

    private JButton btnVerCatalogo;
    private JButton btnDescargar;
    private JButton btnSalir;

    public UserWindow(Steam steam, int codigoUsuario) {
        this.steam = steam;
        this.codigoUsuario = codigoUsuario;

        initComponents();
    }

    private void initComponents() {
        setTitle("Panel Usuario - Código " + codigoUsuario);
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Bienvenido, usuario #" + codigoUsuario, SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnVerCatalogo = new JButton("Ver catálogo de juegos");
        btnDescargar   = new JButton("Descargar un juego");
        btnSalir       = new JButton("Salir");

        center.add(btnVerCatalogo);
        center.add(btnDescargar);

        add(center, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.add(btnSalir);
        add(south, BorderLayout.SOUTH);

        // Eventos
        btnVerCatalogo.addActionListener(e -> verCatalogo());
        btnDescargar.addActionListener(e -> descargarJuego());
        btnSalir.addActionListener(e -> dispose());
    }


    private void verCatalogo() {
        try {
            steam.printGames();
            JOptionPane.showMessageDialog(this, "Catálogo impreso en la consola.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al mostrar catálogo.");
        }
    }


    private void descargarJuego() {
        try {
            String codStr = JOptionPane.showInputDialog(this, "Código del juego a descargar:");
            if (codStr == null) return;
            int codJuego = Integer.parseInt(codStr);

            String[] opcionesSO = {"W - Windows", "M - Mac", "L - Linux"};
            String soStr = (String) JOptionPane.showInputDialog(
                    this,
                    "Sistema operativo:",
                    "SO",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesSO,
                    opcionesSO[0]
            );
            if (soStr == null) return;
            char so = soStr.charAt(0); // W/M/L

            boolean ok = steam.downloadGame(codJuego, codigoUsuario, so);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Descarga realizada correctamente.\nSe generó el archivo en steam/downloads/");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo realizar la descarga.\nVerifique edad, SO, existencia del juego/usuario.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código de juego inválido.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al descargar juego.");
        }
    }
}
