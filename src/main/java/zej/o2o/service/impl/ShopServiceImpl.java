package zej.o2o.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zej.o2o.dao.ShopDao;
import zej.o2o.dto.ShopExecution;
import zej.o2o.entity.Shop;
import zej.o2o.enums.ShopStateEnum;
import zej.o2o.exceptions.ShopOperationException;
import zej.o2o.service.ShopService;
import zej.o2o.util.ImageUtil;
import zej.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService{

	@Autowired
	private ShopDao shopDao;
	
	@Transactional
	public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException{
		//空值判断
		if(shop==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//初始化店铺信息
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effectedNum=shopDao.insertShop(shop);
			//System.out.println("========================插入成功============================");
			if(effectedNum<=0) {
				throw new ShopOperationException("店铺创建失败");//抛出RuntimeException事务才能回滚
			}else {
				if(shopImgInputStream!=null) {
					
					
					//存储图片
					//System.out.println("=========="+shopImg.getAbsolutePath()+"==============");
					try {
						addShopImg(shop,shopImgInputStream,fileName);
					}catch(Exception e) {
						throw new ShopOperationException("addShopImg error: "+e.getMessage());
					}
					//更新店铺图片地址
					effectedNum=shopDao.updateShop(shop);
					if(effectedNum<=0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
		}catch(Exception e) {
			throw new ShopOperationException("addShop error: "+e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	private void addShopImg(Shop shop, InputStream shopImgInputStream,String fileName) {
		//获取shop图片目录的相对值路径
		String dest=PathUtil.getShopImagePath(shop.getShopId());
		//System.out.println("============dest:"+dest+"===============");
		String shopImgAddr=ImageUtil.generateThumbnail(shopImgInputStream, fileName,dest);
		shop.setShopImg(shopImgAddr);
	}

}
