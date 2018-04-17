package service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTest;
import zej.o2o.entity.Area;
import zej.o2o.service.AreaService;

public class AreaServiceTest extends BaseTest{

	@Autowired
	private AreaService areaService;
	@Test
	public void testGetAreaList() {
		List<Area>list=areaService.getAreaList();
		System.out.println(list.get(0).getAreaName());
	}
}
