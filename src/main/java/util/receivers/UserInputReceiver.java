package util.receivers;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class UserInputReceiver implements Receiver {

    @Override
    public Map<String, Object> receiveUserInput(HttpServletRequest req, Set<String> requiredParamNames) {
        Map<String, Object> attributes = new HashMap<>();
        requiredParamNames.forEach(name -> attributes.put(name, req.getParameter(name)));
        return attributes;
    }
}
