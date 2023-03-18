package hw3;

import java.io.*;

public class Sorter {
    private void split(File dataFile, File file1, File file2, int step) {
        try (BufferedReader dataFileReader = new BufferedReader(new FileReader(dataFile));
             PrintWriter printWriterFile1 = new PrintWriter(file1);
             PrintWriter printWriterFile2 = new PrintWriter(file2)) {
            String line = dataFileReader.readLine();
            while (line != null) {
                for (int i = 0; i < step && line != null; i++) {
                    printWriterFile1.println(line);
                    line = dataFileReader.readLine();
                }
                for (int j = 0; j < step && line != null; j++) {
                    printWriterFile2.println(line);
                    line = dataFileReader.readLine();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void merge(File dataFile, File file1, File file2, int step) {
        try (PrintWriter printWriterDataFile = new PrintWriter(dataFile);
             BufferedReader readerFile1 = new BufferedReader(new FileReader(file1));
             BufferedReader readerFile2 = new BufferedReader(new FileReader(file2))) {
            String numberFile1 = readerFile1.readLine();
            String numberFile2 = readerFile2.readLine();
            while (numberFile1 != null && numberFile2 != null) {
                int i = 0;
                int j = 0;
                while (i < step && j < step && numberFile1 != null && numberFile2 != null) {
                    if (Long.parseLong(numberFile1) < Long.parseLong(numberFile2)) {
                        printWriterDataFile.println(numberFile1);
                        numberFile1 = readerFile1.readLine();
                        ++i;
                    } else {
                        printWriterDataFile.println(numberFile2);
                        numberFile2 = readerFile2.readLine();
                        ++j;
                    }
                }
                while (i < step && numberFile1 != null) {
                    printWriterDataFile.println(numberFile1);
                    numberFile1 = readerFile1.readLine();
                    ++i;
                }
                while (j < step && numberFile2 != null) {
                    printWriterDataFile.println(numberFile2);
                    numberFile2 = readerFile2.readLine();
                    ++j;
                }
            }
            while (numberFile1 != null) {
                printWriterDataFile.println(numberFile1);
                numberFile1 = readerFile1.readLine();
            }
            while (numberFile2 != null) {
                printWriterDataFile.println(numberFile2);
                numberFile2 = readerFile2.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private int countLines(File dataFile) {
        int amountOfNumbers = 0;
        try (BufferedReader dataFileReader = new BufferedReader(new FileReader(dataFile))) {
            String lineFromDataFile = dataFileReader.readLine();
            while (lineFromDataFile != null) {
                amountOfNumbers++;
                lineFromDataFile = dataFileReader.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return amountOfNumbers;
    }

    public File sortFile(File dataFile) throws IOException {
        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");
        File resultFile = new File("hw3/result.txt");
        int amountOfNumbers = countLines(dataFile);
        int stepSize = 1;
        split(dataFile, file1, file2, stepSize);
        merge(resultFile, file1, file2, stepSize);
        stepSize *= 2;
        while (stepSize < amountOfNumbers) {
            split(resultFile, file1, file2, stepSize);
            merge(resultFile, file1, file2, stepSize);
            stepSize *= 2;
        }
        file1.delete();
        file2.delete();
        return resultFile;
    }
}
