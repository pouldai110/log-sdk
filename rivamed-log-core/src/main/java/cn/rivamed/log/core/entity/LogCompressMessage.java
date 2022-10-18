package cn.rivamed.log.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogCompressMessage {

    private Integer length;
    private byte[] body;

}
