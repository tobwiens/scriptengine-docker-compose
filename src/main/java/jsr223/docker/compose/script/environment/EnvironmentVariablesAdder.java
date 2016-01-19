/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package jsr223.docker.compose.script.environment;

import java.util.Map;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;

@Log4j
@NoArgsConstructor
public class EnvironmentVariablesAdder {

    public void AddEntryToEnvironmentOtherThanPureStrings(@NotNull Map<String, String> environment,
            Map.Entry<String, Object> entry) {
        log.warn("Got Binding binding: " + entry.getKey() + entry.getValue());
        if (containsKeyAndValue(entry) && valueIsMapType(entry)) {
            addEntryToEnvironmentWhichIsAMapContainingStrings(environment, entry);
        } else {
            log.warn(
                    "Ignored binding: " + entry.getKey() + "(" + getClassName(
                            entry.getKey()) + "):" + entry.getValue() + "(" + getClassName(
                            entry.getValue()) + ")");

        }
    }

    private String getClassName(Object object) {
        return object != null ? object.getClass().getName() : null;
    }


    private boolean valueIsMapType(Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof Map<?, ?>;
    }

    private boolean containsKeyAndValue(Map.Entry<String, Object> entry) {
        return entry.getKey() != null && entry.getValue() != null;
    }

    private void addEntryToEnvironmentWhichIsAMapContainingStrings(@NotNull Map<String, String> environment,
            Map.Entry<String, Object> entry) {
        for (Map.Entry<?, ?> mapEntry : ((Map<?, ?>) entry.getValue()).entrySet()) {
            if (mapEntry.getValue() instanceof String && mapEntry.getKey() instanceof String) {
                environment.put((String) mapEntry.getKey(), (String) mapEntry.getValue());
                log.debug("Added binding: " + mapEntry.getKey() + ":" + mapEntry.getValue().toString());
            }
        }
    }
}
