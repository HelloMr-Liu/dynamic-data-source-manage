package org.king2.dynamicdatasource.config;


import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.king2.dynamicdatasource.constant.RedisKeyEnum;
import org.king2.dynamicdatasource.entity.dto.DataSourceTableFieldDto;
import org.king2.dynamicdatasource.entity.dto.DataSourceDto;
import org.king2.dynamicdatasource.entity.dto.DataSourceTableFieldValueDto;
import org.king2.dynamicdatasource.mapper.DataSourceTableFieldMapper;
import org.king2.dynamicdatasource.utils.ApplicationUtil;
import org.king2.dynamicdatasource.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 功能：动态操作数据源配置
 * 作者：刘梓江
 * 时间：2020/9/8  20:24
 */
@Configuration
public class DataSourceConfiguration {

    private static  final Logger logger= LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 初始化数据源表信息
     * @param dataSourceDtos
     */
    public void initDataSourceTableInfo(List<DataSourceDto> dataSourceDtos){

        /**获取前公网IP信息**/
        String beforeInternetIp=redisUtil.get(RedisKeyEnum.INTERNET_IP.name());

        /**获取当前公网IP信息**/
        String internetIp = ApplicationUtil.getHostInternetIp();

        if(!StringUtils.isEmpty(internetIp)&&(StringUtils.isEmpty(beforeInternetIp)||!beforeInternetIp.equals(internetIp))){

            //存储对应的指定数据源连接对象
            List<Connection> connectionList = new ArrayList<>();
            try{
                /**
                 * 对实务管理器中的默认连接初始化情况选择手动创建连接池中的连接
                 * 提示：这样下面的  connection.setAutoCommit(false); 才能生效否则每次默认执行完sql就默认提交
                 *      每次获取的连接都是一个新的连接而且是自动执行完sql就会自动提交
                 */
                TransactionSynchronizationManager.initSynchronization();

                for (DataSourceDto dataSourceDto : dataSourceDtos) {

                    /**创建连接池属性**/
                    Properties properties = new Properties();
                    properties.setProperty("driverClassName", dataSourceDto.getDriverClassName());
                    properties.setProperty("url", dataSourceDto.getDataSourceUrl());
                    properties.setProperty("username", dataSourceDto.getDataSourceAccount());
                    properties.setProperty("password", dataSourceDto.getDataSourcePass());
                    DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

                    /**获取当前数据源连接**/
                    Connection connection = DataSourceUtils.getConnection(dataSource);
                    connection.setAutoCommit(false);//设置非自动提交
                    connectionList.add(connection);

                    /**对jdbc模板指定对应的数据源**/
                    jdbcTemplate.setDataSource(dataSource);

                    Integer updateCount=0;
                    StringBuilder sqlStatementsPrefix=new StringBuilder();
                    StringBuilder sqlStatementsSuffix=new StringBuilder();

                    for(DataSourceTableFieldDto tableField:dataSourceDto.getTableFields()){

                        /**获取当前数据源表字段内容值信息集**/
                        String sql=" select "+tableField.getPrimaryKeyName()+" primaryKeyValue , "+tableField.getFieldName()+" fieldValue from "+tableField.getTableName();
                        List<DataSourceTableFieldValueDto> tableFieldFieldValueDtos = jdbcTemplate.query(sql,new DataSourceTableFieldMapper());

                        /**遍历更换字段内容信息**/
                        for(DataSourceTableFieldValueDto fieldValueDto:tableFieldFieldValueDtos){
                            String replaceNewIpInfo = ApplicationUtil.replaceStrIPOnNewIp(fieldValueDto.getFieldValue(), internetIp);
                            fieldValueDto.setFieldValue(replaceNewIpInfo);
                        }

                        /**
                         * 修改语句格式
                         * UPDATE mytable SET
                         *   myfield = CASE id
                         *       WHEN 1 THEN 'value'
                         *       WHEN 2 THEN 'value'
                         *       WHEN 3 THEN 'value'
                         *   END
                         * WHERE id IN (1,2,3)
                         */

                        /**遍历进行对更换后的值进行SQL语句拼接**/
                        for(DataSourceTableFieldValueDto fieldValueDto:tableFieldFieldValueDtos){

                            if(updateCount++==0){
                                //拼接字符串(前缀部分)
                                sqlStatementsPrefix.append(" UPDATE "+tableField.getTableName()+" SET ");
                                sqlStatementsPrefix.append( tableField.getFieldName()+" = CASE "+ tableField.getPrimaryKeyName());
                                //拼接字符串(后缀部分)
                                sqlStatementsSuffix.append(" END WHERE "+tableField.getPrimaryKeyName()+" IN ( ");
                            }
                            //拼接修改语句
                            sqlStatementsPrefix.append(" WHEN '"+fieldValueDto.getPrimaryKeyValue()+"' THEN '"+fieldValueDto.getFieldValue()+"'");

                            if(updateCount==400){
                                //代表已经拼接了400条记录
                                sqlStatementsSuffix.append(fieldValueDto.getPrimaryKeyValue()).append(")");
                                sqlStatementsPrefix.append(sqlStatementsSuffix.toString());

                                //执行sql语句
                                jdbcTemplate.execute(sqlStatementsPrefix.toString());

                                updateCount=0;                              //清空记录
                                sqlStatementsPrefix=new StringBuilder();    //清空拼接字符串(前缀部分)
                                sqlStatementsSuffix=new StringBuilder();    //清空拼接字符串(后缀部分)
                            }else{
                                //拼接对应的主键
                                sqlStatementsSuffix.append("'"+fieldValueDto.getPrimaryKeyValue()+"', ");
                            }
                        }

                        if(updateCount!=0){
                            //代表已经拼接了少于400条的插入信息 所以手动拼接二次执行
                            String lastPrimaryKeyIds=sqlStatementsSuffix.toString().substring(0,sqlStatementsSuffix.toString().lastIndexOf(","));
                            sqlStatementsPrefix.append(lastPrimaryKeyIds).append(" )");

                            //执行sql语句
                            jdbcTemplate.execute(sqlStatementsPrefix.toString());

                            updateCount=0;                                  //清空记录
                            sqlStatementsPrefix=new StringBuilder();        //清空拼接字符串(前缀部分)
                            sqlStatementsSuffix=new StringBuilder();        //清空拼接字符串(后缀部分)
                        }
                    }
                }

                /**
                 * 实务全局提交
                 */
                if(!CollectionUtils.isEmpty(connectionList)){
                    for (Connection connection : connectionList) {
                        connection.commit();
                        connection.close();
                    }
                    redisUtil.set(RedisKeyEnum.INTERNET_IP.name(),internetIp);
                    logger.info("=============多数据源更换原公网IP信息成功！=============");
                }
            }catch (Exception e){
                if(!CollectionUtils.isEmpty(connectionList)){
                    for (Connection connection : connectionList) {
                        try{
                            /**实务全局回滚**/
                            connection.rollback();
                            connection.close();
                        }catch (Exception e1){
                            logger.error("=============事物回滚异常=============");
                        }
                    }
                }
                logger.warn("=============数据源操作异常=============");
            }finally {
                /**
                 * 移除事物连接管理器
                 */
                TransactionSynchronizationManager.clearSynchronization();
            }

        }else{
            logger.info("=========原公网IP："+beforeInternetIp+"、现公网IP："+internetIp+"一致");
        }
    }

}
    
    
    