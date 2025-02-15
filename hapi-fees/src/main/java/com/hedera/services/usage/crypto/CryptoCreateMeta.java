/*
 * Copyright (C) 2021-2022 Hedera Hashgraph, LLC
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
package com.hedera.services.usage.crypto;

import static com.hedera.services.usage.TxnUsage.keySizeIfPresent;
import static com.hederahashgraph.fee.FeeBuilder.BASIC_ENTITY_ID_SIZE;

import com.google.common.base.MoreObjects;
import com.hederahashgraph.api.proto.java.CryptoCreateTransactionBody;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CryptoCreateMeta {
    private final long baseSize;
    private final long lifeTime;
    private final int maxAutomaticAssociations;

    public CryptoCreateMeta(CryptoCreateTransactionBody cryptoCreateTxnBody) {
        baseSize = getCryptoCreateTxnBaseSize(cryptoCreateTxnBody);
        lifeTime = cryptoCreateTxnBody.getAutoRenewPeriod().getSeconds();
        maxAutomaticAssociations = cryptoCreateTxnBody.getMaxAutomaticTokenAssociations();
    }

    public CryptoCreateMeta(Builder builder) {
        this.baseSize = builder.baseSize;
        this.lifeTime = builder.lifeTime;
        this.maxAutomaticAssociations = builder.maxAutomaticAssociations;
    }

    private long getCryptoCreateTxnBaseSize(CryptoCreateTransactionBody op) {
        long variableBytes = op.getMemoBytes().size();
        variableBytes +=
                keySizeIfPresent(
                        op,
                        CryptoCreateTransactionBody::hasKey,
                        CryptoCreateTransactionBody::getKey);
        if (op.hasProxyAccountID()) {
            variableBytes += BASIC_ENTITY_ID_SIZE;
        }
        return variableBytes;
    }

    public long getBaseSize() {
        return baseSize;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public int getMaxAutomaticAssociations() {
        return maxAutomaticAssociations;
    }

    public static class Builder {
        private long baseSize;
        private long lifeTime;
        private int maxAutomaticAssociations;

        public Builder() {
            // empty here on purpose.
        }

        public CryptoCreateMeta.Builder baseSize(final int baseSize) {
            this.baseSize = baseSize;
            return this;
        }

        public CryptoCreateMeta.Builder lifeTime(final long lifeTime) {
            this.lifeTime = lifeTime;
            return this;
        }

        public CryptoCreateMeta.Builder maxAutomaticAssociations(
                final int maxAutomaticAssociations) {
            this.maxAutomaticAssociations = maxAutomaticAssociations;
            return this;
        }

        public CryptoCreateMeta build() {
            return new CryptoCreateMeta(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("baseSize", baseSize)
                .add("lifeTime", lifeTime)
                .add("maxAutomaticAssociations", maxAutomaticAssociations)
                .toString();
    }
}
