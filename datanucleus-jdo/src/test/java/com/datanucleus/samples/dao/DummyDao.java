package com.datanucleus.samples.dao;

import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Repository;

import com.datanucleus.samples.entity.Inventory;
import com.datanucleus.samples.entity.Product;
import com.platform.common.dao.jdo.impl.BaseJDODaoImpl;

@Repository
public class DummyDao extends BaseJDODaoImpl<Inventory, String> {

    public Inventory insert(Inventory inv) {
        Inventory newObj = super.save(inv);
        return newObj;
    }

    public Inventory queryByName(String name) {
        return this.getById(name);
    }

    public long clearAll() {
        List<Inventory> inventorys = this.queryAll();
        for (Inventory inventory : inventorys) {
            inventory.getProducts().clear();
        }

        this.getPersistenceManager().deletePersistentAll(inventorys);
        Query q = this.getPersistenceManager().newQuery(Product.class);
        return q.deletePersistentAll();
    }

    public List<Inventory> queryAll() {
        return super.queryAll();
    }
}
