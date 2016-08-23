package article.command;

import article.service.*;
import auth.service.User;
import mvc.command.CommandHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModifyArticleHandler implements CommandHandler {
    private static final String FORM_VIEW = "/WEB-INF/view/modifyForm.jsp";

    private ReadArticleService readService = new ReadArticleService();
    private ModifyArticleService modifyService = new ModifyArticleService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (req.getMethod().equalsIgnoreCase("GET")) {
            return processForm(req, resp);
        } else if (req.getMethod().equalsIgnoreCase("POST")) {
            return processSubmit(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
    }

    private String processForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String noVal = req.getParameter("no");
            int no = Integer.parseInt(noVal);

            ArticleData articleData = readService.getArticle(no, false);
            User authUser = (User) req.getSession().getAttribute("authUser");
            if (!canModify(authUser, articleData)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return null;
            }
            ModifyRequest modReq = new ModifyRequest(authUser.getId(), no,
                    articleData.getArticle().getTitle(), articleData.getContent().getContent());
            req.setAttribute("modReq", modReq);
            return FORM_VIEW;
        } catch (ArticleNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    private boolean canModify(User authUser, ArticleData articleData) {
        String writerId = articleData.getArticle().getWriter().getId();
        return authUser.getId().equals(writerId);
    }

    private String processSubmit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User authUser = (User) req.getSession().getAttribute("authUser");
        String noVal = req.getParameter("no");
        int no = Integer.parseInt(noVal);

        ModifyRequest modReq = new ModifyRequest(authUser.getId(), no,
                req.getParameter("title"), req.getParameter("content"));
        req.setAttribute("modReq", modReq);

        Map<String, Boolean> errors = new HashMap<>();
        req.setAttribute("errors", errors);
        modReq.validate(errors);
        if (!errors.isEmpty()) {
            return FORM_VIEW;
        }

        try {
            modifyService.Modify(modReq);
            return "/WEB-INF/view/modifySuccess.jsp";
        } catch (ArticleNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } catch (PermissionDeniedException e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
    }
}
