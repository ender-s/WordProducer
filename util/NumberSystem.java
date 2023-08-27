package util;

import java.util.Arrays;
import java.util.Collections;

public class NumberSystem {

    /**
     * Converts the given decimal number to base-n number system.
     * The result is padded to length <padTo>.
     * @param decimal the decimal number to be converted.
     * @param n target number system base.
     * @param padTo the result is padded to <padTo> elements.
     *              0s are added to the beginning of the resulting list.
     * @return an array consisting of integers which range from 0 to n-1.
     */
    public static Integer[] decimalToBaseN(int decimal, int n, int padTo)
    {
        Integer[] baseNResult = new Integer[padTo];
        Arrays.fill(baseNResult, 0);
        int index = 0;
        while (decimal > 0)
        {
            int remainder = decimal % n;
            decimal = decimal / n;
            baseNResult[index] = remainder;
            index++;
        }
        Collections.reverse(Arrays.asList(baseNResult));
        return baseNResult;
    }
}
