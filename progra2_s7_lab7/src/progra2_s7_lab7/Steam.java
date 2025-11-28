package progra2_s7_lab7;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Steam {

    private static final String ROOT = "steam";
    private static final String DOWNLOADS = "downloads";

    private static final int TITLE_LEN = 40;
    private static final int USER_LEN = 20;
    private static final int PASS_LEN = 20;
    private static final int NAME_LEN = 40;
    private static final int IMG_LEN = 200;
    private static final int TYPE_LEN = 10;

    // (No los estamos usando directamente, pero sirven como referencia de tamaño)
    private static final int GAME_RECORD = 4 + (TITLE_LEN * 2) + 2 + 4 + 8 + 4 + (IMG_LEN * 2);
    private static final int PLAYER_RECORD = 4 + (USER_LEN * 2) + (PASS_LEN * 2) +
            (NAME_LEN * 2) + 8 + 4 + (IMG_LEN * 2) + (TYPE_LEN * 2);

    private RandomAccessFile codesRaf;
    private RandomAccessFile gamesRaf;
    private RandomAccessFile playersRaf;

    private File rootDir;
    private File downloadsDir;

    public Steam() {
        try {
            rootDir = new File(ROOT);
            if (!rootDir.exists()) rootDir.mkdirs();

            downloadsDir = new File(rootDir, DOWNLOADS);
            if (!downloadsDir.exists()) downloadsDir.mkdirs();

            File codes = new File(rootDir, "codes.stm");
            File games = new File(rootDir, "games.stm");
            File players = new File(rootDir, "player.stm");

            codesRaf = new RandomAccessFile(codes, "rw");
            gamesRaf = new RandomAccessFile(games, "rw");
            playersRaf = new RandomAccessFile(players, "rw");

            if (codesRaf.length() < 12) {
                codesRaf.seek(0);
                codesRaf.writeInt(1); // game code
                codesRaf.writeInt(1); // player code
                codesRaf.writeInt(1); // download code
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =============================================
    // STRING UTILITIES
    // =============================================

    private String fix(String s, int length) {
        if (s == null) s = "";
        if (s.length() > length) return s.substring(0, length);
        while (s.length() < length) s += " ";
        return s;
    }

    private String readFixed(RandomAccessFile raf, int len) throws IOException {
        char[] c = new char[len];
        for (int i = 0; i < len; i++) {
            c[i] = raf.readChar();
        }
        return new String(c).trim();
    }

    // =============================================
    // CODE HANDLING
    // =============================================

    private synchronized int getNextCode(int index) {
        try {
            long pos = index * 4L;
            codesRaf.seek(pos);
            int value = codesRaf.readInt();
            codesRaf.seek(pos);
            codesRaf.writeInt(value + 1);
            return value;
        } catch (Exception e) {
            return -1;
        }
    }

    public int getNextGameCode() { return getNextCode(0); }
    public int getNextPlayerCode() { return getNextCode(1); }
    public int getNextDownloadCode() { return getNextCode(2); }

    // =============================================
    // ADD GAME
    // =============================================

    public boolean addGame(String titulo, char so, int edadMin, double precio, String imgPath) {
        try {
            int code = getNextGameCode();
            if (code == -1) return false;

            gamesRaf.seek(gamesRaf.length());

            gamesRaf.writeInt(code);
            gamesRaf.writeChars(fix(titulo, TITLE_LEN));
            gamesRaf.writeChar(so);
            gamesRaf.writeInt(edadMin);
            gamesRaf.writeDouble(precio);
            gamesRaf.writeInt(0); // descargas
            gamesRaf.writeChars(fix(imgPath, IMG_LEN));

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // =============================================
    // ADD PLAYER
    // =============================================

    public boolean addPlayer(String user, String pass, String nombre,
                             long nacimiento, String img, String tipo) {

        if (usernameExists(user)) return false;

        try {
            int code = getNextPlayerCode();
            if (code == -1) return false;

            playersRaf.seek(playersRaf.length());

            playersRaf.writeInt(code);
            playersRaf.writeChars(fix(user, USER_LEN));
            playersRaf.writeChars(fix(pass, PASS_LEN));
            playersRaf.writeChars(fix(nombre, NAME_LEN));
            playersRaf.writeLong(nacimiento);
            playersRaf.writeInt(0);
            playersRaf.writeChars(fix(img, IMG_LEN));
            playersRaf.writeChars(fix(tipo, TYPE_LEN));

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // =============================================
    // FIND GAME / PLAYER (RECORD FIX)
    // =============================================

    public Game findGameByCode(int codeBuscado) {
        try {
            gamesRaf.seek(0);

            while (gamesRaf.getFilePointer() < gamesRaf.length()) {

                int code = gamesRaf.readInt();
                String title = readFixed(gamesRaf, TITLE_LEN);
                char so = gamesRaf.readChar();
                int edad = gamesRaf.readInt();
                double precio = gamesRaf.readDouble();
                int desc = gamesRaf.readInt();
                String img = readFixed(gamesRaf, IMG_LEN);

                if (code == codeBuscado) {
                    return new Game(code, title, so, edad, precio, desc, img);
                }
            }

        } catch (Exception e) {}
        return null;
    }

    public Player findPlayerByCode(int codeBuscado) {
        try {
            playersRaf.seek(0);

            while (playersRaf.getFilePointer() < playersRaf.length()) {

                int code = playersRaf.readInt();
                String user = readFixed(playersRaf, USER_LEN);
                String pass = readFixed(playersRaf, PASS_LEN);
                String nombre = readFixed(playersRaf, NAME_LEN);
                long nac = playersRaf.readLong();
                int desc = playersRaf.readInt();
                String img = readFixed(playersRaf, IMG_LEN);
                String tipo = readFixed(playersRaf, TYPE_LEN);

                if (code == codeBuscado) {
                    return new Player(code, user, pass, nombre, nac, desc, img, tipo);
                }
            }

        } catch (Exception e) {}
        return null;
    }

    public Player findPlayerByUsername(String username) {
        try {
            playersRaf.seek(0);

            while (playersRaf.getFilePointer() < playersRaf.length()) {

                int code = playersRaf.readInt();
                String user = readFixed(playersRaf, USER_LEN);
                String pass = readFixed(playersRaf, PASS_LEN);
                String nombre = readFixed(playersRaf, NAME_LEN);
                long nac = playersRaf.readLong();
                int desc = playersRaf.readInt();
                String img = readFixed(playersRaf, IMG_LEN);
                String tipo = readFixed(playersRaf, TYPE_LEN);

                if (user.equalsIgnoreCase(username)) {
                    return new Player(code, user, pass, nombre, nac, desc, img, tipo);
                }
            }

        } catch (Exception e) {}
        return null;
    }

    public boolean usernameExists(String username) {
        return findPlayerByUsername(username) != null;
    }

    // =============================================
    // LOGIN
    // =============================================

    public Player login(String user, String pass) {
        Player p = findPlayerByUsername(user);
        return (p != null && p.getPassword().equals(pass)) ? p : null;
    }

    // =============================================
    // DELETE GAME – FIXED RECORD
    // =============================================

    public boolean deleteGame(int codeBuscado) {
        try {
            File temp = new File("steam/tempGames.stm");
            RandomAccessFile tmp = new RandomAccessFile(temp, "rw");

            gamesRaf.seek(0);

            while (gamesRaf.getFilePointer() < gamesRaf.length()) {

                int code = gamesRaf.readInt();
                String title = readFixed(gamesRaf, TITLE_LEN);
                char so = gamesRaf.readChar();
                int edad = gamesRaf.readInt();
                double precio = gamesRaf.readDouble();
                int desc = gamesRaf.readInt();
                String img = readFixed(gamesRaf, IMG_LEN);

                if (code != codeBuscado) {
                    tmp.writeInt(code);
                    tmp.writeChars(fix(title, TITLE_LEN));
                    tmp.writeChar(so);
                    tmp.writeInt(edad);
                    tmp.writeDouble(precio);
                    tmp.writeInt(desc);
                    tmp.writeChars(fix(img, IMG_LEN));
                }
            }

            tmp.close();
            gamesRaf.close();

            File original = new File("steam/games.stm");
            original.delete();
            temp.renameTo(original);

            gamesRaf = new RandomAccessFile(original, "rw");
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean deletePlayer(int codeBuscado) {
        try {
            File temp = new File("steam/temp_players.stm");
            RandomAccessFile tmp = new RandomAccessFile(temp, "rw");

            playersRaf.seek(0);

            while (playersRaf.getFilePointer() < playersRaf.length()) {

                int code = playersRaf.readInt();
                String user = readFixed(playersRaf, USER_LEN);
                String pass = readFixed(playersRaf, PASS_LEN);
                String nombre = readFixed(playersRaf, NAME_LEN);
                long nac = playersRaf.readLong();
                int desc = playersRaf.readInt();
                String img = readFixed(playersRaf, IMG_LEN);
                String tipo = readFixed(playersRaf, TYPE_LEN);

                if (code != codeBuscado) {
                    tmp.writeInt(code);
                    tmp.writeChars(fix(user, USER_LEN));
                    tmp.writeChars(fix(pass, PASS_LEN));
                    tmp.writeChars(fix(nombre, NAME_LEN));
                    tmp.writeLong(nac);
                    tmp.writeInt(desc);
                    tmp.writeChars(fix(img, IMG_LEN));
                    tmp.writeChars(fix(tipo, TYPE_LEN));
                }
            }

            tmp.close();
            playersRaf.close();

            File original = new File("steam/player.stm");
            original.delete();
            temp.renameTo(original);

            playersRaf = new RandomAccessFile(original, "rw");
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // =============================================
    // UPDATE GAME – FIXED RECORD
    // =============================================

    public boolean updateGame(int codeBuscado, String title, char so,
                              int edad, double precio, String img) {
        try {
            gamesRaf.seek(0);

            while (gamesRaf.getFilePointer() < gamesRaf.length()) {

                long pos = gamesRaf.getFilePointer();

                int code = gamesRaf.readInt();
                String oldTitle = readFixed(gamesRaf, TITLE_LEN);
                char oldSO = gamesRaf.readChar();
                int oldEdad = gamesRaf.readInt();
                double oldPrecio = gamesRaf.readDouble();
                int desc = gamesRaf.readInt();
                String oldImg = readFixed(gamesRaf, IMG_LEN);

                if (code == codeBuscado) {
                    gamesRaf.seek(pos);

                    gamesRaf.writeInt(code);
                    gamesRaf.writeChars(fix(title, TITLE_LEN));
                    gamesRaf.writeChar(so);
                    gamesRaf.writeInt(edad);
                    gamesRaf.writeDouble(precio);
                    gamesRaf.writeInt(desc);
                    gamesRaf.writeChars(fix(img, IMG_LEN));

                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    // =============================================
    // UPDATE PLAYER – FIXED RECORD
    // =============================================

    public boolean updatePlayer(int codeBuscado, String newUser, String newPass,
                                String newNombre, long newNac,
                                String newImg, String newTipo) {
        try {
            playersRaf.seek(0);

            while (playersRaf.getFilePointer() < playersRaf.length()) {

                long pos = playersRaf.getFilePointer();

                int code = playersRaf.readInt();
                String user = readFixed(playersRaf, USER_LEN);
                String pass = readFixed(playersRaf, PASS_LEN);
                String nombre = readFixed(playersRaf, NAME_LEN);
                long nac = playersRaf.readLong();
                int desc = playersRaf.readInt();
                String img = readFixed(playersRaf, IMG_LEN);
                String tipo = readFixed(playersRaf, TYPE_LEN);

                if (code == codeBuscado) {
                    playersRaf.seek(pos);

                    playersRaf.writeInt(code);
                    playersRaf.writeChars(fix(newUser, USER_LEN));
                    playersRaf.writeChars(fix(newPass, PASS_LEN));
                    playersRaf.writeChars(fix(newNombre, NAME_LEN));
                    playersRaf.writeLong(newNac);
                    playersRaf.writeInt(desc); // se conserva contador
                    playersRaf.writeChars(fix(newImg, IMG_LEN));
                    playersRaf.writeChars(fix(newTipo, TYPE_LEN));

                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =============================================
    // CHANGE PRICE
    // =============================================

    public boolean updatePriceFor(int codeBuscado, double newPrice) {
        try {
            gamesRaf.seek(0);

            while (gamesRaf.getFilePointer() < gamesRaf.length()) {

                int code = gamesRaf.readInt();
                String title = readFixed(gamesRaf, TITLE_LEN);
                char so = gamesRaf.readChar();
                int edad = gamesRaf.readInt();
                long posPrecio = gamesRaf.getFilePointer();
                double precio = gamesRaf.readDouble();
                int desc = gamesRaf.readInt();
                String img = readFixed(gamesRaf, IMG_LEN);

                if (code == codeBuscado) {
                    gamesRaf.seek(posPrecio);
                    gamesRaf.writeDouble(newPrice);
                    return true;
                }
            }

        } catch (Exception e) {}
        return false;
    }

    // =============================================
    // DESCARGA DE JUEGOS
    // =============================================

    public boolean downloadGame(int codeGame, int codePlayer, char soSolicitado) {
        Game game = findGameByCode(codeGame);
        Player player = findPlayerByCode(codePlayer);

        if (game == null || player == null) return false;
        if (game.getSistemaOperativo() != soSolicitado) return false;
        if (calcularEdad(player.getNacimiento()) < game.getEdadMinima()) return false;

        int codeDownload = getNextDownloadCode();
        if (codeDownload == -1) return false;

        try {
            File d = new File(downloadsDir, "download_" + codeDownload + ".stm");
            PrintWriter pw = new PrintWriter(new FileWriter(d));

            pw.println("[FECHA] " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            pw.println("(IMAGEN) " + game.getRutaImagen());
            pw.println("Download #" + codeDownload);
            pw.println(player.getNombre() + " ha bajado " + game.getTitulo());
            pw.println("Precio: $" + game.getPrecio());

            pw.close();

            incrementarDescargasGame(codeGame);
            incrementarDescargasPlayer(codePlayer);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void incrementarDescargasGame(int codeBuscado) {
        try {
            gamesRaf.seek(0);

            while (gamesRaf.getFilePointer() < gamesRaf.length()) {

                int code = gamesRaf.readInt();
                String title = readFixed(gamesRaf, TITLE_LEN);
                char so = gamesRaf.readChar();
                int edad = gamesRaf.readInt();
                double precio = gamesRaf.readDouble();
                long posCont = gamesRaf.getFilePointer();
                int desc = gamesRaf.readInt();
                String img = readFixed(gamesRaf, IMG_LEN);

                if (code == codeBuscado) {
                    gamesRaf.seek(posCont);
                    gamesRaf.writeInt(desc + 1);
                    return;
                }
            }

        } catch (Exception e) {}
    }

    private void incrementarDescargasPlayer(int codeBuscado) {
        try {
            playersRaf.seek(0);

            while (playersRaf.getFilePointer() < playersRaf.length()) {

                int code = playersRaf.readInt();
                String user = readFixed(playersRaf, USER_LEN);
                String pass = readFixed(playersRaf, PASS_LEN);
                String nombre = readFixed(playersRaf, NAME_LEN);
                long nac = playersRaf.readLong();
                long posCont = playersRaf.getFilePointer();
                int desc = playersRaf.readInt();
                String img = readFixed(playersRaf, IMG_LEN);
                String tipo = readFixed(playersRaf, TYPE_LEN);

                if (code == codeBuscado) {
                    playersRaf.seek(posCont);
                    playersRaf.writeInt(desc + 1);
                    return;
                }
            }

        } catch (Exception e) {}
    }

    private int calcularEdad(long nacimientoMillis) {
        long now = System.currentTimeMillis();
        return (int) ((now - nacimientoMillis) / (1000L * 60 * 60 * 24 * 365));
    }

    // =============================================
    // REPORTES
    // =============================================

    public boolean reportForClient(int codePlayer, String archivoSalida) {
        Player p = findPlayerByCode(codePlayer);
        if (p == null) {
            System.out.println("NO SE PUEDE CREAR REPORTE");
            return false;
        }

        try {
            File out = new File(rootDir, archivoSalida);
            PrintWriter pw = new PrintWriter(new FileWriter(out));

            pw.println("=== REPORTE DE CLIENTE ===");
            pw.println("Código: " + p.getCode());
            pw.println("Username: " + p.getUsername());
            pw.println("Nombre: " + p.getNombre());
            pw.println("Nacimiento (millis): " + p.getNacimiento());
            pw.println("Descargas: " + p.getContadorDownloads());
            pw.println("Imagen: " + p.getRutaImagen());
            pw.println("Tipo: " + p.getTipoUsuario());

            pw.close();

            System.out.println("REPORTE CREADO");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("NO SE PUEDE CREAR REPORTE");
        return false;
    }

    // =============================================
    // GET ALL GAMES
    // =============================================

    public List<Game> getAllGames() {
        List<Game> list = new ArrayList<>();

        try {
            gamesRaf.seek(0);

            while (gamesRaf.getFilePointer() < gamesRaf.length()) {

                int code = gamesRaf.readInt();
                String title = readFixed(gamesRaf, TITLE_LEN);
                char so = gamesRaf.readChar();
                int edad = gamesRaf.readInt();
                double precio = gamesRaf.readDouble();
                int desc = gamesRaf.readInt();
                String img = readFixed(gamesRaf, IMG_LEN);

                list.add(new Game(code, title, so, edad, precio, desc, img));
            }

        } catch (Exception e) {}
        return list;
    }

    // =============================================
    // GET ALL PLAYERS
    // =============================================

    public List<Player> getAllPlayers() {
        List<Player> list = new ArrayList<>();

        try {
            playersRaf.seek(0);

            while (playersRaf.getFilePointer() < playersRaf.length()) {

                int code = playersRaf.readInt();
                String user = readFixed(playersRaf, USER_LEN);
                String pass = readFixed(playersRaf, PASS_LEN);
                String nombre = readFixed(playersRaf, NAME_LEN);
                long nac = playersRaf.readLong();
                int desc = playersRaf.readInt();
                String img = readFixed(playersRaf, IMG_LEN);
                String tipo = readFixed(playersRaf, TYPE_LEN);

                list.add(new Player(code, user, pass, nombre, nac, desc, img, tipo));
            }

        } catch (Exception e) {}
        return list;
    }

    // =============================================
    // CLOSE FILES
    // =============================================

    public void close() {
        try {
            if (codesRaf != null) codesRaf.close();
            if (gamesRaf != null) gamesRaf.close();
            if (playersRaf != null) playersRaf.close();
        } catch (Exception e) {}
    }
}
