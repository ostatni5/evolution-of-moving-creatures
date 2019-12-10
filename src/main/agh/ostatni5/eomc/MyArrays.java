package agh.ostatni5.eomc;

import java.util.Random;

public class MyArrays {
    private static Random random = new Random();
    public static void shuffleArray(int[] array)
    {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }
}
