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

public class PlayersSTM {
    
    private File file;
    private RandomAccessFile raf;
    
    public PlayersSTM(File ruta) {
        try {
            file = new File(ruta, "players.stm");
            AsegurarAbierto();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void AsegurarAbierto() throws IOException {
        if (raf == null) {
            raf = new RandomAccessFile(file, "rw");
        }
    }
    
    /*
        Cantidad de registros en el archivo
    */
    public synchronized long Conteo() throws IOException {
        return raf.length() / OffsetPlayers.TAMANO_REC;
    }
    
    /*
        Agregar un jugador al final del archivo
    
        Formato:
        int codigo
        String usuario
        String contrasena   
        String nombrecompleto
        long fechanacimiento
        int descargas (que siempre empieza en 0)
        String imgrel (la ruta relativa a la imagen)
        String tipo (puede ser admin o normal)
    */
    public synchronized int AnadirJugador(int codigo, String usuario, String contrasena, String nombrecompleto, long fechaN, int descargas, String imgrel, String tipo) throws IOException {
        long pos = raf.length();
        raf.seek(pos);
        
        raf.writeInt(codigo);
        FixedIO.EscribirStringArreglado(raf, safe(usuario), 40);
        FixedIO.EscribirStringArreglado(raf, safe(contrasena), 40);
        FixedIO.EscribirStringArreglado(raf, safe(nombrecompleto), 60);
        raf.writeLong(fechaN);
        raf.writeInt(0);
        FixedIO.EscribirStringArreglado(raf, safe(imgrel), 120);
        FixedIO.EscribirStringArreglado(raf, safe(tipo), 10);
        
        return codigo;
    }
    
    /*
        Busca un jugador por su codigo
    */
    public synchronized long EncontrarIndicePorCodigo(int codigo) throws IOException {
        long n = Conteo();
        
        for (int i = 0; i < n; i++) {
            long base = (long) i * OffsetPlayers.TAMANO_REC;
            raf.seek(base + OffsetPlayers.CODIGO.getPos());
            
            if (raf.readInt() == codigo) {
                return i;
            }
        }
        
        return -1;
    }
    
    /*
        Lee un registro por indice y lo devuelve como arreglo de objetos
    
        Formato:
        int codigo
        String usuario
        String contrasena   
        String nombrecompleto
        long fechanacimiento
        int descargas (que siempre empieza en 0)
        String imgrel (la ruta relativa a la imagen)
        String tipo (puede ser admin o normal)
    */
    public synchronized Object[] LeerRegistroComoArreglo(int indice) throws IOException {
        long base = (long) indice * OffsetPlayers.TAMANO_REC;
        raf.seek(base);
        
        int codigo = raf.readInt();
        String usuario = FixedIO.LeerStringArreglado(raf, 40);
        String contrasena = FixedIO.LeerStringArreglado(raf, 40);
        String nombrecompleto = FixedIO.LeerStringArreglado(raf, 60);
        long fechanacimiento = raf.readLong();
        int descargas = raf.readInt();
        String imgrel = FixedIO.LeerStringArreglado(raf, 120);
        String tipo = FixedIO.LeerStringArreglado(raf, 10);
        
        return new Object[]{codigo, usuario, contrasena, nombrecompleto, fechanacimiento, descargas, imgrel, tipo};
    }
    
    /*
        Lee un registro por indice y devuelve una linea formateada
    */
    public synchronized String LeerRegistroComoLinea(int inice) throws IOException {
        Object[] r = LeerRegistroComoArreglo(inice);
        
        int codigo = (Integer) r[0];
        String usuario = (String) r[1];
        String contrasena = (String) r[2];
        String nombrecompleto = (String) r[2];
        long fechanacimiento = (Long) r[3];
        int descargas = (Integer) r[4];
        String imgrel = (String) r[5];
        String tipo = (String) r[6];
        
        return String.format("#%d | %s | SO:%c | +%d | $%.2f | dls:%d | %s", codigo, usuario, contrasena, nombrecompleto, fechanacimiento, descargas, imgrel, tipo);
    }
    
    /*
        Imprime todos los juegos
    */
    public synchronized void ImprimirTodo() throws IOException {
        long n = Conteo();
        
        for (int i = 0; i < n; i++) {
            System.out.println(LeerRegistroComoLinea(i));
        }
    }
    
    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
