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
package com.hedera.services.bdd.spec.utilops;

import com.hedera.services.bdd.spec.HapiApiSpec;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogMessage extends UtilOp {
    static final Logger log = LogManager.getLogger(CustomSpecAssert.class);
    private final Function<HapiApiSpec, String> messageFn;

    public LogMessage(Function<HapiApiSpec, String> messageFn) {
        this.messageFn = messageFn;
    }

    public LogMessage(String hardcoded) {
        this(ignore -> hardcoded);
    }

    @Override
    protected boolean submitOp(HapiApiSpec spec) {
        log.info(messageFn.apply(spec));
        return false;
    }
}
