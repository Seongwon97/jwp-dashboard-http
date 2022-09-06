package org.apache.coyote.http11;

import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.coyote.http11.model.ContentType;
import org.apache.coyote.http11.model.HttpStatus;
import org.apache.coyote.http11.model.RequestParser;
import org.apache.coyote.http11.model.request.HttpRequest;
import org.apache.coyote.http11.model.response.HttpResponse;

import nextstep.jwp.handler.IndexHandler;
import nextstep.jwp.handler.LoginHandler;
import nextstep.jwp.handler.RegisterHandler;
import nextstep.jwp.handler.ResourceHandler;

public enum HandlerMapping {

    DEFAULT("/", IndexHandler::perform),
    LOGIN("/login", LoginHandler::perform),
    REGISTER("/register", RegisterHandler::perform),
    STATIC_FILE(Constants.NULL, ResourceHandler::perform),
    NOF_FOUND(Constants.NULL, HandlerMapping::returnNotFountResponse);

    private static class Constants {
        private static final String NULL = "null";
    }

    private final String url;
    private final Function<HttpRequest, HttpResponse> executor;

    HandlerMapping(String url, Function<HttpRequest, HttpResponse> executor) {
        this.url = url;
        this.executor = executor;
    }

    public static HandlerMapping findHandler(HttpRequest request) {
        String url = RequestParser.extractUrl(request.getUri());
        if (ContentType.hasFileExtension(url)) {
            return STATIC_FILE;
        }

        return Stream.of(HandlerMapping.values())
                .filter(v -> v.url.startsWith(url))
                .findAny()
                .orElse(NOF_FOUND);
    }

    public static HttpResponse returnNotFountResponse(HttpRequest request) {
        return new HttpResponse.Builder()
                .statusCode(HttpStatus.NOT_FOUND)
                .build();
    }

    public HttpResponse execute(HttpRequest request) {
        return this.executor.apply(request);
    }
}
