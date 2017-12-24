package shakenbeer.com.cmindtest.list;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GeneratorUnitTest {

    private ListGeneratorImpl generator = new ListGeneratorImpl();

    @Test
    public void conversionIsCorrect21() throws Exception {
        String s = generator.convert(new BigInteger("21"));
        assertEquals("one 2, one 1", s);
    }

    @Test
    public void conversionIsCorrect1121() throws Exception {
        String s = generator.convert(new BigInteger("1121"));
        assertEquals("two 1s, one 2, one 1", s);
    }

    @Test
    public void conversionIsCorrect333() throws Exception {
        String s = generator.convert(new BigInteger("333"));
        assertEquals("three 3s", s);
    }

    @Test
    public void conversionIsCorrect55566632477742321565() throws Exception {
        String s = generator.convert(new BigInteger("55566632477777777565"));
        assertEquals("three 5s, three 6s, one 3, one 2, one 4, eight 7s, one 5, one 6, one 5", s);
    }

    @Test
    public void conversionIsCorrect7() throws Exception {
        String s = generator.convert(new BigInteger("7"));
        assertEquals("one 7", s);
    }

    @Test
    public void conversionIsCorrect10000000000000000000() throws Exception {
        String s = generator.convert(new BigInteger("10000000000000000000"));
        assertEquals("one 1, nineteen 0s", s);
    }
}