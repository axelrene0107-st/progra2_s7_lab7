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

public class UserProfileDialog extends JDialog {

    private Steam steam;
    private Player user;

    private JTextField txtNombre;
    private JPasswordField txtPassword;
    private JLabel lblImagen;
    private JButton btnSeleccionarImagen;
    private JButton btnGuardar;

    private String rutaImagenSeleccionada;

    public UserProfileDialog(JFrame owner, Steam steam, Player user) {
        super(owner, true);
        this.steam = steam;
        this.user = user;
        this.rutaImagenSeleccionada = user.getRutaImagen();
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Configurar Perfil");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Nueva contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        btnSeleccionarImagen = new JButton("Seleccionar nueva imagen");
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        panel.add(btnSeleccionarImagen);

        lblImagen = new JLabel("Imagen actual", JLabel.CENTER);
        panel.add(lblImagen);

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        add(panel, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        txtNombre.setText(user.getNombre());
        txtPassword.setText(user.getPassword());

        if (user.getRutaImagen() != null && !user.getRutaImagen().isEmpty()) {
            Image img = new ImageIcon(user.getRutaImagen())
                    .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblImagen.setText("");
            lblImagen.setIcon(new ImageIcon(img));
        }
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            rutaImagenSeleccionada = fc.getSelectedFile().getAbsolutePath();
            Image img = new ImageIcon(rutaImagenSeleccionada)
                    .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);

            lblImagen.setIcon(new ImageIcon(img));
            lblImagen.setText("");
        }
    }

    private void guardarCambios() {
        String nuevoNombre = txtNombre.getText().trim();
        String nuevaPass = new String(txtPassword.getPassword()).trim();

    if (nuevoNombre.isEmpty() || nuevaPass.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Debe completar nombre y contraseña.",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    boolean ok = steam.updatePlayer(
            user.getCode(),
            user.getUsername(),        // se mantiene igual
            nuevaPass,
            nuevoNombre,
            user.getNacimiento(),      // conserva fecha
            rutaImagenSeleccionada,
            user.getTipoUsuario()      // conserva tipo
    );

    if (ok) {
        JOptionPane.showMessageDialog(this,
                "Perfil actualizado correctamente.");

        // Actualizar en RAM
        user.setNombre(nuevoNombre);
        user.setPassword(nuevaPass);
        user.setRutaImagen(rutaImagenSeleccionada);

        dispose();
    } else {
        JOptionPane.showMessageDialog(this,
                "No se pudo actualizar el perfil.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
