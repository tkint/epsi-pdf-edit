/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
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
     * @return
     */
    public static synchronized Instance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Instance();
        }
        return INSTANCE;
    }

    /**
     * Ajoute un docFile à la liste
     *
     * @param document
     * @param file
     * @throws java.io.IOException
     */
    public static void addDocFile(PDDocument document, File file) throws IOException {
        // Si le fichier n'est pas déjà ouvert
        if (!isFileAlreadyOpened(file)) {
            DocFile docFile = new DocFile(0, document, file);
            docFiles.add(docFile);
            opened = docFiles.size() - 1;
            getDocFileOpened().setId(opened);

            // Si le fichier existe on l'enregistre dans les fichiers récents
            if (file.exists()) {
                saveInSaveFile(docFile, TRANSLATOR.getString("APP_NAME") + "_recent");
            }
        }
    }

    /**
     * Ferme un docFile de la liste
     *
     * @param id
     */
    public static void closeDocFile(int id) {
        try {
            docFiles.get(id).getDocument().close();
            docFiles.remove(id);
            if (docFiles.size() > id) {
                opened = id;
            } else {
                opened = docFiles.size();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Met à jour le docFile ouvert
     *
     * @param document
     * @param file
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
     * @return
     */
    public static DocFile getDocFileOpened() {
        DocFile docFile = null;
        if (opened > -1 && opened < docFiles.size()) {
            docFile = docFiles.get(opened);
        }
        return docFile;
    }

    /**
     * Vérifie si le fichier est déjà ouvert
     *
     * @param file
     * @return
     */
    public static boolean isDocFileOpen(File file) {
        boolean r = false;
        int i = 0;
        while (i < docFiles.size() && r == false) {
            if (docFiles.get(i).getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                r = true;
            }
            i++;
        }
        return r;
    }

    /**
     * Charge le fichier d'instance
     *
     * @throws IOException
     */
    public static void load() throws IOException {
        for (DocFile docFile : loadSaveFile(TRANSLATOR.getString("APP_NAME"))) {
            addDocFile(docFile.getDocument(), docFile.getFile());
        }
    }

    /**
     * Sauvegarde le fichier d'instance
     *
     * @throws IOException
     */
    public static void save() throws IOException {
        BufferedWriter writer = null;
        File saveFile = new File(TRANSLATOR.getString("APP_NAME") + ".txt");

        writer = new BufferedWriter(new FileWriter(saveFile));

        for (DocFile docFile : docFiles) {
            if (docFile.isSaved()) {
                writer.write(docFile.toSaveString());
                writer.newLine();
                System.out.println(TRANSLATOR.getString("FILE") + " " + docFile.getFileName() + " " + TRANSLATOR.getString("HAS_BEEN_SAVED_IN") + " " + TRANSLATOR.getString("APP_NAME") + ".txt");
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * Sauvegarde le fichier dans le fichier de sauvegarde désigné
     *
     * @param docFile
     * @param saveFilename
     * @throws IOException
     */
    public static void saveInSaveFile(DocFile docFile, String saveFilename) throws IOException {
        if (!isAlreadyInSaveFile(docFile, saveFilename)) {
            BufferedWriter writer = null;
            File saveFile = new File(saveFilename + ".txt");

            if (saveFile.exists()) {
                writer = new BufferedWriter(new FileWriter(saveFile, true));
            } else {
                writer = new BufferedWriter(new FileWriter(saveFile));
            }

            writer.write(docFile.toSaveString());
            writer.newLine();
            writer.flush();
            writer.close();
            System.out.println(TRANSLATOR.getString("FILE") + " " + docFile.getFileName() + " " + TRANSLATOR.getString("HAS_BEEN_SAVED_IN") + " " + saveFilename);
        } else {
            System.out.println(TRANSLATOR.getString("FILE") + " " + docFile.getFileName() + " " + TRANSLATOR.getString("IS_ALREADY_SAVED_IN") + " " + saveFilename);
        }
    }

    /**
     * Charge les fichiers du fichier de sauvegarde ciblé
     *
     * @param saveFilename
     * @return
     */
    public static ArrayList<DocFile> loadSaveFile(String saveFilename) {
        ArrayList<DocFile> docFiles = new ArrayList<>();
        try {
            File saveFile = new File(saveFilename + ".txt");
            if (saveFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(saveFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] lineRead = line.split(TRANSLATOR.getString("INSTANCE_DATA_SEPARATOR"));
                    File file = new File(lineRead[1]);
                    if (file.exists()) {
                        PDDocument document = PDDocument.load(file);
                        DocFile docFile = new DocFile(INSTANCE.docFiles.size() - 1, document, file);
                        docFile.setSelectedPage(Integer.parseInt(lineRead[2]));
                        docFiles.add(docFile);
                        //document.close();
                        System.out.println(TRANSLATOR.getString("FILE_LOAD_SUCCESS_1") + " " + lineRead[1] + " " + TRANSLATOR.getString("FILE_LOAD_SUCCESS_2") + " " + saveFilename);
                    } else {
                        System.out.println(TRANSLATOR.getString("FILE_LOAD_FAIL_1") + " " + lineRead[1] + " " + TRANSLATOR.getString("FILE_LOAD_FAIL_2") + " " + saveFilename);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return docFiles;
    }

    /**
     * Vérifie si le fichier est déjà dans le fichier de sauvegarde
     *
     * @param docFile
     * @param saveFilename
     * @return
     */
    private static boolean isAlreadyInSaveFile(DocFile docFile, String saveFilename) {
        boolean r = false;
        for (DocFile df : loadSaveFile(saveFilename)) {
            if (docFile.getFile().equals(df.getFile())) {
                r = true;
            }
        }
        return r;
    }

    /**
     * Vérifie si le fichier est déjà ouvert
     *
     * @param file
     * @return
     */
    public static boolean isFileAlreadyOpened(File file) {
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
}
