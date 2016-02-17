package com.datanucleus.samples.service;

import java.util.List;

import javax.jdo.JDOHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.datanucleus.samples.dao.DummyDao;
import com.datanucleus.samples.entity.Inventory;

@Service
public class DummyService {

	@Autowired
	private DummyDao dummy;

	@Transactional
	public Inventory service(Inventory inv) {
	    inv = this.dummy.insert(inv);
		Assert.notNull(inv);
		return inv;
	}
	
	public Inventory queryById(String id) {
		Inventory inventory = this.dummy.queryByName(id);
		System.err.println("queryById:" + JDOHelper.getObjectState(inventory));
		
		return inventory;
	}
	
	public List<Inventory> queryAll() {
		return this.dummy.queryAll();
	}
	
	@Transactional
	public void removeAll() {
		System.out.println("remove number:" + this.dummy.clearAll());
	}
}
