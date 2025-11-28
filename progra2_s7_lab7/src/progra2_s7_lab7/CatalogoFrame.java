package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CatalogoFrame extends JFrame {

    private Steam steam;
    private Player user;

    private JPanel panelCatalogo;
    private JScrollPane scroll;

    public CatalogoFrame(Steam steam, Player user) {
        this.steam = steam;
        this.user = user;
        initComponents();
        cargarCatalogo();
    }

    private void initComponents() {
        setTitle("Catálogo de Juegos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panelCatalogo = new JPanel();
        panelCatalogo.setLayout(new GridLayout(0, 3, 15, 15));
        panelCatalogo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scroll = new JScrollPane(panelCatalogo);
        add(scroll, BorderLayout.CENTER);
    }

    private void cargarCatalogo() {
        panelCatalogo.removeAll();

        List<Game> games = steam.getAllGames();

        for (Game game : games) {
            panelCatalogo.add(crearCardGame(game));
        }

        panelCatalogo.revalidate();
        panelCatalogo.repaint();
    }

    private JPanel crearCardGame(Game g) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setBackground(Color.WHITE);

        // IMAGEN
        JLabel lblImg;
        if (g.getRutaImagen() != null && !g.getRutaImagen().isBlank()) {
            Image img = new ImageIcon(g.getRutaImagen())
                    .getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            lblImg = new JLabel(new ImageIcon(img));
        } else {
            lblImg = new JLabel("Sin imagen", JLabel.CENTER);
        }

        // INFO
        JPanel panelInfo = new JPanel(new GridLayout(6, 1));
        panelInfo.add(new JLabel("Título: " + g.getTitulo()));
        panelInfo.add(new JLabel("SO: " + g.getSistemaOperativo()));
        panelInfo.add(new JLabel("Edad mínima: " + g.getEdadMinima()));
        panelInfo.add(new JLabel("Precio: $" + g.getPrecio()));
        panelInfo.add(new JLabel("Descargas: " + g.getContadorDownloads()));

        JButton btnDescargar = new JButton("DESCARGAR");
        btnDescargar.addActionListener(e -> descargarJuego(g));

        // Orden visual
        card.add(lblImg, BorderLayout.NORTH);
        card.add(panelInfo, BorderLayout.CENTER);
        card.add(btnDescargar, BorderLayout.SOUTH);

        return card;
    }

    private void descargarJuego(Game game) {

        char so = userSistemaOperativo();

        if (so == 0) return; // cancelado

        boolean ok = steam.downloadGame(game.getCode(), user.getCode(), so);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Descarga exitosa!\nArchivo generado en steam/downloads/.");

            // actualizar descargas en memoria
            user.setContadorDownloads(user.getContadorDownloads() + 1);
            game.setContadorDownloads(game.getContadorDownloads() + 1);

            cargarCatalogo();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo descargar el juego.\n"
                    + "Posibles motivos:\n"
                    + "• No cumple la edad mínima.\n"
                    + "• No es compatible con el sistema operativo.\n"
                    + "• El juego o usuario no existe.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private char userSistemaOperativo() {
        String[] opciones = {"Windows", "Mac", "Linux"};
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione su sistema operativo:",
                "Sistema Operativo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == null) return 0;

        switch (seleccion) {
            case "Windows": return 'W';
            case "Mac": return 'M';
            case "Linux": return 'L';
        }
        return 0;
    }
}
