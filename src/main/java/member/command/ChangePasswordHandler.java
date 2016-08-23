package member.command;

import auth.service.User;
import member.service.ChangePasswordService;
import member.service.InvalidPasswordException;
import member.service.MemberNotFoundException;
import mvc.command.CommandHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordHandler implements CommandHandler{
    private static final String FORM_VIEW = "/WEB-INF/view/changePwdForm.jsp";
    private ChangePasswordService changePwdSvc = new ChangePasswordService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if(req.getMethod().equalsIgnoreCase("GET")) {
            return ProcessForm(req, resp);
        } else if(req.getMethod().equalsIgnoreCase("POST")) {
            return ProcessSubmit(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
    }

    private String ProcessForm(HttpServletRequest req, HttpServletResponse resp) {
        return FORM_VIEW;
    }

    private String ProcessSubmit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User)req.getSession().getAttribute("authUser");

        Map<String, Boolean> errors = new HashMap<>();
        req.setAttribute("errors", errors);

        String curPwd = req.getParameter("curPwd");
        String newPwd = req.getParameter("newPwd");

        if(curPwd == null || curPwd.isEmpty()) {
            errors.put("curPwd", Boolean.TRUE);
        }

        if(newPwd == null || newPwd.isEmpty()) {
            errors.put("newPwd", Boolean.TRUE);
        }

        if(!errors.isEmpty()) {
            return FORM_VIEW;
        }

        try {
            changePwdSvc.changePassword(user.getId(), curPwd, newPwd);
            return "/WEB-INF/view/changePwdSuccess.jsp";
        } catch (InvalidPasswordException e) {
            errors.put("badCurPwd", Boolean.TRUE);
            return FORM_VIEW;
        } catch (MemberNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }
}
