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
package com.hedera.services.bdd.spec.transactions.token;

import static com.hedera.services.bdd.spec.transactions.token.CustomFeeSpecs.builtFixedHts;
import static com.hedera.services.bdd.spec.transactions.token.CustomFeeSpecs.builtFractional;
import static com.hedera.services.bdd.spec.transactions.token.CustomFeeSpecs.builtRoyaltyNoFallback;
import static com.hedera.services.bdd.spec.transactions.token.CustomFeeSpecs.builtRoyaltyWithFallback;
import static com.hedera.services.bdd.spec.transactions.token.CustomFeeSpecs.fixedHbarFeeInheritingRoyaltyCollector;
import static com.hedera.services.bdd.spec.transactions.token.CustomFeeSpecs.fixedHtsFeeInheritingRoyaltyCollector;

import com.hedera.services.bdd.spec.HapiApiSpec;
import com.hederahashgraph.api.proto.java.CustomFee;
import java.util.List;
import java.util.OptionalLong;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.Assertions;

public class CustomFeeTests {
    public static BiConsumer<HapiApiSpec, List<CustomFee>> fixedHbarFeeInSchedule(
            long amount, String collector) {
        return (spec, actual) -> {
            final var expected = CustomFeeSpecs.builtFixedHbar(amount, collector, spec);
            failUnlessPresent("fixed ℏ", actual, expected);
        };
    }

    public static BiConsumer<HapiApiSpec, List<CustomFee>> fixedHtsFeeInSchedule(
            long amount, String denom, String collector) {
        return (spec, actual) -> {
            final var expected = builtFixedHts(amount, denom, collector, spec);
            failUnlessPresent("fixed HTS", actual, expected);
        };
    }

    public static BiConsumer<HapiApiSpec, List<CustomFee>> fractionalFeeInSchedule(
            long numerator,
            long denominator,
            long min,
            OptionalLong max,
            boolean netOfTransfers,
            String collector) {
        return (spec, actual) -> {
            final var expected =
                    builtFractional(
                            numerator, denominator, min, max, netOfTransfers, collector, spec);
            failUnlessPresent("fractional", actual, expected);
        };
    }

    public static BiConsumer<HapiApiSpec, List<CustomFee>> royaltyFeeWithoutFallbackInSchedule(
            long numerator, long denominator, String collector) {
        return (spec, actual) -> {
            final var expected = builtRoyaltyNoFallback(numerator, denominator, collector, spec);
            failUnlessPresent("royalty", actual, expected);
        };
    }

    public static BiConsumer<HapiApiSpec, List<CustomFee>> royaltyFeeWithFallbackInHbarsInSchedule(
            long numerator, long denominator, long fallbackAmount, String collector) {
        return (spec, actual) -> {
            final var expected =
                    builtRoyaltyWithFallback(
                            numerator,
                            denominator,
                            collector,
                            fixedHbarFeeInheritingRoyaltyCollector(fallbackAmount),
                            spec);
            failUnlessPresent("royalty", actual, expected);
        };
    }

    public static BiConsumer<HapiApiSpec, List<CustomFee>> royaltyFeeWithFallbackInTokenInSchedule(
            long numerator,
            long denominator,
            long fallbackAmount,
            String fallbackDenom,
            String collector) {
        return (spec, actual) -> {
            final var expected =
                    builtRoyaltyWithFallback(
                            numerator,
                            denominator,
                            collector,
                            fixedHtsFeeInheritingRoyaltyCollector(fallbackAmount, fallbackDenom),
                            spec);
            failUnlessPresent("royalty", actual, expected);
        };
    }

    private static void failUnlessPresent(
            String detail, List<CustomFee> actual, CustomFee expected) {
        for (var customFee : actual) {
            if (expected.equals(customFee)) {
                return;
            }
        }
        Assertions.fail("Expected a " + detail + " fee " + expected + ", but only had: " + actual);
    }
}
