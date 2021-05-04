package bg.sofia.uni.fmi.mjt.splitwise.utilities;

import org.junit.Test;

import java.time.LocalDateTime;
import static org.junit.Assert.assertEquals;

public class DateToStringParserTest {
    @Test(expected = IllegalArgumentException.class)
    public void testParseThrowsIllegalArgumentException() {
        DateToStringParser.parse(null);
    }

    @Test
    public void testParse() {
        LocalDateTime testTime =
                LocalDateTime.of(2021, 1, 22, 8, 33, 14, 123);
        String expected = "22.01.2021 08:33:14";
        String actual = DateToStringParser.parse(testTime);

        assertEquals("Should be in dd.MM.yyyy HH:mm:ss format", expected, actual);
    }
}
