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
package backbaseApi;

import backbaseApi.config.ConfigurationManager;
import backbaseApi.config.Configuration;
import backbaseApi.specs.UserSpecs;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.config.JsonPathConfig.NumberReturnType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.*;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;

public abstract class BaseAPI {

    protected static Configuration configuration;

    @BeforeAll
    public static void beforeAllTests() {
        configuration = ConfigurationManager.getConfiguration();

        baseURI = configuration.baseURI();

        // solve the problem with big decimal assertions
        config = newConfig().
            jsonConfig(jsonConfig().numberReturnType(NumberReturnType.BIG_DECIMAL)).
            sslConfig(new SSLConfig().allowAllHostnames());

        RestAssured.useRelaxedHTTPSValidation();

        determineLog();
    }

    public Response getWithoutJWT(String endpoint){
        configuration = ConfigurationManager.getConfiguration();

        return given().
                auth().basic(configuration.username(), configuration.password()).
                when().
                get(endpoint);
    }

    public Response getWithJWT(String endpoint, String headerKey, String headerValue){
        configuration = ConfigurationManager.getConfiguration();

        return given().
                auth().basic(configuration.username(), configuration.password()).
                when().
                header(headerKey, headerValue).
                get(endpoint);
    }

    public Response postWithoutJWT(String endpoint, RequestSpecification body){
        configuration = ConfigurationManager.getConfiguration();

        return given().
                spec(body).
                auth().basic(configuration.username(), configuration.password()).
                when().
                post(endpoint);
    }

    public Response postWithJWT(String endpoint, RequestSpecification body, String headerKey, String headerValue){
        configuration = ConfigurationManager.getConfiguration();

        return given().
                spec(body).
                auth().basic(configuration.username(), configuration.password()).
                when().
                header(headerKey, headerValue).
                post(endpoint);
    }

    public Response postWithJWTNoBody(String endpoint, String headerKey, String headerValue){
        configuration = ConfigurationManager.getConfiguration();

        return given().
                body("").
                auth().basic(configuration.username(), configuration.password()).
                when().
                header(headerKey, headerValue).
                post(endpoint);
    }

    public Response putWithJWT(String endpoint, RequestSpecification body, String headerKey, String headerValue){
        configuration = ConfigurationManager.getConfiguration();

        return given().
                spec(body).
                auth().basic(configuration.username(), configuration.password()).
                when().
                header(headerKey, headerValue).
                put(endpoint);
    }

    public Response deleteWithJWT(String endpoint, String headerKey, String headerValue){
        configuration = ConfigurationManager.getConfiguration();

        return given().
                auth().basic(configuration.username(), configuration.password()).
                when().
                header(headerKey, headerValue).
                delete(endpoint);
    }

    /*
     * if log.all is true in the config.properties file all the request and response information will be logged
     * otherwise it will log only when the test fails
     */
    private static void determineLog() {
        if (configuration.logAll()) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        } else {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }
    }
}
