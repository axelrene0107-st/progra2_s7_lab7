/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author Hp
 */
public enum OffsetJuegos {
    CODIGO(0),
    TITULO(4),
    SO(124),
    EDAD_MIN(126),
    PRECIO(130),
    CONT_DESCARGAS(138),
    IMG_REL(142);
    
    public static final int TAMANO_REC = 382;
    private final int pos;
    
    OffsetJuegos(int pos) {
        this.pos = pos;
    }
    
    public int getPos() {
        return pos;
    }
}
