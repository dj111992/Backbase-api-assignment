package backbaseApi.model.article;

public class ArticleWrapper {
    private Article article;

    public ArticleWrapper(Article article) {
        this.article = article;
    }
    // getter and setter for the field

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

}
