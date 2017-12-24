package shakenbeer.com.cmindtest.list;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import shakenbeer.com.cmindtest.domain.ListGenerator;

public class ListGeneratorImpl implements ListGenerator {

    private final static String[] WORDS = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty"};
    private static final int ITEMS_COUNT = 100;
    private static final BigInteger UPPER_LIMIT = new BigInteger("100000000000000000000");

    @Inject
    ListGeneratorImpl() {
    }

    @Override
    public List<String> generate() {
        List<String> result = new ArrayList<>(ITEMS_COUNT);

        for (int i = 0; i < ITEMS_COUNT; i++) {
            BigInteger random = generateRandomBigInteger();
            result.add(convert(random));
        }

        return result;
    }

    private BigInteger generateRandomBigInteger() {
        Random rand = new Random();
        BigInteger result;
        do {
            result = new BigInteger(UPPER_LIMIT.bitLength(), rand);
        } while (result.compareTo(UPPER_LIMIT) >= 0);
        return result;
    }

    String convert(BigInteger n) {
        String s = n.toString();

        if (s.length() == 0) {
            return s;
        }

        int traced = -1;
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int currentDigit = Character.getNumericValue(s.charAt(i));
            if (currentDigit != traced) {
                if (traced != -1) {
                    add(sb, traced, counter, false);
                }
                traced = currentDigit;
                counter = 1;
            } else {
                counter++;
            }
        }
        add(sb, traced, counter, true);

        return sb.toString();
    }

    private void add(StringBuilder sb, int traced, int counter, boolean isLast) {
        sb.append(WORDS[counter]).append(" ").append(traced);
        if (counter > 1) {
            sb.append("s");
        }
        if (!isLast) {
            sb.append(", ");
        }
    }
}
