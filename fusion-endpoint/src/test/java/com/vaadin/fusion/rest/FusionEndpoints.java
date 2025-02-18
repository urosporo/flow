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
package com.vaadin.fusion.rest;

import java.time.LocalTime;
import java.time.ZonedDateTime;

import com.vaadin.fusion.Endpoint;

@Endpoint
public class FusionEndpoints {

    public BeanWithZonedDateTimeField getBeanWithZonedDateTimeField() {
        return new BeanWithZonedDateTimeField();
    }

    public BeanWithPrivateFields getBeanWithPrivateFields() {
        return new BeanWithPrivateFields();
    }

    public BeanWithJacksonAnnotation getBeanWithJacksonAnnotation() {
        return new BeanWithJacksonAnnotation();
    }

    public LocalTime getLocalTime() {
        return LocalTime.of(8, 0, 0);
    }

    public static class BeanWithZonedDateTimeField {
        private ZonedDateTime zonedDateTime = ZonedDateTime.now();

        public ZonedDateTime getZonedDateTime() {
            return zonedDateTime;
        }

        public void setZonedDateTime(ZonedDateTime zonedDateTime) {
            this.zonedDateTime = zonedDateTime;
        }
    }
}
