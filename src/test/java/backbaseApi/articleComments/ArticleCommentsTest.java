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
package backbaseApi.articleComments;

import backbaseApi.BaseAPI;
import backbaseApi.data.staticData.TestSuiteTags;
import backbaseApi.specs.ArticleSpecs;
import backbaseApi.specs.UserSpecs;
import backbaseApi.specs.ArticleCommentSpecs;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static backbaseApi.data.staticData.ArticlesEndpoints.ARTICLES_SERVICE;
import static backbaseApi.data.staticData.UsersEndpoints.USER_LOGIN_SERVICE;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;

class ArticleCommentsTest extends BaseAPI {

    @Test
    @Tag(TestSuiteTags.BACKBASEAPI)
    @DisplayName("Successfully creating new article comment")
    void successfulCreatingArticleComment() {
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

        Response response2 = postWithJWT(ARTICLES_SERVICE + "/" + slug + "/comments", ArticleCommentSpecs.addNewComment(),
                "jwtauthorization",
                "Token " + token);
        response2.
                then().
                statusCode(SC_OK).
                body("comment.author.username", is("deepak"));
    }

    @Test
    @Tag(TestSuiteTags.BACKBASEAPI)
    @DisplayName("Successfully deleting new article comment")
    void successfulDeletingArticleComment() {
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

        Response response2 = postWithJWT(ARTICLES_SERVICE + "/" + slug + "/comments", ArticleCommentSpecs.addNewComment(),
                "jwtauthorization",
                "Token " + token);
        response2.
                then().
                statusCode(SC_OK);

        JsonPath jsonPathEvaluator2 = response2.jsonPath();
        String commentId = jsonPathEvaluator2.get("comment.id");

        Response response3 = deleteWithJWT(ARTICLES_SERVICE + "/" + slug + "/comments/" + commentId,
                "jwtauthorization",
                "Token " + token);
        response3.
                then().
                statusCode(SC_NO_CONTENT);
    }

    @Test
    @Tag(TestSuiteTags.BACKBASEAPI)
    @DisplayName("Getting all article comments")
    void successfulGettingArticleComment() {
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

        Response response2 = postWithJWT(ARTICLES_SERVICE + "/" + slug + "/comments", ArticleCommentSpecs.addNewComment(),
                "jwtauthorization",
                "Token " + token);
        response2.
                then().
                statusCode(SC_OK);

        Response response3 = getWithJWT(ARTICLES_SERVICE + "/" + slug + "/comments",
                "jwtauthorization",
                "Token " + token);
        response3.
                then().
                statusCode(SC_OK);

        List<String> listComments = response3.jsonPath().get("comments");
        Assertions.assertTrue(listComments.size() > 0, "Comments size is equal to 0");
    }
}
