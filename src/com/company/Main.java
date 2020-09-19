package com.company;

public class Main {

    public static void main(String[] args) {
        Processor processor = new Processor();
        String fileStream = processor.readFile();
        processor.processFile(fileStream);
        processor.printHierarchy();
    }


}
