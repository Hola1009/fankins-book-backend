package com.fankinsbook.framework.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @author <a href="https://github.com/hola1009">fancier</a>
 * 逻辑删除字段枚举
 **/
@Getter
@AllArgsConstructor
public enum DeletedEnum {

    YES(true),
    NO(false);

    private final Boolean value;
}