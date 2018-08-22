package wannagohome.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {

    private String subscribeTopicUrl;

    private String message;
}
