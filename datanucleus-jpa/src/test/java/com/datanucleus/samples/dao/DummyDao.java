package com.datanucleus.samples.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.datanucleus.samples.entity.Inventory;
import com.platform.common.dao.jpa.impl.BaseJPADaoImpl;

@Repository
public class DummyDao extends BaseJPADaoImpl<Inventory, String> {

    public Inventory insert(Inventory inv) {
        EntityManager pm = super.getEntityManager();
        pm.persist(inv);

        return inv;
    }

    public Inventory queryByName(String id) {
        return super.getById(id);
    }

    public long clearAll() {
        EntityManager daoTemplate = this.getEntityManager();
        List<Inventory> inventorys = this.queryAll();
        for (Inventory inventory : inventorys) {
            inventory.getProducts().clear();
            daoTemplate.flush();
        }
        daoTemplate.remove(inventorys);
        
        Query query = daoTemplate.createQuery("DELETE FROM Product p");
        query.executeUpdate();

        return inventorys.size();
    }

    public List<Inventory> queryAll() {
        return super.queryAll();
    }

}
