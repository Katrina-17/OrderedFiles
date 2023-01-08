import orderedfiles.Document;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import orderedfiles.*;

public class Main {
    public static void main(String[] args) {

        var scanner = new Scanner(System.in);
        System.out.println("Enter the absolute path to the root folder: ");
        String rootFolderPath = scanner.nextLine();

        try {
            Document.setRootFolder(rootFolderPath);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Создаём список для хранения файлов
        var documentList = new DocumentList();
        var rootFolder = new File(rootFolderPath);
        // Находим все текстовые файлы в корневой папке и её подпапках и вставляем их в список.
        var rootChildren = getChildrenFiles(rootFolder);
        if (rootChildren.isEmpty()) {
            System.out.println("No files in such directory");
            return;
        }
        for (var file : rootChildren) {

            Document doc;
            try {
                doc = new Document(file.getPath());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            if (!documentList.insert(doc)) {
                return;
            }
        }

        // Формируем результат.
        File resultFile = documentList.getResultFile();
        if (resultFile == null) {
            System.out.println("Impossible to generate the result file!");
        } else {
            System.out.println("Result is in the file: " + resultFile.getPath());
        }
    }

    /**
     * Метод для создания списка всех текстовых файлов в заданной папке и её подпапках.
     * @param initialDirectory Начальная папка
     * @return Созданный список файлов.
     */
    private static ArrayList<File> getChildrenFiles(File initialDirectory) {
        var result = new ArrayList<File>();
        var currentChildren = initialDirectory.list();
        if (currentChildren == null) {
            return result;
        }
        for (String filePath : currentChildren) {
            var file = new File(initialDirectory + File.separator + filePath);
            if (file.isFile() && filePath.endsWith(".txt")) {
                result.add(file);
            } else if (file.isDirectory()) {
                var subResult = getChildrenFiles(file);
                result.addAll(subResult);
            }
        }
        return result;
    }
}