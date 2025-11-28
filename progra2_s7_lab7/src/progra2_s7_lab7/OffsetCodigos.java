/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author Hp
 */
public enum OffsetCodigos {
    GAME(0),
    PLAYER(4),
    DOWNLOAD(8);
    
    private final int pos;
    
    OffsetCodigos(int pos) {
        this.pos = pos;
    }
    
    public int getPos() {
        return pos;
    }
}
