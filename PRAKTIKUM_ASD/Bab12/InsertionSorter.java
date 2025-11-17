package PRAKTIKUM_ASD.Bab12;

public class InsertionSorter {
    int[] L = new int[7]; // Array ukuran 7 untuk 1-based indexing

    void insertionSort() {
        int k, temp, j;

        // Inisialisasi data (sesuai modul)
        L[1] = 29;
        L[2] = 27;
        L[3] = 10;
        L[4] = 8;
        L[5] = 76;
        L[6] = 21;

        for (k = 2; k <= 6; k++) {
            temp = L[k];
            j = k - 1;
            System.out.println("Langkah" + (k - 1));

            // Geser elemen yang lebih besar ke kanan
            // Modul menggunakan logic 'while' yang sedikit aneh
            // Ini adalah 'while' yang lebih standar:
            while (j >= 1 && L[j] > temp) {
                L[j + 1] = L[j];
                j--;
            }
            L[j + 1] = temp; // Sisipkan 'temp' ke posisi yang tepat

            // Kode 'if/else' di modul (baris 21-26) sangat membingungkan
            // dan tidak standar. Logika 'while' di atas sudah
            // mencakup fungsionalitasnya.

            // Cetak hasil tiap langkah
            for (int i = 1; i <= 6; i++) {
                System.out.println(L[i] + " index:" + i);
            }
        }

        System.out.println("Hasil Akhir:");
        for (int i = 1; i <= 6; i++) {
            System.out.println(L[i] + " index:" + i);
        }
    }

    public static void main(String[] args) {
        InsertionSorter sorter = new InsertionSorter();
        sorter.insertionSort();
    }
}