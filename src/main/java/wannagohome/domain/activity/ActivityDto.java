package wannagohome.domain.activity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class ActivityDto {
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registeredDate;

    private ActivityDto(String message, Date registeredDate) {
        this.message = message;
        this.registeredDate = registeredDate;
    }


    public static ActivityDto valueOf(MessageSourceAccessor messageSourceAccessor, Activity activity) {
        return new ActivityDto(messageSourceAccessor.getMessage(activity.getCode(), activity.getArguments())
                , activity.getRegisteredDate());
    }


}
