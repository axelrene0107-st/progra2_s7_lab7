package progra2_s7_lab7;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PlayerManagerFrame extends JFrame {

    private Steam steam;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PlayerManagerFrame(Steam steam) {
        this.steam = steam;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Players");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        modelo = new DefaultTableModel(new String[]{
                "Código", "Username", "Nombre", "Nacimiento",
                "Descargas", "Tipo", "Imagen"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JButton btnNuevo = new JButton("Registrar Player");
        btnNuevo.addActionListener(e -> onNuevo());

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> onEditar());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> onEliminar());

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarTabla());

        JPanel botones = new JPanel(new GridLayout(1, 4, 10, 10));
        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        add(scroll, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Player> lista = steam.getAllPlayers();
        for (Player p : lista) {
            modelo.addRow(new Object[]{
                    p.getCode(),
                    p.getUsername(),
                    p.getNombre(),
                    p.getNacimiento(),
                    p.getContadorDownloads(),
                    p.getTipoUsuario(),
                    p.getRutaImagen()
            });
        }
    }

    private Integer getSelectedCode() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un player.");
            return null;
        }
        return (Integer) tabla.getValueAt(row, 0);
    }

    private void onNuevo() {
        new PlayerFormDialog(this, steam, null).setVisible(true);
        cargarTabla();
    }

    private void onEditar() {
        Integer code = getSelectedCode();
        if (code == null) return;

        Player p = steam.findPlayerByCode(code);
        new PlayerFormDialog(this, steam, p).setVisible(true);
        cargarTabla();
    }

    private void onEliminar() {
        Integer code = getSelectedCode();
        if (code == null) return;

        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este player?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (r == JOptionPane.YES_OPTION) {
            if (steam.deletePlayer(code))
                JOptionPane.showMessageDialog(this, "Player eliminado.");
            else
                JOptionPane.showMessageDialog(this, "Error al eliminar.");
            cargarTabla();
        }
    }
}
