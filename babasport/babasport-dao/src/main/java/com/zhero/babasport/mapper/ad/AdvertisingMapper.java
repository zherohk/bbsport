package com.zhero.babasport.mapper.ad;

import com.zhero.babasport.pojo.ad.Advertising;
import com.zhero.babasport.pojo.ad.AdvertisingQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdvertisingMapper {
    int countByExample(AdvertisingQuery example);

    int deleteByExample(AdvertisingQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Advertising record);

    int insertSelective(Advertising record);

    List<Advertising> selectByExample(AdvertisingQuery example);

    Advertising selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Advertising record, @Param("example") AdvertisingQuery example);

    int updateByExample(@Param("record") Advertising record, @Param("example") AdvertisingQuery example);

    int updateByPrimaryKeySelective(Advertising record);

    int updateByPrimaryKey(Advertising record);
}