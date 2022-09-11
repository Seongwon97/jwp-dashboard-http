package org.apache.coyote.http11;

import java.util.stream.Stream;

import org.apache.coyote.http11.model.ContentType;
import org.apache.coyote.http11.model.RequestParser;
import org.apache.coyote.http11.model.request.HttpRequest;
import org.apache.coyote.http11.model.response.HttpResponse;

import nextstep.jwp.controller.Controller;
import nextstep.jwp.controller.IndexController;
import nextstep.jwp.controller.LoginController;
import nextstep.jwp.controller.NotMappedErrorController;
import nextstep.jwp.controller.RegisterController;
import nextstep.jwp.controller.ResourceController;

public enum HandlerMapping {

    DEFAULT("/", IndexController.getInstance()),
    LOGIN("/login", LoginController.getInstance()),
    REGISTER("/register", RegisterController.getInstance()),
    STATIC_FILE(Constants.NULL, ResourceController.getInstance()),
    NOF_FOUND(Constants.NULL, NotMappedErrorController.getInstance());

    private static class Constants {
        private static final String NULL = "null";
    }

    private final String url;
    private final Controller controller;

    HandlerMapping(String url, Controller controller) {
        this.url = url;
        this.controller = controller;
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

    public void execute(HttpRequest request, HttpResponse response) {
        this.controller.service(request, response);
    }
}
