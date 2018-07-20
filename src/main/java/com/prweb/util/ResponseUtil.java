package com.prweb.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;

public class ResponseUtil {
    public static void write(HttpServletResponse response,Object object)throws Exception{
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(object);
        out.flush();
        out.close();
    }




}
