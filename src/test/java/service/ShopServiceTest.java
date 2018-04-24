package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTest;
import zej.o2o.dto.ShopExecution;
import zej.o2o.entity.Area;
import zej.o2o.entity.PersonInfo;
import zej.o2o.entity.Shop;
import zej.o2o.entity.ShopCategory;
import zej.o2o.enums.ShopStateEnum;
import zej.o2o.exceptions.ShopOperationException;
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
	
	@Test
	public void testModifyShop()throws ShopOperationException,FileNotFoundException{
		Shop shop=new Shop();
		shop.setShopId(13L);
		shop.setShopName("修改后名称");
		File shopImg=new File("D:/itpm/顺序图/添加学生.jpg");
		InputStream is=new FileInputStream(shopImg);
		ShopExecution shopExecution=shopService.modifyShop(shop, is, "添加学生.jpg");
		System.out.println(shopExecution.getShop().getShopImg());
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
		ShopExecution se=shopService.getShopList(shopCondition, 3, 3);
		System.out.println(se.getCount());
		List<Shop>shopList=se.getShopList();
		for(Shop s:shopList) {
			System.out.println(s.getShopId());
		}
	}
}
