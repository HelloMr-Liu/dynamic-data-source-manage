package org.king2.dynamicdatasource.entity.dto;

import java.util.List;

/**
 * 功能：接收封装数据源信息
 * 作者：刘梓江
 * 时间：2020/9/9  9:15
 */
public class DataSourceDto {

    /**
     * 数据源地址
     */
    private String dataSourceUrl;

    /**
     * 驱动
     */
    private String driverClassName;

    /**
     * 数据源密码
     */
    private String dataSourcePass;

    /**
     * 数据源账号
     */
    private String dataSourceAccount;

    /**
     * 表列表信息
     */
    private List<DataSourceTableFieldDto> tableFields;


    public DataSourceDto() {
    }


    public DataSourceDto(String dataSourceUrl, String driverClassName, String dataSourcePass, String dataSourceAccount, List<DataSourceTableFieldDto> tableFields) {
        this.dataSourceUrl = dataSourceUrl;
        this.driverClassName = driverClassName;
        this.dataSourcePass = dataSourcePass;
        this.dataSourceAccount = dataSourceAccount;
        this.tableFields = tableFields;
    }

    public String getDataSourceUrl() {
        return dataSourceUrl;
    }

    public void setDataSourceUrl(String dataSourceUrl) {
        this.dataSourceUrl = dataSourceUrl;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDataSourcePass() {
        return dataSourcePass;
    }

    public void setDataSourcePass(String dataSourcePass) {
        this.dataSourcePass = dataSourcePass;
    }

    public String getDataSourceAccount() {
        return dataSourceAccount;
    }

    public void setDataSourceAccount(String dataSourceAccount) {
        this.dataSourceAccount = dataSourceAccount;
    }

    public List<DataSourceTableFieldDto> getTableFields() {
        return tableFields;
    }

    public void setTableFields(List<DataSourceTableFieldDto> tableFields) {
        this.tableFields = tableFields;
    }
}


    
    
    