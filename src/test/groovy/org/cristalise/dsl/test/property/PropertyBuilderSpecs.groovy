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
package org.cristalise.dsl.test.property

import org.cristalise.dsl.property.PropertyBuilder
import org.cristalise.test.CristalTestSetup

import spock.lang.Specification


/**
 *
 */
class PropertyBuilderSpecs extends Specification implements CristalTestSetup {
    
    def setup()   { loggerSetup()    }
    def cleanup() { cristalCleanup() }

    def 'Property can be concrete, i.e. not abstract'() {
        when:
        def props = PropertyBuilder.build { Property("String": 'dummy', "Integer": -1) }

        then:
        props == ["String": 'dummy', "Integer": -1]
        props.getAbstract().size() == 0
    }

    def 'Property can be abstract'() {
        when:
        def props = PropertyBuilder.build { AbstractProperty("Boolean": true) }

        then:
        props == ["Boolean": true]
        props.getAbstract() == ["Boolean"]
    }

    def 'Existing concrete Property can be changed to abstract'() {
        when:
        def props = PropertyBuilder.build { 
            Property("Boolean": true)
            AbstractProperty("Boolean": false)
        }

        then:
        props == ["Boolean": false]
        props.getAbstract() == ["Boolean"]
    }
}
