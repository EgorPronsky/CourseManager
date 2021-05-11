package util.receivers;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public interface Receiver {
    Map<String, Object> receiveUserInput(HttpServletRequest req, Set<String> requiredParamNames);
}
