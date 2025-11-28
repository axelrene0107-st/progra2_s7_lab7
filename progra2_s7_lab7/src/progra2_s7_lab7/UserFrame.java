/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class UserFrame extends JFrame {

    private Steam steam;
    private Player user;

    private JButton btnCatalogo;
    private JButton btnMisDescargas;
    private JButton btnPerfil;
    private JButton btnDescargasTotales;
    private JButton btnCerrar;

    public UserFrame(Steam steam, Player user) {
        this.steam = steam;
        this.user = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Bienvenido, " + user.getNombre());
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        btnCatalogo = new JButton("Ver Catálogo de Juegos");
        btnCatalogo.addActionListener(e ->
                new CatalogoFrame(steam, user).setVisible(true)
        );

        btnMisDescargas = new JButton("Mis Descargas");
        btnMisDescargas.addActionListener(e -> mostrarMisDescargas());

        btnPerfil = new JButton("Configurar Perfil");
        btnPerfil.addActionListener(e ->
                new UserProfileDialog(this, steam, user).setVisible(true)
        );

        btnDescargasTotales = new JButton("Mi Contador de Descargas");
        btnDescargasTotales.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Has realizado: " + user.getContadorDownloads() + " descargas.")
        );

        btnCerrar = new JButton("Cerrar Sesión");
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnCatalogo);
        panel.add(btnMisDescargas);
        panel.add(btnPerfil);
        panel.add(btnDescargasTotales);
        panel.add(btnCerrar);

        add(panel, BorderLayout.CENTER);
    }

    private void mostrarMisDescargas() {
        File folder = new File("steam/downloads");

        String[] archivos = folder.list((dir, name) ->
                name.toLowerCase().endsWith(".stm"));

        if (archivos == null || archivos.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Aún no tienes descargas registradas.");
            return;
        }

        JTextArea area = new JTextArea(15, 40);
        area.setEditable(false);

        for (String file : archivos) {
            area.append(file + "\n");
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Mis Descargas", JOptionPane.INFORMATION_MESSAGE);
    }
}
