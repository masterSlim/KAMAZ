package main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Класс для чтения, обработки и вывода на консоль файлов со списком сотрудников формата
 * 1|0| Петров
 * где
 * 1 - id сотрудника
 * 0 - id  руковоителя сотрудника
 * Петров - фамилия сотрудника.
 */

public class Processor {
    /** Поле содержащее полный путь к обрабатываемому файлу. */
    private String filePath;

    /** Поле с внутренним содержимым файла. */
    private StringBuilder fileStream;

    // Объект класса необходимый при работе с вводимыми в консоль данными
    private Scanner in;

    /** Карта содержащая id(ключ) фамилии(значение) сотрудников в файле. */
    private HashMap<Integer, String> staff;

    /** Карта содержащая id(ключ) сотрудника и массив(значение) его подчинённых. */
    private HashMap<Integer, ArrayList<Integer>> hierarchy;

    // Массив, в который заносятся id сотрудников уже учтённых при построении дерева
    ArrayList<Integer> sorted;

    /** Конструкор инициализирующий необходимые поля при создании экзепляра. */
    public Processor() {
        sorted = new ArrayList<>();
        filePath = "";
        in = new Scanner(System.in);
        fileStream = new StringBuilder();
    }

    /**
     * Запрашивает полный путь файла в консоли, обрабатывает введённые данные и читает файл, если он найде. В случае,
     * если файл не найден или его не удаётся прочитать, вывдодит собщение на консоль и запускает себя заново
     */
    public void readFile() {
        // Вывод сообщения на консоль
        System.out.print("Введите полный адрес файла:");
        // Ожидание ввода
        filePath = in.nextLine();
        // Обработка ввода
        try {
            // Открытие файла
            FileReader fr = new FileReader(filePath);

            // Чтение сожеримого файла
            BufferedReader br = new BufferedReader(fr);

            // Обработка пробелов в начале и в конце строки
            String line = br.readLine().trim();

            // Цикл для существующих строк
            while (line != null) {

                // Если строка не пуста
                if (!line.equals("")) {

                    // Полезные данные добавляются в fileStream
                    fileStream.append(line).append("\n");
                }

                // Переход к следующей строке
                line = br.readLine();
            }
            // Закрытие потоков в обратном порядке
            br.close();
            fr.close();
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден\n");
            // Перезаупск
            readFile();
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла\n");
            // Перезаупск
            readFile();
        }
    }

    /**
     * Обрабатывает данные находящиеся в fileStream
     */
    public void processFile() {
        // Инициализация карты для списка всех сотрудников
        staff = new HashMap<>();

        // Инициализация карты для хранения структуры сотрудников
        hierarchy = new HashMap<>();
        // Преобразование данных из fileStream в массив String состоящих из отдельных строк
        String lines[] = fileStream.toString().split("\n");

        // Обработка каждого эелемента (строки) массива
        for (String s : lines) {
            // Очистка от пробелов
            s = s.replace(" ", "");

            // Разделение данных внутри строки
            String[] sliced = s.split("\\|");
            Integer ownNumb = Integer.parseInt(sliced[0]);
            Integer mngNumb = Integer.parseInt(sliced[1]);
            String surname = sliced[2];
            // Добавление id(ключ) и фамилия(значение) в карту списка всех сотрудников
            staff.put(ownNumb, surname);
            // Если сотрудник еще не добавлен в карту дерева сотрудников
            if (!hierarchy.containsKey(ownNumb)) {
                // Сотрудник добавляется в формате id(ключ) и массив(значение) для id его подчинённых
                hierarchy.put(ownNumb, new ArrayList<>());
            }
            // Если руководитель сотрудника еще не добавлен в карту дерева сотрудников
            if (!hierarchy.containsKey(mngNumb)) {
                // Его руководитель добавляется в формате id(ключ) и массив(значение) для id его подчинённых
                hierarchy.put(mngNumb, new ArrayList<>());
            }
            // Если руководитель сотрудника уже есть в карте дерева, то к нему добавляется id текущего сотрудника
            hierarchy.get(mngNumb).add(ownNumb);
        }

    }

    /**
     * Выводит дерево сотрудников на консоль
     */
    public void printHierarchy() {
    try{
        printHierarchy(0, hierarchy.keySet());
    }catch (NullPointerException e){
        System.out.println("Иерархия не выстроена, так как файл не был обработан");
    }
    }

    /*
     * Закрытый метод, который выстраивает в правильном виде вывод дерева сотрудников на консоль, вызывая самого себя
     * для каждого нового уровня
     * startLevel - уровень в иерархии дерева, с которого был вызван метод
     * al - коллекция, содержащая список подчинённых текущего сотрудника
     */

    private void printHierarchy(int level, Collection<Integer> al) {
        // Обработка каждого подчинённого по его id
        for (Integer i : al) {
            // Проверка, не был ли уже обработан этот id ранее
            if (!sorted.contains(i)) {
                // Самый верхний уровень (key=0) который не закреплён ни за кем (value=null) не выводится в дереве
                // В выводе на консоль вызывается метод separate() добавляет разделители "-" для наглядности

                if (staff.get(i) != null) System.out.println(separate(level) + staff.get(i));
                sorted.add(i);
                printHierarchy(level + 1, hierarchy.get(i));
            }
        }
    }

    /*
     * Закрытый метод, который  в соотетствии с переданным в качетсве аргумента (level) уровнем возвращает строку
     * с соответствующим количеством разделителей "-" для наглядности
     * level - уровень в иерархии дерева, с которого был вызван метод
     */

    private String separate(int level) {
        StringBuilder separator = new StringBuilder();
        for (int i = 1; i < level; i++) {
            separator.append("-");
        }
        return separator.toString();
    }
}
