package progra2_s7_lab7;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private Steam steam;

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JButton btnCrearCuenta;


    public LoginFrame(Steam steam) {
        this.steam = steam;
        initComponents();
    }

    private void initComponents() {
        setTitle("Steam - Inicio de Sesión");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(new JLabel("Usuario:"));
        txtUser = new JTextField();
        panel.add(txtUser);

        panel.add(new JLabel("Contraseña:"));
        txtPass = new JPasswordField();
        panel.add(txtPass);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(e -> onLogin());
        panel.add(btnLogin);
        
        btnCrearCuenta = new JButton("Crear Cuenta");
        btnCrearCuenta.addActionListener(e -> abrirRegistro());
        panel.add(btnCrearCuenta);


        add(panel, BorderLayout.CENTER);
    }
    
    private void abrirRegistro() {
    new RegisterDialog(this, steam).setVisible(true);
    }

    private void onLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar usuario y contraseña.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Player p = steam.login(user, pass);

        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "Credenciales inválidas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // LOGIN EXITOSO
        JOptionPane.showMessageDialog(this,
                "Bienvenido, " + p.getNombre() + ".");

        if (p.getTipoUsuario().equalsIgnoreCase("admin")) {
            new AdminFrame(steam, p).setVisible(true);
        } else {
            new UserFrame(steam, p).setVisible(true);
        }

        dispose(); // cerrar login
    }
}
