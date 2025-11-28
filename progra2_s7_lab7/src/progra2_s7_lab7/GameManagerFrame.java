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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GameManagerFrame extends JFrame {

    private Steam steam;
    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnPrecio;
    private JButton btnActualizar;

    public GameManagerFrame(Steam steam) {
        this.steam = steam;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Juegos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columnas = {
                "Código", "Título", "SO", "Edad Mínima",
                "Precio", "Descargas", "Imagen"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        btnNuevo = new JButton("Registrar Juego");
        btnNuevo.addActionListener(e -> onNuevo());

        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> onEditar());

        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> onEliminar());

        btnPrecio = new JButton("Cambiar Precio");
        btnPrecio.addActionListener(e -> onCambiarPrecio());

        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarTabla());

        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 10, 10));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnPrecio);
        panelBotones.add(btnActualizar);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);

        List<Game> lista = steam.getAllGames();
        for (Game g : lista) {
            modelo.addRow(new Object[]{
                    g.getCode(),
                    g.getTitulo(),
                    g.getSistemaOperativo(),
                    g.getEdadMinima(),
                    g.getPrecio(),
                    g.getContadorDownloads(),
                    g.getRutaImagen()
            });
        }
    }

    private Integer getSelectedCode() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un juego");
            return null;
        }
        return (Integer) tabla.getValueAt(row, 0);
    }

    private void onNuevo() {
        GameFormDialog dlg = new GameFormDialog(this, steam, null);
        dlg.setVisible(true);
        cargarTabla();
    }

    private void onEditar() {
        Integer code = getSelectedCode();
        if (code == null) return;

        Game g = steam.findGameByCode(code);
        if (g == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el juego.");
            return;
        }

        GameFormDialog dlg = new GameFormDialog(this, steam, g);
        dlg.setVisible(true);
        cargarTabla();
    }

    private void onEliminar() {
        Integer code = getSelectedCode();
        if (code == null) return;

        int r = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar este juego?", "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (r == JOptionPane.YES_OPTION) {
            if (steam.deleteGame(code)) {
                JOptionPane.showMessageDialog(this, "Juego eliminado.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
            }
        }
    }

    private void onCambiarPrecio() {
        Integer code = getSelectedCode();
        if (code == null) return;

        String input = JOptionPane.showInputDialog(this,
                "Nuevo precio:");

        if (input == null) return;

        try {
            double precio = Double.parseDouble(input);
            if (steam.updatePriceFor(code, precio)) {
                JOptionPane.showMessageDialog(this, "Precio actualizado.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error actualizando precio.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
        }
    }
}