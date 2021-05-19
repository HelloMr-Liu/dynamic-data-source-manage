package org.king2.dynamicdatasource.mapper;

import org.king2.dynamicdatasource.entity.dto.DataSourceTableFieldValueDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 功能：描述当前接口
 * 作者：刘梓江
 * 时间：2020/9/9  12:13
 */
public class DataSourceTableFieldMapper implements RowMapper<DataSourceTableFieldValueDto> {
    @Override
    public DataSourceTableFieldValueDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        //获取指定字段结果集
        String primaryKeyValue = resultSet.getString("primaryKeyValue");
        String fieldValue = resultSet.getString("fieldValue");
        return new DataSourceTableFieldValueDto(primaryKeyValue,fieldValue);
    }
}
