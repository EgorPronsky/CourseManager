package view_handlers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspViewHandler implements ViewHandler {

    @Override
    public void renderView(String uri, Map<String, Object> attributes,
                       HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Setting attributes for jsp
        attributes.forEach(req::setAttribute);

        req.getServletContext().getRequestDispatcher(uri).forward(req, resp);
    }
}
