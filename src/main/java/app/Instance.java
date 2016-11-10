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
import src.view.Displayer;

/**
 *
 * @author Thomas
 */
public class Instance implements Config {

    private static Instance INSTANCE = new Instance();

    public static int opened;
    public static List<DocFile> docFiles = new ArrayList<>();

    public static Stage stage;

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
     */
    public static void addDocFile(PDDocument document, File file) {
        docFiles.add(new DocFile(0, document, file));
        opened = docFiles.size() - 1;
        getDocFileOpened().setId(opened);
        saveRecent(getDocFileOpened());
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
        DocFile doc = null;
        if (opened > -1 && opened < docFiles.size()) {
            doc = docFiles.get(opened);
        }
        return doc;
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
     * Sauvegarde l'instance dans un fichier temporaire
     */
    public static void save() {
        try {
            BufferedWriter writer = null;
            File save = new File(APP_NAME + ".txt");

            writer = new BufferedWriter(new FileWriter(save));

            for (DocFile docFile : getDocFilesOpened()) {
                writer.write(docFile.toSaveString());
                writer.newLine();
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Charge l'instance depuis le fichier
     */
    public static void load() {
        try {
            File save = new File(APP_NAME + ".txt");
            if (save.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(save));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] l = line.split(INSTANCE_SAVE_DATA_SEPARATOR);
                    File file = new File(l[1]);
                    if (file.exists()) {
                        PDDocument document = PDDocument.load(file);
                        addDocFile(document, file);
                        getDocFileOpened().setSaved(true);
                        getDocFileOpened().setSelectedPage(Integer.parseInt(l[2]));
                        Displayer.displayDocFileNewTab(getDocFileOpened().getShortFileName());
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Ajoute le fichier à la liste des récemment ouverts
     *
     * @param docFile
     */
    public static void saveRecent(DocFile docFile) {
        if (!isAlreadyRecent(docFile)) {
            try {
                BufferedWriter writer = null;
                File save = new File(APP_NAME + "_recent.txt");

                if (save.exists()) {
                    writer = new BufferedWriter(new FileWriter(save, true));
                } else {
                    writer = new BufferedWriter(new FileWriter(save));
                }

                writer.write(docFile.toSaveString());
                writer.newLine();
                writer.flush();
                writer.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Charge les fichiers récemment ouverts
     *
     * @return
     */
    public static ArrayList<DocFile> loadRecent() {
        ArrayList<DocFile> docFiles = new ArrayList<>();
        try {
            File temp = new File(APP_NAME + "_recent.txt");
            if (temp.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(temp));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] l = line.split(INSTANCE_SAVE_DATA_SEPARATOR);
                    File file = new File(l[1]);
                    if (file.exists()) {
                        PDDocument document = PDDocument.load(file);
                        DocFile docFile = new DocFile(INSTANCE.docFiles.size() - 1, document, file);
                        docFile.setSelectedPage(Integer.parseInt(l[2]));
                        docFiles.add(docFile);
                        document.close();
                    } else {
                        System.out.println("Le fichier " + l[1] + " n'existe pas");
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
     * Vérifie si le fichier est déjà enregistré dans les récemment ouverts
     *
     * @param docFile
     * @return
     */
    private static boolean isAlreadyRecent(DocFile docFile) {
        boolean r = false;
        for (DocFile df : loadRecent()) {
            if (docFile.getFile().getAbsolutePath().equals(df.getFile().getAbsolutePath())) {
                r = true;
            }
        }
        return r;
    }

    /**
     * Liste les docFile sauvegardés
     *
     * @return
     */
    private static List<DocFile> getDocFilesOpened() {
        List<DocFile> df = new ArrayList<>();
        for (DocFile docFile : docFiles) {
            if (docFile.isSaved()) {
                df.add(docFile);
            }
        }
        return df;
    }
}
