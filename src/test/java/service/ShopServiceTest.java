package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTest;
import zej.o2o.dto.ShopExecution;
import zej.o2o.entity.Area;
import zej.o2o.entity.PersonInfo;
import zej.o2o.entity.Shop;
import zej.o2o.entity.ShopCategory;
import zej.o2o.enums.ShopStateEnum;
import zej.o2o.service.ShopService;

public class ShopServiceTest extends BaseTest{

	@Autowired 
	private ShopService shopService;
	
	@Test
	public void testAddShop() throws FileNotFoundException {
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
		shop.setShopName("测试4");
		shop.setShopDesc("test4");
		shop.setShopAddr("test4");
		//shop.setShopImg("test1");
		shop.setPhone("test4");
		shop.setCreateTime(new Date());
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg=new File("src/main/resources/test.jpg");
		InputStream is=new FileInputStream(shopImg);
		ShopExecution shopExecution=shopService.addShop(shop, is,shopImg.getName());
		System.out.println(shopExecution);
	}
}
