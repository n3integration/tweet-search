/*
 *  Copyright 2016 n3integration
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */
package security;

import com.google.common.io.BaseEncoding;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Naive {@link Authenticator} to process BASIC authentication.
 * Please note that this is only meant to demonstrate a BASIC
 * authentication implementation and should not be used as-is
 * in a real application.
 */
public class BasicAuthenticator extends Security.Authenticator {

    static final String AUTH_HEADER_NAME = "Authentication";
    static final String SECRET_PASSWORD  = System.getenv("SECRET_PASSWORD");

    @Override
    public String getUsername(Http.Context ctx) {
        if(ctx.request().hasHeader(AUTH_HEADER_NAME)) {
            try {
                String authHeader = ctx.request().getHeader(AUTH_HEADER_NAME);
                String decoded = new String(BaseEncoding.base64().decode(authHeader));
                String tokens[] = decoded.split(":");
                if(tokens.length == 2 && tokens[1].equalsIgnoreCase(SECRET_PASSWORD)) {
                    return tokens[0];
                }
            }
            catch(Exception ex) {
                Logger.error("failed to extract user token from authentication header");
            }
        }
        return null; // indicates that the user is not authorized
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized("unable to verify client");
    }
}
