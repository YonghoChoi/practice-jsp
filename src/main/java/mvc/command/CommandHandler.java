package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommandHandler {
    String process(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
