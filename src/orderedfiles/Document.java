package orderedfiles;

import java.io.*;
import java.util.ArrayList;

/**
 * Класс, представляющий файл с директивами require
 */
public class Document {

    /**
     * Заданный пользователем путь к корневой папке.
     */
    private static String rootFolderPath = "";

    /**
     * Используемый файл.
     */
    private File document;

    /**
     * Список путей к файлам, require для которых содержится в данном файле.
     */
    private ArrayList<String> requires;


    // path - путь от "корневой" папки, её собственный адрес ещё нужно прибавить

    /**
     * Конструктор для создания нового объекта-файла.
     * @param path Абсолютный путь к файлу
     * @throws IllegalArgumentException Неверный аргумент: файл не существует или не является файлом.
     */
    public Document(String path) throws IllegalArgumentException {
        String absolutePath = path;
        document = new File(absolutePath);
        if (!document.exists() || !document.isFile()) {
            throw new IllegalArgumentException("Incorrect file path" + absolutePath + "!");
        }
        requires = new ArrayList<String>();
        if (!getRequires()) {
            throw new IllegalArgumentException("File " + absolutePath + " cannot be read correctly");
        }
    }

    /**
     * Метод для заполнения списка requires для данного файла.
     * @return true в случае успешного заполнения, false в случае ошибки.
     */
    private boolean getRequires() {
        try (var reader = new BufferedReader(new FileReader(document))) {
            String nextLine = reader.readLine();
            while (nextLine != null) {
                if (nextLine.contains("require '")) {
                    var nextFile = new File(rootFolderPath + nextLine.substring(9, nextLine.length() - 1));
                    requires.add(nextFile.getPath());
                }
                nextLine = reader.readLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Метод, задающий адрес корневой папки, указанной пользователем в начале работы.
     * @param path Абсолютнрый путь к корневой папке
     * @throws IllegalArgumentException Неверный аргумент: папка не существует или не является
     * папкой
     */
    public static void setRootFolder(String path) throws IllegalArgumentException {
        if (!(new File(path)).exists() || !(new File(path).isDirectory())) {
            throw new IllegalArgumentException("Folder " + path + " does not exist");
        }
        rootFolderPath = path + File.separator;
    }

    /**
     * Метод для получения пути к корневой папке.
     * @return Абсолютный путь к корневой папке
     */
    public static String getRootFolderPath() {
        return rootFolderPath;
    }

    /**
     * Метод для получения абсолютного пути к данному файлу.
     * @return Абсолютный путь к файлу.
     */
    public String getPath() {
        return document.getAbsolutePath();
    }

    /**
     * Метод, проверяющий, содержится ли в данном файле директива require на указанный файл.
     * @param file Файл, для которого производится проверка.
     * @return true, если директива содержится, false, если нет.
     */
    public boolean isRequired(Document file) {
        return requires.contains(file.getPath());
    }

    // дописывает текст из document в файл по адресу. Возвращает информацию об успехе/неудаче.

    /**
     * Метод, дописывающий в указанный файл текст из текущего файла.
     * @param destinationFileName Файл, в который должна производиться запись.
     * @return true, если запись была произведена успешно, false, если возникла ошибка.
     */
    public boolean appendAllText(String destinationFileName) {
        try (var reader = new BufferedReader(new FileReader(document));
                var writer = new BufferedWriter(new FileWriter(destinationFileName, true))) {
            String nextLine = reader.readLine();
            while (nextLine != null) {
                writer.append("\n");
                writer.append(nextLine);
                nextLine = reader.readLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}
