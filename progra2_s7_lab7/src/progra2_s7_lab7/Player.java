/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2_s7_lab7;

/**
 *
 * @author axelr
 */
public class Player {
    private int code;
    private String username;
    private String password;
    private String nombre;
    private long nacimiento; // millis desde 1970
    private int contadorDownloads;
    private String rutaImagen;
    private String tipoUsuario; // "admin" o "normal"

    public Player(int code, String username, String password, String nombre,
                  long nacimiento, int contadorDownloads,
                  String rutaImagen, String tipoUsuario) {
        this.code = code;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.contadorDownloads = contadorDownloads;
        this.rutaImagen = rutaImagen;
        this.tipoUsuario = tipoUsuario;
    }

    public int getCode() { return code; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNombre() { return nombre; }
    public long getNacimiento() { return nacimiento; }
    public int getContadorDownloads() { return contadorDownloads; }
    public String getRutaImagen() { return rutaImagen; }
    public String getTipoUsuario() { return tipoUsuario; }

    public void setPassword(String password) { this.password = password; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }
    public void setContadorDownloads(int contadorDownloads) { this.contadorDownloads = contadorDownloads; }

    @Override
    public String toString() {
        return code + " - " + nombre + " (" + username + ") - " + tipoUsuario;
    }
}
