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
import org.cristalise.kernel.lifecycle.instance.WfVertex.Types
import org.cristalise.kernel.utils.Logger


/**
 *
 */
@CompileStatic
class SplitDelegate extends BlockDelegate {
    Types type
    List<BlockDelegate> childBlocks = []

    SplitDelegate() {}

    public SplitDelegate(Types t, CompActDelegate caBlock, Map<String, WfVertex> cache) {
        assert t
        assert caBlock
        assert cache

        type = t
        parentCABlock = caBlock
        vertexCache = cache
    }

    public void processClosure(BlockDelegate parentBlock, Closure cl) {
        assert cl, "Block only works with a valid Closure"

        Logger.msg 1, "Split(type: $type) ----------------------------------"

        def aSplit = parentCABlock.createVertex(type, '')
        def aJoin  = parentCABlock.createVertex(Types.Join, '')

        cl.delegate = this
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        childBlocks.each {
            aSplit.addNext(it.firstVertex)
            it.lastVertex.addNext(aJoin)
        }

        firstVertex = aSplit
        lastVertex = aJoin

        Logger.msg 1, "Split(end) +++++++++++++++++++++++++++++++++++++++++"
    }

    public void ElemAct(String name = "", Closure cl = null) {
        throw new RuntimeException("Split cannot have standalone Activity, it shall be within a Block/CA/Split/Loop")
    }

    public void Block(Closure cl) {
        def b = new BlockDelegate(parentCABlock, vertexCache)
        childBlocks.add(b)
        b.processClosure(cl)
    }

    public void CompAct(String name = "", Closure cl) {
        CompositeActivity ca = (CompositeActivity)addVertex(Types.Composite, name)
        def caB = new CompActDelegate(name, ca, vertexCache)
        childBlocks.add(caB)
        caB.processClosure(cl)
    }

    public void Split(Types type, Closure cl) {
        def sB = new SplitDelegate(type, parentCABlock, vertexCache)
        childBlocks.add(sB)
        sB.processClosure(this, cl)
    }
}