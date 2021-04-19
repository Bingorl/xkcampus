AE.Form = {

    validatePatterns: {
        'number': function () {

            // 正整数+0
            if (this.value.toString() == "0") {
                return true;
            } else {
                return /^([1-9][0-9]*)$/.test(this.value);
            }
        },
        'numberic': /^(([1-9]\d*)|0)(\.\d{1,2})?$/,
        'email': /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
        'zip': /^[1-9]\d{5}$/,
        'mobile': /^1[3|5|8]\d{9}$/,
        'url': /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
        'tel': /^[0-9-]{7,18}$/,
        'datePicker': /^\d{4}(\-|\/|\.)?\d{1,2}(\-|\/|\.)\d{1,2}( (\d{2}):(\d{2}):(\d{2}))?$/
    },

    // 绑定全局按钮事件
    bindGlobalKey: function () {

        $(document.body).keydown(function () {

            try {
                switch (event.keyCode) {

                    case 8 : // backspace:阻止页面回退

                        // 焦点在input,textarea框内,且type不为button,hidden,file时backspace有效,否则事件失效
                        if (document.activeElement.tagName
                            && document.activeElement.type
                            && (document.activeElement.tagName.toUpperCase() == "INPUT" || document.activeElement.tagName.toUpperCase() == "TEXTAREA")) {
                            var v_type = document.activeElement.type;

                            if (v_type != "button" && v_type != "hidden" && v_type != "file"
                                && !document.activeElement.getAttribute('readonly')) {
                                return true;
                            }
                        }
                        return false;
                    case 78 :

                        // 禁用Ctrl+N
                        return !event.ctrlKey;
                    case 116 :

                        // F5刷新页面时,暂时不禁用
                        return true;
                    default :
                        return true;
                }
            } catch (e) {

                // 出错后正常退出
                return true;
            }
        });
    },

    // 绑定样式
    bindCss: function () {

        // input 获得焦点时变颜色[IE11时,uploadify会有异常]
        $(document.body).on('focus', ":input:visible:enabled", function () {

            // 失去焦点时,左右去空格
            if ($(this).val().trim() != $(this).val()) {
                $(this).val($(this).val().trim());
            }

            $(this).addClass("on");
        });

        // input 失去焦点时变颜色[IE11时,uploadify会有异常]
        $(document.body).on('blur', ":input:visible:enabled", function () {
            $(this).removeClass("on");
        });

        // 图片点击后展示，img标签中含有属性showImg
        if (window.top.$("#img-show").length > 0) {
            $(document.body).on('click', ":image[showImg='true'],img[showImg='true']", function () {

                // 去掉压缩属性
                window.top.$("#img-show-src").attr("src", $(this).attr("src").replace("fileImgSize", "remove-fileImgSize"));
                window.top.$("#img-show").show();
            });
        }

        // 图片点击后进入裁剪
        if (window.top.$("#img-edit").length > 0 && window.top["frame.img.editer"]) {
            $(document.body).on('click', ":image[enableEdit],img[enableEdit]", function () {
                AE.UI.imgEdit(this);
            });
        }

    },

    // 绑定焦点
    // 因该是动态判定focusIndex的,而不应该开始的时候就设定好
    bindFocus: function () {

        // 绑定输入框获获得焦点后选中文本(鼠标点击)
        $(document.body).on('focus',
            ":input:visible:enabled[type!='hidden'][type!='button'][type!='submit'][type!='date'][bindFocusSelect!=true]", function () {
                var v_item = this;

                if (v_item != null && v_item.onfocus == null) {

                    v_item.onfocus = function () {
                        try {
                            if (v_item.getAttribute('readonly') == false && v_item.select) {
                                v_item.select();
                            }
                        } catch (e) {
                        }
                    };
                    $(v_item).attr("bindFocusSelect", true);
                }
            });

    },

    // 得到此对象下个焦点对象
    getNextFocusItem: function (v_item, v_step, v_scope) {

        // 选择显示的,非disable的,readonly选择器有问题,手动判断
        var v_itemArray;
        if (v_scope == "button") {
            v_itemArray = $(":input:visible:enabled[type='button'],input:visible:enabled[type='submit']");
        } else {
            v_itemArray = $(":input:visible:enabled[type!='hidden']");
        }

        var i;
        for (i = 0; i < v_itemArray.length; i++) {
            if (v_item == v_itemArray[i]) {
                break;
            }
        }

        var j = i;
        do {
            j += v_step;

            j = (j == v_itemArray.length ? 0 : j);

            j = (j == -1 ? v_itemArray.length - 1 : j);

            if (j == i) {
                return v_itemArray[j];
            }

            // 非下拉框或非只读的情况 ,下拉框readonly是true
            if (v_itemArray[j].type.indexOf("select") == 0 || v_itemArray[j].getAttribute('readonly') == false) {
                return v_itemArray[j];
            }
        } while (true);
    },

    // 禁用事件
    disableEvent: function (v_item, v_eventName) {

        // TODO 此处没有加入jquery的绑定事件
        // alert($(currFocusEmt).data("events")["change"].length);
        // alert($(currFocusEmt).data("events")["onchange"].length);

        if (typeof v_item["frameEventHandlers"] == "undefined") {
            v_item["frameEventHandlers"] = {};
        }

        v_item["frameEventHandlers"][v_eventName] = v_item[v_eventName];
        v_item[v_eventName] = null;

        return v_item["frameEventHandlers"][v_eventName];
    },

    // 启用事件
    enableEvent: function (v_item, v_eventName) {
        if (v_item["frameEventHandlers"][v_eventName]) {
            v_item[v_eventName] = v_item["frameEventHandlers"][v_eventName];
        }
        v_item["frameEventHandlers"][v_eventName] = null;
    },

    // 绑定元素 元素焦点顺序change-->blur-->submit
    bindType: function () {

        // 绑定日期类型:type='datePicker'
        $(document.body).on('focus', ":input:visible:enabled[type='datePicker'][bindType!=true]", function () {

            var v_item = this;
            var v_dataFormat = v_item.getAttribute('dataFormat');
            if (!v_dataFormat || v_dataFormat == "") {
                v_dataFormat = "yyyy.MM.dd HH:mm:ss";
            }

            AE.UI.my97datepicker(v_item, v_dataFormat, v_item.value);
            v_item.setAttribute("bindType", "true");
        });

        // 绑定文件类型
        $.each($(":file[bindType!=true]"), function (i, v_item) {
            AE.UI.file(v_item);
        });

        // 绑定dic类型
        $.each($("select:visible:enabled[dic][bindType!=true]"), function (i, v_item) {

            AE.UI.select((v_item.name ? v_item.name : v_item.id), "encodeService.forSsdictionary", {
                typeid: v_item.getAttribute('dic')
            }, v_item.value, null, {
                code: "",
                name: "--请选择--"
            });

            v_item.setAttribute("bindType", true);
        });
    },

    // html5模拟validate
    bindValidate: function () {

        // 失去焦点时做验证，关闭相关提示框
        $(document.body).on('blur', ":input[type!='hidden'][type!='button'][type!='submit']", function () {
            AE.Form.validateElement(this);
            if ($(this).data("frame-validateErrorTipsId")) {
                AE.UI.hideTips($(this).data("frame-validateErrorTipsId"));
            }
        });

        // 获得焦点时判断,是否有错误信息,没有的话关闭当前提醒
        $(document.body).on('focus', ":input[type!='hidden'][type!='button'][type!='submit']", function () {
            if (($(this).data("frame-validateErrorMsg") || "") != "") {
                $(this).data("frame-validateErrorTipsId", AE.UI.showTips(0, $(this).data("frame-validateErrorMsg")));
            } else {
                AE.UI.hideTips();
            }
        });
    },

    // 检查表单数据
    validate: function (v_formName) {

        var v_invalidCnt = 0;

        $.each($("#" + v_formName + " :input[type!='hidden'][type!='button'][type!='submit']"), function (i, item) {
            if (!AE.Form.validateElement(item)) {
                v_invalidCnt++;
            }
        });

        return v_invalidCnt == 0;
    },

    // 检查元素
    validateElement: function (v_item) {

        try {
            var v_errorMsg = "";

            // 失去焦点时,左右去空格
            if ($(v_item).val().trim() != $(v_item).val()) {
                $(v_item).val($(v_item).val().trim());
            }

            // 进行必须项检查,必须项检查前会先trim
            if ($(v_item).attr("required") && v_item.value.trim() == "") {
                v_errorMsg += "此为必须项!";
            }

            // 进行最小,最大长度检查
            if ($(v_item).attr("minlengthb") || $(v_item).attr("maxlengthb")) {
                var v_len = v_item.value.lengthb();

                var v_min = $(v_item).attr("minlengthb") || 0;
                var v_max = $(v_item).attr("maxlengthb") || Number.MAX_VALUE;
                if (v_min > v_len || v_len > v_max) {
                    v_errorMsg += "长度必须必须在" + v_min + "-" + v_max + "字节以内!";
                }
            }

            // 进行数据格式检查
            if (v_item.value != "") {

                var v_match = true;

                // 判断次序:pattern-->dataType-->type
                var v_pattern = $(v_item).attr("pattern") ? $(v_item).attr("pattern") : AE.Form.validatePatterns[$(v_item).attr("dataType")
                    ? $(v_item).attr("dataType")
                    : $(v_item).attr("type")];

                if (v_pattern) {

                    if (typeof v_pattern == "object") {

                        // 调用默认的pattern字符串校验
                        v_match = v_pattern.test(v_item.value);

                    } else if (typeof v_pattern == "function") {

                        // 自定义函数的话,调用函数
                        v_match = v_pattern.call(v_item);
                    }
                }

                if (!v_match) {
                    v_errorMsg += "格式不符!";
                }
            } else {
                // html5在格式错误情况下,得到不value
                $(v_item).val("");
            }
        } catch (e) {
        } finally {

            // 错误图片
            var v_imgId = "frame_errorImg_" + $(v_item).attr("id");

            if (v_errorMsg != "") {
                $(v_item).addClass("validateError");

                if ($("#" + v_imgId).length == 0) {
                    $(v_item).after("<img id='frame_errorImg_" + $(v_item).attr("id") + "' src='" + v_contextPath
                        + "/base/images/notice.png' style='position:relative;left:-25px;' width=\"15px\">");
                } else {
                    $("#" + v_imgId).show();
                }

                $(v_item).data("frame-validateErrorMsg", v_errorMsg);
            } else {
                $(v_item).data("frame-validateErrorMsg", "");
                $("#" + v_imgId).hide();
                $(v_item).removeClass("validateError");
            }

            return v_errorMsg == "";
        }
    },

    bindTypeDic: function (v_item, v_dicType, defaultValue) {

        AE.UI.select(v_item.name, 'SSDictionaryService.find', {
            typeName: v_dicType
        }, defaultValue, {
            code: 'code',
            name: 'name'
        }, null);

        v_item.setAttribute("binded", "true");
    },

    /**
     * <pre>
     * 禁用按钮
     * </pre>
     */
    disableButton: function () {
        $("#toolbar").find(":input[type!='button'],:input[type!='submit']").each(function (i) {
            this.disabled = 'disabled';
        });
    },
    /**
     * <pre>
     * 启用按钮
     * </pre>
     */
    enableButton: function () {
        $("#toolbar").find(":input[type!='button'],:input[type!='submit']").each(function (i) {
            this.disabled = '';
        });
    },

    // 清理IE导致的iframe内存泄漏
    clearIframe: function (v_doc) {

        var v_iframs = v_doc.getElementsByTagName('IFRAME');

        if (v_iframs.length > 0) {

            for (var i = 0; i < v_iframs.length; i++) {

                AE.Form.clearIframe(v_iframs[i].contentWindow.document);

                // 清空iframe的内容
                v_iframs[i].contentWindow.document.write('');
                v_iframs[i].contentWindow.document.clear();

                v_iframs[i].src = 'about:blank';

                // 删除iframe
                v_iframs[i].parentNode.removeChild(v_iframs[i]);
            }
        }
        CollectGarbage();
    },

    exportDataPOST: function (serviceName, params) {
        var tempForm = document.createElement("form");

        var url = "invoke.do?FRAMEserviceName=" + serviceName + "&FRAMEactionMode=2&FRAMEinvokeSeqno=" + Utils.createUUID()
            + "&FRAMEparams=" + encodeURIComponent(Object.toJSON(params || {}));

        tempForm.action = url;
        // tempForm.id= 'iframe_export_form';
        tempForm.method = "post";
        tempForm.style.display = "none";
        tempForm.target = "_blank";

        document.body.appendChild(tempForm);
        tempForm.submit();

        document.body.removeChild(tempForm);
    },

    getTopForm: function () {

        var win = window;

        while (win.name != 'main' && win.parent != win) {
            win = win.parent;
        }

        return win;
    }
};