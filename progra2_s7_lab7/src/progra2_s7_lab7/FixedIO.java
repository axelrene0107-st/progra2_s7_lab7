/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author Hp
 */

import java.io.RandomAccessFile;
import java.io.IOException;

public class FixedIO {
    
    public static void EscribirStringArreglado(RandomAccessFile raf, String s, int longitud) throws IOException {
        if (s == null) {
            s = "";
        }
        
        int n = Math.min(s.length(), longitud);
        
        for (int i = 0; i < n; i++) {
            raf.writeChar(s.charAt(i));
        }
        
        for (int i = n; i < longitud; i++) {
            raf.writeChar(' ');
        }
    }
    
    public static String LeerStringArreglado(RandomAccessFile raf, int longitud) throws IOException {
        StringBuilder sb = new StringBuilder(longitud);
        
        for (int i = 0; i < longitud; i++) {
            sb.append(raf.readChar());
        }
        
        return rtrim(sb.toString());
    }
    
    private static String rtrim(String s) {
        int i = s.length() - 1;
        
        while (i >= 0 && s.charAt(i) == ' ') {
            i--;
        }
        
        return s.substring(0, i + 1);
    }
}
