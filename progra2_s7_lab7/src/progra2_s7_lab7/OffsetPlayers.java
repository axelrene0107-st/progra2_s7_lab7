/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author Hp
 */
public enum OffsetPlayers {
    CODIGO(0),
    USUARIO(4),
    CONTRASENA(84),
    NOMBRE(164),
    MES_NACIMIENTO(284),
    CONT_DESCARGAS(292),
    IMG_REL(296),
    TIPO(536);
    
    public static final int TAMANO_REC = 556;
    private final int pos;
    
    OffsetPlayers(int pos) {
        this.pos = pos;
    }
    
    public int getPos() {
        return pos;
    }
}
