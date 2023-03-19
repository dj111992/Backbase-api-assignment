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
package backbaseApi.users;

import backbaseApi.BaseAPI;
import backbaseApi.specs.UserSpecs;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static backbaseApi.data.staticData.TestSuiteTags.BACKBASEAPI;
import static backbaseApi.data.staticData.UsersEndpoints.USERS_PROFILE_SERVICE;
import static backbaseApi.data.staticData.UsersEndpoints.USER_LOGIN_SERVICE;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;

class UserProfilesTest extends BaseAPI {
    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful user profile fetching")
    void successfulGettingExistingUserInfo() {
        Response res = getWithoutJWT(USERS_PROFILE_SERVICE + "deepak");

        res.
                then().
                statusCode(SC_OK).
                body("profile.username", is("deepak"),
                        "profile.following", is(false));
    }
    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful user follow")
    void successfulUserFollow() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();

        String token = jsonPathEvaluator.get("user.token");

        Response response = postWithJWT(USERS_PROFILE_SERVICE + "deepak/follow",
                UserSpecs.postSuccessfulLoginUser(), "jwtauthorization", "Token " + token);

        response.
                then().
                statusCode(SC_OK).
                body("profile.username", is("deepak"),
                        "profile.following", is(false));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful user un-follow")
    void successfulUserUnfollow() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();

        String token = jsonPathEvaluator.get("user.token");

        Response response = deleteWithJWT(USERS_PROFILE_SERVICE + "deepak/follow", "jwtauthorization", "Token " + token);

        response.
                then().
                statusCode(SC_OK).
                body("profile.username", is("deepak"),
                        "profile.following", is(false));
    }
}