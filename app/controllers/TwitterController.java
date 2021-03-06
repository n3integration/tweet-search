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
package controllers;

import actors.TwitterSearchActor;
import akka.actor.ActorRef;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import play.libs.EventSource;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.TwitterEventService;
import views.html.search;

import javax.inject.Inject;
import javax.inject.Named;

//    authenticated at a controller-level
//    @Security.Authenticated(BasicAuthenticator.class)
public class TwitterController extends Controller {

    private final ActorRef searchActor;
    private final WebJarAssets webJarAssets;
    private final TwitterEventService twitterEventService;

    @Inject
    public TwitterController(WebJarAssets webJarAssets, @Named("search-actor") ActorRef searchActor, TwitterEventService twitterEventService) {
        this.webJarAssets = webJarAssets;
        this.searchActor = searchActor;
        this.twitterEventService = twitterEventService;
    }

//    authenticated at a route-level
//    @Security.Authenticated(BasicAuthenticator.class)
    public Result toggle() {
        return ok(toJson("status", twitterEventService.toggle()));
    }

    /**
     * Performs an async call to the Twitter API through a fire-and-forget actor
     *
     * @param text
     *          the search text
     * @return 202 if accepted; otherwise, 400 if search is disabled
     */
    public Result search(String text) {
        if(twitterEventService.isEnabled()) {
            searchActor.tell(TwitterSearchActor.Protocol.newInstance(text), ActorRef.noSender());
            return status(202);
        }
        return badRequest(toJson("reason", "Search is disabled"));
    }

    /**
     * Renders our single-page web application
     *
     * @return SPA
     */
    public Result searchPage() {
        return ok(search.render(webJarAssets));
    }

    /**
     * Allows clients to subscribe to our search result
     *
     * @return chunked event stream
     */
    public Result events() {
        return ok()
            .chunked(Source.fromPublisher(twitterEventService).via(EventSource.flow()))
            .as(Http.MimeTypes.EVENT_STREAM);
    }

    // Helper to convert a key-value pair into a json string
    private static <T> JsonNode toJson(String key, T value) {
        return Json.toJson(Maps.immutableEntry(key, value));
    }
}
