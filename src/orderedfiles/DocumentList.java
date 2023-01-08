package orderedfiles;

import java.io.*;
import java.util.LinkedList;

/**
 * Класс, представляющий отсортированный в требуемом порядке список файлов.
 */
public class DocumentList {

    /**
     * Двусвязный список файлов, вставка в который производится в соответствии с требованиями.
     */
    private LinkedList<Document> list;

    /**
     * Конструктор.
     */
    public DocumentList() {
        list = new LinkedList<Document>();
    }

    // вставка нового элемента. false, Если возникла циклическая зависимость, true иначе

    /**
     * Метод для вставки в список нового элемента в соответствии с требованиями.
     * @param doc Документ для вставки.
     * @return false, если при вставке возникла циклическая зависимость, true иначе.
     */
    public boolean insert(Document doc) {
        if (list.isEmpty()) {
            list.add(doc);
            return true;
        }
        // шаг 1: минимальная позиция в списке, на которую может быть вставлен
        // файл согласно своим require
        int minPosition = 0;
        for (int i = 0; i < list.size(); i++) {
            if (doc.isRequired(list.get(i))) {
                minPosition = i + 1;
            }
        }
        // шаг 2: проверяем, есть ли до minPosition файлы, которые require doc
        for (int i = 0; i < minPosition; i++) {
            if (list.get(i).isRequired(doc)) {
                System.out.println("Requirement cycle with files " + list.get(i).getPath() +
                        " and " + doc.getPath());
                return false;
            }
        }
        // шаг 3: если всё хорошо, производим вставку
        list.add(minPosition, doc);
        return true;
    }

    /**
     * Метод для получения файла-результата путём конкатенации файлов из списка.
     * @return Объект File для файла-результата.
     */
    public File getResultFile() {
        File result = new File(Document.getRootFolderPath() + "Result.txt");
        try {
            if (result.exists()) {
                result.delete();
                result.createNewFile();
            }
        } catch (IOException e) {
            return null;
        }
        for (Document doc : list) {
            if (!doc.appendAllText(result.getAbsolutePath())) {
                return null;
            }
        }
        return result;
    }

    /**
     * Вспомогательный метод для вывода в консоль элементов списка.
     */
    public void printList() {
        System.out.println("\n");
        for (var i : list) {
            System.out.println(i.getPath());
        }
        System.out.println("\n");
    }
}
