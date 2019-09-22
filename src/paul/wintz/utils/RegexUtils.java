package paul.wintz.utils;

public class RegexUtils {

    public static final String FLOAT_REG_EX = "\\d*(\\..\\d*)?";
    public static final String PLUS_OR_MINUS_REG_EX = "(-|\\+)?";
    public static final String NATURAL_NUMBER_REG_EX = "\\d+";
    public static final String INTEGER_REG_EX = PLUS_OR_MINUS_REG_EX + NATURAL_NUMBER_REG_EX;
    public final static String WORD_BOUNDARY = "\\b";

    private RegexUtils() {
        // Prevent instantiation
    }

}
