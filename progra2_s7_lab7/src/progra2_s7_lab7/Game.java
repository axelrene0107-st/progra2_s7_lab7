/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author axelr
 */
public class Game {
    private int code;
    private String titulo;
    private char sistemaOperativo; // 'W' = Windows, 'M' = Mac, 'L' = Linux
    private int edadMinima;
    private double precio;
    private int contadorDownloads;
    private String rutaImagen;

    public Game(int code, String titulo, char sistemaOperativo, int edadMinima,
                double precio, int contadorDownloads, String rutaImagen) {
        this.code = code;
        this.titulo = titulo;
        this.sistemaOperativo = sistemaOperativo;
        this.edadMinima = edadMinima;
        this.precio = precio;
        this.contadorDownloads = contadorDownloads;
        this.rutaImagen = rutaImagen;
    }

    public int getCode() { return code; }
    public String getTitulo() { return titulo; }
    public char getSistemaOperativo() { return sistemaOperativo; }
    public int getEdadMinima() { return edadMinima; }
    public double getPrecio() { return precio; }
    public int getContadorDownloads() { return contadorDownloads; }
    public String getRutaImagen() { return rutaImagen; }

    public void setPrecio(double precio) { this.precio = precio; }
    public void setContadorDownloads(int contadorDownloads) { this.contadorDownloads = contadorDownloads; }

    @Override
    public String toString() {
        return code + " - " + titulo + " [" + sistemaOperativo + "] $" + precio;
    } 
}
