/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.similarity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * A simple word {@link Tokenizer} that utilizes a regex to find words. It applies a regex {@code (\w)+} over the input text to extract words from a given
 * character sequence.
 * <p>
 * Instances of this class are immutable and are safe for use by multiple concurrent threads.
 * </p>
 *
 * @since 1.0
 */
final class RegexTokenizer implements CharSequenceTokenizer<CharSequence> {

    /** The whitespace pattern. */
    private static final Pattern PATTERN = Pattern.compile("(\\w)+");

    /**
     * The singleton instance.
     */
    static final RegexTokenizer INSTANCE = new RegexTokenizer();

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the input text is blank.
     */
    @Override
    public CharSequence[] apply(final CharSequence text) {
        Validate.isTrue(StringUtils.isNotBlank(text), "Invalid text");
        final Matcher matcher = PATTERN.matcher(text);
        final List<String> tokens = new ArrayList<>();
        while (matcher.find()) {
            tokens.add(matcher.group(0));
        }
        return tokens.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

}
