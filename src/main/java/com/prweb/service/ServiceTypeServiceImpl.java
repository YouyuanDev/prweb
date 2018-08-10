package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.ServiceTypeDao;
import com.prweb.entity.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService{

    @Autowired
    ServiceTypeDao serviceTypeDao;

    //得到所有的ServiceType
    public String getAllServiceType(){
        List<ServiceType> list=serviceTypeDao.getAllServiceType();
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }


    //获取所有ServiceType列表
    public String getServiceTypeByLike(String service_type_name,int start,int rows){
        List<HashMap<String,Object>> list=serviceTypeDao.getAllByLike(service_type_name,start,rows);
        int count=serviceTypeDao.getCountAllByLike(service_type_name);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;
    }

    //保存ServiceType
    public String saveServiceType(ServiceType serviceType){
        System.out.print("saveServiceType");
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;

            if(serviceType.getId()==0){
                //添加
                resTotal=serviceTypeDao.addServiceType(serviceType);

            }else{
                //修改！

                resTotal=serviceTypeDao.updateServiceType(serviceType);
            }
            if(resTotal>0){
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());

        }finally {
            String mmp= JSONArray.toJSONString(json);
            System.out.print("mmp:"+mmp);
            return mmp;
        }
    }


    //删除serviceType信息
    public String delServiceType(String hlparam ){
        JSONObject json=new JSONObject();
        try{
            String[]idArr=hlparam.split(",");
            int resTotal=0;
            resTotal=serviceTypeDao.delServiceType(idArr);
            StringBuilder sbmessage = new StringBuilder();
            sbmessage.append("总共");
            sbmessage.append(Integer.toString(resTotal));
            sbmessage.append("项服务类型信息删除成功\n");
            if(resTotal>0){
                //System.out.print("删除成功");
                json.put("success",true);
            }else{
                //System.out.print("删除失败");
                json.put("success",false);
            }
            json.put("message",sbmessage.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
        }
        finally{
            String mmp= JSONArray.toJSONString(json);
            System.out.print("mmp:"+mmp);
            return mmp;
        }

    }

    public ServiceType getServiceRateByServiceCode(String service_code){
        try{

             ServiceType serviceType=serviceTypeDao.getServiceRateByServiceCode(service_code);
             return serviceType;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
