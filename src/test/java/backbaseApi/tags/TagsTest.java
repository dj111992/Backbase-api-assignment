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
package backbaseApi.tags;

import backbaseApi.BaseAPI;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static backbaseApi.data.staticData.TagsEndpoints.TAGS_SERVICE;
import static backbaseApi.data.staticData.TestSuiteTags.BACKBASEAPI;
import static org.apache.http.HttpStatus.SC_OK;

class TagsTest extends BaseAPI {

    @Test
    @Tag(BACKBASEAPI)
    @DisplayName("Getting all Tags")
    void successfulGettingAllTags() {
        Response response = getWithoutJWT(TAGS_SERVICE);

        JsonPath jsonPathEvaluator = response.jsonPath();

        List<Object> jsonArray = jsonPathEvaluator.getList("tags");

        Assertions.assertEquals(response.getStatusCode(), SC_OK, "Response code is not correct");
        Assertions.assertTrue(jsonArray.contains("matrix"), "Tag list is correct");
        Assertions.assertTrue(jsonArray.size() > 100, "Tag list size is correct");
    }
}
