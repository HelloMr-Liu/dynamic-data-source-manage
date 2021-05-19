package org.king2.dynamicdatasource.entity.dto;


/**
 * 功能：表字段内容信息
 * 作者：刘梓江
 * 时间：2020/9/9  11:03
 */
public class DataSourceTableFieldValueDto {

    /**
     * 主键
     */
    private String primaryKeyValue;

    /**
     * 字段内容
     */
    private String fieldValue;

    public DataSourceTableFieldValueDto() {
    }

    public DataSourceTableFieldValueDto(String primaryKeyValue, String fieldValue) {
        this.primaryKeyValue = primaryKeyValue;
        this.fieldValue = fieldValue;
    }

    public String getPrimaryKeyValue() {
        return primaryKeyValue;
    }

    public void setPrimaryKeyValue(String primaryKeyValue) {
        this.primaryKeyValue = primaryKeyValue;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
    
    
    