package org.apache.coyote.http11;

public class HttpResponse {

    private final String protocolVersion;
    private final String statusCode;
    private final String contentType;
    private final int contentLength;
    private final String responseBody;

    public HttpResponse(String statusCode, String contentType, String responseBody) {
        this("HTTP/1.1", statusCode, contentType, responseBody);
    }

    public HttpResponse(String protocolVersion, String statusCode, String contentType, String responseBody) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.contentLength = responseBody.getBytes().length;
        this.responseBody = responseBody;
    }

    public byte[] toResponse() {
        return String.join("\r\n",
                protocolVersion + " " + statusCode,
                "Content-Type: " + contentType + ";charset=utf-8",
                "Content-Length: " + contentLength + " ",
                "",
                responseBody).getBytes();
    }
}
