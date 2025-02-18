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
package com.vaadin.flow.router;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.vaadin.flow.router.internal.RouteUtil;

/**
 * Default implementation for {@link RoutePathProvider}.
 * 
 * @author Vaadin Ltd
 * @since
 *
 */
@Component(service = RoutePathProvider.class, property = Constants.SERVICE_RANKING
        + ":Integer=" + Integer.MIN_VALUE)
public class DefaultRoutePathProvider implements RoutePathProvider {

    @Override
    public String getRoutePath(Class<?> navigationTarget) {
        Route route = navigationTarget.getAnnotation(Route.class);
        if (route == null) {
            return null;
        }
        return RouteUtil.resolve(navigationTarget, route);
    }

}
