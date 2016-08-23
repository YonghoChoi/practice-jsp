package article.command;

import article.service.ArticlePage;
import article.service.ListArticleService;
import mvc.command.CommandHandler;

import javax.print.attribute.IntegerSyntax;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListArticleHandler implements CommandHandler{
    private ListArticleService listArticleService = new ListArticleService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String pageNoVal = req.getParameter("pageNo");
        int pageNo = 1;

        if(pageNoVal != null) {
            pageNo = Integer.parseInt(pageNoVal);
        }

        ArticlePage articlePage = listArticleService.getArticlePage(pageNo);
        req.setAttribute("articlePage", articlePage);
        return "/WEB-INF/view/listArticle.jsp";
    }
}
