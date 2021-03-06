package test.dao;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTest;
import zej.o2o.dao.ShopDao;
import zej.o2o.entity.Area;
import zej.o2o.entity.PersonInfo;
import zej.o2o.entity.Shop;
import zej.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest{

	@Autowired
	private ShopDao shopDao;
	
	@Test
	@Ignore
	public void testInsertShop() {
		Shop shop=new Shop();
		PersonInfo owner=new PersonInfo();
		Area area= new Area();
		ShopCategory shopCategory=new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试");
		shop.setShopDesc("test");
		shop.setShopAddr("test");
		shop.setShopImg("test");
		shop.setPhone("test");
		shop.setCreateTime(new Date());
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		int effectedNum=shopDao.insertShop(shop);
		System.out.println(effectedNum);
	}
	
	@Test
	public void testUpdateShop() {
		Shop shop=new Shop();
		shop.setShopDesc("描述");
		shop.setShopAddr("地址");
		shop.setShopId(1L);
		shop.setLastEditTime(new Date());
		int effectedNum=shopDao.updateShop(shop);
		System.out.println(effectedNum);
	}
	
	@Test
	public void testQueryByShopId() {
		Shop shop=shopDao.queryByShopId(1L);
		System.out.println(shop.getArea().getAreaName());
		System.out.println(shop);
	}
	
	@Test
	public void testQueryShopList() {
		Shop shopCondition=new Shop();
		PersonInfo owner=new PersonInfo();
		Area area=new Area();
		area.setAreaId(2);
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		shopCondition.setArea(area);
		List<Shop>shopList=shopDao.queryShopList(shopCondition, 0, 2);
		int count=shopDao.queryShopCount(shopCondition);
		System.out.println(count);
		for(Shop s:shopList) {
			System.out.println(s.getPhone());
		}
	}
}
