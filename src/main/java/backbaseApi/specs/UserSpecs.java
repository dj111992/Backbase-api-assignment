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
package backbaseApi.specs;

import backbaseApi.data.factory.UsersDataFactory;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class UserSpecs {

    private UserSpecs() {}

    public static RequestSpecification postNewUser() {
        var validUserData = new UsersDataFactory().validUser();

        return new RequestSpecBuilder().
                addRequestSpecification(InitialStateSpecs.set()).
                setContentType(ContentType.JSON).
                setBody(validUserData).
                build();
    }

    public static RequestSpecification postNewUserEmptyPassword() {
        var validUserData = new UsersDataFactory().validUserEmptyPassword();

        return new RequestSpecBuilder().
                addRequestSpecification(InitialStateSpecs.set()).
                setContentType(ContentType.JSON).
                setBody(validUserData).
                build();
    }

    public static RequestSpecification postUserWithEmptyUsernamePassword() {
        var validUserData = new UsersDataFactory().invalidUser();

        return new RequestSpecBuilder().
                addRequestSpecification(InitialStateSpecs.set()).
                setContentType(ContentType.JSON).
                setBody(validUserData).
                build();
    }

    public static RequestSpecification postUserWhichAlreadyExists() {
        var validUserData = new UsersDataFactory().existingUser();

        return new RequestSpecBuilder().
                addRequestSpecification(InitialStateSpecs.set()).
                setContentType(ContentType.JSON).
                setBody(validUserData).
                build();
    }

    public static RequestSpecification postLoginUserWithInvalidCredentials() {
        var validUserData = new UsersDataFactory().userWithInvalidCredentials();

        return new RequestSpecBuilder().
                addRequestSpecification(InitialStateSpecs.set()).
                setContentType(ContentType.JSON).
                setBody(validUserData).
                build();
    }

    public static RequestSpecification postSuccessfulLoginUser() {
        var validUserData = new UsersDataFactory().userWithValidCredentials();

        return new RequestSpecBuilder().
                addRequestSpecification(InitialStateSpecs.set()).
                setContentType(ContentType.JSON).
                setBody(validUserData).
                build();
    }

    public static RequestSpecification postSuccessfulUserBioChange() {
        var validUserData = new UsersDataFactory().userWithValidBioChange();

        return new RequestSpecBuilder().
                addRequestSpecification(InitialStateSpecs.set()).
                setContentType(ContentType.JSON).
                setBody(validUserData).
                build();
    }
}
