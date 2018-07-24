package com.prweb.dao;

import com.prweb.entity.Location;

public interface LocationDao {
    //修改Location
    public int updateLocation(Location location);
    //增加Location
    public int addLocation(Location location);
    //删除Location
    public int delLocation(String[]arrId);
}
