package net.siehe.wiki.domain;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试
 * </p>
 *
 * @author zy
 * @since 2023-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Demo对象", description="测试")
public class Demo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;


}
