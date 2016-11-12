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
     * D�finition du singleton
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
     * Ajoute un docFile � la liste
     *
     * @param document PDDocument
     * @param file File
     * @return DocFile
     * @throws java.io.IOException Exception
     */
    public static DocFile addDocFile(PDDocument document, File file) throws IOException {
        DocFile docFile = null;
        // Si le fichier est d�j� ouvert
        if (isFileAlreadyOpened(file)) {
            // On le r�cup�re
            docFile = getDocFileByFile(file);
            document.close();
        } else {
            // Sinon on le cr��
            docFile = new DocFile(docFiles.size(), document, file);
            docFiles.add(docFile);
        }
        opened = docFile.getId();

        // Si le fichier existe on l'enregistre dans les fichiers r�cents
        if (file.exists()) {
            docFile.setSaved(true);
            saveInSaveFile(docFile, TRANSLATOR.getString("APP_NAME") + "_recent");
        }
        
        return docFile;
    }

    /**
     * Ferme un docFile de la liste
     *
     * @param id Id du DocFile � fermer
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
     * Met � jour le docFile ouvert
     *
     * @param document PDDocument � soumettre
     * @param file File � soumettre
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
     * R�cup�re le docFile ouvert
     *
     * @return DocFile
     */
    public static DocFile getDocFileOpened() {
        DocFile docFile = null;
        if (opened > -1 && opened < docFiles.size()) {
            docFile = docFiles.get(opened);
        }
        return docFile;
    }

    /**
     * Charge le fichier d'instance
     *
     * @throws IOException Exception
     */
    public static void load() throws IOException {
        for (DocFile docFile : loadSaveFile(TRANSLATOR.getString("APP_NAME"))) {
            addDocFile(docFile.getDocument(), docFile.getFile());
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
                writer.write(docFile.toSaveString());
                writer.newLine();
                System.out.println(TRANSLATOR.getString("FILE") + " " + docFile.getFileName() + " " + TRANSLATOR.getString("HAS_BEEN_SAVED_IN") + " " + TRANSLATOR.getString("APP_NAME") + ".txt");
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * Sauvegarde le fichier dans le fichier de sauvegarde d�sign�
     *
     * @param docFile DocFile � sauvegarder
     * @param saveFilename String nom du fichier de sauvegarde
     * @throws IOException Exception
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
     * Charge les fichiers du fichier de sauvegarde cibl�
     *
     * @param saveFilename String nom du fichier de sauvegarde
     * @return Liste de DocFile
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
                        DocFile docFile = new DocFile(INSTANCE.docFiles.size(), document, file);
                        docFile.setSelectedPage(Integer.parseInt(lineRead[2]));
                        docFiles.add(docFile);
                        System.out.println(TRANSLATOR.getString("FILE_LOAD_SUCCESS_1") + " " + file.getName() + " " + TRANSLATOR.getString("FILE_LOAD_SUCCESS_2") + " " + saveFilename);
                    } else {
                        System.out.println(TRANSLATOR.getString("FILE_LOAD_FAIL_1") + " " + file.getName() + " " + TRANSLATOR.getString("FILE_LOAD_FAIL_2") + " " + saveFilename);
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
     * V�rifie si le fichier est d�j� dans le fichier de sauvegarde
     *
     * @param docFile DocFile � v�rifier
     * @param saveFilename String nom du fichier de sauvegarde
     * @return true si le DocFile est pr�sent, false sinon
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
     * R�cup�re le DocFile du File renseign�
     * 
     * @param file File dont on cherche le DocFile
     * @return DocFile
     */
    private static DocFile getDocFileByFile(File file) {
        DocFile docFile = null;
        int i = 0;
        while (i < docFiles.size() && docFile == null) {
            if (docFiles.get(i).getFile().equals(file)) {
                docFile = docFiles.get(i);
            }
            i++;
        }
        return docFile;
    }

    /**
     * V�rifie si le fichier est d�j� ouvert
     *
     * @param file File ) v�rifier
     * @return true si le fichier est d�j� ouvert, false sinon
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
