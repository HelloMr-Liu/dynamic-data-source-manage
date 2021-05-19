package org.king2.dynamicdatasource.schedule;

import com.alibaba.fastjson.JSON;
import org.king2.dynamicdatasource.config.DataSourceConfiguration;
import org.king2.dynamicdatasource.constant.RedisKeyEnum;
import org.king2.dynamicdatasource.entity.dto.DataSourceDto;
import org.king2.dynamicdatasource.entity.dto.DataSourceTableFieldDto;
import org.king2.dynamicdatasource.utils.ApplicationUtil;
import org.king2.dynamicdatasource.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：多数据源表更新
 * 作者：刘梓江
 * 时间：2020/9/8  22:15
 */
@Component
public class DynamicUpdateTableFieldValueSchedule {

    private static final Logger logger= LoggerFactory.getLogger(DynamicUpdateTableFieldValueSchedule.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DataSourceConfiguration dataSources;

    /** 表字段文件信息对象**/
    public static File tableFieldFile=null;

    /**数据源表字段文件名**/
    public static String dataSourceTableFieldFileName="dataSourceTableField.txt";

    /**
     * 初始化配置文件信息格式
     * @throws IOException
     */
    @PostConstruct
    private void initDataSourceTableFieldFile() throws IOException {
        redisUtil.del(RedisKeyEnum.INTERNET_IP.name());
        tableFieldFile=new File(new ApplicationHome(getClass()).getSource().getParentFile().getPath()+"//"+dataSourceTableFieldFileName);
        if(!tableFieldFile.exists()){

            //创建多数据源配置文件格式
            List<DataSourceDto> dataSourceDtos=new ArrayList<>();

            //创建一个数据源信息结构
            DataSourceDto dataSourceDto=new DataSourceDto("","","","",new ArrayList<>());
            dataSourceDto.getTableFields().add(new DataSourceTableFieldDto("","",""));
            dataSourceDtos.add(dataSourceDto);

            FileWriter fw = new FileWriter(tableFieldFile.getPath());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(JSON.toJSONString(dataSourceDtos,true));
            bw.flush();
            bw.close();
        }
    }

    /**每1分钟执行一次**/
    @Scheduled(cron = "0 */1 * * * ?")
    private void updateDataSourceTableFieldValue() {
        logger.info("执行了一次数据源信息更新操作");
        /**获取数据源信息**/
        String s = ApplicationUtil.readJsonFile(tableFieldFile.getPath());
        if(!StringUtils.isEmpty(s)){
            List<DataSourceDto> dataSourceDtos = JSON.parseArray(s, DataSourceDto.class);
            dataSources.initDataSourceTableInfo(dataSourceDtos);
        }
    }

}
    
    
    