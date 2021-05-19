package org.king2.dynamicdatasource.entity.dto;

/**
 * 描述：接收封装数据源表字段信息
 * @author 刘梓江
 * @date 2021/5/19 14:00
 */
public class DataSourceTableFieldDto {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表主键名
     */
    private String primaryKeyName;

    /**
     * 字段字段名
     */
    private String fieldName;

    public DataSourceTableFieldDto() {
    }

    public DataSourceTableFieldDto(String tableName, String primaryKeyName, String fieldName) {
        this.tableName = tableName;
        this.primaryKeyName = primaryKeyName;
        this.fieldName = fieldName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
    
    