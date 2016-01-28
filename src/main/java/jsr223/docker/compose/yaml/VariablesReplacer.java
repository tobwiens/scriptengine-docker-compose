/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2016 INRIA/University of
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
package jsr223.docker.compose.yaml;

import java.util.Map;

public class VariablesReplacer {

    /**
     * Replaces all varialbes with look like $[VariableName]. With matching variables in the
     * provided map.
     * @param script String to replace all occurrences in.
     * @param variables Map which contains variables names mapped to values. Anything which has a key or
     *                  value null is not replaced.
     * @return Returns a copy of script where all occurrences are replaced. If any parameter is null,
     * then script is returned.
     */
    public String replaceVariables(String script, Map<String, String> variables) {
        if(script == null || variables == null) {
            return script;
        }

        String result = script;
        // Replace all variables one by one
        for (Map.Entry<String, String> variable : variables.entrySet()) {
            if (variable.getValue() != null && variable.getKey() != null) {
                result = result.replace("$" + variable.getKey(), variable.getValue());
            }
        }
        return result;
    }
}
