package PRAKTIKUM_ASD.Bab12;

public class BubbleSorter {

    int[] L = { 25, 27, 10, 8, 76, 21 };

    void bubbleSort() {
        int hitungPerbandingan = 0;
        int hitungTukar = 0;

        int i, j, Max = 6, temp;
        for (i = 0; i < Max - 1; i++) {
            System.out.println("Langkah " + (i + 1) + ".");
            for (j = Max - 1; j > i; j--) {
                hitungPerbandingan++;
                if (L[j - 1] < L[j]) {
                    temp = L[j];
                    L[j] = L[j - 1];
                    L[j - 1] = temp;
                    hitungTukar++;
                }
                System.out.println(L[j] + " index: " + (j + 1));
            }
            System.out.println(L[j] + " index: " + (j + 1));
        }
        System.out.println("Hasil akhir:");
        for (i = 0; i <= 5; i++) {
            System.out.println(L[i] + " index:" + (i + 1));
        }

        System.out.println("\nJumlah Perbandingan: " + hitungPerbandingan);
        System.out.println("Jumlah Pertukaran: " + hitungTukar);
    }

    public static void main(String[] args) {
        BubbleSorter sorter = new BubbleSorter();
        sorter.bubbleSort();
    }
}