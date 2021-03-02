/*
 * Copyright 2000-2020 Vaadin Ltd.
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
package com.vaadin.flow.server.communication.rpc;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.page.History.HistoryStateChangeEvent;
import com.vaadin.flow.component.page.History.HistoryStateChangeHandler;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.NavigationTrigger;
import com.vaadin.flow.shared.JsonConstants;
import org.slf4j.LoggerFactory;

import elemental.json.JsonObject;
import elemental.json.JsonValue;

/**
 * RPC handler for Navigation.
 *
 * @see JsonConstants#RPC_TYPE_NAVIGATION
 * @author Vaadin Ltd
 * @since 1.0
 *
 */
public class NavigationRpcHandler implements RpcInvocationHandler {

    @Override
    public String getRpcType() {
        return JsonConstants.RPC_TYPE_NAVIGATION;
    }

    @Override
    public Optional<Runnable> handle(UI ui, JsonObject invocationJson) {
        History history = ui.getPage().getHistory();

        HistoryStateChangeHandler historyStateChangeHandler = history
                .getHistoryStateChangeHandler();
        if (historyStateChangeHandler != null) {
            JsonValue state = invocationJson
                    .get(JsonConstants.RPC_NAVIGATION_STATE);
            String location = invocationJson
                    .getString(JsonConstants.RPC_NAVIGATION_LOCATION);
            boolean triggeredByLink = invocationJson
                    .hasKey(JsonConstants.RPC_NAVIGATION_ROUTERLINK);
            NavigationTrigger trigger = triggeredByLink
                    ? NavigationTrigger.ROUTER_LINK
                    : NavigationTrigger.HISTORY;
            // When the UI is made inert, all router link navigation is
            // ignored. For the part of the UI that is not inert, navigation
            // could be allowed by using something else than router links (with
            // server side listeners).
            // Blocking browser history back / forward navigation is not done
            // as it would be dubious and even impossible on the client side.
            if (trigger == NavigationTrigger.ROUTER_LINK
                    && ui.getElement().getNode().isInert()) {
                LoggerFactory.getLogger(NavigationRpcHandler.class.getName())
                        .trace("Ignored router link click for location '{}' because the UI is inert.",
                                location);
                return Optional.empty();
            }

            HistoryStateChangeEvent event = new HistoryStateChangeEvent(history,
                    state, new Location(location), trigger);
            historyStateChangeHandler.onHistoryStateChange(event);
        }

        return Optional.empty();
    }

}
