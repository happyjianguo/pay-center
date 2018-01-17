package com.dream.pay.center.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by mengzhenbin on 2016/11/10.
 * Base基类，用于
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 8764387520074751983L;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
