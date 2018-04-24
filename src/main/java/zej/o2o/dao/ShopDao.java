package zej.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import zej.o2o.entity.Shop;

public interface ShopDao {

	int insertShop(Shop shop);
	
	int updateShop(Shop shop);
	
	Shop queryByShopId(long shopId);
	
	//分页查询店铺，可输入条件：店铺名，店铺状态，店铺类别，区域Id，区域id，owner
	List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);
	
	//返回queryShopList总数
	int queryShopCount(@Param("shopCondition")Shop shopCondition);
}
