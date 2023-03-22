package cn.rivamed.log.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 描述: 文件序列化
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 23/03/21 17:33
 */
public class MultipartFileSerializer extends StdScalarSerializer<MultipartFile> {


    public static final MultipartFileSerializer INSTANCE = new MultipartFileSerializer();

    public MultipartFileSerializer() {
        super(MultipartFile.class);
    }

    @Override
    public void serialize(MultipartFile value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getOriginalFilename());
    }
}
