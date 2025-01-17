
import java.util.Random;

public class MergeSortThreaded {

    public static void finalMerge(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        int i = 0;
        int j = 0;
        int r = 0;
        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                result[r] = a[i];
                i++;
                r++;
            } else {
                result[r] = b[j];
                j++;
                r++;
            }
            if (i == a.length) {
                while (j < b.length) {
                    result[r] = b[j];
                    r++;
                    j++;
                }
            }
            if (j == b.length) {
                while (i < a.length) {
                    result[r] = a[i];
                    r++;
                    i++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        int[] original = new int[10000000];
        for (int i = 0; i < original.length; i++) {
            original[i] = rand.nextInt(1000);
        }

        long startTime = System.currentTimeMillis();
        int[] subArr1 = new int[original.length / 2];
        int[] subArr2 = new int[original.length - original.length / 2];
        System.arraycopy(original, 0, subArr1, 0, original.length / 2);
        System.arraycopy(original, original.length / 2, subArr2, 0, original.length - original.length / 2);

        Worker runner1 = new Worker(subArr1);
        Worker runner2 = new Worker(subArr2);
        runner1.start();
        runner2.start();
        runner1.join();
        runner2.join();
        finalMerge(runner1.getInternal(), runner2.getInternal());
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("2-thread MergeSort takes: " + (float) elapsedTime / 1000 + " seconds");
    }

}

class Worker extends Thread {

    private int[] internal;

    public int[] getInternal() {
        return internal;
    }

    public void mergeSort(int[] array) {
        if (array.length > 1) {
            int[] left = leftHalf(array);
            int[] right = rightHalf(array);

            mergeSort(left);
            mergeSort(right);

            merge(array, left, right);
        }
    }

    public int[] leftHalf(int[] array) {
        int size1 = array.length / 2;
        int[] left = new int[size1];
        for (int i = 0; i < size1; i++) {
            left[i] = array[i];
        }
        return left;
    }

    public int[] rightHalf(int[] array) {
        int size1 = array.length / 2;
        int size2 = array.length - size1;
        int[] right = new int[size2];
        for (int i = 0; i < size2; i++) {
            right[i] = array[i + size1];
        }
        return right;
    }

    public void merge(int[] result, int[] left, int[] right) {
        int i1 = 0;
        int i2 = 0;

        for (int i = 0; i < result.length; i++) {
            if (i2 >= right.length || (i1 < left.length && left[i1] <= right[i2])) {
                result[i] = left[i1];
                i1++;
            } else {
                result[i] = right[i2];
                i2++;
            }
        }
    }

    Worker(int[] arr) {
        internal = arr;
    }

    public void run() {
        mergeSort(internal);
    }
}
