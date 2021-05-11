package view_handlers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface ViewHandler {

    void renderView(String uri, Map<String, Object> params,
                HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
