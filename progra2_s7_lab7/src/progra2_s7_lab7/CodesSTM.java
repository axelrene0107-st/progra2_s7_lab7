/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author Hp
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CodesSTM {
    
    private File file;
    private RandomAccessFile raf;
    
    public CodesSTM(File ruta) {
        try {
            file = new File(ruta, "codes.stm");
            AsegurarAbierto();
            
            // Si es nuevo, se inicializa con 1, 1, 1
            if (raf.length() == 0) {
                raf.seek(0);
                
                raf.writeInt(1); // game
                raf.writeInt(1); // player
                raf.writeInt(1); // download
                
                raf.getFD().sync();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void AsegurarAbierto() throws IOException {
        if (raf == null) {
            raf = new RandomAccessFile(file, "rw");
        }
    }


    public synchronized int SiguienteCodigoGame() throws IOException {
        raf.seek(OffsetCodigos.GAME.getPos());
        
        int codigo = raf.readInt();
        
        raf.seek(OffsetCodigos.GAME.getPos());
        raf.writeInt(codigo + 1);
        raf.getFD().sync();
        
        return codigo;
    }


    public synchronized int SiguienteCodigoPlayer() throws IOException {
        raf.seek(OffsetCodigos.PLAYER.getPos());
        
        int codigo = raf.readInt();
        
        raf.seek(OffsetCodigos.PLAYER.getPos());
        raf.writeInt(codigo + 1);
        raf.getFD().sync();
        
        return codigo;
    }

    public synchronized int SiguienteCodigoDescargas() throws IOException {
        raf.seek(OffsetCodigos.DOWNLOAD.getPos());
        
        int codigo = raf.readInt();
        
        raf.seek(OffsetCodigos.DOWNLOAD.getPos());
        raf.writeInt(codigo + 1);
        raf.getFD().sync();
        
        return codigo;
    }
    
}
