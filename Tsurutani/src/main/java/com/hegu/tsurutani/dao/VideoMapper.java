package com.hegu.tsurutani.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface VideoMapper {
    void add(@Param("msg") String msg,@Param("opt") String opt,@Param("date") String date,@Param("uId") int uId);

    List<Map<String,Object>> findLogPage(@Param("userno") String userno,@Param("optdate") String optdate);

    List<Map<String,Object>> findCoypPage();

    void saveBackupsRoder(@Param("uuId")String uuId,@Param("bdate") String bdate);


    void backupsAssets(@Param("uuId") String uuId);

    void backupsAssetsck(@Param("uuId")String uuId);

    void backupsAssetsUpdate(@Param("uuId")String uuId);

    void backupsAssetsBx(@Param("uuId")String uuId);

    void backupsAssetszzc(@Param("uuId")String uuId);


    void restoreAssets(@Param("backupsno") String backupsno);

    void restoreAssetsck(@Param("backupsno")String backupsno);

    void restoreAssetsUpdate(@Param("backupsno")String backupsno);

    void restoreAssetsBx(@Param("backupsno")String backupsno);

    void restoreAssetszzc(@Param("backupsno")String backupsno);


    void deleteAssets();

    void deleteAssetsck();

    void deleteAssetsUpdate();

    void deleteAssetsBx();

    void deleteAssetszzc();
}
