package wannagohome.domain;

import java.util.Arrays;

public enum Color {
    RED("#FF0000"),
    BLUE("#026aa7"),
    GRAY("#aaaaaa");

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

    public String getCode() {
        return code;
    }
}
