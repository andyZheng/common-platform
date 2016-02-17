/**********************************************************************
Copyright (c) 2011 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package com.datanucleus.samples.entity;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.platform.common.entity.BaseEntity;

/**
 * Definition of an Inventory of products.
 */
@PersistenceCapable
public class Inventory extends BaseEntity
{
    @PrimaryKey
    protected String name=null;
    
    @Embedded
    protected Remarks remarks;

    protected Set<Product> products = new HashSet<Product>();

    public Inventory(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Set<Product> getProducts()
    {
        return products;
    }

	public Remarks getRemarks() {
		return remarks;
	}

	public String toString()
    {
        return "Inventory : { name : " + name + ", remarks : " + remarks + ", product : " + products + "}";
    }

    public void setRemarks(Remarks remarks) {
        this.remarks = remarks;
    }
}