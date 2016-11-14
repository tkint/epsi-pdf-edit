/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import static app.Config.TRANSLATOR;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import src.model.DocFile;

/**
 *
 * @author Thomas
 */
public class Instance implements Config {

    private static Instance INSTANCE = new Instance();

    public static int opened;
    public static List<DocFile> docFiles = new ArrayList<>();

    public static Stage stage;
    public static String stageName;

    /**
     * Contructeur
     */
    private Instance() {

    }

    /**
     * Définition du singleton
     *
     * @return Instance
     */
    public static synchronized Instance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Instance();
        }
        return INSTANCE;
    }

    /**
     * Charge le fichier d'instance
     *
     * @throws IOException Exception
     */
    public static void load() throws IOException {
        for (File file : loadSaveFile(TRANSLATOR.getString("APP_NAME"))) {
            addFile(file);
        }
    }

    /**
     * Sauvegarde le fichier d'instance
     *
     * @throws IOException Exception
     */
    public static void save() throws IOException {
        BufferedWriter writer = null;
        File saveFile = new File(TRANSLATOR.getString("APP_NAME") + ".txt");

        writer = new BufferedWriter(new FileWriter(saveFile));

        for (DocFile docFile : docFiles) {
            if (docFile.isSaved()) {
                writer.write(toSaveFile(docFile.getFile()));
                writer.newLine();
                System.out.println(TRANSLATOR.getString("FILE") + " " + docFile.getFileName() + " " + TRANSLATOR.getString("HAS_BEEN_SAVED_IN") + " " + TRANSLATOR.getString("APP_NAME") + ".txt");
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * Ajoute un nouveau fichier
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static DocFile addNewFile(String fileName) {
        DocFile docFile = null;
        
        File file = new File(fileName);
        
        PDDocument document = new PDDocument();
        document.addPage(new PDPage());
        
        docFile = new DocFile(docFiles.size(), document, file);
        
        opened = docFile.getId();
        docFiles.add(docFile);
        return docFile;
    }

    /**
     * Ajoute un fichier déjà existant
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static DocFile addFile(File file) throws IOException {
        DocFile docFile = null;
        PDDocument document = null;
        try {
            if (file.exists()) {
                if (!isAlreadyOpened(file)) {
                    document = PDDocument.load(file);
                    if (document != null) {
                        docFile = new DocFile(docFiles.size(), document, file);
                    } else {
                        System.out.println("Le document est null");
                    }
                } else {
                    docFile = getDocFileByFile(file);
                    System.out.println("Le fichier est déjà ouvert");
                }
                opened = docFile.getId();
                docFiles.add(docFile);
                docFile.setSaved(true);
                saveInSaveFile(file, TRANSLATOR.getString("APP_NAME") + "_recent");
            } else {
                System.out.println("Le fichier n'existe pas");
            }
        } finally {
            if (document != null) {
                //document.close();
            }
        }
        return docFile;
    }

    /**
     * Ferme un docFile de la liste
     *
     * @param id Id du DocFile à fermer
     */
    public static void closeDocFile(int id) {
        try {
            DocFile docFile = getDocFileById(id);
            if (docFile != null) {
                docFile.getDocument().close();
                docFiles.remove(docFile);
                if (docFiles.size() > id) {
                    opened = id;
                } else {
                    opened = 0;
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Met à jour le docFile ouvert
     *
     * @param document PDDocument à soumettre
     * @param file File à soumettre
     */
    public static void updateDocFile(PDDocument document, File file) {
        if (document != null) {
            docFiles.get(opened).setDocument(document);
        }
        if (file != null) {
            docFiles.get(opened).setFile(file);
        }
    }

    /**
     * Récupère le docFile ouvert
     *
     * @return DocFile
     */
    public static DocFile getDocFileOpened() {
        return docFiles.get(opened);
    }

    /**
     * Sauvegarde le fichier dans le fichier de sauvegarde désigné
     *
     * @param docFile DocFile à sauvegarder
     * @param saveFilename String nom du fichier de sauvegarde
     * @throws IOException Exception
     */
    public static void saveInSaveFile(File file, String saveFilename) throws IOException {
        if (!isAlreadyInSaveFile(file, saveFilename)) {
            BufferedWriter writer = null;
            File saveFile = new File(saveFilename + ".txt");

            if (saveFile.exists()) {
                writer = new BufferedWriter(new FileWriter(saveFile, true));
            } else {
                writer = new BufferedWriter(new FileWriter(saveFile));
            }

            writer.write(toSaveFile(file));
            writer.newLine();
            writer.flush();
            writer.close();
            System.out.println(TRANSLATOR.getString("FILE") + " " + file.getName() + " " + TRANSLATOR.getString("HAS_BEEN_SAVED_IN") + " " + saveFilename);
        } else {
            System.out.println(TRANSLATOR.getString("FILE") + " " + file.getName() + " " + TRANSLATOR.getString("IS_ALREADY_SAVED_IN") + " " + saveFilename);
        }
    }

    /**
     * Charge les fichiers du fichier de sauvegarde ciblé
     *
     * @param saveFilename String nom du fichier de sauvegarde
     * @return Liste de DocFile
     */
    public static ArrayList<File> loadSaveFile(String saveFilename) {
        ArrayList<File> files = new ArrayList<>();
        try {
            File saveFile = new File(saveFilename + ".txt");
            if (saveFile.exists()) {
                List<String> content = new ArrayList<>(Files.readAllLines(saveFile.toPath(), StandardCharsets.UTF_8));
                for (int i = 0; i < content.size(); i++) {
                    File file = new File(content.get(i));
                    if (file.exists()) {
                        files.add(file);
                    } else {
                        content.set(i, System.getProperty("line.separator"));
                        break;
                    }
                }
                Files.write(saveFile.toPath(), content, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return files;
    }

    /**
     * Vérifie si le fichier est déjà dans le fichier de sauvegarde
     *
     * @param docFile DocFile à vérifier
     * @param saveFilename String nom du fichier de sauvegarde
     * @return true si le DocFile est présent, false sinon
     */
    public static boolean isAlreadyInSaveFile(File file, String saveFilename) {
        boolean r = false;
        for (File f : loadSaveFile(saveFilename)) {
            if (file.getAbsolutePath().equals(f.getAbsolutePath())) {
                r = true;
            }
        }
        return r;
    }

    /**
     * Vérifie si le fichier est déjà ouvert
     *
     * @param file File ) vérifier
     * @return true si le fichier est déjà ouvert, false sinon
     */
    public static boolean isAlreadyOpened(File file) {
        boolean r = false;
        int i = 0;
        while (i < docFiles.size() && !r) {
            if (docFiles.get(i).getFile().equals(file)) {
                r = true;
            }
            i++;
        }
        return r;
    }

    /**
     * Récupère le DocFile du File renseigné
     *
     * @param file File dont on cherche le DocFile
     * @return DocFile
     */
    public static DocFile getDocFileByFile(File file) {
        DocFile docFile = null;
        int i = 0;
        while (i < docFiles.size() && docFile == null) {
            if (docFiles.get(i).getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                docFile = docFiles.get(i);
            }
            i++;
        }
        return docFile;
    }

    /**
     * Récupère le DocFile en fonction de l'id
     *
     * @param id
     * @return
     */
    private static DocFile getDocFileById(int id) {
        DocFile docFile = null;
        int i = 0;
        while (i < docFiles.size() && docFile == null) {
            if (docFiles.get(i).getId() == id) {
                docFile = docFiles.get(i);
            }
            i++;
        }
        return docFile;
    }

    private static String toSaveFile(File file) {
        return file.getAbsolutePath();
    }
}
