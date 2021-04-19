/**
 * 功能描述： 浏览器是否为IE 输入参数： null 返 回 值： true/false
 */
Utils.isIE = function () { // ie?
    if (window.navigator.appName == "Microsoft Internet Explorer")
        return true;
    else
        return false;
}

/**
 * 功能描述： 浏览器是否为IE6 输入参数： null 返 回 值： true/false
 */
Utils.isIE6 = function () { // ie6?
    if (Utils.isIE()) {
        if (window.navigator.appVersion.toLowerCase().match(/msie 6./) == 'msie 6.') {// 是IE6
            return true;
        } else {
            return false;
        }
    }
}

/**
 * 功能描述： 检查是否为数字 输入参数： val——传入要检查的值 返 回 值： true/false
 */
Utils.isNumber = function (val) {
    var regu = "^\\d+$";
    var re = new RegExp(regu);
    return !(val.search(re) == -1);
};

/**
 * 功能描述： 是否为货币 (格式不能带","号，最多只能有3位小数) 输入参数： val-传入操作的字符串 返 回 值： true/false
 */
Utils.isMoney = function (val) {
    var regu = "^[0-9]+[.]{0,1}[0-9]{0,2}$";
    var re = new RegExp(regu);
    return re.test(val);
};

/**
 * 功能描述： 是否为日期 (判定格式为：yyyymmdd) 输入参数： value-日期 返 回 值： day
 */
Utils.isDate = function (value) {
    if (value.length != 8 || !Utils.isMoney(value)) {
        return false;
    }
    var year = value.substring(0, 4);
    if (year > "2100" || year < "1900") {
        return false;
    }
    var month = value.substring(4, 6);
    if (month > "12" || month < "01") {
        return false;
    }
    var day = value.substring(6, 8);
    if (day > Utils.getMaxDay(year, month) || day < "01") {
        return false;
    }
    return true;
};

/**
 * 功能描述： 是否为数组 输入参数： v_value - 测试的对象 返 回 值： true/false
 */
Utils.isArray = function (v_value) {
    return $.isArray(v_value);
};

/**
 * 功能描述： 只允许输入数字 输入参数： code- 返 回 值： true/false
 */
Utils.isDigit = function (code) {
    if (code == 1) {
        return ((event.keyCode >= 48) && (event.keyCode <= 57));
    } else {
        return (((event.keyCode >= 48) && (event.keyCode <= 57)) || (event.keyCode == 46));
    }
};

/**
 * 功能描述： 判断字符串是否为空 返 回 值：true/false
 */
Utils.StringIsBlank = function (str) {
    if (str == null || str == "") {
        return true;
    }
    return false;
};

/**
 * 功能描述： 检查身份证合法性
 */
Utils.isIdcard = function (idcard, isex, ibirthday) {
    var idcard = idcard.toUpperCase();
    var Errors = new Array("验证通过!", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!", "身份证号码校验错误!", "身份证地区非法!");
    var area = {
        11: "北京",
        12: "天津",
        13: "河北",
        14: "山西",
        15: "内蒙古",
        21: "辽宁",
        22: "吉林",
        23: "黑龙江",
        31: "上海",
        32: "江苏",
        33: "浙江",
        34: "安徽",
        35: "福建",
        36: "江西",
        37: "山东",
        41: "河南",
        42: "湖北",
        43: "湖南",
        44: "广东",
        45: "广西",
        46: "海南",
        50: "重庆",
        51: "四川",
        52: "贵州",
        53: "云南",
        54: "西藏",
        61: "陕西",
        62: "甘肃",
        63: "青海",
        64: "宁夏",
        65: "新疆",
        71: "台湾",
        81: "香港",
        82: "澳门",
        91: "国外"
    };
    var idcard, Y, JYM;
    var S, M;
    var ereg;
    var idcard_array = new Array();
    idcard_array = idcard.split("");
    // 地区检验
    if (area[parseInt(idcard.substr(0, 2))] == null) return Errors[4];
    // 身份号码位数及格式检验
    switch (idcard.length) {
        case 15 :
            if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
                || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
                ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
            } else {
                ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
            }
            if (ereg.test(idcard)) {
                var Ai = idcard.length == 18 ? idcard.substring(0, 17) : idcard.slice(0, 6) + "19" + idcard.slice(6, 16);
                var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12), dd = Ai.slice(12, 14);
                var id = String(Ai), sex = id.slice(14, 17) % 2 ? "1" : "0";
                if ($(isex)) {
                    $(isex).value = sex;
                }
                if ($(ibirthday)) {
                    $(ibirthday).value = yyyy + "." + mm + "." + dd;
                }
                return true; // 检测ID的校验位
            } else
                return Errors[2];
            break;
        case 18 :
            // 18位身份号码检测
            // 出生日期的合法性检查
            // 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
            // 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
            if (parseInt(idcard.substr(6, 4)) % 4 == 0
                || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
                ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
            } else {
                ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
            }
            if (ereg.test(idcard)) {// 测试出生日期的合法性

                /*
                 * 计算校验位 S = (parseInt(idcard_array[0]) +
                 * parseInt(idcard_array[10])) 7 + (parseInt(idcard_array[1]) +
                 * parseInt(idcard_array[11])) 9 + (parseInt(idcard_array[2]) +
                 * parseInt(idcard_array[12])) 10 + (parseInt(idcard_array[3]) +
                 * parseInt(idcard_array[13])) 5 + (parseInt(idcard_array[4]) +
                 * parseInt(idcard_array[14])) 8 + (parseInt(idcard_array[5]) +
                 * parseInt(idcard_array[15])) 4 + (parseInt(idcard_array[6]) +
                 * parseInt(idcard_array[16])) 2 + parseInt(idcard_array[7]) * 1 +
                 * parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9]) *
                 * 3; Y = S % 11; M = "F"; JYM = "10X98765432"; M =
                 * JYM.substr(Y, 1);// 判断校验位
                 */

                // if (M == idcard_array[17]) {
                // 对最后一位校验位只做0~9或X,x的检测
                var regu = /(\d{1}|x{1}|X{1})$/;
                var re = new RegExp(regu);

                if (re.test(idcard_array[17])) {
                    var Ai = idcard.length == 18 ? idcard.substring(0, 17) : idcard.slice(0, 6) + "19" + idcard.slice(6, 16);
                    var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12), dd = Ai.slice(12, 14);
                    var id = String(Ai), sex = id.slice(14, 17) % 2 ? "1" : "0";
                    if ($(isex)) {
                        $(isex).value = sex;
                    }
                    if ($(ibirthday)) {
                        $(ibirthday).value = yyyy + "." + mm + "." + dd;
                    }
                    return true;
                    // 检测ID的校验位
                } else
                    return Errors[3];

                var Ai = idcard.length == 18 ? idcard.substring(0, 17) : idcard.slice(0, 6) + "19" + idcard.slice(6, 16);
                var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12), dd = Ai.slice(12, 14);
                var id = String(Ai), sex = id.slice(14, 17) % 2 ? "1" : "0";
                if ($(isex)) {
                    $(isex).value = sex;
                }
                if ($(ibirthday)) {
                    $(ibirthday).value = yyyy + "." + mm + "." + dd;
                }
                return true; // 检测ID的校验位
            } else
                return Errors[2];
            break;
        default :
            return Errors[1];
            break;
    }
};

/**
 * 检查扫描收件
 *
 * @param {}
 *            arc_tabname=收件表名;arc_id=业务ID;arc_ywlx=业务类型;arc_cs=业务参数
 * @return {}
 */
Utils.checkArcSj = function (values) {
    var checksjFlag = false;

    AppEngine.Service('09120A', values, function (res) {
        // alert(Object.toJSON(res));
        if (res == null || res == '') {
            checksjFlag = true;
        } else if (res[0] == 0) {
            checksjFlag = true;
        } else if (res[0] > 0) {
            var datas = res[1];
            var tempNote = '';
            var flag = '';
            for (var i = 0; i < datas.length; i++) {
                tempNote = tempNote + datas[i].c2 + '\r\n';
                flag = datas[i].flag;
            }
            if (flag == '1') {// 强制收件
                alert(tempNote);
                checksjFlag = false;
            } else {// 非强制收件
                if (confirm(tempNote + "收件未齐全，是否提交？")) {
                    checksjFlag = true;
                }
            }
        } else {
            alert(res);
            checksjFlag = false;
        }
    }, {
        async: false
    });

    return checksjFlag;
};

/**
 * 功能描述： 邮政编码验证
 *
 * @param id :
    *            控件ID
 * @return true/false
 */
Utils.checkZip = function (id) {
    var zip_test = /[1-9]\d{5}(?!d)/;
    var zip = $(id);

    if (!zip) return false;

    if (zip_test.test(zip.value)) {
        return true;
    }
    if (zip.value != '') {
        if (zip.value.length != 6) {
            alert("邮政编码长度应为6位");
            zip.value = "";
            zip.focus();
            return false;

        } else if (zip.value.substring(0, 1) == '0') {
            alert("邮政编码首位不能为0");
            zip.value = "";
            zip.focus();
            return false;
        } else {
            alert("邮政编码格式错误!");
            zip.value = "";
            zip.focus();
            return false;
        }

    }
};

/**
 * 功能描述： 手机号码验证
 *
 * @param id :
    *            控件ID
 * @return true/false
 */
Utils.checkPhone = function (id) {
    var re = /^(13[0-9]{9})|(15[89][0-9]{8})$/;
    var phone = $(id);

    if (!phone) return;

    if (re.test(phone.value)) {
        return true;
    } else {
        alert("请输入正确的手机号码！");
        phone.value = "";
        phone.focus();
        return false;
    }
};

/**
 * 功能描述： 邮件地址验证
 *
 * @param id :
    *            控件ID
 * @return true/false
 */
Utils.checkEmail = function (id) {
    var re = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    var email = $(id);

    if (!email) return;

    if (re.test(email.value)) {
        return true;
    } else {
        alert("请输入正确的Email地址！");
        email.value = "";
        email.focus();
        return false;
    }
};
