/*---------------------------------------------------------------------------*\
|  Subject:       Util Class                                              |
|  Version:       2.0.0                                                      |
|  FileName:      util.js                                                 |
|  Created:       2011-05                                                    |
|  Comment:       核心方法封装,此类暂时依赖于jquery                   |
\*---------------------------------------------------------------------------*/

var Utils = function () {
};

/**
 * Object 增加JS对象转变为JSON字符串
 */
Object.toJSON = function (v_data) {
    return JSON.stringify(v_data);
};

Utils = {

    /**
     * 功能描述： 字符转换为数字 输入参数： _char——需转换的字符串 返 回 值： number
     */
    toNumber: function (_char) {
        _char = _char.toUpperCase();
        var len = _char.length;
        var number = 0;
        var lastnumber = 0;

        for (var i = 0; i < len; i++) {
            var index = _char.charCodeAt(i) - 64;
            number = number + index + 26 * lastnumber - lastnumber;
            lastnumber = index;
        }
        return number;
    },
    /**
     * 格式化数字
     * num:需要格式化的数字
     * d:需要保留的小数位数
     */
    formatNum: function (num, d) {
        var digit = 0;
        if (d) {
            digit = d;
        } else {
            digit = 2;
        }
        //num = num + ",";
        //num = this.replaceComma(num);
        num = parseFloat(num).toFixed(digit);
        if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(num)) {
            return "0.00";
        }
        var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
//		var re = new RegExp().compile("(\\d)(\\d{3})(,|$)");
//		while (re.test(b)) { 
//			b = b.replace(re, "$1,$2$3");
//		}
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
    },

    /**
     * 功能描述： 日期固定格式化为 yyyy.mm.dd 输入参数： id——日期（字符串） 返 回 值： 格式化后的日期
     */
    formatDate: function (id) {
        var datetime = id;

        if (typeof id == "object") {
            datetime = $("#" + id).value;
        }
        if (datetime == "")
            return "";
        datetime = datetime.replaceAll("\\.", "");

        if (datetime.length == 6) {
            return datetime.substring(0, 4) + "." + datetime.substring(4, 6);
        }

        if (datetime.length != 8 || !Utils.isDate(datetime)) {
            return datetime;
        }

        return datetime.substring(0, 4) + "." + datetime.substring(4, 6) + "."
            + datetime.substring(6, 8);
    },

    /**
     * 功能描述： 去除纯HTML代码部分 输入参数： html-需要操作的HTML代码段 返 回 值： 能够在页面正文显示的内容
     */
    removeHtml: function (html) {
        if (html == null)
            return "";
        html = html.replace(/\<[^>]*>[1-9]/g, "<");
        html = html.replace(/\<[^>]*>[1-9]/g, "<");
        html = html.replace(/\<[^>]*>/g, "");
        html = html.replace(/&nbsp;/g, "");
        return html;
    },

    /**
     * 功能描述：获取一组选中的checkbox数据对象[{id:序号,v:checkbox的value值}...]
     * 输入参数：name—一组checkbox的name值 返 回 值：对象数组
     */
    checkbox: function (name) {
        var reV = new Array();
        var ele = eval('document.all.' + name);
        if (ele) {
            if (ele.length > 1) {
                for (var i = 0; i < ele.length; ++i) {
                    if (ele[i].checked == true) {
                        reV.push({
                            id: i,
                            v: ele[i].value
                        });
                    }
                }
            } else {
                if (ele.checked == true) {
                    reV.push({
                        id: 1,
                        v: ele.value
                    });
                }
            }
        }
        return reV;
    },

    /**
     * 选择所有checkbox
     */
    checkboxAll: function (name, type) {
        var ele = eval('document.all.' + name);
        var bool = new Boolean(type);

        if (ele) {
            if (ele.length > 1) {
                for (var i = 0; i < ele.length; i++) {
                    ele[i].checked = bool;
                }
            } else {
                ele.checked = bool;
            }
        }
    },

    /**
     * 检测所输入代码是否符合相应的级次 (333) id: 值 level: 传入的级次
     */
    checklevel: function (id, level) {
        if (level != '' && id != '') {
            var idlen = id.length;
            var lastlevel = 0;
            for (var i = 0; i < level.length; i++) {
                if ((parseFloat(level.substring(i, i + 1)) + parseFloat(lastlevel)) == idlen) {
                    return true;
                }
                lastlevel += parseFloat(level.substring(i, i + 1));
            }
            return false;
        }
    },

    /**
     * 获取当前代码相应的（上 或 下）一 级次 id: 值 (如果传入的值是空： 则返回第一级) level: 传入的级次 bz: 1:
     * 获取当前代码的下一级长度 (如果当前代码已是最后一级：则返回：-1) 2: 获取当前代码的上一级长度 (如果当前代码是第一级：则返回：-2)
     */
    getlevel: function (id, level, bz) {
        if (id == null || id == '') {
            return level.substring(0, 1)
        }
        if (level != '' && id != '') {
            var idlen = id.length;
            var lastlevel = 0;
            for (var i = 0; i < level.length; i++) {
                if ((parseFloat(level.substring(i, i + 1)) + parseFloat(lastlevel)) == idlen) {
                    if (bz == 1) {
                        if ((i + 1) < level.length) {
                            return level.substring(i + 1, i + 2);
                        } else {
                            return -1;
                        }
                    } else {
                        if (i != 0) {
                            return level.substring(i - 1, i);
                        } else {
                            return -2;
                        }
                    }
                }
                lastlevel += parseFloat(level.substring(i, i + 1));
            }
            return false;
        }
    },

    /**
     * 获取查询参数
     *
     * @param name
     * @returns
     */
    getQueryString: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)
            return unescape(r[2]);
        return null;
    },

    select: {
        /**
         * 功能描述： 设置select选择项 输入参数： id——select控件名, val-值 返 回 值： null
         */
        setValue: function (id, val) {
            if (!id && !$("#" + id))
                return;
            var obj = $("#" + id);
            if (!val)
                return;
            for (var i = 0; i < obj.options.length; i++) {
                if (obj.options[i].value == val) {
                    obj.options[i].selected = true;
                    return;
                }
            }
        },

        /**
         * 功能描述： 给指定select添加一组选项 输入参数： id——select控件名, ops—对象数组, t—设置text的对象的属性名，
         * v—设置value的对象的属性名 返 回 值： null
         */
        addOptions: function (id, ops, v, t) {
            if (!id && !$("#" + id) && !ops && !t && !v)
                return;
            var obj = $("#" + id);

            for (var i = 0; i < ops.length; i++) {
                var op = document.createElement("option");
                var item = ops[i];

                if (v in item && t in item) {
                    op.text = item[t];
                    op.value = item[v];

                    // 下拉框中不存在此value才能添加
                    if (!Utils.select.existItem(id, op.value)) {
                        obj.add(op);
                    }
                }
                op = null;
            }
        },

        // 选择的值往上移
        moveup: function (id) {

            var strTempValue;
            var strTempText;
            var intCurIndex;
            var optionsObjects = $("#" + id);
            intCurIndex = optionsObjects.selectedIndex;

            if (intCurIndex > 0) {

                strTempValue = optionsObjects.options.item(intCurIndex).value;
                strTempText = optionsObjects.options.item(intCurIndex).text;

                optionsObjects.options.item(intCurIndex).value = optionsObjects.options
                    .item(intCurIndex - 1).value;
                optionsObjects.options.item(intCurIndex).text = optionsObjects.options
                    .item(intCurIndex - 1).text;
                optionsObjects.options.item(intCurIndex - 1).value = strTempValue;
                optionsObjects.options.item(intCurIndex - 1).text = strTempText;
                optionsObjects.selectedIndex = intCurIndex - 1;
            }
        },

        // 选择的值往下移
        movedown: function (id) {
            var optionsObjects = $("#" + id);

            var strTempValue;
            var strTempText;
            var intCurIndex;
            var intIndexCount;

            intCurIndex = optionsObjects.selectedIndex;
            intIndexCount = optionsObjects.length;

            if (intCurIndex == -1) {
                return;
            }

            if (intCurIndex < intIndexCount - 1) {
                strTempValue = optionsObjects.options.item(intCurIndex).value;
                strTempText = optionsObjects.options.item(intCurIndex).text;

                optionsObjects.options.item(intCurIndex).value = optionsObjects.options
                    .item(intCurIndex + 1).value;
                optionsObjects.options.item(intCurIndex).text = optionsObjects.options
                    .item(intCurIndex + 1).text;
                optionsObjects.options.item(intCurIndex + 1).value = strTempValue;
                optionsObjects.options.item(intCurIndex + 1).text = strTempText;
                optionsObjects.selectedIndex = intCurIndex + 1;
            }
        },

        // 判断是否存在此键值
        existItem: function (id, value) {
            var isExit = false;
            for (var i = 0; i < $("#" + id).options.length; i++) {
                if ($("#" + id).options[i].value == value) {
                    isExit = true;
                    break;
                }
            }
            return isExit;
        },

        /**
         * 功能描述： 给指定select添加一项 输入参数： id——select控件名,
         * text—option的text值,val-option的value值 返 回 值： null
         */
        removeAllOptions: function (id) {
            if (!id && !$("#" + id))
                return;
            var obj = $("#" + id);

            obj.options.length = 0;
        },

        /**
         * 功能描述： 在下拉框中删除选中的项目
         */
        removeSelectOptions: function (id) {
            var v_select = $(id);
            for (var o = v_select.length - 1; o >= 0; o--) {
                if (v_select.options[o].selected == true) {
                    v_select.options.remove(o);
                }
            }
        }
    },

    /**
     * 功能描述： 设置radio选中值 输入参数： frm——radio所在表单名, radioname-需操作的radio名, value-值 返 回
     * 值： null
     */
    setRadio: function (frm, radioname, value) {
        if (!frm) {
            return;
        }
        if (value == null || value.toString() == "") {
            return;
        }
        var a = frm.getElementsByTagName("input");
        for (var i = 0; i < a.length; i++) {
            if (a[i].type == "radio" && a[i].name == radioname) {
                if (a[i].value == value) {
                    a[i].checked = true;
                    return;
                }
            }
        }
    },

    /**
     * 功能描述： 获取radio选中值 输入参数： frm——radio所在表单名, radioname-需操作的radio名 返 回 值： null
     * or radio选中值
     */
    getRadio: function (frm, radioname) {
        if (!frm) {
            return null;
        }
        var a = frm.getElementsByTagName("input");
        for (var i = 0; i < a.length; i++) {
            if (a[i].type == "radio" && a[i].name == radioname) {
                if (a[i].checked) {
                    return a[i].value;
                }
            }
        }
        return null;
    },

    /**
     * 功能描述： 获取当前年月的最后一天 输入参数： year-当前年份， month-当前月份 返 回 值： day
     */
    getMaxDay: function (year, month) {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return "30";
        }
        if (month == 2) {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                return "29";
            } else {
                return "28";
            }
        }
        return "31";
    },

    /**
     * 功能描述： 通过name获取指定组的多选框 输入参数： frm-表单名， name-多选框名 返 回 值： checkBox对象数组
     */
    getCheckBoxList: function (frm, name) {
        var chkboxs = [];
        if (!frm) {
            return chkboxs;
        }
        var a = frm.getElementsByTagName("input");
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i].type == "checkbox" && a[i].name == name) {
                chkboxs.push(a[i]);
            }
        }
        return chkboxs;
    },

    /**
     * 功能描述： 设置鼠标划入样式 输入参数： obj-要操作的控件 返 回 值： null
     */
    setMouseOverStyle: function (obj) {
        if (obj.className != 'click')
            obj.className = 'over';
    },

    /**
     * 功能描述： 设置鼠标划出样式 输入参数： obj-要操作的控件 返 回 值： null
     */
    setMouseOutStyle: function (obj) {
        if (obj.className != 'click')
            obj.className = 'remove';
    },

    /**
     * 功能描述： 获取父窗体 输入参数： null 返 回 值： 父窗体
     */
    getOpener: function () {
        return parent;
    },

    /**
     * 得到随机UUID(不是真正意义的UUID,只是模拟)
     *
     * @return {}
     */
    createUUID: function () {
        var s = [];
        var hexDigits = "0123456789ABCDEF";
        for (var i = 0; i < 32; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[12] = "4";
        s[16] = hexDigits.substr((s[16] & 0x3) | 0x8, 1);
        var uuid = s.join("");
        return uuid;
    },
    /**
     * 功能描述： 日期加天数
     *
     * @param {}
     *            strInterval
     * @param {}
     *            NumDay 天数
     * @param {}
     *            dtDate 原日期
     * @return {} 新日期
     */
    dateAdd: function (strInterval, NumDay, dtDate) {
        var dtTmp = new Date(dtDate);
        if (isNaN(dtTmp))
            dtTmp = new Date();
        switch (strInterval) {
            case "s":
                dtTmp = new Date(Date.parse(dtTmp) + (1000 * parseInt(NumDay)));
                break;
            case "n":
                dtTmp = new Date(Date.parse(dtTmp) + (60000 * parseInt(NumDay)));
                break;
            case "h":
                dtTmp = new Date(Date.parse(dtTmp) + (3600000 * parseInt(NumDay)));
                break;
            case "d":
                dtTmp = new Date(Date.parse(dtTmp) + (86400000 * parseInt(NumDay)));
                break;
            case "w":
                dtTmp = new Date(Date.parse(dtTmp)
                    + ((86400000 * 7) * parseInt(NumDay)));
                break;
            case "m":
                dtTmp = new Date(dtTmp.getFullYear(), (dtTmp.getMonth())
                    + parseInt(NumDay), dtTmp.getDate(), dtTmp.getHours(),
                    dtTmp.getMinutes(), dtTmp.getSeconds());
                break;
            case "y":
                // alert(dtTmp.getFullYear());
                dtTmp = new Date(dtTmp.getFullYear() + parseInt(NumDay), dtTmp
                    .getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp
                    .getMinutes(), dtTmp.getSeconds());
                // alert(dtTmp);
                break;
        }
        var mStr = new String(dtTmp.getMonth() + 1);
        var dStr = new String(dtTmp.getDate());
        if (mStr.length == 1) {
            mStr = "0" + mStr;
        }
        if (dStr.length == 1) {
            dStr = "0" + dStr;
        }
        return dtTmp.getFullYear() + mStr + dStr;
    },
    /**
     * 功能描述： 取得一个月中最大天数
     *
     * @param {}
     *            sYear 年
     * @param {}
     *            sMOnth 月
     * @return {} 最大天数
     */
    getMaxDays: function (iYear, iMonth) {
        var isLeap = false;
        var maxDay = "31";
        if (((iYear % 4 == 0) && (iYear % 100 != 0)) || (iYear % 400 == 0)) {
            isLeap = true;
        } else {
            isLeap = false;
        }
        if (iMonth == 2) {
            if (isLeap == true) {
                maxDay = "29";
            } else {
                maxDay = "28";
            }
        } else {
            if (iMonth == 4 || iMonth == 6 || iMonth == 9 || iMonth == 11) {
                maxDay = "30";
            }
        }
        return maxDay;
    },
    /**
     * 创建动态页面
     *
     * @param {}
     *            objs 页面元素定义
     * @param {}
     *            id 动态页面所属divId
     * @param {}
     *            options 自定义参数:options.column=2|3 每行显示列数;
     */
    createDynamicPage: function (objs, id, options) {
        var sb = new StringBuffer();
        sb
            .append("<table style='margin: 0px; width: 100%; border-collapse: collapse; padding: 0px;' border='0' align='center'>");

        var rows = Math.ceil((objs.length) / options.column);
        var k = 0;
        for (var i = 0; i < rows; i++) {
            sb.append("<tr>");
            for (var j = 0; j < options.column; j++) {
                var obj = objs[k];
                k = k + 1;
                if (obj && obj.txt && obj != 'undefined') {
                    // alert(obj.name);
                    if (obj.type && obj.type == 'checkbox') {
                        sb.append("<td nowrap='nowrap' colspan='2'>");
                    } else {
                        if (obj.txtstyle) {
                            sb.append("<td class='tdbg' nowrap='nowrap'")
                                .append(" style='").append(obj.txtstyle)
                                .append("' >");
                        } else {
                            sb.append("<td class='tdbg' nowrap='nowrap'>");
                        }
                        sb.append(obj.txt);
                        if (obj.colspan) { // 目前只支持一行两列或者一行三列的情况
                            if (obj.colspan == 3) {
                                j++;
                                k++;
                            } else if (obj.colspan == 5) {
                                j = j + 2;
                                k = k + 2;
                            }
                            sb.append("</td><td colspan='").append(obj.colspan)
                                .append("'>");
                        } else {
                            sb.append("</td><td>");
                        }
                    }
                    if (obj.type && obj.type == 'select') {
                        sb.append("<select ");
                    } else if (obj.type && obj.type == 'textarea') {
                        sb.append("<textarea ");
                    } else if (obj.type && obj.type == 'checkbox') {
                        sb.append("<input type='checkbox' ");
                    } else {
                        sb.append("<input type='text' ");
                    }
                    sb.append(" style='").append(obj.style).append("'");
                    sb.append(" name='").append(obj.name).append("'");
                    if (obj.maxlength) {
                        sb.append(" maxlength='").append(obj.maxlength).append(
                            "'");
                    }
                    if (obj.size) {
                        sb.append(" size='").append(obj.size).append("'");
                    }
                    if (obj.eventchange) {
                        sb.append(" onchange='").append(obj.eventchange)
                            .append("'");
                    }
                    if (obj.checked) {
                        sb.append(" checked='").append(obj.checked).append("'");
                    }
                    if (obj.callbackFun) {
                        sb.append(" callbackFun=true");
                    }
                    if (obj.readonly) {
                        sb.append(" readonly='readonly' class='readtxt' ");
                    } else {
                        sb.append(" class='txt' ");
                    }
                    if (obj.dataType) {
                        sb.append(" dataType='").append(obj.dataType).append(
                            "'");
                    }
                    if (obj.dataFormat) {
                        sb.append(" dataFormat='").append(obj.dataFormat)
                            .append("'");
                    }
                    if (obj.cols) {
                        sb.append(" cols='").append(obj.cols).append("'");
                    }
                    if (obj.disabled) {
                        sb.append(" disabled='").append(obj.disabled).append(
                            "'");
                    }

                    sb.append(" />");
                    if (obj.type && obj.type == 'select') {
                        sb.append("</select>");
                    } else if (obj.type && obj.type == 'checkbox') {
                        sb.append(obj.txt);
                    }
                    sb.append(obj.endstr);

                    sb.append("</td>");
                } else {
                    sb.append("<td>&nbsp;</td><td>&nbsp;</td>");
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        // alert(sb.toString());
        $(id).innerHTML = sb.toString();

        // 重新初始化页面元素
        AppEngine.App.bindFormElement();
        // AppEngine.Form.bindFocus();
    },
    /**
     * 将动态定义的页面元素设成readonly
     *
     * @param {}
     *            objs 页面元素定义
     * @param {}
     *            id 动态页面所属divId
     * @param {}
     *            options 自定义参数
     */
    grayedDynamicPage: function (objs, options) {
        for (var j = 0; j < objs.length; j++) {
            var obj = objs[j];
            // alert(Object.toJSON(obj));
            if (obj && obj.txt && obj != 'undefined') {
                if (obj.type && obj.type == 'select') {
                    // $(obj.name).disabled = true;
                    $(obj.name).className = "readtxt";
                    $(obj.name).onbeforeactivate = function () {
                        return false;
                    };
                    $(obj.name).onfocus = function () {
                        this.blur();
                    };
                    // 下面两个方法会引起页面无响应
                    // obj.onmouseover = function(){obj.setCapture();};
                    // obj.onmouseout = function(){obj.releaseCapture();};
                    $(obj.name).onclick = function () {
                        var index = this.selectedIndex;
                        this.onchange = function () {
                            this.selectedIndex = index;
                        };
                    }
                } else if (obj.type && obj.type == 'textarea') {
                    $(obj.name).readOnly = true;
                    $(obj.name).className = "readtxt";
                } else {
                    $(obj.name).readOnly = true;
                    $(obj.name).className = "readtxt";
                }
            }
        }
    },
    /**
     * 清空指定页面容器内的输入框
     *
     * @param {}
     *            container 页面容器
     */
    clearInput: function (container, flag) {
        var tags = $A($(container).getElementsByTagName('input'));

        tags.each(function (tag) {
            if (tag.type == 'text') {
                if (flag && flag != 'undefund') {
                    if (tag.alt == flag) {
                        tag.value = '';
                    }
                } else {
                    tag.value = '';
                }
            }
        });
    },

    yyyyMMdd: function (time) {
        try {
            return time.split(" ")[0];
        } catch (e) {
            // TODO: handle exception
            return "";
        }

    }
}
