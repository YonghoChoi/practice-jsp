package member.command;

import member.service.DuplicateIdException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;
import util.MultipartParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class JoinHandler implements CommandHandler{
    private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
    private static final String SUCCESS_VIEW = "/WEB-INF/view/joinSuccess.jsp";

    private JoinService joinService = new JoinService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if(req.getMethod().equalsIgnoreCase("GET")) {
            return processForm(req, resp);
        } else if(req.getMethod().equalsIgnoreCase("POST")) {
            return processSubmit(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
    }

    private String processForm(HttpServletRequest req, HttpServletResponse resp) {
        return FORM_VIEW;
    }

    private String processSubmit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(isMultiPartForm(req)) {
            Map<String, String[]> parameterNames = MultipartParser.getInstance().parse(req.getInputStream());

            for(Map.Entry<String, String[]> el : parameterNames.entrySet()) {
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("key : %s, ", el.getKey()));
                for(String value : el.getValue()) {
                    sb.append(String.format("value : %s, ", value));
                }
                System.out.println(sb.toString());
            }
        }

        JoinRequest joinReq = new JoinRequest();
        joinReq.setId(req.getParameter("id"));
        joinReq.setName(req.getParameter("name"));
        joinReq.setPassword(req.getParameter("password"));
        joinReq.setConfirmPassword(req.getParameter("confirmPassword"));

        Map<String, Boolean> errors = new HashMap<>();
        req.setAttribute("errors", errors);

        joinReq.validate(errors);

        if(!errors.isEmpty()) {
            return FORM_VIEW;
        }

        try {
            joinService.join(joinReq);
            return SUCCESS_VIEW;
        } catch(DuplicateIdException e) {
            errors.put("duplicateId", Boolean.TRUE);
            return FORM_VIEW;
        }
    }

    private boolean isMultiPartForm(HttpServletRequest req) {
        return req.getContentType().contains("multipart/form-data");
    }

    private void printReqData(HttpServletRequest req) throws IOException {
        System.out.println("=============post body=============");
        InputStream input = req.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(input));

        String line;
        StringBuffer sb = new StringBuffer();
        while((line = rd.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        System.out.println(sb.toString());
        rd.close();
    }
}
