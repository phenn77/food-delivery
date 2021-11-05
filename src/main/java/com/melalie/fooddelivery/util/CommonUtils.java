package com.melalie.fooddelivery.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.engine.jdbc.ClobProxy;

import java.io.StringWriter;
import java.sql.Clob;

@Log4j2
public class CommonUtils {

    public CommonUtils() {

    }

    public static Clob getClob(String s) {
        return ClobProxy.generateProxy(s);
    }

    public static String getClobAsString(Clob clob) {
        try {
            char[] clobVal = new char[(int) clob.length()];
            StringWriter sw = new StringWriter();
            sw.write(clobVal);

            return sw.toString();
        } catch (Exception e) {
            log.error("Failed to convert Clob to String. Error: {}", ExceptionUtils.getRootCauseStackTrace(e));

            return StringUtils.EMPTY;
        }
    }
}
