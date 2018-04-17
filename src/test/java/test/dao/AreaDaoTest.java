package test.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTest;
import zej.o2o.dao.AreaDao;
import zej.o2o.entity.Area;

public class AreaDaoTest extends BaseTest{

	@Autowired
	private AreaDao areaDao;
	
	@Test
	public void testQueryArea() {
		List<Area>list=areaDao.queryArea();
		System.out.println(list.size());
	}
}
