package com.company.manager.handlers.view_handlers;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Map;

public interface ViewHandler {

    void renderView(String uri, Map<String, Object> attributes,
                    ServletRequest req, ServletResponse resp) throws ServletException, IOException;
}
