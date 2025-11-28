package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    private Steam steam;

    private JTextField txtUser;
    private JPasswordField txtPass;

    private JButton btnLogin;
    private JButton btnRegister;

    public LoginWindow(Steam steam) {
        this.steam = steam;
        initComponents();
    }

    private void initComponents() {

        setTitle("Steam - Inicio de Sesión");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //-------------------------- PANEL TITULO --------------------------
        JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        //-------------------------- PANEL FORMULARIO ------------------------
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUser = new JTextField();
        txtPass = new JPasswordField();

        // ANCHOS PREFERIDOS (CORRECCIÓN)
        txtUser.setPreferredSize(new Dimension(180, 25));
        txtPass.setPreferredSize(new Dimension(180, 25));

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        form.add(txtUser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        form.add(txtPass, gbc);

        add(form, BorderLayout.CENTER);

        //-------------------------- BOTONES ---------------------------------
        JPanel buttons = new JPanel();

        btnLogin = new JButton("Entrar");
        btnRegister = new JButton("Crear Cuenta");

        buttons.add(btnLogin);
        buttons.add(btnRegister);

        add(buttons, BorderLayout.SOUTH);

        //-------------------------- EVENTOS ----------------------------------
        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> openRegister());
    }

    private void doLogin() {

        String usuario = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        try {
            Object[] p = steam.buscarUsuarioPorCredenciales(usuario, pass);

            if (p == null) {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
                return;
            }

            int codigo = (int) p[0];
            String tipo = p[7].toString().trim();

            if (tipo.equals("admin")) {
                new AdminWindow(steam, codigo).setVisible(true);
            } else {
                new UserWindow(steam, codigo).setVisible(true);
            }

            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openRegister() {
        new RegisterWindow(this, steam).setVisible(true);
    }
}
