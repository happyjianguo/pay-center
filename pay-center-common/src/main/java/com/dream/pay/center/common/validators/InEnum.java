/**
 * Youzan.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.dream.pay.center.common.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 校验在枚举中的注解。
 *
 * 即待校验值等于枚举名称或某个属性值。
 *
 * <h1> 示例1: hairColor 等于枚举Color的name</h1>
 * <pre>
 *    &#64;InEnum(clazz = Color.class)
 *   private String hairColor;
 * </pre>
 * <h1> 示例2:hairColorCode 等于枚举的code属性值</h1>
 * <pre>
 *    &#64;InEnum(clazz = Color.class, property = "bizcode",message = "错误的颜色码")
 *   private int hairColorCode;
 * </pre>
 *
 * @author saber
 * @version InEnum.java, v 0.1 2017-01-10 20:06
 */
@Documented
@Constraint(validatedBy = InEnumValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })

public @interface InEnum {

  /**
   * 枚举类的class
   */
  Class<? extends Enum<?>> clazz();

  /**
   * 枚举属性名:默认为空。
   */
  String property() default "";

  /**
   * 校验不通过的消息
   */
  String message() default "非法枚举值";

  /**
   * 校验组
   */
  Class<?>[] groups() default {};


  Class<? extends Payload>[] payload() default {};



}
