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
     * @param document
     * @param file 
     */
    public static void addDocFile(PDDocument document, File file) {
        docFiles.add(new DocFile(0, document, file));
        opened = docFiles.size() - 1;
        getDocOpened().setId(opened);
    }

    /**
     * Ferme un docFile de la liste
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
     * @return 
     */
    public static DocFile getDocOpened() {
        DocFile doc = null;
        if (opened > -1 && opened < docFiles.size()) {
            doc = docFiles.get(opened);
        }
        return doc;
    }

    /**
     * Vérifie si le fichier est déjà ouvert
     * @param file
     * @return 
     */
    public static boolean isDocFileOpen(File file) {
        boolean r = false;
        int i = 0;
        while (i < docFiles.size() && r == false) {
            if (docFiles.get(i).getFile().equals(file)) {
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
            File temp = new File(APP_NAME + ".txt");

            writer = new BufferedWriter(new FileWriter(temp));

            for (DocFile docFile : getDocFilesOpened()) {
                writer.write(docFile.toTemp());
                writer.newLine();
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Charge l'instance depuis le fichier temp
     */
    public static void load() {
        try {
            File temp = new File(APP_NAME + ".txt");
            if (temp.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(temp));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] l = line.split("-");
                    File file = new File(l[1]);
                    if (file.exists()) {
                        PDDocument document = PDDocument.load(file);
                        addDocFile(document, file);
                        getDocOpened().setSaved(true);
                        Displayer.displayDocFileNewTab(getDocOpened().getShortFileName());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Liste les docFile sauvegardés
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
