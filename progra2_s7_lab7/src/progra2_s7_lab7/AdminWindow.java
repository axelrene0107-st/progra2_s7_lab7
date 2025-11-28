package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Calendar;

public class AdminWindow extends JFrame {

    private Steam steam;
    private int codigoAdmin;

    public AdminWindow(Steam steam, int codigoAdmin) {
        this.steam = steam;
        this.codigoAdmin = codigoAdmin;

        initComponents();
    }

    private void initComponents() {

        setTitle("Panel Administrador");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // --------- CONTENEDOR PRINCIPAL ---------
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Registrar Juegos", crearPanelRegistrarJuegos());
        tabs.addTab("Registrar Jugadores", crearPanelRegistrarJugadores());
        tabs.addTab("Modificar Precio", crearPanelModificarPrecio());
        tabs.addTab("Reportes", crearPanelReportes());
        tabs.addTab("Ver Juegos", crearPanelVerJuegos());

        add(tabs);
    }

    private JPanel crearPanelRegistrarJuegos() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtTitulo = new JTextField();
        JComboBox<String> cmbSO = new JComboBox<>(new String[]{"W - Windows", "M - Mac", "L - Linux"});
        JSpinner spEdad = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
        JTextField txtPrecio = new JTextField();
        JTextField txtImagen = new JTextField();
        JButton btnBuscarImg = new JButton("Buscar Imagen...");
        JButton btnGuardar = new JButton("Registrar Juego");

        // -------- BUSCAR IMAGEN --------
        btnBuscarImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Seleccionar imagen del juego");
            int op = fc.showOpenDialog(this);

            if (op == JFileChooser.APPROVE_OPTION) {
                File img = fc.getSelectedFile();
                txtImagen.setText(img.getAbsolutePath());  // Ruta absoluta
            }
        });

        // ------- CAMPOS DEL FORMULARIO -------
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; panel.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Sistema Operativo:"), gbc);
        gbc.gridx = 1; panel.add(cmbSO, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Edad mínima:"), gbc);
        gbc.gridx = 1; panel.add(spEdad, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1; panel.add(txtPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Imagen:"), gbc);
        gbc.gridx = 1; panel.add(txtImagen, gbc);

        gbc.gridx = 2; gbc.gridy = 4; panel.add(btnBuscarImg, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(btnGuardar, gbc);

        // ------- GUARDAR JUEGO -------
        btnGuardar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                char so = cmbSO.getSelectedItem().toString().charAt(0);
                int edad = (int) spEdad.getValue();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                String img = txtImagen.getText().trim();

                int cod = steam.addGame(titulo, so, edad, precio, img);

                JOptionPane.showMessageDialog(this,
                        "Juego registrado con código #" + cod);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearPanelRegistrarJugadores() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtUsuario = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JTextField txtNombre = new JTextField();

        JSpinner spDia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        JSpinner spMes = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        JSpinner spAno = new JSpinner(new SpinnerNumberModel(2000, 1950, 2030, 1));

        JTextField txtImagen = new JTextField();

        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"normal", "admin"});

        JButton btnRegistrar = new JButton("Registrar Jugador");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1; panel.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; panel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1; panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Fecha nacimiento:"), gbc);
        gbc.gridx = 1;
        JPanel fecha = new JPanel();
        fecha.add(spDia); fecha.add(new JLabel("/"));
        fecha.add(spMes); fecha.add(new JLabel("/"));
        fecha.add(spAno);
        panel.add(fecha, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Imagen (ruta relativa):"), gbc);
        gbc.gridx = 1; panel.add(txtImagen, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; panel.add(cmbTipo, gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(btnRegistrar, gbc);

        btnRegistrar.addActionListener(e -> {

            try {
                String usuario = txtUsuario.getText().trim();
                String pass = new String(txtPass.getPassword()).trim();
                String nombre = txtNombre.getText().trim();

                int d = (int) spDia.getValue();
                int m = (int) spMes.getValue() - 1;
                int a = (int) spAno.getValue();
                Calendar cal = Calendar.getInstance();
                cal.set(a, m, d, 0, 0, 0);
                long fechaN = cal.getTimeInMillis();

                String img = txtImagen.getText().trim();
                String tipo = cmbTipo.getSelectedItem().toString();

                int cod = steam.addPlayer(usuario, pass, nombre, fechaN, img, tipo);

                JOptionPane.showMessageDialog(this,
                        "Jugador registrado con código #" + cod);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        return panel;
    }
    
    

    private JPanel crearPanelModificarPrecio() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtCodigo = new JTextField();
        JTextField txtPrecio = new JTextField();
        JButton btnCambiar = new JButton("Aplicar Cambio");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Código del juego:"), gbc);
        gbc.gridx = 1; panel.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nuevo precio:"), gbc);
        gbc.gridx = 1; panel.add(txtPrecio, gbc);

        gbc.gridx = 1; gbc.gridy = 2; panel.add(btnCambiar, gbc);

        btnCambiar.addActionListener(e -> {
            try {
                int cod = Integer.parseInt(txtCodigo.getText().trim());
                double nuevo = Double.parseDouble(txtPrecio.getText().trim());

                boolean ok = steam.updatePriceFor(cod, nuevo);

                JOptionPane.showMessageDialog(this,
                        ok ? "Precio actualizado." : "Juego no encontrado.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
            }
        });

        return panel;
    }

    private JPanel crearPanelReportes() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField txtCod = new JTextField(10);
        JTextField txtArchivo = new JTextField("reporte.txt", 10);
        JButton btnGenerar = new JButton("Generar");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Código cliente:"), gbc);
        gbc.gridx = 1; panel.add(txtCod, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Archivo salida:"), gbc);
        gbc.gridx = 1; panel.add(txtArchivo, gbc);

        gbc.gridx = 1; gbc.gridy = 2; panel.add(btnGenerar, gbc);

        btnGenerar.addActionListener(e -> {
            try {
                int cod = Integer.parseInt(txtCod.getText().trim());
                String archivo = txtArchivo.getText().trim();

                boolean ok = steam.reportForClient(cod, archivo);

                JOptionPane.showMessageDialog(this,
                        ok ? "Reporte creado." : "Error, cliente no existe.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
            }
        });

        return panel;
    }

    private JPanel crearPanelVerJuegos() {

        JPanel panel = new JPanel(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        JButton btnCargar = new JButton("Cargar Juegos");

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(btnCargar, BorderLayout.SOUTH);

        btnCargar.addActionListener(e -> {
            try {
                long n = steam.gamesSTM.Conteo();
                area.setText("");

                for (int i = 0; i < n; i++) {
                    area.append(steam.gamesSTM.LeerRegistroComoLinea(i) + "\n");
                }

            } catch (Exception ex) {
                area.setText("Error al cargar juegos.");
            }
        });

        return panel;
    }
}
