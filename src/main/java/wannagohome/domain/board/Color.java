package wannagohome.domain.board;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Color {
    VERY_DARK_BLUE("#003366"),
    SLIGHTLY_DESATURATED_CYAN("#7DB9B3"),
    DARK_LIME_GREEN("#228B22"),
    GRAYISH_RED("#D4AAA8"),
    GRAYISH_GREEN("#AFC09A"),
    MOSTLY_DESATURATED_DARK_YELLOW("#A8A461"),
    BROWN("#4D3727"),
    LIGHT_BLUE("#5F72FF"),
    VIVID_BLUE("#18B6FD");

    private String code;

    Color(String code) {
        this.code = code;
    }

    public static Color of(String code) {
        return Arrays.stream(values())
                .filter(color -> color.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
