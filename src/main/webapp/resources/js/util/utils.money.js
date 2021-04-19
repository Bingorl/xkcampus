/**
 * <pre>
 * 根据传入的运算符号对两个金额作运算 n1:传入的参数 n2:传入的参数 symbol:计算符号 &quot;+&quot;,&quot;-&quot;,&quot;*&quot;,&quot;/&quot; fix:保留尾数
 * </pre>
 *
 */
Utils.operateMoneyEx = function (n1, n2, symbol, fix) {
    return Utils.formatNum(Utils.operateNumberEx(n1, n2, symbol, fix), 0);
};

/* 比较数值大小 */
Utils.compareMoney = function (n1, n2) {
    var l_n1 = (n1 == "") ? "0.00" : Utils.removeComma(n1);
    var l_n2 = (n2 == "") ? "0.00" : Utils.removeComma(n2);

    if (parseFloat(l_n1) > parseFloat(l_n2))
        return 1;
    else if (parseFloat(l_n1) == parseFloat(l_n2))
        return 0;
    else
        return -1;
};

/*
 * 根据传入的运算符号对两个数值 作运算 n1:传入的参数 n2:传入的参数 symbol:计算符号 "+","-","*","/" fix:保留尾数
 */
Utils.operateNumberEx = function (n1, n2, symbol, fix) {
    var l_n1 = (n1 == "") ? "0.00" : Utils.removeComma(n1);
    var l_n2 = (n2 == "") ? "0.00" : Utils.removeComma(n2);
    var l_n = "0.00";
    switch (symbol) {
        case "+" :
            l_n = (parseFloat(l_n1) + parseFloat(l_n2)).toFixed(fix);
            break;
        case "-" :
            l_n = (parseFloat(l_n1) - parseFloat(l_n2)).toFixed(fix);
            break;
        case "*" :
            l_n = (parseFloat(l_n1) * parseFloat(l_n2)).toFixed(fix);
            break;
        case "/" :
            l_n = (parseFloat(l_n1) / parseFloat(l_n2)).toFixed(fix);
            break;
        case "%" :
            l_n = (parseFloat(l_n1) % parseFloat(l_n2)).toFixed(fix);
    }
    return l_n;
};

/**
 * 功能描述： 去除字符中逗号，一般去除格式化金额后的逗号 输入参数： n-传入操作的字符串 返 回 值： 处理后的字符串
 */
Utils.removeComma = function (n) {
    if (typeof (n) == 'undefined') {
        return;
    }
    var l_n = "";
    for (var i = 0; i < n.length; i++) {
        var str = n.substring(i, i + 1);
        l_n += str.replace(",", "");
    }
    return l_n;
};

/**
 * 功能描述： 格式化金额 输入参数： num——传入要检查的值，digit-小数点保留几位数字 返 回 值：
 * 能够正确格式化返回格式化完成后值l_num，否则返回"0.00"
 */
Utils.formatNum = function (num, digit) {
    if (!num)
        return "0.00";
    if (digit) {
        digit = digit;
    } else {
        digit = 2;
    }
    num = num + ",";
    num = Utils.removeComma(num);
    num = parseFloat(num).toFixed(digit);
    if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(num)) {
        return "0.00";
    }
    var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
    var re = new RegExp().compile("(\\d)(\\d{3})(,|$)");
    while (re.test(b)) {
        b = b.replace(re, "$1,$2$3");
    }
    if (c && digit && new RegExp("^.(\\d{" + digit + "})(\\d)").test(c)) {
        if (RegExp.$2 > 4) {
            c = (parseFloat(RegExp.$1) + 1) / Math.pow(10, digit);
        } else {
            c = "." + RegExp.$1;
        }
    }
    var l_num = a + "" + b + "" + (c + "").substr((c + "").indexOf("."));
    if (l_num.indexOf(".") < 0) {
        l_num += ".00";
    }
    return l_num;
};
