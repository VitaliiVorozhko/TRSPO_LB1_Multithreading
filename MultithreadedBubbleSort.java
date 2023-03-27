import java.io.*;
import java.util.*;

public class MultithreadedBubbleSort {
    public static void main(String[] args) throws Exception {
        int[] arr;
        int n = 1000; // Заданий розмір масиву



        // Створюємо масив і заповнюємо його випадковими числами
        arr = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(100);
        }

        // Зберігаємо масив у файл
        String filename = "input.txt";
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (int i = 0; i < n; i++) {
                writer.println(arr[i]);
            }
        }

        // Зчитуємо масив з файлу
        try (Scanner scanner = new Scanner(new File(filename))) {
            arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }
        }

        // Створюємо потоки для сортування масиву
        int numThreads = Runtime.getRuntime().availableProcessors();
        List<SortThread> threads = new ArrayList<>();
        int chunkSize = n / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? n : (i + 1) * chunkSize;
            threads.add(new SortThread(arr, start, end));
        }

        // Запускаємо потоки
        long startTime = System.currentTimeMillis();
        for (SortThread thread : threads) {
            thread.start();
        }

        // Чекаємо завершення потоків
        for (SortThread thread : threads) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();

        // Виводимо результати
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();

        // Виводимо час сортування
        System.out.println("Sorting time: " + (endTime - startTime) + " ms");
    }
}

class SortThread extends Thread {
    private int[] arr;
    private int start;
    private int end;

    public SortThread(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    public void run() {
        for (int i = start; i < end; i++) {
            for (int j = start; j < end - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr
                            [j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}
