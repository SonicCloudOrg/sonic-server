package org.cloud.sonic.controller.models.interfaces;

public interface UrlType {
    /**
     * 白名单 url，不需要鉴权
     */
    int WHITE = 0;
    int PARENT = 0;
    /**
     * 正常的 url，需要鉴权
     */
    int NORMAL = 1;

}
