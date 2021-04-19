/*---------------------------------------------------------------------------*\
|  Subject:       AppCore Class
|  Version:       3.5.0
|  Author: tyotann
|  Comment:       核心方法封装,此类暂时依赖于jquery
\*---------------------------------------------------------------------------*/

if (typeof AppEngine == "undefined") {
    window.AppEngine = {
        version: '3.5',
        UI: {},
        Form: {},
        App: {}
    };

    // 框架变量
    document["frame"] = {
        "btnDisabled": {}
    };

    window.AE = window.AppEngine;
}

window.onerror = function (m, u, l) {
    // alert("JavaScript Error:" + m + "\nline:" + l + "\nURL:" + u);
    return true;
};

if (!window.jQuery) {
    alert("页面没有加载成功,请查看网络是否存在延迟!");
}

// jQuery重定义,防止$与velocity冲突
window.jq = window.jQuery;

// 回调函数
var onloadcallbacks = [];

// debug标志
var debug = true;

/**
 * 页面加载完成后执行的方法
 */
AE.ready = function (funcb) {
    if (funcb) {
        onloadcallbacks[onloadcallbacks.length] = funcb;
    }
};

/**
 * <pre>
 * 页面元素绑定eventv_isFirst：true的话将是最先触发，反之放在事件链的最后
 * </pre>
 */
AE.event = function (id, eventname, v_function, v_isFirst) {

    var eId = id;
    if (typeof id == "string") {
        eId = $("#" + id);
    }
    $(eId).bind(eventname, v_function);
};

/**
 * 页面元素取消绑定event
 */
AE.unevent = function (id, eventname, fun) {
    var eId = id;
    if (typeof id == "string") {
        eId = $("#" + id);
    }

    if (fun) {
        $(eId).unbind(eventname, fun);
    } else {
        $(eId).unbind(eventname);
    }

};

/**
 * 页面ready后，初始化工作
 */
$(document).ready(function () {

    // 绑定页面元素类型
    if (AE.Form.bindType) {
        AE.Form.bindType();
    }

    // 绑定页面元素validate
    if (AE.Form.bindValidate) {
        AE.Form.bindValidate();
    }

    // 调用页面初始化AE.ready内的回调函数
    // 所有控件事件绑定都需要在此内完成,然后框架再绑定事件
    if (onloadcallbacks) {
        for (var i = 0; i < onloadcallbacks.length; i++) {
            try {
                onloadcallbacks[i].call();
            } catch (e) {
                alert(e.message);
            }
        }
    }

    // 绑定样式
    if (AE.Form.bindCss) {
        AE.Form.bindCss();
    }

    // 输入框的回车事件=移动焦点,按钮的前后按键=移动焦点
    // AE.Form.bindFocus();

    // 键盘输入控制
    // AE.Form.bindGlobalKey();

    // 控制右键菜单
    var f_debug = function () {
        return debug;
    };
    $(document).oncontextmenu = f_debug;
    $(document).onselectstart = f_debug;
    $(document).oncut = f_debug;
    $(document).ondrag = f_debug;
    $(document).oncopy = f_debug;
});

/**
 * <pre>
 * 通过服务名调用后台服务
 * 调用ESB两种方式
 * 1.服务名后加入 @ESB 比如 101010@ESB;
 * 2.在options参数中加入esb
 * </pre>
 *
 * @param 服务名(后面加入@ESB的话为调用ESB服务)
 * @param 服务参数
 * @param 处理成功后的回调函数
 * @param options
 *            async : false || true <br>
 *            esb : esb{type:'http'||'file'} unShowWait : true 不显示等待遮罩
 */
AE.ServiceEx = function (serviceName, params, v_callback, options) {

    var tranobject = null;

    if (params) {
        tranobject = params || {};
        if (typeof params == "string" && $("#" + params)) {
            tranobject = Form.serialize(params, true);
        }
    }

    // 所有参数加入appid
    /*if (!tranobject.appid) {
        tranobject.appid = window.top["frame_appid"];
    }*/
    tranobject.appid = "";

    try {

        // 防止按钮多次点击:ajax前使按钮disable,后使enable
        var v_btnItem = (typeof (event) != "undefined" && event && event.srcElement && event.srcElement.type && (event.srcElement.type == "button" || event.srcElement.type == "submit"))
            ? event.srcElement
            : null;

        var seqno = Utils.createUUID();

        // ESB的话,拼装ESB调用的serviceName
        var v_callServiceName = serviceName;

        if (v_callServiceName.indexOf('@') == -1 && options && options.esb) {
            v_callServiceName += (options.esb.esbProtocol ? ('@ESB.' + options.esb.esbProtocol) : '@ESB');
        }

        $.ajax({
            url: "invoke.do",
            type: 'post',
            data: {
                "FRAMEserviceName": v_callServiceName,
                "appid": tranobject.appid,
                "FRAMEparams": Object.toJSON(tranobject || {}),
                "FRAMEinvokeSeqno": seqno,
                "FRAMEesbSeqno": (options && options.esb && options.esb.esbSeqno) ? options.esb.esbSeqno : seqno,
                "FRAMEsessionCheck": (options && typeof (options.sessionCheck) == "boolean" && !options.sessionCheck)
                    ? false
                    : true
            },
            async: (options && options.async) ? options.async : false,
            beforeSend: function (jqXHR, settings) {

                try {

                    if (!options || !options.unShowWait) {
                        AE.UI.showwaitform();
                    }

                    // 防止按钮多次提交设置
                    if (v_btnItem != null) {
                        if (!document["frame"]["btnDisabled"][v_btnItem]) {
                            document["frame"]["btnDisabled"][v_btnItem] = 1;
                            v_btnItem.disabled = true;
                        } else {
                            document["frame"]["btnDisabled"][v_btnItem] = document["frame"]["btnDisabled"][v_btnItem] + 1;
                        }
                    }
                } catch (e) {
                    alert("ajax请求beforeSend时出现异常:" + e.message + ":" + e.description);
                }
            },
            success: function (responseText) {

                try {
                    var data = null;

                    if (responseText.indexOf("<html>") >= 0 || responseText.indexOf("</html>") >= 0) {
                        window.location.href = "session_out.do";
                        return;
                    }

                    if (responseText && responseText != "") {
                        data = eval("(" + responseText + ")");
                    }

                    if (data && typeof data["code"] != "undefined") {
                        var code = Number(data.code);

                        // 0:正常 1:业务警告 -1:业务逻辑异常 -2:系统异常 -98:Session 超时
                        if (code == 0 || code == 1) {
                            if (code == 1) {
                                alert("调用服务" + serviceName + "警告，警告信息：" + data["text"]);
                            }
                            if (v_callback) {
                                v_callback.call(this, data.data, data.pageLimit);
                            }
                        } else if (code < 0) {

                            // 如果用户有自定义异常,则处理
                            if (options && options.ECallBack) {
                                options.ECallBack.call(this, data);
                            } else {
                                if (code == -1) {
                                    alert(data["text"]);
                                } else if (code == -2) {
                                    alert("系统异常，异常信息：" + data["text"]);
                                } else if (code == -98) {

                                    if (window.top["f_frame_sessionout"]) {
                                        window.top["f_frame_sessionout"].call();
                                    } else if (data && data.data && data.data["sessionoutPage"]) {
                                        window.location.href = data.data["sessionoutPage"];
                                    } else {
                                        window.location.href = "session_out.do";
                                    }
                                    return;
                                } else {
                                    alert("调用服务" + serviceName + "异常，异常信息：" + data["text"]);
                                }
                            }
                        } else {
                            alert(data["text"]);
                        }
                    }
                } catch (e) {
                    alert("ajax请求success时出现异常:" + e.message + ":" + e.description);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                try {
                    if (jqXHR.status == 12029) {

                        if (confirm("服务器连接中断，是否关闭系统？")) {
                            var win = window.parent;
                            while (win.name != 'main') {
                                win = win.parent;
                            }

                            // 清除首页定时器
                            if (win.update_news) {
                                win.clearInterval(win.update_news);
                                win.update_news = undefined;
                            }
                            win.close();
                        }
                    } else if (jqXHR.status == 404) {
                        alert("【" + jqXHR.status + "】- 请查询请求的资源是否存活或已被锁定!");
                    } else if (jqXHR.status == 0) {
                        // status=0的情况不报异常
                    } else {
                        alert("【" + jqXHR.status + "】- 请求出错!");
                    }
                } catch (e) {
                    alert("ajax请求error时出现异常:" + e.message + ":" + e.description);
                }
            },
            complete: function () {

                try {

                    // sucess回调函数中关闭页面，会导致此处AE为undefine
                    if (document.body) {

                        if (!options || !options.unShowWait) {
                            AE.UI.hidewaitform();
                        }

                        if (v_btnItem != null) {
                            try {
                                document["frame"]["btnDisabled"][v_btnItem] = document["frame"]["btnDisabled"][v_btnItem] - 1;
                                if (document["frame"]["btnDisabled"][v_btnItem] == 0) {
                                    v_btnItem.disabled = false;
                                    if (!$(v_btnItem).is(":hidden")) {
                                        v_btnItem.focus();
                                    }
                                }
                            } catch (se) {
                                v_btnItem.disabled = false;
                                if (!$(v_btnItem).is(":hidden")) {
                                    v_btnItem.focus();
                                }
                            }
                        }
                    }
                } catch (e) {
                    alert("ajax请求complete时出现异常:" + e.message + ":" + e.description);
                }
            }
        });
    } catch (e) {
        alert("请求服务过程,程序出现异常:" + e.message + ":" + e.description);
    }
};

/**
 * 页面域对象序列化
 */
AppEngine.serialize = function (v_formId) {

    var v_serializedata = {};

    // 重复name的设置为数组
    var f_setData = function (v_data, v_name, v_value) {
        if (v_data[v_name]) {

            if (!Utils.isArray(v_data[v_name])) {
                var v_tmp = v_data[v_name];
                v_data[v_name] = [];
                v_data[v_name].push(v_tmp);
            }
            v_data[v_name].push(v_value);
        } else {
            v_data[v_name] = v_value;
        }
    };

    // 遍历所有非隐藏，非按钮，非checkbox的值
    $.each($("#" + v_formId + " input[type!='hidden'][type!='button'][type!='checkbox']").serializeArray(), function (i, item) {
        if (item.name || item.id) {
            f_setData(v_serializedata, (item.name ? item.name : item.id), item.value);
        }
    });

    // 模糊查询设置
    var allEmts = $("#" + v_formId + " input[type!='hidden'][type!='button'][likeQuery='true']");
    $.each(allEmts, function (i, item) {
        if (v_serializedata[item.name] != "") {
            v_id = item.name == "" ? item.id : item.name;
            v_serializedata[item.name] = "%" + v_serializedata[item.name] + "%";
        }
    });

    // checkbox值序列化问题
    var v_checkbox = $("#" + v_formId + " input[type!='hidden'][type=checkbox]");
    $.each(v_checkbox, function (i, item) {
        if (item.name || item.id) {
            f_setData(v_serializedata, (item.name ? item.name : item.id), (item.checked ? "1" : "0"));
        }
    });

    return v_serializedata;
};

AppEngine.deserialize = function (v_formId, v_data) {

    if (!v_data) {
        return;
    }

    // 遍历所有input
    $.each($("#" + v_formId + " input[type!='button']"), function (i, v_item) {

        var value = v_data[(v_item.name || v_item.id)];

        if (value == null) {
            return;
        }

        if (v_item.type == 'text' || v_item.type == 'password' || v_item.type == 'hidden' || v_item.type == 'textarea') {
            v_item.value = value;
        } else if (v_item.type == "checkbox") {
            v_item.checked = (v_item.value == value || value == "1");
        } else if (v_item.type == "radio") {
            if (v_item.value == value) {
                v_item.checked = true;
                return;
            }
        } else if (v_item.type == "select-one") {
            $A(v_item.options).each(function (e) {
                if (e.value == value) {
                    e.selected = true;
                    return;
                }
            });
        }
    });
};

AE.Storage = function (v_schema) {
    return new LocalStorage(v_schema);
};
