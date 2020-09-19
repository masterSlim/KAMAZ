package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Processor {
    private String filePath;
    private StringBuilder fileStream;
    private Scanner in;
    private HashMap<Integer, String> staff;
    private HashMap<Integer, ArrayList<Integer>> hierarchy;
    ArrayList<Integer> sorted;

    public Processor() {
        sorted = new ArrayList<>();
        filePath = "";
        in = new Scanner(System.in);
        fileStream = new StringBuilder();
    }

    public String readFile() {
        System.out.print("Введите полный адрес файла:");
        filePath = in.nextLine();
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine().trim();
            while (line != null) {
                if (!line.equals("")) {
                    fileStream.append(line).append("\n");
                }
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден\n");
            readFile();
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла\n");
            readFile();
        }
        return fileStream.toString();
    }

    public void processFile(String fileStream) {
        staff = new HashMap<>();
        hierarchy = new HashMap<>();
        String lines[] = fileStream.split("\n");
        for (String s : lines) {
            s = s.replace(" ", "");
            String[] sliced = s.split("\\|");
            Integer ownNumb = Integer.parseInt(sliced[0]);
            Integer mngNumb = Integer.parseInt(sliced[1]);
            String surname = sliced[2];
            staff.put(ownNumb, surname);
            if (!hierarchy.containsKey(ownNumb)) {
                hierarchy.put(ownNumb, new ArrayList<Integer>());
            }
            if (!hierarchy.containsKey(mngNumb)) {
                hierarchy.put(mngNumb, new ArrayList<>());
            }
            hierarchy.get(mngNumb).add(ownNumb);
        }

    }

    public void printHierarchy() {
        printHierarchy(0, hierarchy.keySet());
    }

    private void printHierarchy(int startLevel, Collection<Integer> al) {
        int level = startLevel;
        for (Integer i : al) {
            if (!sorted.contains(i)) {
                if (staff.get(i) != null) System.out.println(separate(level) + staff.get(i));
                sorted.add(i);
                printHierarchy(level + 1, hierarchy.get(i));
            }
        }
    }

    private String separate(int level) {
        StringBuilder separator = new StringBuilder();
        for (int i = 1; i < level; i++) {
            separator.append("-");
        }
        return separator.toString();
    }
}
