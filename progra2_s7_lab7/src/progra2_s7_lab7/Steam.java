package progra2_s7_lab7;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Steam {

    private File rootDir;
    private File downloadsDir;

    private CodesSTM codesSTM;
    GamesSTM gamesSTM;
    private PlayersSTM playersSTM;


    public Steam() {
        try {
            // Crear carpeta steam/
            rootDir = new File("steam");
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }

            // Crear carpeta steam/downloads/
            downloadsDir = new File(rootDir, "downloads");
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }

            // Inicializar manejadores de archivo
            codesSTM = new CodesSTM(rootDir);
            gamesSTM = new GamesSTM(rootDir);
            playersSTM = new PlayersSTM(rootDir);

            // Crear admin si no existe
            asegurarAdminDefecto();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void asegurarAdminDefecto() {
        try {
            long n = playersSTM.Conteo();

            for (int i = 0; i < n; i++) {
                Object[] reg = playersSTM.LeerRegistroComoArreglo(i);
                String tipo = ((String) reg[7]).trim();

                if (tipo.equalsIgnoreCase("admin")) {
                    return; // Ya existe admin
                }
            }

            // No existe admin → crear uno por defecto
            int cod = codesSTM.SiguienteCodigoPlayer();

            playersSTM.AnadirJugador(
                    cod,
                    "admin",
                    "admin",
                    "Administrador del sistema",
                    0L,     // fecha de nacimiento
                    0,      // descargas
                    "",     // imagen
                    "admin"
            );

            System.out.println("ADMIN CREADO: admin / admin");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int addGame(String titulo, char so, int edad, double precio, String imgrel) {
        try {
            int codigo = codesSTM.SiguienteCodigoGame();
            return gamesSTM.AnadirJuego(codigo, titulo, so, edad, precio, imgrel);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public int addPlayer(String usuario, String clave, String nombre, long fechaN, String imgrel, String tipo) {
        try {
            int codigo = codesSTM.SiguienteCodigoPlayer();
            return playersSTM.AnadirJugador(codigo, usuario, clave, nombre, fechaN, 0, imgrel, tipo);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public boolean downloadGame(int codJuego, int codPlayer, char soSolicitado) {
        try {
            // Validar juego
            long idxJ = gamesSTM.EncontrarIndicePorCodigo(codJuego);
            if (idxJ < 0) return false;

            Object[] j = gamesSTM.LeerRegistroComoArreglo((int) idxJ);
            char soJuego = (char) j[2];
            int edadMin = (int) j[3];
            double precio = (double) j[4];
            int descJ = (int) j[5];
            String titulo = (String) j[1];

            if (soJuego != soSolicitado) return false;

            // Validar player
            long idxP = playersSTM.EncontrarIndicePorCodigo(codPlayer);
            if (idxP < 0) return false;

            Object[] p = playersSTM.LeerRegistroComoArreglo((int) idxP);
            String nombreP = (String) p[3];
            long fechaN = (long) p[4];
            int descP = (int) p[5];

            // Validar edad
            int edad = calcularEdad(fechaN);
            if (edad < edadMin) return false;

            // Obtener código de descarga
            int codD = codesSTM.SiguienteCodigoDescargas();

            // Crear archivo de descarga
            File out = new File(downloadsDir, "download_" + codD + ".stm");
            FileWriter fw = new FileWriter(out);

            fw.write("DOWNLOAD #" + codD + "\n");
            fw.write("Fecha: " + new java.util.Date() + "\n\n");
            fw.write(nombreP + " ha descargado el juego: " + titulo + "\n");
            fw.write("Precio: $" + precio + "\n");

            fw.close();

            // Incrementar contadores
            incrementarContadorJuego((int) idxJ, descJ + 1);
            incrementarContadorPlayer((int) idxP, descP + 1);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void incrementarContadorJuego(int indice, int nuevoValor) throws IOException {
        long base = (long) indice * OffsetJuegos.TAMANO_REC;
        RandomAccessFile raf = new RandomAccessFile(new File(rootDir, "games.stm"), "rw");
        raf.seek(base + OffsetJuegos.CONT_DESCARGAS.getPos());
        raf.writeInt(nuevoValor);
        raf.close();
    }

    private void incrementarContadorPlayer(int indice, int nuevoValor) throws IOException {
        long base = (long) indice * OffsetPlayers.TAMANO_REC;
        RandomAccessFile raf = new RandomAccessFile(new File(rootDir, "players.stm"), "rw");
        raf.seek(base + OffsetPlayers.CONT_DESCARGAS.getPos());
        raf.writeInt(nuevoValor);
        raf.close();
    }

    private int calcularEdad(long fechaN) {
        if (fechaN <= 0) return 99; // Evita fallos en admin
        long diff = System.currentTimeMillis() - fechaN;
        return (int) (diff / (1000L * 60 * 60 * 24 * 365));
    }

    public boolean updatePriceFor(int codigo, double nuevoPrecio) {
        try {
            return gamesSTM.ActualizarPrecio(codigo, nuevoPrecio);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean reportForClient(int codCliente, String archivoSalida) {
        try {
            long idx = playersSTM.EncontrarIndicePorCodigo(codCliente);
            if (idx < 0) return false;

            Object[] r = playersSTM.LeerRegistroComoArreglo((int) idx);

            FileWriter fw = new FileWriter(new File(archivoSalida));

            fw.write("REPORTE DEL CLIENTE\n\n");
            fw.write("Codigo: " + r[0] + "\n");
            fw.write("Usuario: " + r[1] + "\n");
            fw.write("Nombre: " + r[3] + "\n");
            fw.write("Fecha nacimiento: " + new java.util.Date((long) r[4]) + "\n");
            fw.write("Descargas: " + r[5] + "\n");
            fw.write("Tipo: " + r[7] + "\n");

            fw.close();
            return true;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public void printGames() {
        try {
            gamesSTM.ImprimirTodo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public Object[] buscarUsuarioPorCredenciales(String usuario, String pass) throws IOException {
        long n = playersSTM.Conteo();

        for (int i = 0; i < n; i++) {
            Object[] p = playersSTM.LeerRegistroComoArreglo(i);

            String userBD = ((String) p[1]).trim();
            String passBD = ((String) p[2]).trim();

            if (userBD.equals(usuario) && passBD.equals(pass)) {
                return p;   // encontramos el usuario
            }
        }
        return null;        // no existe
    }

}
