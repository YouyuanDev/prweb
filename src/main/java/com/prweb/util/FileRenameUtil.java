package com.prweb.util;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class FileRenameUtil implements FileRenamePolicy{

    public File rename(File file) {
         String fileName=file.getName();
         String extName=fileName.substring(fileName.lastIndexOf('.'));
         fileName= UUID.randomUUID().toString()+extName;
         file=new File(file.getParent(),fileName);
         return file;
    }
    public static Date getFileCreateTime(String fullFileName){
        Path path= Paths.get(fullFileName);
        BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
        BasicFileAttributes attr;
        try {
            attr = basicview.readAttributes();
            Date createDate = new Date(attr.creationTime().toMillis());
            return createDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 0, 1, 0, 0, 0);
        return cal.getTime();
    }
    //删除非今天的历史垃圾文件
    public static void cleanTrashFiles(String basePath){
        try{
            File fileFolder=new File(basePath+"/upload/pdf/");
            if(fileFolder.exists()&&fileFolder.isDirectory()){
                String fileList[]=fileFolder.list();
                for (String fileName:fileList){
                    File file=new File(basePath+"/upload/pdf/"+fileName);
                    if(file.isFile()&&(file.getName().endsWith(".zip")||file.getName().endsWith(".pdf"))){
                        Date fileCreateTime=FileRenameUtil.getFileCreateTime(basePath+"/upload/pdf/"+fileName);
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTime(fileCreateTime);
                        int fileCreateDay=calendar.get(Calendar.DAY_OF_YEAR);
                        System.out.println("fileCreateDay="+fileCreateDay);
                        calendar.setTime(new Date());
                        int now=calendar.get(Calendar.DAY_OF_YEAR);
                        System.out.println("now="+now);
                        if(now>fileCreateDay){
                            System.out.println("执行删除:"+fileName);
                            file.delete();
                        }

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
