/*
 * Copyright 2000-2021 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.internal;

import org.junit.Assert;
import org.junit.Test;

public class UrlUtilTest {

    private String encodeURIShouldNotBeEscaped = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789;,/?:@&=+$-_.!~*'()#";
    private String encodeURIComponentShouldNotBeEscaped = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    @Test
    public void isExternal_URLStartsWithTwoSlashes_returnsTrue() {
        Assert.assertTrue(UrlUtil.isExternal("//foo"));
    }

    @Test
    public void isExternal_URLContainsAnySchemaAsPrefix_returnsTrue() {
        Assert.assertTrue(UrlUtil.isExternal("http://foo"));
        Assert.assertTrue(UrlUtil.isExternal("https://foo"));
        Assert.assertTrue(UrlUtil.isExternal("context://foo"));
        Assert.assertTrue(UrlUtil.isExternal("base://foo"));
    }

    @Test
    public void isExternal_URLDoesnotContainSchema_returnsFalse() {
        Assert.assertFalse(UrlUtil.isExternal("foo"));
    }

    @Test
    public void plusAndSpaceHandledCorrectly() {
        Assert.assertEquals("Plus+Spa%20+%20ce",
                UrlUtil.encodeURI("Plus+Spa + ce"));
        Assert.assertEquals("Plus%2BSpa%20%2B%20ce",
                UrlUtil.encodeURIComponent("Plus+Spa + ce"));
    }

    @Test
    public void encodeURI_shouldNotBeEscaped() {
        Assert.assertEquals(encodeURIShouldNotBeEscaped,
                UrlUtil.encodeURI(encodeURIShouldNotBeEscaped));
    }

    @Test
    public void encodeURI_mustBeEscaped() {
        for (char c = 0; c < 255; c++) {
            String s = String.valueOf(c);
            if (encodeURIShouldNotBeEscaped.contains(s)) {
                continue;
            }
            Assert.assertNotEquals(UrlUtil.encodeURI(s), s);
        }
    }

    @Test
    public void encodeURIComponent_shouldNotBeEscaped() {
        Assert.assertEquals(encodeURIComponentShouldNotBeEscaped, UrlUtil
                .encodeURIComponent(encodeURIComponentShouldNotBeEscaped));
    }

    @Test
    public void encodeURIComponent_mustBeEscaped() {
        for (char c = 0; c < 255; c++) {
            String s = String.valueOf(c);
            if (encodeURIComponentShouldNotBeEscaped.contains(s)) {
                continue;
            }
            Assert.assertNotEquals(UrlUtil.encodeURIComponent(s), s);
        }
    }
}
