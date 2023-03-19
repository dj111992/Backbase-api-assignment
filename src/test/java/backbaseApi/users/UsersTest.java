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
import backbaseApi.data.staticData.ErrorsData;
import backbaseApi.data.factory.UsersDataFactory;
import backbaseApi.specs.UserSpecs;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static backbaseApi.data.staticData.TestSuiteTags.BACKBASEAPI;
import static backbaseApi.data.staticData.UsersEndpoints.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

class UsersTest extends BaseAPI {

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Should be able to create a new user")
    void apiNewUsers() {
        Response res = postWithoutJWT(USERS_SERVICE, UserSpecs.postNewUser());

        res.
                then().
                statusCode(SC_OK).
                body("user.username", is(UsersDataFactory.expectedUsername.get()),
                        "user.email", is(UsersDataFactory.expectedEmail.get()));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Should be able to create a new user with empty password")
    void apiNewUsersEmptyPassword() {
        Response res = postWithoutJWT(USERS_SERVICE, UserSpecs.postNewUserEmptyPassword());

        res.
                then().
                statusCode(SC_OK).
                body("user.username", is(UsersDataFactory.expectedUsername.get()),
                        "user.email", is(UsersDataFactory.expectedEmail.get()));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Error creating user when email and username is empty")
    void apiErrorNewUsersForEmptyUsernameEmail() {
        Response res = postWithoutJWT(USERS_SERVICE, UserSpecs.postUserWithEmptyUsernamePassword());

        res.
                then().
                statusCode(SC_UNPROCESSABLE_ENTITY).
                body(ErrorsData.CANT_BE_BLANK_USERNAME.key, is(ErrorsData.CANT_BE_BLANK_USERNAME.message),
                        ErrorsData.CANT_BE_BLANK_EMAIL.key, is(ErrorsData.CANT_BE_BLANK_EMAIL.message));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Error creating user which already exists")
    void apiErrorExistingUserPost() {
        Response res = postWithoutJWT(USERS_SERVICE, UserSpecs.postUserWhichAlreadyExists());

        res.
                then().
                statusCode(SC_UNPROCESSABLE_ENTITY).
                body(ErrorsData.ALREADY_TAKEN_USERNAME.key, is(ErrorsData.ALREADY_TAKEN_USERNAME.message),
                        ErrorsData.ALREADY_TAKEN_EMAIL.key, is(ErrorsData.ALREADY_TAKEN_EMAIL.message));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Error getting user without authorization")
    void apiErrorGettingExistingUserWithoutAuth() {
        Response res = getWithoutJWT(GET_USER_SERVICE);

        res.
                then().
                statusCode(SC_UNAUTHORIZED).
                body(ErrorsData.TOKEN_NOT_FOUND.key, is(ErrorsData.TOKEN_NOT_FOUND.message),
                        "errors.error.code", is("credentials_required"));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Successful getting user info with authorization")
    void successfulGettingExistingUserInfo() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();

        String token = jsonPathEvaluator.get("user.token");

        Response response = getWithJWT(GET_USER_SERVICE, "jwtauthorization", "Token " + token);

        response.
                then().
                statusCode(SC_OK).
                body("user.email", is("dj111992@gmail.com"),
                        "user.username", is("deepak"));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Changing user details using PUT request")
    void changingUserDetailsWithPUTRequest() {
        Response authResponse = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        JsonPath jsonPathEvaluator = authResponse.jsonPath();

        String token = jsonPathEvaluator.get("user.token");

        Response response = putWithJWT(GET_USER_SERVICE, UserSpecs.postSuccessfulUserBioChange(),
                "jwtauthorization", "Token " + token);
        response.
                then().
                statusCode(SC_OK).
                body("user.email", is(UsersDataFactory.expectedEmail.get()),
                        "user.bio", is(UsersDataFactory.expectedBio.get()));
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Error in login API with invalid username or password")
    void loginAPIErrorInvalidUsernamePassword() {
        Response response = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postLoginUserWithInvalidCredentials());

        response.
                then().
                statusCode(SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Login with valid username or password")
    void successfulLogin() {
        Response response = postWithoutJWT(USER_LOGIN_SERVICE, UserSpecs.postSuccessfulLoginUser());

        response.
                then().
                statusCode(SC_OK).
                body("user.username", is(UsersDataFactory.expectedUsername.get()),
                        "user.email", is(UsersDataFactory.expectedEmail.get()));
    }
}