package com.future.module.system.util;

import java.util.Map;

import com.future.common.util.RandomUtil;
import com.future.module.system.model.datainterface.DataInterfaceMarkModel;
import com.future.permission.model.datainterface.DataInterfaceVarConst;

public class DataInterfaceParamUtil {

    /**
     * 获取指定字符串所在的位置及应该赋予的值
     *
     * @param map 位置、值
     * @param str 字符串
     * @param specifyString 指定字符串
     * @param value 值
     * @return
     */
    public static Map<Double, DataInterfaceMarkModel> getParamModel(Map<Double, DataInterfaceMarkModel> map, String str, String specifyString, Object value) {
        int frontLength = 0;
        while (str.contains(specifyString)) {
            int index = str.indexOf(specifyString);
            boolean flag = false;
            Double aDouble = new Double(index + frontLength + 1);
            while (!flag) {
                if (map.containsKey(aDouble)) {
                    aDouble += 0.0001;
                } else {
                    if (specifyString.equals(DataInterfaceVarConst.ID_LOT)) {
                        value = RandomUtil.uuId();
                    }
                    map.put(aDouble, new DataInterfaceMarkModel(specifyString, value));
                    flag = true;
                }
            }
            frontLength += (index + specifyString.length());
            str = str.substring(index + specifyString.length());
        }
        return map;
    }
}
