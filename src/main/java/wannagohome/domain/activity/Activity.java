package wannagohome.domain.activity;

import java.util.Date;

public interface Activity {
    Object[] getArguments();

    String getCode();
    Date getRegisteredDate();
}

