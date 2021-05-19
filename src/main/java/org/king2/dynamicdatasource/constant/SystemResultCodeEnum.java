package org.king2.dynamicdatasource.constant;

/**
 * 描述：系统响应状态码信息
 *
 * @author 刘梓江
 * @date 2020/6/9  14:14
 */
public enum SystemResultCodeEnum {

    ERROR(500,"系统出错,请联系管理人员"),
    SUCCESS(200,"操作成功"),
    CHECK_ERROR(100,"数据校验异常"),


    SELECT_FAILURE(600,"查询失败"),
    INSERT_FAILURE(601,"添加失败"),
    UPDATE_FAILURE(602,"修改失败"),
    DELETE_FAILURE(603,"删除失败"),

    LOGIN_FAILURE( 700,"用户名密码错误"),
    LOGIN_DISABLE( 701,"该账号已经被禁用"),
    LOGIN_NO_LOOK_AUTH(702,"该账号没有查看权限"),
    LOGIN_LOOK_AUTH(703,"该账号只有查看权限"),
    LOGIN_SET_AUTH(704,"该账号只有编辑权限,不包含删除权限"),
    LOGIN_NO_AUTH( 705,"访问失败,需要权限");

    public Integer STATE;           //反馈的状态码
    public String MESS;             //反馈的描述信息
    SystemResultCodeEnum(Integer state, String mess){
        this.STATE=state;
        this.MESS=mess;
    }
}