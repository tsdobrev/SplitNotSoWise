package bg.sofia.uni.fmi.mjt.splitwise.utilities;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NameValidatorTest {
    @Test
    public void testIsValidWithEmptyString() {
        assertFalse("Zero characters are not allowed. Minimum one is required",
                NameValidator.isValid(""));
    }

    @Test
    public void testIsValidWithWrongNames() {
        assertFalse("It contains symbol that is not allowed",
                NameValidator.isValid("my name is"));
        assertFalse("It contains symbol that is not allowed",
                NameValidator.isValid("my_name.is-SpeCial%But*Invalid"));
    }

    @Test
    public void testIsValidWithCorrectNames() {
        assertTrue("It contains acceptable characters: letters, digits, -_.",
                NameValidator.isValid("."));
        assertTrue("It contains acceptable characters: letters, digits, -_.",
                NameValidator.isValid("my_name.is-SpeCial_But.valId"));
    }
}
