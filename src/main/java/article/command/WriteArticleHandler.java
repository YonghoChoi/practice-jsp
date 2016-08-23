package article.command;

import article.model.Writer;
import article.service.WriteArticleService;
import article.service.WriteRequest;
import auth.service.User;
import mvc.command.CommandHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class WriteArticleHandler implements CommandHandler{
    private static final String FORM_VIEW = "/WEB-INF/view/newArticleForm.jsp";
    private static final String SUCCESS_VIEW = "/WEB-INF/view/newArticleSuccess.jsp";
    private WriteArticleService writeService = new WriteArticleService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if(req.getMethod().equalsIgnoreCase("GET")) {
            return processForm(req, resp);
        } else if(req.getMethod().equalsIgnoreCase("POST")) {
            return processSubmit(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
    }

    private String processForm(HttpServletRequest req, HttpServletResponse resp) {
        return FORM_VIEW;
    }

    private String processSubmit(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Map<String, Boolean> errors = new HashMap<>();
        req.setAttribute("errors", errors);

        User user = (User)req.getSession().getAttribute("authUser");
        WriteRequest writeReq = createWriteRequest(user, req);
        writeReq.validate(errors);

        if(!errors.isEmpty()) {
            return FORM_VIEW;
        }

        int newArticleNo = writeService.write(writeReq);
        req.setAttribute("newArticleNo", newArticleNo);

        return SUCCESS_VIEW;
    }

    private WriteRequest createWriteRequest(User user, HttpServletRequest req) {
        return new WriteRequest(
                new Writer(user.getId(), user.getName()),
                req.getParameter("title"),
                req.getParameter("content")
        );
    }
}
