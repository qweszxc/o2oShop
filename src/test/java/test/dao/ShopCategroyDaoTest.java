package test.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTest;
import zej.o2o.dao.ShopCategoryDao;
import zej.o2o.entity.ShopCategory;

public class ShopCategroyDaoTest extends BaseTest{

	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Test
	public void testQueryShopCategory() {
		
		List<ShopCategory>list=shopCategoryDao.queryShopCategory(new ShopCategory());
		System.out.println("=============="+list.size()+"========");
	}
}
