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
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import play.libs.EventSource;
import play.libs.Json;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TwitterEventService implements Publisher {

    private final TwitterStatus status;
    private final List<Subscriber> clients = Lists.newCopyOnWriteArrayList();

    @Inject
    public TwitterEventService() {
        this.status = new TwitterStatus();
    }

    @Override
    public void subscribe(Subscriber s) {
        this.clients.add(s);
    }

    public TwitterEventService unsubscribe(Subscriber s) {
        this.clients.remove(s);
        return this;
    }

    public boolean toggle() {
        return status.compareAndSwap();
    }

    public boolean isEnabled() {
        return status.isEnabled();
    }

    public <T> void forward(T eventData) {
        clients.forEach((eventSource) -> eventSource.onNext(createEvent(eventData)));
    }

    private static <T>EventSource.Event createEvent(T data) {
        return EventSource.Event.event(Json.toJson(data));
    }
}
