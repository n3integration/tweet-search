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
package actors;

import akka.actor.UntypedActor;
import play.Logger;
import services.TwitterEventService;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

import javax.inject.Inject;

/**
 * An {@link UntypedActor} that asynchronously processes our
 * Twitter search request.
 */
public class TwitterSearchActor extends UntypedActor {

    @Inject
    private Twitter twitter;

    @Inject
    private TwitterEventService twitterEventService;

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof Protocol) {
            QueryResult queryResult = twitter.search(((Protocol) message).toQuery());
            twitterEventService.forward(queryResult);
        }
        else {
            Logger.error("Received an unknown event ==> {}", message.getClass().getSimpleName());
        }
    }

    /**
     * Twitter search protocol.
     */
    public static final class Protocol {

        private final String text;

        Protocol(String text) {
            this.text = text;
        }

        public static Protocol newInstance(final String text) {
            return new Protocol(text);
        }

        public Query toQuery() {
            return new Query(text);
        }
    }
}
