/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author axelr
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminFrame extends JFrame {

    private Steam steam;
    private Player admin;

    private JButton btnGestionPlayers;
    private JButton btnGestionGames;
    private JButton btnVerCatalogo;
    private JButton btnReportes;

    public AdminFrame(Steam steam, Player admin) {
        this.steam = steam;
        this.admin = admin;
        initComponents();
    }

    private void initComponents() {
        setTitle("Panel Admin - " + admin.getNombre());
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        btnGestionPlayers = new JButton("Gestionar Players");
        btnGestionPlayers.addActionListener(e -> onGestionPlayers());
        panel.add(btnGestionPlayers);

        btnGestionGames = new JButton("Gestionar Juegos");
        btnGestionGames.addActionListener(e -> new GameManagerFrame(steam).setVisible(true));
        panel.add(btnGestionGames);

        btnVerCatalogo = new JButton("Ver catálogo juegos");
        btnVerCatalogo.addActionListener(e -> new CatalogoFrame(steam, admin).setVisible(true));
        panel.add(btnVerCatalogo);

        btnReportes = new JButton("Reportes Clientes");
        btnReportes.addActionListener(e -> onReportes());
        panel.add(btnReportes);
        

        add(panel, BorderLayout.CENTER);
    }

    private void onGestionPlayers() {
        new PlayerManagerFrame(steam).setVisible(true);
    }

    private void onReportes() {
        String codStr = JOptionPane.showInputDialog(this, "Código del cliente:");
        if (codStr != null) {
            try {
                int code = Integer.parseInt(codStr);
                String nombreArchivo = "reporte_player_" + code + ".txt";
                boolean ok = steam.reportForClient(code, nombreArchivo);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "REPORTE CREADO: " + nombreArchivo);
                } else {
                    JOptionPane.showMessageDialog(this, "NO SE PUEDE CREAR REPORTE");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Código inválido");
            }
        }
    }
}
