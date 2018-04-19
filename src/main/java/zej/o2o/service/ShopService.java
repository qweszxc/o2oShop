package zej.o2o.service;

import java.io.InputStream;

import zej.o2o.dto.ShopExecution;
import zej.o2o.entity.Shop;
import zej.o2o.exceptions.ShopOperationException;

public interface ShopService {

	ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName)throws ShopOperationException;
}