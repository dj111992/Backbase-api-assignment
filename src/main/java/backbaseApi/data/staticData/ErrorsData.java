/*
 * MIT License
 *
 * Copyright (c) 2022 Elias Nogueira
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
package backbaseApi.data.staticData;

public enum ErrorsData {

    CANT_BE_BLANK_USERNAME("errors.username", "can't be blank"),

    CANT_BE_BLANK_EMAIL("errors.email", "can't be blank"),

    ALREADY_TAKEN_USERNAME("errors.username", "is already taken."),
    ALREADY_TAKEN_EMAIL("errors.email", "is already taken."),
    TOKEN_NOT_FOUND("errors.message", "No authorization token was found");

    public final String key;
    public final String message;

    ErrorsData(String key, String message) {
        this.key = key;
        this.message = message;
    }
}
