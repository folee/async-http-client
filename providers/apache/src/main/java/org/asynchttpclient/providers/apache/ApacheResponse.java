/*
 * Copyright (c) 2010-2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.asynchttpclient.providers.apache;

import org.asynchttpclient.org.jboss.netty.handler.codec.http.CookieDecoder;
import org.asynchttpclient.Cookie;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.providers.ResponseBase;
import org.asynchttpclient.util.AsyncHttpProviderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApacheResponse extends ResponseBase {

	public ApacheResponse(HttpResponseStatus status,
                          HttpResponseHeaders headers,
                          List<HttpResponseBodyPart> bodyParts) {
        super(status, headers, bodyParts);
    }

    /* @Override */

    public String getResponseBodyExcerpt(int maxLength) throws IOException {
        return getResponseBodyExcerpt(maxLength, DEFAULT_CHARSET);
    }

    /* @Override */

    public String getResponseBodyExcerpt(int maxLength, String charset) throws IOException {
        charset = calculateCharset(charset);
        String response = AsyncHttpProviderUtils.contentToString(bodyParts, charset);
        return response.length() <= maxLength ? response : response.substring(0, maxLength);
    }

    /* @Override */
    public List<Cookie> buildCookies() {
    	List<Cookie> cookies = new ArrayList<Cookie>();
        for (Map.Entry<String, List<String>> header : headers.getHeaders().entrySet()) {
            if (header.getKey().equalsIgnoreCase("Set-Cookie")) {
                // TODO: ask for parsed header
                List<String> v = header.getValue();
                for (String value : v) {
                    cookies.addAll(CookieDecoder.decode(value));
                }
            }
        }
        return Collections.unmodifiableList(cookies);
    }
}
