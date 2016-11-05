/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author Thomas
 */
public class Instance {

    private static Instance INSTANCE = new Instance();

    public static int opened;
    public static String fileOpened;
    public static String filenameOpened;
    public static PDDocument documentOpened;
    public static List<PDDocument> documents = new ArrayList<>();
    public static List<File> files = new ArrayList<>();

    public static Stage stage;

    private Instance() {

    }

    public static void addDocument(PDDocument document, File file) {
        documents.add(document);
        files.add(file);
        opened = documents.size() - 1;
        updateDocument(null, null);
        System.out.println("File : " + file.getName() + " | Opened : " + opened);
    }

    public static boolean isOpen(File file) {
        for (File f : INSTANCE.files) {
            if (f.equals(file)) {
                return true;
            }
        }
        return false;
    }

    public static void updateDocument(PDDocument document, File file) {
        if (document != null) {
            documents.set(opened, document);
        }
        if (file != null) {
            files.set(opened, file);
        }
        documentOpened = documents.get(opened);
        fileOpened = files.get(opened).getName();
        filenameOpened = fileOpened.substring(0, fileOpened.length() - 4);
    }

    /**
     * Définition du singleton
     * @return 
     */
    public static synchronized Instance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Instance();
        }
        return INSTANCE;
    }
}
