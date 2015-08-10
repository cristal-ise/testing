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

package org.cristalise.dsl.lifecycle.instance

import groovy.transform.CompileStatic

import org.cristalise.kernel.lifecycle.instance.CompositeActivity
import org.cristalise.kernel.lifecycle.instance.WfVertex
import org.cristalise.kernel.lifecycle.instance.Workflow
import org.cristalise.kernel.lifecycle.instance.predefined.server.ServerPredefinedStepContainer

/**
 *
 */
@CompileStatic
class WorkflowBuilder {
    protected Workflow wf = null

    Map<String, WfVertex> vertexCache = null

    /**
     * 
     * @param cl
     * @return
     */
    public Workflow build(Closure cl) {
        vertexCache = [:]
        CompositeActivity rootCA = new CompositeActivity()

        wf = new Workflow(rootCA, new ServerPredefinedStepContainer())
        vertexCache['rootCA'] = rootCA

        assert cl, "buildWf() only works with a valid Closure"
        new CompActDelegate('rootCA', (CompositeActivity)vertexCache['rootCA'], vertexCache).processClosure(cl)
        return wf
    }

}