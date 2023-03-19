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
package backbaseApi.data.factory;

import backbaseApi.model.user.UserWrapper;
import backbaseApi.model.user.UserBuilder;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsersDataFactory {

    private static final Logger log = LogManager.getLogger(UsersDataFactory.class);
    private final Faker faker = new Faker();

    public static ThreadLocal<String> expectedUsername = new ThreadLocal<>();
    public static ThreadLocal<String> expectedEmail = new ThreadLocal<>();
    public static ThreadLocal<String> expectedPassword = new ThreadLocal<>();
    public static ThreadLocal<String> expectedBio = new ThreadLocal<>();

    public UserWrapper validUser() {
        return newUser();
    }

    public UserWrapper validUserEmptyPassword() {
        return newUserEmptyPassword();
    }

    public UserWrapper invalidUser() {
        return userWithEmptyUsernameEmail();
    }

    public UserWrapper existingUser() {
        return existingUserDetails();
    }

    public UserWrapper userWithInvalidCredentials() {
        return userInvalidCredentials();
    }

    public UserWrapper userWithValidBioChange() {
        return userBioChange();
    }

    public UserWrapper userWithValidCredentials() {
        return validUserCreds();
    }

    private UserWrapper newUser() {
        expectedUsername.set(faker.text().text(3,5));
        expectedEmail.set(faker.text().text(3,5) + "@" + faker.text().text(3,5) + "." + faker.text().text(3,5));
        expectedPassword.set(faker.text().text(3,5));

        var newUser =
                new UserBuilder().
                        username(expectedUsername.get()).
                        email(expectedEmail.get()).
                        password(expectedPassword.get()).build();

        log.info(newUser);
        return newUser;
    }

    private UserWrapper validUserCreds() {
        expectedUsername.set("deepak");
        expectedEmail.set("dj111992@gmail.com");
        expectedPassword.set("Deepak@6");

        var newUser =
                new UserBuilder().
                        email(expectedEmail.get()).
                        password(expectedPassword.get()).build();

        log.info(newUser);
        return newUser;
    }

    private UserWrapper newUserEmptyPassword() {
        expectedUsername.set(faker.text().text(3,5));
        expectedEmail.set(faker.text().text(3,5) + "@" + faker.text().text(3,5) + "." + faker.text().text(3,5));
        expectedPassword.set(StringUtils.EMPTY);

        var newUser =
                new UserBuilder().
                        username(expectedUsername.get()).
                        email(expectedEmail.get()).
                        password(expectedPassword.get()).build();

        log.info(newUser);
        return newUser;
    }

    private UserWrapper userWithEmptyUsernameEmail() {
        expectedUsername.set(StringUtils.EMPTY);
        expectedEmail.set(StringUtils.EMPTY);
        expectedPassword.set(faker.text().text(3,5));

        var newUser =
                new UserBuilder().
                        username(expectedUsername.get()).
                        email(expectedEmail.get()).
                        password(expectedPassword.get()).build();

        log.info(newUser);
        return newUser;
    }

    private UserWrapper existingUserDetails() {
        expectedUsername.set("deepak");
        expectedEmail.set("dj111992@gmail.com");
        expectedPassword.set(faker.text().text(3,5));

        var newUser =
                new UserBuilder().
                        username(expectedUsername.get()).
                        email(expectedEmail.get()).
                        password(expectedPassword.get()).build();

        log.info(newUser);
        return newUser;
    }

    private UserWrapper userInvalidCredentials() {
        expectedEmail.set("john@john.john");
        expectedPassword.set("johnjohn");

        var newUser =
                new UserBuilder().
                        email(expectedEmail.get()).
                        password(expectedPassword.get()).build();

        log.info(newUser);
        return newUser;
    }

    private UserWrapper userBioChange() {
        expectedEmail.set("dj111992@gmail.com");
        expectedBio.set(faker.text().text(8,10));

        var newUser =
                new UserBuilder().
                        email(expectedEmail.get()).
                        username("deepak").
                        bio(expectedBio.get()).
                        password("Deepak@6").
                        image(StringUtils.EMPTY).build();

        log.info(newUser);
        return newUser;
    }
}
