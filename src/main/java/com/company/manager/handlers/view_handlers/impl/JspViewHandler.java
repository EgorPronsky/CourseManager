package com.company.manager.handlers.view_handlers.impl;

import com.company.manager.handlers.view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspViewHandler implements ViewHandler {

    @Override
    public void renderView(String uri, Map<String, Object> attributes,
                           ServletRequest req, ServletResponse resp) throws ServletException, IOException {

        // Setting attributes in request for jsp
        if (attributes != null) attributes.forEach(req::setAttribute);

        req.getServletContext().getRequestDispatcher(uri).forward(req, resp);
    }
}
