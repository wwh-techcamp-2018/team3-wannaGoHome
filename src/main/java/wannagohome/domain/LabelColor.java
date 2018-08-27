package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum LabelColor {
    RED("#ff3352"),
    ORANGE("#ff7a33"),
    YELLOW("#f9d814"),
    GREEN("#7ae576"),
    BLUE("#7fa0fb");


    private String code;

    LabelColor(String code) {
        this.code = code;
    }

    public static LabelColor of(String code) {
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
