/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author axelr
 */

import com.toedter.calendar.JDateChooser; // Solo si usas la librería JCalendar

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Date;

public class PlayerRegisterDialog extends JDialog {

    private Steam steam;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtNombre;
    private JDateChooser dateNacimiento;  
    private JButton btnImagen;
    private JLabel lblImagen;
    private JButton btnRegistrar;

    private String rutaImagen = "";

    public PlayerRegisterDialog(JFrame owner, Steam steam) {
        super(owner, true);
        this.steam = steam;
        initComponents();
    }

    private void initComponents() {
        setTitle("Crear Cuenta Nueva");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(10, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtNombre = new JTextField();

        dateNacimiento = new JDateChooser();
        dateNacimiento.setDateFormatString("dd/MM/yyyy");

        btnImagen = new JButton("Seleccionar Imagen");
        btnImagen.addActionListener(e -> seleccionarImagen());

        lblImagen = new JLabel("Sin imagen", JLabel.CENTER);

        btnRegistrar = new JButton("Crear Cuenta");
        btnRegistrar.addActionListener(e -> registrar());

        panel.add(new JLabel("Nombre Completo:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Username:"));
        panel.add(txtUsername);

        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);

        panel.add(new JLabel("Fecha de nacimiento:"));
        panel.add(dateNacimiento);

        panel.add(btnImagen);
        panel.add(lblImagen);

        add(panel, BorderLayout.CENTER);
        add(btnRegistrar, BorderLayout.SOUTH);
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            rutaImagen = fc.getSelectedFile().getAbsolutePath();

            lblImagen.setText("");
            Image img = new ImageIcon(rutaImagen).getImage()
                    .getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(img));
        }
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        Date date = dateNacimiento.getDate();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || date == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe completar todos los campos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long nacimientoMillis = date.getTime();

        boolean ok = steam.addPlayer(username, password, nombre,
                nacimientoMillis, rutaImagen, "normal");

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Cuenta creada correctamente.\nAhora puede iniciar sesión.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al crear cuenta. ¿Username ya existe?",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

