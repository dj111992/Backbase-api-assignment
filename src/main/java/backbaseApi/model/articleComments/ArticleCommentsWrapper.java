package backbaseApi.model.articleComments;

public class ArticleCommentsWrapper {
    private ArticleComments articleComments;

    public ArticleCommentsWrapper(ArticleComments articleComments) {
        this.articleComments = articleComments;
    }
    // getter and setter for the field

    public ArticleComments getArticleComments() {
        return articleComments;
    }

    public void setArticleComments(ArticleComments articleComments) {
        this.articleComments = articleComments;
    }

}
