/**
 * 功能描述： String添加去空格方法 输入参数： null 返 回 值： 去空格后的字符串
 */
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

/**
 * 功能描述： 把所有比配的字符串替换成指定字符串 输入参数： search - 匹配字符串， replace - 代替字符串 返 回 值： 替换后的字符串
 */
String.prototype.replaceAll = function (search, replace) {
    var regex = new RegExp(search, "g");
    return this.replace(regex, replace);
};

/**
 * 功能描述： 获取字符串长度 输入参数： null 返 回 值： 字符串长度
 */
String.prototype.lengthb = function () {
    return this.replace(/[^\x00-\xff]/g, "**").length;
};

/**
 * 功能描述： 查找尾部是否包含指定字符串 输入参数： str - 查找字符 返 回 值： true/false
 */
String.prototype.endWith = function (str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length) return false;
    if (this.substring(this.length - str.length) == str)
        return true;
    else
        return false;
    return true;
};

/**
 * 功能描述： URL编码
 */
String.prototype.URLEncode = function () {
    var str = this;
    str = str.replace(/./g, function (sHex) {
        window.EnCodeStr = "";
        window.sHex = sHex;
        window.execScript("window.EnCodeStr=Hex(Asc(window.sHex))", "vbscript");
        return window.EnCodeStr.replace(/../g, "%$&");
    });
    return str;
};

/**
 * 功能描述： URL编码
 */
String.prototype.encodeURIComponent = function () {
    var str = this;
    if (window.RegExp && window.encodeURIComponent) {
        if (str != null) {
            return window.encodeURIComponent(str);
        }
    }
    return str;
};

/**
 * 功能描述： URL解码
 */
String.prototype.decodeURIComponent = function () {
    var str = this;
    if (window.RegExp && window.decodeURIComponent) {
        if (str != null) {
            return window.decodeURIComponent(str);
        }
    }
    return str;
};

/**
 * 功能描述： 过滤HTML标签
 */
String.prototype.HMTLEncode = function () {
    var str = this;
    str = str.replace(/</g, "&lt;");
    str = str.replace(/>/g, "&gt");
    // str = str.replace(/"/g,"&quot");
    str = str.replace(/©/g, '&copy');
    str = str.replace(/®/g, "&reg");
    str = str.replace(/™/g, "™");
    return str;
};

/**
 * 功能描述： 数组对象添加插入方法，可以在指定位置插入值 输入参数： vVal - 插入值， nIdx - 指定位置 返 回 值： 插入新值后的新数组
 */
Array.prototype.insert = function (vVal, nIdx) {
    var arrTemp = this;
    if (nIdx > arrTemp.length) nIdx = arrTemp.length;
    if (nIdx < -arrTemp.length) nIdx = 0;
    if (nIdx < 0) nIdx = arrTemp.length + nIdx;
    for (var ii = arrTemp.length; ii > nIdx; ii--) {
        arrTemp[ii] = arrTemp[ii - 1];
    }
    arrTemp[nIdx] = vVal;
    return arrTemp;
};

/**
 * 按索引移除数组中值
 *
 * @param v_idx
 *            索引号
 * @returns {Boolean}
 */
Array.prototype.remove = function (v_idx) {
    if (isNaN(v_idx) || v_idx > this.length) {
        return false;
    }
    for (var i = 0, n = 0; i < this.length; i++) {
        if (this[i] != this[v_idx]) {
            this[n++] = this[i];
        }
    }
    this.length -= 1;
};

/**
 * <pre>
 * 包含
 * </pre>
 *
 * @param obj
 * @returns {Boolean}
 */
Array.prototype.contains = function (obj) {
    if (obj && obj != null) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] != obj) {
                return true;
            }
        }
    }
    return false;
};

/**
 * 功能描述： 返回数组长度 输入参数： null 返 回 值： 数组长度
 */
Array.prototype.lengthb = function () {
    return this.length;
};

/**
 * <pre>
 * 字符串格式化日期输入参数： format——指定格式 返 回 值： 格式化后的日期字符串
 * (new Date()).format('yyyy-MM-dd HH:mm:ss')
 * </pre>
 *
 * @param v_format
 *            格式，hh为12小时显示,HH为24小时显示
 * @returns
 */
Date.prototype.format = function (v_format) {
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, // 小时
        "H+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds()
        // 毫秒
    };
    var week = {
        "0": "\u65e5",
        "1": "\u4e00",
        "2": "\u4e8c",
        "3": "\u4e09",
        "4": "\u56db",
        "5": "\u4e94",
        "6": "\u516d"
    };
    if (/(y+)/.test(v_format)) {
        v_format = v_format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(v_format)) {
        v_format = v_format.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468") : "")
            + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(v_format)) {
            v_format = v_format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return v_format;
};

/**
 * <pre>
 * 日期加减
 * 使用 : 当前日期减一年(new Date()).addTime('y', -1);
 * </pre>
 *
 * @param v_cnt
 *            加减日期数
 * @param v_type
 *            加减日期类别:h小时;d天数;w周数;m月份;y年
 * @returns
 */
Date.prototype.addTime = function (v_type, v_cnt) {

    var v_result = null;

    switch (v_type) {
        case "h" :
            v_result = new Date(Date.parse(this) + (3600000 * v_cnt));
            break;
        case "d" :
            v_result = new Date(Date.parse(this) + (86400000 * (v_cnt + 1)));
            break;
        case "w" :
            v_result = new Date(Date.parse(this) + ((86400000 * 7) * v_cnt) + 86400000);
            break;
        case "m" :
            v_result = new Date(this.getFullYear(), (this.getMonth()) + v_cnt, this.getDate(), this.getHours(), this.getMinutes(), this
                .getSeconds());
            break;
        case "y" :
            v_result = new Date((this.getFullYear() + v_cnt), this.getMonth(), this.getDate(), this.getHours(), this.getMinutes(), this
                .getSeconds());
            break;
    }

    return v_result;
};

/**
 * 日期转中文
 */
Date.prototype.toString = function (showWeek) {

    var myDate = this;
    var str = myDate.toLocaleDateString();

    if (showWeek) {
        var Week = ['日', '一', '二', '三', '四', '五', '六'];
        str += ' 星期' + Week[myDate.getDay()];
    }
    return str;
};

/* 写入CELL时空值转为0 */
function checkNumForCell(val) {
    if (val == null || val == "null" || val == "") val = 0;
    return val;
}

function checkStrForCell(val) {
    if (val == null || val == "null" || val.trim() == "") val = "";
    return val;
}

/**
 * <pre>
 * 弹出页面显示凭证内容
 * </pre>
 *
 * @param pzid
 *            凭证ID
 */
function pzView(pzid) {
    AE.UI.xWinEx("fmis/pzgl/pzcl/pzprint", "pzid=" + pzid, 660, 471, {
        hasTitle: false
    });
}