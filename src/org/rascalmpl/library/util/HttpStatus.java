/*******************************************************************************
 * Copyright (c) 2014-2026 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.rascalmpl.library.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Practical two-directional bridge between HTTP integer status codes and their
 * Rascal constructors as defined in Content.rsc and used in Webclient and Webserver Java code.
 */
public enum HttpStatus {

    OK(200, "ok"),
    CREATED(201, "created"),
    ACCEPTED(202, "accepted"),
    NO_CONTENT(204, "noContent"),

    MOVED_TEMPORARILY(302, "found"),
    SEE_OTHER(303, "redirectSeeOther"),
    TEMPORARY_REDIRECT(307, "temporaryRedirect"),
    NOT_MODIFIED(304, "notModified"),

    BAD_REQUEST(400, "badRequest"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "notFound"),
    METHOD_NOT_ALLOWED(405, "methodNotAllowed"),
    NOT_ACCEPTABLE(406, "notAcceptible"),
    REQUEST_TIMEOUT(408, "requestTimeout"),
    CONFLICT(409, "conflict"),
    GONE(410, "gone"),

    PAYLOAD_TOO_LARGE(413, "payloadTooLarge"),
    UNSUPPORTED_MEDIA_TYPE(415, "unsupportedMediaType"),
    RANGE_NOT_SATISFIABLE(416, "rangeNotSatisfiable"),
    EXPECTATION_FAILED(417, "expectationFailed"),

    TOO_MANY_REQUESTS(429, "tooManyRequests"),

    INTERNAL_ERROR(500, "internalError"),
    NOT_IMPLEMENTED(501, "notImplemented"),
    SERVICE_UNAVAILABLE(503, "serviceUnavailable"),

    HTTP_VERSION_NOT_SUPPORTED(505, "unsupportedHTTPVersion"),
    LENGTH_REQUIRED(411, "lengthRequired"),

    MULTI_STATUS(207, "multiStatus"),
    SWITCHING_PROTOCOLS(101, "switchProtocol"),

    REDIRECT(302, "redirect");

    private final int code;
    private final String constructor;

    HttpStatus(int code, String constructor) {
        this.code = code;
        this.constructor = constructor;
    }

    public int code() {
        return code;
    }

    public String constructor() {
        return constructor;
    }

    private static final Map<Integer, HttpStatus> BY_CODE = new HashMap<>();
    private static final Map<String, HttpStatus> BY_CONSTRUCTOR = new HashMap<>();

    static {
        for (HttpStatus s : values()) {
            BY_CODE.put(s.code, s);
            BY_CONSTRUCTOR.put(s.constructor, s);
        }
    }

    public static Iterable<HttpStatus> allCodes() {
        return BY_CODE.values();
    }

    public static HttpStatus of(int code) {
        return BY_CODE.get(code);
    }

    public static HttpStatus of(String name) {
        return BY_CONSTRUCTOR.get(name);
    }

    public int toCode() {
        return code;
    }

    public String toConstructor() {
        return constructor;
    }
}
