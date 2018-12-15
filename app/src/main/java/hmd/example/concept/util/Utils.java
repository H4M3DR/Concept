package hmd.example.concept.util;

import java.math.BigInteger;
import java.util.Random;

public class Utils {

    //This method gives a random big integer with a maximum threshold
    public static BigInteger nextRandomBigInteger(BigInteger maxNumber) {
        Random rand = new Random();
        BigInteger result = new BigInteger(maxNumber.bitLength(), rand);
        while( result.compareTo(maxNumber) >= 0) {
            result = new BigInteger(maxNumber.bitLength(), rand);
        }

        return result;
    }

    //This method splits sequential repetitive numbers
    public static String[] splitStringSameCharacters(String string)
    {
        return string.split("(?<=(.))(?!\\1)");
    }

    //This method joins the splitted Strings with divider
    public static String joinStrings(String[] arr,String divider){
        StringBuilder builder = new StringBuilder();
        for(String s : arr) {
            builder.append(divider + s);
        }
        String str = builder.toString();
        return str.substring(1);
    }
}
