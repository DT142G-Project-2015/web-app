package controller.filter;


import controller.LoginManager;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {

            try {

                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) resp;


                // Allow everyone access to login page
                if (request.getRequestURI().contains("/system/admin/login") ||
                    request.getRequestURI().contains("/system/style.css")) {
                    chain.doFilter(request, response);
                    return;
                }

                String auth = request.getHeader("Authorization");

                if (auth != null && auth.startsWith("Basic")) {
                    String hash = auth.split(" ")[1];
                    String creds = new String(Base64.getMimeDecoder().decode(hash), "UTF-8");

                    String username = creds.substring(0, creds.indexOf(":")).trim();
                    String password = creds.substring(creds.indexOf(":") + 1).trim();

                    if (LoginManager.isLoggedIn(username, password)) {
                        chain.doFilter(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                } else if (request.getCookies() != null) {
                    Optional<Cookie> loginCookie = Stream.of(request.getCookies())
                            .filter(c -> c.getName().equals("login"))
                            .findFirst();

                    if (loginCookie.isPresent()) {
                        String hash = loginCookie.get().getValue();

                        String creds = new String(Base64.getMimeDecoder().decode(hash), "UTF-8");

                        String username = creds.substring(0, creds.indexOf(":")).trim();
                        String password = creds.substring(creds.indexOf(":") + 1).trim();

                        if (LoginManager.isLoggedIn(username, password)) {
                            response.setHeader("X-Username", username);
                            response.addCookie(loginCookie.get());
                            chain.doFilter(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        }
                    }
                } else {
                    // TODO: Change to sendError: forbidden
                    chain.doFilter(req, resp);
                    //response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }

            } catch (SQLException e) {
                throw new IOException(e);
            }
        }



    }

    public void init(FilterConfig filterConfig) throws ServletException {}
    public void destroy() {}
}
