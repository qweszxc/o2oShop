package zej.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import zej.o2o.dto.ShopExecution;
import zej.o2o.entity.Area;
import zej.o2o.entity.PersonInfo;
import zej.o2o.entity.Shop;
import zej.o2o.entity.ShopCategory;
import zej.o2o.enums.ShopStateEnum;
import zej.o2o.exceptions.ShopOperationException;
import zej.o2o.service.AreaService;
import zej.o2o.service.ShopCategoryService;
import zej.o2o.service.ShopService;
import zej.o2o.util.CodeUtil;
import zej.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object>getShopInitInfo(){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList=new ArrayList<>();
		List<Area> areaList= new ArrayList<>();
		try {
			shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList=areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object>registerShop(HttpServletRequest request){
		//接受并转化相应参数，包括店铺信息以及图片信息
		Map<String,Object>modelMap=new HashMap<>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("sucess",false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper=new ObjectMapper();
		Shop shop=null;
		try {
			shop=mapper.readValue(shopStr, Shop.class);
		}catch(Exception e) {
			modelMap.put("sucess",false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg=null;
		CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)) {
			//判断是否有上传的文件流
			MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
			shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("errMsg", "上传图片不能为空");
			modelMap.put("success", false);
			return modelMap;
		}
		//注册店铺
		if(shop!=null&&shopImg!=null) {
			PersonInfo owner=new PersonInfo();
			//TODO
			owner.setUserId(1L);/////////////////////////////////
			shop.setOwner(owner);
			/*File shopImgFile=new File(PathUtil.getImgBasePath()+ImageUtil.getRandomFileName());
			try {
				shopImgFile.createNewFile();
			}catch(Exception e) {
				modelMap.put("errMsg", e.getMessage());
				modelMap.put("success", false);
				return modelMap;
			}*/
			/*try {
				inputStreamToFile(shopImg.getInputStream(),shopImgFile);
			} catch (IOException e) {
				modelMap.put("errMsg", e.getMessage());
				modelMap.put("success", false);
				return modelMap;
			}*/
			
			ShopExecution se;
			try {
				
				se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("errMsg", e.getMessage());
				modelMap.put("success", false);
				return modelMap;
			} catch (IOException e) {
				modelMap.put("errMsg", e.getMessage());
				modelMap.put("success", false);
				return modelMap;
			}
			
			return modelMap;
		}else {
			modelMap.put("errMsg", "请输入店铺信息");
			modelMap.put("success", false);
			return modelMap;
		}
		//返回结果
		
	}
	/*private static void inputStreamToFile(InputStream ins,File file) {
		OutputStream os=null;
		try {
			os=new FileOutputStream(file);
			int bytesRead=0;
			byte[]buff=new byte[1024];
			while((bytesRead=ins.read(buff))!=-1) {
				os.write(buff, 0, bytesRead);
			}
		}catch(Exception e) {
			throw new RuntimeException("inputStreamToFile异常"+e.getMessage());
		}finally {
			try {
				if(os!=null) {
					os.close();
				}
				if(ins!=null) {
					ins.close();
				}
			}catch(IOException e) {
				throw new RuntimeException("io关闭异常"+e.getMessage());
			}
		}
	}*/
}
