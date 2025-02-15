/*
 * Copyright (C) 2020-2022 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hedera.services.bdd.spec.keys;

public class ControlForKey {
    private final String keyName;
    private final SigControl controller;

    public String getKeyName() {
        return keyName;
    }

    public SigControl getController() {
        return controller;
    }

    public static ControlForKey forKey(String key, SigControl control) {
        return new ControlForKey(key, control);
    }

    private ControlForKey(String keyName, SigControl controller) {
        this.keyName = keyName;
        this.controller = controller;
    }
}
