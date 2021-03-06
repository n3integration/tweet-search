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
package resources;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Holds our application state
 */
public class TwitterStatus {

    private AtomicBoolean enabled;

    public TwitterStatus() {
        this.enabled = new AtomicBoolean();
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public boolean compareAndSwap() {
        boolean enabled = isEnabled();
        this.enabled.compareAndSet(enabled, !enabled);
        return isEnabled();
    }
}
