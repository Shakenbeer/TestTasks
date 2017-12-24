package com.shakenbeer.wolttest;

import com.shakenbeer.wolttest.convert.Json2Hours;
import com.shakenbeer.wolttest.model.Hour;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JsonConverterTest {

    Map<String, Hour[]> values;

    @Before
    public void convert() {
        values = Json2Hours.getInstance().convert(TestData.example1);
    }

    @Test
    public void conversion_isCorrect1() throws Exception {
        assertEquals(1, values.get("monday").length);
    }

    @Test
    public void conversion_isCorrect2() throws Exception {
        assertEquals(2, values.get("tuesday").length);
    }

    @Test
    public void conversion_isCorrect3() throws Exception {
        assertEquals(36000, values.get("thursday")[0].getValue());
    }

    @Test
    public void conversion_isCorrect4() throws Exception {
        assertEquals(4, values.get("saturday").length);
        assertTrue(values.get("saturday")[1].isClose());
    }

    @Test
    public void conversion_badSource1() throws Exception {
        Map<String, Hour[]> result = Json2Hours.getInstance().convert(TestData.example2);
        assertEquals(0, result.size());
    }

    @Test
    public void conversion_badSource2() throws Exception {
        Map<String, Hour[]> result = Json2Hours.getInstance().convert(TestData.example3);
        assertEquals(0, result.size());
    }

    @Test
    public void conversion_emptySource() throws Exception {
        Map<String, Hour[]> result = Json2Hours.getInstance().convert(TestData.example4);
        assertEquals(0, result.size());
    }

    @Test
    public void conversion_TwoDays() throws Exception {
        Map<String, Hour[]> result = Json2Hours.getInstance().convert(TestData.exampleTwoDays);
        assertEquals(2, result.size());
    }

}