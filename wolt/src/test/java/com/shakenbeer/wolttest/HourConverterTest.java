package com.shakenbeer.wolttest;


import com.shakenbeer.wolttest.convert.Hours2Strings;
import com.shakenbeer.wolttest.convert.Json2Hours;
import com.shakenbeer.wolttest.model.Hour;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class HourConverterTest {

    private static final Map<String, Hour[]> week = Json2Hours.getInstance().convert(TestData.example1);

    private List<String> strings;

    @Before
    public void converts() {
        strings = Hours2Strings.getInstance().convert(week);
    }

    @Test
    public void mondayTest() {
        assertEquals("Monday: Malformed data", strings.get(0));
    }

    @Test
    public void tuesdayTest() {
        assertEquals("Tuesday: 10.30 am - 6 pm", strings.get(1));
    }

    @Test
    public void wednesdayTest() {
        assertEquals("Wednesday: Closed", strings.get(2));
    }

    @Test
    public void thursdayTest() {
        assertEquals("Thursday: 10 am - 6 pm", strings.get(3));
    }

    @Test
    public void fridayTest() {
        assertEquals("Friday: Malformed data", strings.get(4));
    }

    @Test
    public void saturdayTest() {
        assertEquals("Saturday: 9 am - 11 am, 4 pm - 11 pm", strings.get(5));
    }

    @Test
    public void sundayTest() {
        assertEquals("Sunday: 12 pm - 9 pm", strings.get(6));
    }

    @Test
    public void conversion_TwoDays() throws Exception {
        Map<String, Hour[]> result = Json2Hours.getInstance().convert(TestData.exampleTwoDays);
        List<String> days = Hours2Strings.getInstance().convert(result);
        assertEquals("Monday: No data provided", days.get(0));
    }
}
