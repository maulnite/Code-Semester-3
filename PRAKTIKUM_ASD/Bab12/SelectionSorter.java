package PRAKTIKUM_ASD.Bab12;

public class SelectionSorter {
    int[] L = { 25, 27, 10, 8, 76, 21 };

    void selectionSort() {
        int hitungPerbandingan = 0;
        int hitungTukar = 0;

        int j, k, i, temp;
        int jmin, u = 5;

        for (j = 0; j < 6; j++) {
            jmin = 0;
            System.out.println("Langkah " + (j + 1) + ".");

            for (k = 1; k <= u; k++) {
                if (L[k] < L[jmin]) {
                    jmin = k;
                }
                hitungPerbandingan++;
            }

            if (jmin != u) {
                temp = L[u];
                L[u] = L[jmin];
                L[jmin] = temp;
                hitungTukar++;
            }
            u--;

            for (i = 0; i <= 5; i++) {
                System.out.println(L[i] + " index:" + (i + 1));
            }
        }

        System.out.println("Hasil akhir:");
        for (i = 0; i <= 5; i++) {
            System.out.println(L[i] + " index:" + (i + 1));
        }

        System.out.println("\nJumlah Perbandingan: " + hitungPerbandingan);
        System.out.println("Jumlah Pertukaran: " + hitungTukar);
    }

    public static void main(String[] args) {
        SelectionSorter sorter = new SelectionSorter();
        sorter.selectionSort();
    }
}