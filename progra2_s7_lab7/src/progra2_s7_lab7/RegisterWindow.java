package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Calendar;

public class RegisterWindow extends JDialog {

    private Steam steam;

    private JTextField txtUsuario;
    private JPasswordField txtPass;
    private JTextField txtNombre;

    private JSpinner spDia;
    private JSpinner spMes;
    private JSpinner spAno;

    private JTextField txtImagen;
    private JButton btnBuscarImg;

    private JButton btnRegistrar;
    private JButton btnCancelar;

    public RegisterWindow(JFrame owner, Steam steam) {
        super(owner, "Registro de Usuario", true);
        this.steam = steam;

        initComponents();
    }

    private void initComponents() {

        setSize(500, 380);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getOwner());

        //------------------------------------------------------
        JLabel lblTitulo = new JLabel("Crear Nueva Cuenta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        //------------------------------------------------------
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUsuario = new JTextField();
        txtPass = new JPasswordField();
        txtNombre = new JTextField();

        txtImagen = new JTextField();
        txtImagen.setEditable(true);
        btnBuscarImg = new JButton("Buscar Imagen...");

        spDia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        spMes = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spAno = new JSpinner(new SpinnerNumberModel(2000, 1950, 2030, 1));

        // ----- BUSCAR IMAGEN -----
        btnBuscarImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Seleccionar imagen de usuario");
            int op = fc.showOpenDialog(this);

            if (op == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                txtImagen.setText(file.getAbsolutePath());
            }
        });

        // ----- CAMPOS DEL FORMULARIO -----
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        form.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        form.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1;
        form.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Fecha nacimiento:"), gbc);
        gbc.gridx = 1;
        JPanel fecha = new JPanel();
        fecha.add(spDia); fecha.add(new JLabel("/"));
        fecha.add(spMes); fecha.add(new JLabel("/"));
        fecha.add(spAno);
        form.add(fecha, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Imagen:"), gbc);
        gbc.gridx = 1;
        form.add(txtImagen, gbc);
        gbc.gridx = 2;
        form.add(btnBuscarImg, gbc);

        add(form, BorderLayout.CENTER);

        //------------------------------------------------------
        JPanel buttons = new JPanel();

        btnRegistrar = new JButton("Registrar");
        btnCancelar  = new JButton("Cancelar");

        buttons.add(btnRegistrar);
        buttons.add(btnCancelar);

        add(buttons, BorderLayout.SOUTH);

        //------------------------------------------------------
        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());
    }

    // =========================================================
    //  REGISTRAR NUEVO USUARIO
    // =========================================================
    private void registrar() {

        try {

            String usuario = txtUsuario.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            String nombre = txtNombre.getText().trim();
            String img = txtImagen.getText().trim();

            if (usuario.isEmpty() || pass.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos.");
                return;
            }

            int dia = (int) spDia.getValue();
            int mes = (int) spMes.getValue() - 1; // Enero = 0
            int ano = (int) spAno.getValue();

            Calendar cal = Calendar.getInstance();
            cal.set(ano, mes, dia, 0, 0, 0);
            long fechaN = cal.getTimeInMillis();

            int cod = steam.addPlayer(usuario, pass, nombre, fechaN, img, "normal");

            if (cod > 0) {
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el usuario.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar usuario.");
        }
    }
}
