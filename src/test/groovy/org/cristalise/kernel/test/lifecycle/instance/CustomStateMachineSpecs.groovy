/**
 * This file is part of the CRISTAL-iSE kernel.
 * Copyright (c) 2001-2015 The CRISTAL Consortium. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 *
 * http://www.fsf.org/licensing/licenses/lgpl.html
 */
package org.cristalise.kernel.test.lifecycle.instance

import org.cristalise.dsl.lifecycle.stateMachine.StateMachineBuilder
import org.cristalise.dsl.test.lifecycle.instance.WorkflowTestBuilder
import org.cristalise.kernel.common.AccessRightsException
import org.cristalise.test.CristalTestSetup

import spock.lang.Specification


/**
 *
 */
class CustomStateMachineSpecs extends Specification implements CristalTestSetup {

    WorkflowTestBuilder wfBuilder

    def setup() {
        inMemoryServer()
        wfBuilder = new WorkflowTestBuilder()
    }

    def cleanup() {
        cristalCleanup()
    }


    def 'Specific Transition is enabled/disabled by a Property'() {
        given:
        wfBuilder.eaSM = StateMachineBuilder.create("testing", "SkipStateMachine", 0) {
            State(id: "0", name: "Waiting")
            State(id: "1", name: "Started")
            State(id: "2", name: "Finished", proceeds: "true")

            Transition(id:"1", name:"Start",    origin:"0", target:"1", reservation:"set")
            Transition(id:"2", name:"Complete", origin:"1", target:"2", reservation:"clear") {
                Outcome(name:"\${SchemaType}", version:"\${SchemaVersion}")
                Script( name:"\${ScriptName}", version:"\${ScriptVersion}")
            }
            Transition(id:"3", name:"Skip", origin:"0", target:"2", enablingProperty: "Skippable", reservation:"clear")
        }

        wfBuilder.buildAndInitWf {
            EA('EA1') {
                Property(StateMachineName: "SkipStateMachine")
                Property(Skippable: true)
            }
            EA('EA2') {
                Property(StateMachineName: "SkipStateMachine")
                Property(Skippable: false)
            }
        }

        when:
        wfBuilder.requestAction("EA1", "Skip")

        then:
        wfBuilder.checkActStatus('EA1',[state: "Finished", active: false])
        wfBuilder.checkActStatus('EA2',[state: "Waiting", active: true])

        when:
        wfBuilder.requestAction("EA2", "Skip")

        then:
        thrown AccessRightsException
    }
}