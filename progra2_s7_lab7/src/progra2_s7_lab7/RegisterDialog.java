/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class RegisterDialog extends JDialog {

    private Steam steam;

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JTextField txtNombre;
    private JSpinner spFecha;
    private JLabel lblImagen;
    private JButton btnImagen;
    private JButton btnRegistrar;

    private String rutaImagen = "";

    public RegisterDialog(JFrame owner, Steam steam) {
        super(owner, true);
        this.steam = steam;
        initComponents();
    }

    private void initComponents() {
        setTitle("Crear Cuenta");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(10, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        form.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        form.add(txtNombre);

        form.add(new JLabel("Username:"));
        txtUser = new JTextField();
        form.add(txtUser);

        form.add(new JLabel("Contraseña:"));
        txtPass = new JPasswordField();
        form.add(txtPass);

        form.add(new JLabel("Fecha de nacimiento:"));
        spFecha = new JSpinner(new SpinnerDateModel());
        form.add(spFecha);

        lblImagen = new JLabel("Sin imagen", JLabel.CENTER);
        form.add(lblImagen);

        btnImagen = new JButton("Seleccionar imagen");
        btnImagen.addActionListener(e -> seleccionarImagen());
        form.add(btnImagen);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrar());
        form.add(btnRegistrar);

        add(form, BorderLayout.CENTER);
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            rutaImagen = fc.getSelectedFile().getAbsolutePath();

            Image img = new ImageIcon(rutaImagen)
                    .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);

            lblImagen.setIcon(new ImageIcon(img));
            lblImagen.setText("");
        }
    }

    private void registrar() {

        String nombre = txtNombre.getText().trim();
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (nombre.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe completar todos los campos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime((java.util.Date) spFecha.getValue());
        long nacimiento = cal.getTimeInMillis();

        boolean ok = steam.addPlayer(user, pass, nombre, nacimiento, rutaImagen, "normal");

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Cuenta creada exitosamente.\nYa puede iniciar sesión.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "El username ya existe. Intente otro.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
