package com.datanucleus.samples.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.datanucleus.samples.dao.DummyDao;
import com.datanucleus.samples.entity.Inventory;

@Service
public class DummyService {

	@Autowired
	private DummyDao dummyDao;

	@Transactional
	public Inventory service(Inventory inv) {
	    inv = this.dummyDao.insert(inv);
		Assert.notNull(inv);
		
		return inv;
	}
	
	public Inventory queryById(String id) {
		Inventory inventory = this.dummyDao.queryByName(id);
		return inventory;
	}
	
	public List<Inventory> queryAll() {
		return this.dummyDao.queryAll();
	}
	
	@Transactional
	public void removeAll() {
		System.out.println("remove number:" + this.dummyDao.clearAll());
	}
}
