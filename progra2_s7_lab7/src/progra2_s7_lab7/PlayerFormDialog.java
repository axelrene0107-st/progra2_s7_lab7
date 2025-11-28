package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PlayerFormDialog extends JDialog {

    private Steam steam;
    private Player edit;

    private JTextField txtUser, txtPass, txtNombre;
    private JSpinner spFecha;
    private JComboBox<String> cbTipo;
    private JLabel lblImg;
    private String rutaSel = "";

    public PlayerFormDialog(JFrame owner, Steam steam, Player edit) {
        super(owner, true);
        this.steam = steam;
        this.edit = edit;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle(edit == null ? "Registrar Player" : "Editar Player");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(10, 1, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtUser = new JTextField();
        txtPass = new JTextField();
        txtNombre = new JTextField();

        spFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spFecha, "dd/MM/yyyy");
        spFecha.setEditor(editor);

        cbTipo = new JComboBox<>(new String[]{"admin", "normal"});

        lblImg = new JLabel("Sin imagen", JLabel.CENTER);

        JButton btnImg = new JButton("Seleccionar imagen");
        btnImg.addActionListener(e -> onSelectImg());

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> onGuardar());

        // Añadir al form
        form.add(new JLabel("Username:"));
        form.add(txtUser);

        form.add(new JLabel("Password:"));
        form.add(txtPass);

        form.add(new JLabel("Nombre Completo:"));
        form.add(txtNombre);

        form.add(new JLabel("Fecha de nacimiento:"));
        form.add(spFecha);

        form.add(new JLabel("Tipo usuario:"));
        form.add(cbTipo);

        form.add(btnImg);
        form.add(lblImg);

        add(form, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        if (edit != null) {
            txtUser.setText(edit.getUsername());
            txtPass.setText(edit.getPassword());
            txtNombre.setText(edit.getNombre());
            spFecha.setValue(new Date(edit.getNacimiento()));
            cbTipo.setSelectedItem(edit.getTipoUsuario());
            rutaSel = edit.getRutaImagen();

            if (rutaSel != null && !rutaSel.isBlank()) {
                Image i = new ImageIcon(rutaSel).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(i));
                lblImg.setText("");
            }
        }
    }

    private void onSelectImg() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            rutaSel = fc.getSelectedFile().getAbsolutePath();
            Image i = new ImageIcon(rutaSel).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(i));
            lblImg.setText("");
        }
    }

    private void onGuardar() {
        try {
            String u = txtUser.getText().trim();
            String p = txtPass.getText().trim();
            String n = txtNombre.getText().trim();
            long nac = ((Date) spFecha.getValue()).getTime();
            String tipo = cbTipo.getSelectedItem().toString();

            if (u.isEmpty() || p.isEmpty() || n.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos.");
                return;
            }

            if (edit == null) {
                boolean ok = steam.addPlayer(u, p, n, nac, rutaSel, tipo);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Player registrado.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Username ya existe.");
                }
            } else {
                boolean ok = steam.updatePlayer(edit.getCode(), u, p, n, nac, rutaSel, tipo);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Player actualizado.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error actualizando player.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en datos.");
        }
    }
}
