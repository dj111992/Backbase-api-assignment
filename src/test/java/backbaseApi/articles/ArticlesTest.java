/*
 * MIT License
 *
 * Copyright (c) 2020 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package backbaseApi.articles;

import backbaseApi.BaseAPI;
import backbaseApi.data.factory.ArticlesDataFactory;
import backbaseApi.specs.ArticleSpecs;
import backbaseApi.specs.UserSpecs;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static backbaseApi.data.staticData.ArticlesEndpoints.ARTICLES_FEED_SERVICE;
import static backbaseApi.data.staticData.ArticlesEndpoints.ARTICLES_SERVICE;
import static backbaseApi.data.staticData.TestSuiteTags.BACKBASEAPI;
import static backbaseApi.data.staticData.UsersEndpoints.USER_LOGIN_SERVICE;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;

class ArticlesTest extends BaseAPI {

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful fetching all articles")
    void successfulGettingAllArticles() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();
        String token = jsonPathEvaluator.get("user.token");

        Response response = getWithJWT(ARTICLES_SERVICE, "jwtauthorization", "Token " + token);

        response.
                then().
                statusCode(SC_OK);

        JsonPath jsonPathEvaluator1 = response.jsonPath();
        int articlesCount = jsonPathEvaluator1.get("articlesCount");

        List<Object> jsonArray = jsonPathEvaluator1.getList("articles");

        Assertions.assertTrue(jsonArray.size() == 20, "Article count is incorrect");
        Assertions.assertTrue(articlesCount > 29054, "Article count is incorrect");
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful fetching all articles with filter in case no articles exist with filter")
    void successfulGettingAllArticlesWithFilterNoArticlesExist() {
        Response response = getWithoutJWT(ARTICLES_SERVICE + "?tag=deepak");

        response.
                then().
                statusCode(SC_OK);

        JsonPath jsonPathEvaluator = response.jsonPath();
        int articlesCount = jsonPathEvaluator.get("articlesCount");

        List<Object> jsonArray = jsonPathEvaluator.getList("articles");

        Assertions.assertTrue(jsonArray.size() == 0, "Article count is incorrect");
        Assertions.assertTrue(articlesCount == 0, "Article count is incorrect");
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful fetching all articles with filter")
    void successfulGettingAllArticlesWithFilter() {
        Response response = getWithoutJWT(ARTICLES_SERVICE + "?author=deepak");

        response.
                then().
                statusCode(SC_OK);

        JsonPath jsonPathEvaluator = response.jsonPath();
        int articlesCount = jsonPathEvaluator.get("articlesCount");

        List<Object> jsonArray = jsonPathEvaluator.getList("articles");

        Assertions.assertTrue(jsonArray.size() == 20, "Article count is incorrect");
        Assertions.assertTrue(articlesCount > 50, "Article count is incorrect");
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful fetching 20 articles")
    void successfulGetting20Articles() {
        Response response = getWithoutJWT(ARTICLES_SERVICE + "?limit=20");

        response.
                then().
                statusCode(SC_OK);

        JsonPath jsonPathEvaluator = response.jsonPath();
        int articlesCount = jsonPathEvaluator.get("articlesCount");

        List<Object> jsonArray = jsonPathEvaluator.getList("articles");

        Assertions.assertTrue(jsonArray.size() == 20, "Article count is incorrect");
        Assertions.assertTrue(articlesCount == 20, "Article count is incorrect");
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful fetching all article feeds")
    void successfulGettingAllArticlesFeeds() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();
        String token = jsonPathEvaluator.get("user.token");

        Response response = getWithJWT(ARTICLES_FEED_SERVICE, "jwtauthorization",
                "Token " + token);

        response.
                then().
                statusCode(SC_OK);

        JsonPath jsonPathEvaluator1 = response.jsonPath();

        int articlesCount = jsonPathEvaluator1.get("articlesCount");

        List<Object> jsonArray = jsonPathEvaluator1.getList("articles");

        Assertions.assertTrue(jsonArray.size() == 0, "Article count is incorrect");
        Assertions.assertTrue(articlesCount == 0, "Article count is incorrect");
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful fetching the specific article details")
    void successfulGettingArticleDetails() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();
        String token = jsonPathEvaluator.get("user.token");

        Response response = postWithJWT(ARTICLES_SERVICE, ArticleSpecs.postNewArticle(), "jwtauthorization",
                "Token " + token);

        JsonPath jsonPathEvaluator1 = response.jsonPath();
        String article = jsonPathEvaluator1.get("article.slug");
        String title = jsonPathEvaluator1.get("article.title");
        String description = jsonPathEvaluator1.get("article.description");

        Response response1 = getWithoutJWT(ARTICLES_SERVICE + "/" + article);

        response.
                then().
                statusCode(SC_OK);

        JsonPath jsonPathEvaluator2 = response1.jsonPath();

        Assertions.assertEquals(article, jsonPathEvaluator2.get("article.slug"),
                "Incorrect article details fetched");
        Assertions.assertEquals(title, jsonPathEvaluator2.get("article.title"),
                "Incorrect article details fetched");
        Assertions.assertEquals(description, jsonPathEvaluator2.get("article.description"),
                "Incorrect article details fetched");
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successfully creating new article")
    void successfulCreatingArticle() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();
        String token = jsonPathEvaluator.get("user.token");

        Response response = postWithJWT(ARTICLES_SERVICE, ArticleSpecs.postNewArticle(), "jwtauthorization",
                "Token " + token);

        response.
                then().
                statusCode(SC_OK).
                body("article.title", is(ArticlesDataFactory.expectedTitle.get()),
                        "article.description", is(ArticlesDataFactory.expectedDescription.get()),
                        "article.body", is(ArticlesDataFactory.expectedBody.get()));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successfully updating created article")
    void successfulUpdatingArticle() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();
        String token = jsonPathEvaluator.get("user.token");

        Response response = postWithJWT(ARTICLES_SERVICE, ArticleSpecs.postNewArticle(), "jwtauthorization",
                "Token " + token);

        JsonPath jsonPathEvaluator1 = response.jsonPath();
        String slug = jsonPathEvaluator1.get("article.slug");

        Response response1 = putWithJWT(ARTICLES_SERVICE + "/" + slug, ArticleSpecs.updateArticle(), "jwtauthorization",
                "Token " + token);
        response1.
                then().
                statusCode(SC_OK).
                body("article.title", is(ArticlesDataFactory.expectedTitle.get()));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successfully deleting article")
    void successfulDeletingArticle() {
        Response authResponse = postWithoutJWT("/api/users/login", UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();
        String token = jsonPathEvaluator.get("user.token");

        Response response = postWithJWT(ARTICLES_SERVICE, ArticleSpecs.postNewArticle(), "jwtauthorization",
                "Token " + token);

        JsonPath jsonPathEvaluator1 = response.jsonPath();
        String slug = jsonPathEvaluator1.get("article.slug");

        Response response1 = deleteWithJWT(ARTICLES_SERVICE + "/" + slug, "jwtauthorization",
                "Token " + token);

        response1.
                then().
                statusCode(SC_NO_CONTENT);
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successfully making the article favourite")
    void successfulMakingTheArticleFavourite() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();
        String token = jsonPathEvaluator.get("user.token");

        Response response = postWithJWT(ARTICLES_SERVICE, ArticleSpecs.postNewArticle(), "jwtauthorization",
                "Token " + token);

        JsonPath jsonPathEvaluator1 = response.jsonPath();
        String slug = jsonPathEvaluator1.get("article.slug");

        Response response1 = postWithJWTNoBody(ARTICLES_SERVICE + "/" + slug + "/favorite", "jwtauthorization",
                "Token " + token);
        response1.
                then().
                statusCode(SC_OK)
                .body("article.slug", is(slug));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successfully making the article un favourite")
    void successfulMakingTheArticleUnFavourite() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();
        String token = jsonPathEvaluator.get("user.token");

        Response response = postWithJWT(ARTICLES_SERVICE, ArticleSpecs.postNewArticle(), "jwtauthorization",
                "Token " + token);

        JsonPath jsonPathEvaluator1 = response.jsonPath();
        String slug = jsonPathEvaluator1.get("article.slug");

        Response response1 = postWithJWTNoBody(ARTICLES_SERVICE + "/" + slug + "/favorite", "jwtauthorization",
                "Token " + token);
        response1.
                then().
                statusCode(SC_OK)
                .body("article.slug", is(slug));

        Response response2 = deleteWithJWT(ARTICLES_SERVICE + "/" + slug + "/favorite", "jwtauthorization",
                "Token " + token);
        response2.
                then().
                statusCode(SC_OK)
                .body("article.slug", is(slug));
    }
}
