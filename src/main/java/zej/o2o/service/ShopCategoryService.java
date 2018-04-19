package zej.o2o.service;

import java.util.List;

import zej.o2o.entity.ShopCategory;

public interface ShopCategoryService {

	List<ShopCategory>getShopCategoryList(ShopCategory shopCategoryCondition);
}
