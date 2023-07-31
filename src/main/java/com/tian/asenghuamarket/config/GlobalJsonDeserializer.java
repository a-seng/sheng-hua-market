package com.tian.asenghuamarket.config;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class GlobalJsonDeserializer {

    /**
     * 字符串反序列化器
     * 过滤特殊字符，解决XXS攻击
     */

    public static class StringDeserializer extends JsonDeserializer<String>{


        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            //防止xss攻击
            return StringEscapeUtils.escapeHtml4(jsonParser.getValueAsString());
        }
    }
}
