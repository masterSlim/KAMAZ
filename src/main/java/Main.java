package main.java;

/**
 * Главный класс приложения.
 */
public class Main {
    /**
     * Запускает приложение. Никакх входных параметров не требуется.
     * @param args
     */
    public static void main(String[] args) {
        // Создается экземпляр класса-обработчика
        Processor processor = new Processor();

        // Чтение файла
        try{
            processor.readFile();
        } catch(Exception e){
            System.out.println("Невозможно прочитать файл:\n" + e);
        }

        // Обработка файла из исходного текста в дерево сотрудников по подчинённости
        try {
            processor.processFile();
        } catch (NumberFormatException ne){
            System.out.println("Нарушена структура входного файла");
        } catch(Exception e){
            System.out.println("Невозможно обработать файл:\n" + e);
        }

        // Вывод на консоль результата обработки
        try{
            processor.printHierarchy();
        } catch(Exception e){
            System.out.println("Невозможно вывести дерево сотрудников на консоль:\n" + e);
        }
    }
}
