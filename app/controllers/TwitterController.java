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

public class TwitterController extends Controller {

    public static final String TEXT_EVENT_STREAM = "text/event-stream";

    private final ActorRef searchActor;
    private final WebJarAssets webJarAssets;
    private final TwitterEventService twitterEventService;

    @Inject
    public TwitterController(WebJarAssets webJarAssets, @Named("search-actor") ActorRef searchActor, TwitterEventService twitterEventService) {
        this.webJarAssets = webJarAssets;
        this.searchActor = searchActor;
        this.twitterEventService = twitterEventService;
    }

//    @Security.Authenticated(BasicAuthenticator.class)
    public Result toggle() {
        return ok(toJson("status", twitterEventService.toggle()));
    }

    public Result search(String text) {
        if(twitterEventService.isEnabled()) {
            searchActor.tell(TwitterSearchActor.Protocol.newInstance(text), null);
            return status(202);
        }
        return badRequest(toJson("reason", "Search is disabled"));
    }

    public Result searchPage() {
        return ok(search.render(webJarAssets));
    }

    public Result events() {
        return ok().chunked(Source.fromPublisher(twitterEventService).via(EventSource.flow()))
            .as(Http.MimeTypes.EVENT_STREAM);
    }

    private static <T> JsonNode toJson(String key, T value) {
        return Json.toJson(Maps.immutableEntry(key, value));
    }
}
