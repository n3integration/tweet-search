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
package services;

import com.google.common.collect.Lists;
import models.TwitterStatus;
import play.libs.Json;
import play.libs.LegacyEventSource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TwitterEventService {

    private final TwitterStatus status;
    private final List<LegacyEventSource> clients = Lists.newCopyOnWriteArrayList();

    @Inject
    public TwitterEventService() {
        this.status = new TwitterStatus();
    }

    public boolean toggle() {
        return status.compareAndSwap();
    }

    public boolean isEnabled() {
        return status.isEnabled();
    }

    public TwitterEventService subscribe(LegacyEventSource legacyEventSource) {
        this.clients.add(legacyEventSource);
        return this;

    }

    public TwitterEventService unsubscribe(LegacyEventSource legacyEventSource) {
        this.clients.remove(legacyEventSource);
        return this;
    }

    public <T> void forward(T eventData) {
        clients.forEach((eventSource) -> eventSource.send(createEvent(eventData)));
    }

    private static <T> LegacyEventSource.Event createEvent(T data) {
        return LegacyEventSource.Event.event(Json.toJson(data));
    }
}
