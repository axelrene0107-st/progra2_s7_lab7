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

public class GameFormDialog extends JDialog {

    private Steam steam;
    private Game gameEditar;

    private JTextField txtTitulo;
    private JComboBox<String> cbSO;
    private JSpinner spEdad;
    private JTextField txtPrecio;
    private JLabel lblImagen;
    private JButton btnImagen;
    private JButton btnGuardar;

    private String rutaImagenSeleccionada = "";

    public GameFormDialog(JFrame owner, Steam steam, Game gameEditar) {
        super(owner, true);
        this.steam = steam;
        this.gameEditar = gameEditar;

        initComponents();
        cargarDatosSiEditando();
    }

    private void initComponents() {
        setTitle(gameEditar == null ? "Registrar Juego" : "Editar Juego");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(8, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        txtTitulo = new JTextField();
        cbSO = new JComboBox<>(new String[]{"W", "M", "L"}); // Windows, Mac, Linux
        spEdad = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        txtPrecio = new JTextField();

        btnImagen = new JButton("Seleccionar Imagen");
        btnImagen.addActionListener(e -> onSeleccionarImagen());

        lblImagen = new JLabel("Sin imagen", JLabel.CENTER);

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> onGuardar());

        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);

        panel.add(new JLabel("Sistema Operativo (W/M/L):"));
        panel.add(cbSO);

        panel.add(new JLabel("Edad mínima:"));
        panel.add(spEdad);

        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);

        panel.add(btnImagen);
        panel.add(lblImagen);

        add(panel, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void cargarDatosSiEditando() {
        if (gameEditar != null) {
            txtTitulo.setText(gameEditar.getTitulo());
            cbSO.setSelectedItem(String.valueOf(gameEditar.getSistemaOperativo()));
            spEdad.setValue(gameEditar.getEdadMinima());
            txtPrecio.setText(String.valueOf(gameEditar.getPrecio()));
            rutaImagenSeleccionada = gameEditar.getRutaImagen();

            if (rutaImagenSeleccionada != null && !rutaImagenSeleccionada.isEmpty()) {
                Image img = new ImageIcon(rutaImagenSeleccionada)
                        .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblImagen.setText("");
                lblImagen.setIcon(new ImageIcon(img));
            }
        }
    }

    private void onSeleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            rutaImagenSeleccionada = fc.getSelectedFile().getAbsolutePath();

            Image img = new ImageIcon(rutaImagenSeleccionada)
                    .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblImagen.setText("");
            lblImagen.setIcon(new ImageIcon(img));
        }
    }

    private void onGuardar() {
        try {
            String titulo = txtTitulo.getText().trim();
            char so = cbSO.getSelectedItem().toString().charAt(0);
            int edadMin = (int) spEdad.getValue();
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            if (titulo.isEmpty() || rutaImagenSeleccionada.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe llenar los campos y seleccionar una imagen");
                return;
            }

            if (gameEditar == null) {
                boolean ok = steam.addGame(titulo, so, edadMin, precio, rutaImagenSeleccionada);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Juego registrado.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error registrando juego.");
                }
            } else {
                boolean ok = steam.updateGame(gameEditar.getCode(), titulo, so, edadMin, precio, rutaImagenSeleccionada);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Juego actualizado.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.");
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
        }
    }
}