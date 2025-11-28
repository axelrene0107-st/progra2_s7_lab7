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

public class GamesSTM {
    
    private File file;
    private RandomAccessFile raf;
    
    public GamesSTM(File ruta) {
        try {
            file = new File(ruta, "games.stm");
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
    
    public synchronized long Conteo() throws IOException {
        return raf.length() / OffsetJuegos.TAMANO_REC;
    }
    
    /*
        Agrega un juego al final del archivo
    
        Formato:
        int codigo
        String titulo
        char so (sistemaoperativo)
        int edadminima
        double precio
        int contadordescargas
        String imgrel (imagen relativa)
    */
    public synchronized int AnadirJuego(int codigo, String titulo, char so, int edadminima, double precio, String imgrel) throws IOException {
        long pos = raf.length();
        raf.seek(pos);
        
        raf.writeInt(codigo);
        FixedIO.EscribirStringArreglado(raf, safe(titulo), 60);
        raf.writeChar(so);
        raf.writeInt(edadminima);
        raf.writeDouble(precio);
        raf.writeInt(0); //Contador de descargas
        FixedIO.EscribirStringArreglado(raf, safe(imgrel), 60);
        
        return codigo;
    }
    
    /*
        Busca un juego por su codigo, y si no lo encuentra, devuelve -1
    */
    public synchronized long EncontrarIndicePorCodigo(int codigo) throws IOException {
        long n = Conteo();
        
        for (int i = 0; i < n; i++) {
            long base = i * OffsetJuegos.TAMANO_REC;
            raf.seek(base + OffsetJuegos.CODIGO.getPos());
            
            if (raf.readInt() == codigo) {
                return i;
            }
        }
        
        return -1;
    }
    
    /*
        Actualiza el precio de un juego que haya encontrado por su codigo
    */
    public synchronized boolean ActualizarPrecio(int codigo, double nuevoprecio) throws IOException {
        long indice = EncontrarIndicePorCodigo(codigo);
        
        if (indice < 0) {
            return false; //Porque no encontro nada
        }
        
        long base = indice * OffsetJuegos.TAMANO_REC;
        
        raf.seek(base * OffsetJuegos.PRECIO.getPos());
        raf.writeDouble(nuevoprecio);
        
        return true;
    }
    
    /*
        Lee un registro por indice y lo devuelve como arreglo de objetos
    
        Formato:
        int codigo
        String titulo
        char so (sistemaoperativo)
        int edadminima
        double precio
        int contadordescargas
        String imgrel (imagen relativa)
    */
    public synchronized Object[] LeerRegistroComoArreglo(int indice) throws IOException {
        long base = (long) indice * OffsetJuegos.TAMANO_REC;
        raf.seek(base);
        
        int codigo = raf.readInt();
        String titulo = raf.readUTF();
        char so = raf.readChar();
        int edadminima = raf.readInt();
        double precio = raf.readDouble();
        int descargas = raf.readInt();
        String imgrel = FixedIO.LeerStringArreglado(raf, 120);
        
        return new Object[]{codigo, titulo, so, edadminima, precio, descargas, imgrel};
    }
    
    /*
        Lee un registro por indice y devuelve una linea formateada
    */
    public synchronized String LeerRegistroComoLinea(int inice) throws IOException {
        Object[] r = LeerRegistroComoArreglo(inice);
        
        int codigo = (Integer) r[0];
        String titulo = (String) r[1];
        char so = (Character) r[2];
        int edadminima = (Integer) r[3];
        double precio = (Double) r[4];
        int descargas = (Integer) r[5];
        String imgrel = (String) r[6];
        
        return String.format("#%d | %s | SO:%c | +%d | $%.2f | dls:%d | %s", codigo, titulo, so, edadminima, precio, descargas, imgrel);
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
