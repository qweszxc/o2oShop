package zej.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

	private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r=new Random();
	private static Logger logger=LoggerFactory.getLogger(ImageUtil.class);
	
	
	public static File transferToFile(CommonsMultipartFile cFile) {
		File newFile=new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException | IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}
	//处理缩略图，并返回新生成图片的相对路径
	//CommonsMultipartFile是spring的文件接口，是一个前台的接口，为方便测试使用File
	public static String generateThumbnail(InputStream thumbnailInputStream,String fileName,String targetAddr) {
		String realFileName=getRandomFileName();
		String extension=getFileExtension(fileName);
		makeDirPath(targetAddr);
		String relativeAddr=targetAddr+realFileName+extension;
		logger.debug("current relativeAddr is:  "+relativeAddr);
		logger.debug("current complete is:  "+PathUtil.getImgBasePath()+relativeAddr);
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		try {
			Thumbnails.of(thumbnailInputStream).size(200, 200).outputQuality(0.8f).toFile(dest);
		}catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}
	//创建目标路径所涉及目录
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
		File dirPath=new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}
	//获取输入文件流的扩展名
	private static String getFileExtension(String fileName) {
		//String originalFileName=thumbnail.getOriginalFilename();
		return fileName.substring(fileName.lastIndexOf("."));
	}
	//生成随机文件，当前年月日时分秒+5位随机数
	public static String getRandomFileName() {
		int rannum=r.nextInt(89999)+10000;
		String nowTimeStr=sDateFormat.format(new Date());
		return nowTimeStr+rannum;
	}
	public static void main(String[] args) throws IOException {
		File f=new File(basePath+"test.jpg");
		Thumbnails.of(f).size(200, 200).watermark(Positions.BOTTOM_RIGHT,
				ImageIO.read(new File(basePath+"1875.png")), 0.25f).outputQuality(0.8f)
				.toFile(basePath+"new.jpg");
	}
	
	public static void deleteFileOrPath(String storePath) {
		//判断storePaht是文件还是目录，是文件就删除，是目录就删除该目录下所有文件
		File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File files[]=fileOrPath.listFiles();
				for(int i=0;i<files.length;i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}

}
