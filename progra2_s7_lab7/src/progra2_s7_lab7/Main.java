/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        Steam steam = new Steam();

        SwingUtilities.invokeLater(() -> {
            LoginWindow login = new LoginWindow(steam);
            login.setVisible(true);
        });
    }
}
