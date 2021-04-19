/*---------------------------------------------------------------------------*\
|  Subject:       AppCore UI Class                                              |
|  Version:       1.0.0                                                      |
|  FileName:      appCore.ui.js                                                 |
|  Created:       2011-05                                                    |
|  Comment:       核心界面UI风格封装,此类暂时依赖于appCore $uery                            |
\*---------------------------------------------------------------------------*/

var frame_xwin;

AE.UI = {

    // 弹出框-release
    alert: function (msg, v_function) {
        AE.UI.showTips(1, msg);
    },
    confirm: function (msg, v_Ycallback, v_Ncallback) {
        AE.UI.showTips(2, msg, v_Ycallback, v_Ncallback);
    },

    promptTwo: function (msg, content, callback) {
        msg == "" ? "提醒输入框" : msg;
        easyDialog.open({
            container: {
                header: msg,
                content: content,
                yesFn: callback
            },
            drag: true
        });
        $(".easyDialog_wrapper").css("min-width", "200px");
        $(".easyDialog_wrapper").css("min-height", "120px");
    },

    prompt: function (msg, callback) {
        msg == "" ? "提醒输入框" : msg;
        easyDialog.open({
            container: {
                header: msg,
                content: '<textarea id="aePrompt" type="text"></textarea><br/>',
                yesFn: callback
            },
            drag: true
        });
        $(".easyDialog_wrapper").css("min-width", "200px");
        $(".easyDialog_wrapper").css("min-height", "120px");
    },

    /**
     * 绑定My97DatePicker日期组件
     *
     * @param id
     * @param format
     * @param defaultValue
     */
    my97datepicker: function (id, format, defaultValue) {

        var v_item;

        if (typeof (id) == "object") {
            v_item = id;
        } else if (typeof (id) == "string" && $("#" + id)) {
            v_item = $("#" + id);
        } else {
            alert("没有找到id为:" + id + "的对象!");
        }

        // 卸载原有的onchange事件
        var v_onchange = this.onchange;
        this.onchange = null;

        var v_opt = {
            dateFmt: format,
            readOnly: true,
            onpicked: function () {

                // 如果存在onchange事件,则先触发onchange事件
                if (v_onchange) {
                    v_onchange.call(this);
                }

                // 如果获得焦点,因为和validate中的事件先后顺序有关，会导致先触发validate再修改值（validate可以考虑使用onchange后触发检查事件）
                $(this).blur();
            }
        };

        v_item.setAttribute("dataType", "date");
        v_item.setAttribute("dataFormat", format);
        // v_item.setAttribute("class", "Wdate");
        v_item.onfocus = function () {
            WdatePicker(v_opt);
        };

        AE.event(v_item, 'keydown', function (event) {

            try {
                $dp.hide();
            } catch (e) {
            }

            // tab丢失焦点问题
            // 模拟回车事件
            // if (event.keyCode == 9) {
            // return;
            // try {
            // var v_nextItem =
            // AE.Form.getNextFocusItem(event.srcElement, 1);
            //
            // if (v_nextItem) {
            // v_nextItem.focus();
            // return false;
            // }
            // } catch (e) {
            // }
            // }
            return true;
        });

        // v_item.onblur = function() {
        //
        // return;
        // var dataValue = this.value.trim();
        // if (dataValue == '') {
        // return true;
        // }
        //
        // var aereg =
        // /^(?:(?!0000)[0-9]{4}[\.](?:(?:0[1-9]|1[0-2])[\.](?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])[\.](?:29|30)|(?:0[13578]|1[02])[\.]31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)[\.]02[\.]29)\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/;
        // var aereg_time =
        // /^(?:(?!0000)[0-9]{4}[\.](?:(?:0[1-9]|1[0-2])[\.](?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])[\.](?:29|30)|(?:0[13578]|1[02])[\.]31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)[\.]02[\.]29)\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/;
        // if (this.value.match(aereg) || this.value.match(aereg_time)) {
        // return true;
        // }
        //
        // /*
        // * var aereg =
        // *
        // /^((^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(10|12|0?[13578])([-\/\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(11|0?[469])([-\/\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(0?2)([-\/\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([3579][26]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][13579][26])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][13579][26])([-\/\._])(0?2)([-\/\._])(29)$))$/;
        // * if (aereg.test(this.value)) { return true; } aereg =
        // * /^[12]\d{3}[\.](0[1-9])|1[0-2]$/; if (aereg.test(this.value)) {
        // * return true; }
        // */
        // var ereg =
        // /^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)$/;
        // var ereg1 = /^[12]\d{3}(0[1-9])|1[0-2]$/;
        // var ereg2 = /^[12]\d{3}$/;
        // // alert(this.value);
        // if (ereg.test(this.value)) {
        // var yyyy = this.value.substr(0, 4);
        // var MM = this.value.substr(4, 2);
        // var dd = this.value.substr(6, 2);
        // this.value = yyyy + '.' + MM + '.' + dd;
        // return true;
        // } else if (ereg1.test(this.value)) {
        // var yyyy = this.value.substr(0, 4);
        // var MM = this.value.substr(4, 2);
        // this.value = yyyy + '.' + MM;
        // } else if (ereg2.test(this.value)) {
        // /*
        // * var yyyy = this.value.substr(0, 4); //var MM =
        // * this.value.substr(4, 2); this.value = yyyy;
        // */
        // } else {
        // // alert("日期格式有误！");
        // // this.value = '';
        // // this.focus();
        // // this.select();
        // // return true;
        // }
        // };
        if (defaultValue) {
            v_item.value = defaultValue;
        }
    },

    /**
     * <pre>
     * 绑定select下拉框
     * </pre>
     *
     * @param selectId
     *            下拉框id
     * @param service
     *            下拉框填充的数据,或者请求的服务名
     * @param v_param
     *            传入参数
     * @param defaultValue
     *            下拉框默认选择的数据
     * @param v_map
     *            下拉框name,code与请求的数据集的对应关系 {name:'name',code:'code'}
     * @param v_blankValue
     *            空白的值,默认为{"code" : "","name" : "--请选择--"}
     *
     */
    select: function (selectId, service, v_param, defaultValue, v_map, v_blankValue) {
        if (!$("#" + selectId)) {
            AE.UI.alert("页面中没有此对象:" + selectId);
        }

        var f_callback = function (selectId, v_data, defaultValue, v_map, v_blankValue) {

            if (typeof v_blankValue == "undefined") {
                v_blankValue = {
                    code: "",
                    name: "--请选择--"
                };
            }

            Utils.select.removeAllOptions(selectId);

            if (v_blankValue) {
                Utils.select.addOptions(selectId, [v_blankValue], "code", "name");
            }

            if (v_data && v_data.length > 0) {
                v_codeMap = "code";
                v_nameMap = "name";
                if (v_map) {
                    v_codeMap = v_map.code;
                    v_nameMap = v_map.name;
                }

                Utils.select.addOptions(selectId, v_data, v_codeMap, v_nameMap);
                Utils.select.setValue(selectId, defaultValue);
            }
        };

        // 如果传入服务名,则请求服务得到数据
        if (typeof (service) == "string") {
            AE.ServiceEx(service, v_param, function (result) {
                f_callback(selectId, result, defaultValue, v_map, v_blankValue);
            });
        } else {
            f_callback(selectId, service, defaultValue, v_map, v_blankValue);
        }
    },

    /**
     * 弹出窗口
     *
     * @param 页面路径
     * @param 标题
     * @param 传入参数
     * @param 扩展属性
     */
    xWin: function (url, v_title, v_getParam, v_options) {

        window.top["frame_win_zIndex"] = (window.top["frame_win_zIndex"] ? window.top["frame_win_zIndex"] + 1 : 1976);

        // 如果设置内容,则直接使用内容
        var url_ = url;
        if (!v_options || !v_options["content"]) {

            if (url.indexOf(".do") == -1) {
                url_ = "action.do?path=" + url;
                if (v_getParam) {
                    if (typeof v_getParam == "string") {
                        if (v_getParam != "") {
                            url_ += "&" + v_getParam;
                        }
                    } else {
                        for (var v_attr in v_getParam) {
                            url_ += "&" + v_attr + "=" + encodeURI(v_getParam[v_attr]);
                        }
                    }
                }
            }
            url_ = url_ + "&appid=" + window.top["frame_appid"];
        }

        if (!window.top["frame_xwin"]) {
            window.top["frame_xwin"] = [];
        }

        window.top["frame_xwin"][window.top["frame_xwin"].length] = $.layer({
            type: 2,
            title: v_title || "弹出框",
            shadeClose: true,
            maxmin: true,
            fix: true,
            shadeClose: false,
            offset: ['100px', ''],
            area: [v_options.width, v_options.height],
            zIndex: window.top["frame_win_zIndex"],
            iframe: {
                src: url_,
                scrolling: 'no'
            },
            // loading : {
            // type : 0
            // },
            close: function () {
                try {
                    // 如果图片展示或图片编辑打开（window.top），则关闭
                    if (window.top.$("#img-show").length > 0) {
                        window.top.$("#img-show").hide();
                    }
                    if (window.top.$("#img-edit").length > 0) {
                        window.top.$("#img-edit").hide();
                    }

                    // 手动移除iframe对象
                    if (this.iframe) {

                        try {
                            if (this.content.document && this.content.document.body) {
                                $(this.content.document.body).html("");
                            }
                        } catch (e) {
                        }

                        $(this.iframe).attr("src", "about:blank");
                        $(this.iframe).remove();
                    }
                } catch (e) {
                } finally {

                    // 移除框架对象
                    // 用JS的array.remove会报异常:js 不能执行已释放的Script代码
                    if (window.top["frame_xwin"].length - 1 >= 0) {
                        window.top["frame_xwin"].length = window.top["frame_xwin"].length - 1;
                    }

                    // 关闭tips
                    if (window.top["frame_req_counter"] > 0) {
                        AE.UI.hidewaitform();
                    }
                }
            }
        });
        if (v_options.full) {
            jq(".xubox_max").click();
        }

    },

    /**
     * <pre>
     * 获得弹出窗口iframe中的文档
     * </pre>
     */
    getxWinParent: function () {
        try {
            return parent;
            // var v_xwin =
            // window.top["frame_xwin"][window.top["frame_xwin"].length - 1];
            // return v_xwin.opener;
        } catch (e) {
            // alert("无法得到弹出框对象!" + e);
            // return null;
        }
    },

    /**
     * 关闭弹出窗口
     */
    closexWin: function () {
        if (window.top["frame_xwin"]) {
            var v_index = window.top["frame_xwin"].length - 1;
            var v_xwin = window.top["frame_xwin"][v_index];

            // 不使用异步的话,jquery 1.7.3的ajax中会报异常：String未定义
            setTimeout(function () {
                parent.layer.close(v_xwin);
            }, 10);
        }
    },

    /**
     * 功能描述： 打开提示界面 输入参数： s-自定义提示 返 回 值： null
     */
    showwaitform: function (s) {

        try {

            // 请求处理计数器
            window.top["frame_req_counter"] = window.top["frame_req_counter"] ? window.top["frame_req_counter"] + 1 : 1;

            window.top.$("#ajax-loading").show();

            // 最长显示5秒后就失效，延迟5秒后计数器清零
            window.top.$("#ajax-loading").delay(5000).hide(1);
            setTimeout(function () {
                window.top["frame_req_counter"] = 0;
            }, 5000);
        } catch (ex) {
        }
    },

    /**
     * 功能描述： 关闭提示界面 输入参数： null 返 回 值： null
     */
    hidewaitform: function () {

        try {
            window.top["frame_req_counter"] = window.top["frame_req_counter"] <= 0 ? 0 : (window.top["frame_req_counter"] - 1);

            if (window.top["frame_req_counter"] == 0) {
                window.top.$("#ajax-loading").hide();
            }
        } catch (ex) {
        }
    },

    tree: function (containerId, v_url, v_options) {
        return new Tree(containerId, v_url, v_options);
    },

    map: function (v_options) {
        AE.UI.xWin("map/map", "地图功能", {
            callback: v_options.callback,
            lng: v_options.lng,
            lat: v_options.lat
        }, {
            width: "600px",
            height: "500px"
        });
    },
    htmlEditor: function (v_id, v_options) {
        return new HtmlEditor(v_id, v_options);
    },
    file: function (v_item) {
        return new FileUploader(v_item);
    },
    // v_type 0:error,1:alert-success默认3秒关闭,2:confirm,3:alert默认不关闭
    showTips: function (v_type, v_info, v_Ycallback, v_Ncallback) {

        if (window.top.$("#frame-tips").length == 0) {
            alert("没有找到框架页面的提醒区!");
            return;
        }

        window.top.$("#frame-tips").show();

        // 设置信息随机号
        var v_tipsId = Utils.createUUID();
        window.top.$("#frame-tips-content").data("frame-tipsId", v_tipsId);

        if (v_type == 0) {
            window.top.$("#frame-tips").addClass("error");
            window.top.$("#frame-tips-img").attr("src", "base/images/icon_03.png");
            window.top.$("#frame-tips-content").html("<label>" + v_info + "</label>");
        } else if (v_type == 1) {
            window.top.$("#frame-tips").addClass("remind");
            window.top.$("#frame-tips-img").attr("src", "base/images/icon_01.png");
            window.top.$("#frame-tips-content").html("<label>" + v_info + "</label>");

            if (window.top.f_hideTips) {
                window.top.f_hideTips(v_tipsId);
            }
        } else if (v_type == 2) {

            window.top.$("#frame-tips").addClass("remind");
            window.top.$("#frame-tips-img").attr("src", "base/images/icon_03.png");
            window.top
                .$("#frame-tips-content")
                .html("<label>"
                    + v_info
                    + "</label><a href=\"#\" class=\"choice_yes\" id='frame-tips-confirm-y'>是</a><a href=\"#\" class=\"choice_no\" id='frame-tips-confirm-n' >否</a>");

            var v_loaderWin = window;
            window.top.$("#frame-tips-confirm-y").bind("click", function () {
                try {
                    if (v_Ycallback) {
                        v_Ycallback.call(v_loaderWin);
                    }
                } finally {
                    AE.UI.hideTips(v_tipsId);
                }
            });
            window.top.$("#frame-tips-confirm-n").bind("click", function () {
                try {
                    if (v_Ncallback) {
                        v_Ncallback.call(v_loaderWin);
                    }
                } finally {
                    AE.UI.hideTips(v_tipsId);
                }
            });
        }

        return v_tipsId;
    },
    hideTips: function (v_tipsId) {

        if (!v_tipsId || window.top.$("#frame-tips-content").data("frame-tipsId") == v_tipsId) {

            window.top.$("#frame-tips").removeClass();
            window.top.$("#frame-tips").hide();

            // 清除绑定事件
            if (window.top.$("#frame-tips-confirm-y").length > 1) {
                window.top.$("#frame-tips-confirm-y").unbind("click");
            }
            if (window.top.$("#frame-tips-confirm-n").length > 1) {
                window.top.$("#frame-tips-confirm-n").unbind("click");
            }

            // 清除内容
            window.top.$("#frame-tips-content").html("");
        }
    },
    // 图片设置enableEdit='200|300',
    imgEdit: function (v_item) {
        if ($(v_item).attr("src")) {
            // 计算图片是否较大，是否需要压缩显示，如果浏览器对图片做缓存，不会触发此事件
            window.top.$("#img-edit-src").one("load", function () {

                // 去除等比例缩小样式，不然图片高度，宽度不准确
                window.top.$("#img-edit-src").css("width", "");
                window.top.$("#img-edit-src").css("height", "");

                var v_maxWinHeight = $(window.top).height() * 0.75;
                var v_maxWinWidth = $(window.top).width() - 100;

                var v_imgHeight = window.top.$("#img-edit-src").height();
                var v_imgWidth = window.top.$("#img-edit-src").width();

                // 进行等比例缩小，设置样式
                if (v_maxWinHeight < v_imgHeight || v_maxWinWidth < v_imgWidth) {
                    window.top["frame.img.editer.param.scale"] = Math.min(v_maxWinHeight / v_imgHeight, v_maxWinWidth / v_imgWidth)
                        .toFixed(2);

                    window.top.$("#img-edit-src").css("width", v_imgWidth * window.top["frame.img.editer.param.scale"]);
                    window.top.$("#img-edit-src").css("height", v_imgHeight * window.top["frame.img.editer.param.scale"]);
                }

                // 设置图片下文字宽度与图片一致
                window.top.$('.img-edit-text').css('width', window.top.$('#img-edit-src').width());
            });

            // 去掉压缩属性
            window.top.$("#img-edit-src").attr("src", $(v_item).attr("src").replace("fileImgSize", "remove-fileImgSize"));
            window.top.$("#img-edit").show();

            // 去除缓存数据
            window.top["frame.img.editer.param.x1"] = null;
            window.top["frame.img.editer.param.y1"] = null;
            window.top["frame.img.editer.param.width"] = null;
            window.top["frame.img.editer.param.height"] = null;
            window.top["frame.img.editer.param.scale"] = 1;

            if (!$(v_item).attr("enableEdit")) {
                alert("请在image中输入enableEdit参数,格式:enableEdit=100x200");
            }

            var v_aspectRatio = $(v_item).attr("enableEdit").toLowerCase().replace("|", "x").split("x");

            var v_loader = v_item;
            // 点击裁剪后触发裁剪事件,同时修改源图片的src
            window.top.$("#img-edit-btn").one("click", function () {
                try {
                    // 进行裁剪过,且裁剪的宽高>0
                    if (window.top["frame.img.editer.param.x1"] != null && window.top["frame.img.editer.param.width"] != null && window.top["frame.img.editer.param.width"] > 0 && window.top["frame.img.editer.param.width"] > 0) {
                        var v_imgSrc = $(v_loader).attr("src");
                        var v_startIdIndex = v_imgSrc.indexOf("id=");
                        var v_endIdIndex = v_imgSrc.indexOf("&", v_startIdIndex);
                        v_endIdIndex = (v_endIdIndex == -1) ? v_imgSrc.length : v_endIdIndex;
                        var v_fileId = v_imgSrc.substring(v_startIdIndex + 3, v_endIdIndex);
                        // 找到图片文件id，进行裁剪
                        AE.ServiceEx('ImageService.cut', {
                            'id': v_fileId,
                            'x': window.top["frame.img.editer.param.x1"],
                            'y': window.top["frame.img.editer.param.y1"],
                            'width': window.top["frame.img.editer.param.width"],
                            'height': window.top["frame.img.editer.param.height"],
                            'scale': window.top["frame.img.editer.param.scale"]
                        }, function (v_data) {
                            // 重新刷新图片
                            var inputId = $(v_loader).attr("fileuploadItemId");
                            $("#file_img_" + v_fileId + " a").attr("onclick", "FileUploader.remove_file(\"" + v_data + "\",\"" + inputId + "\", 'undefined');");
                            $(v_loader).attr("src", $(v_loader).attr("src").replace(v_fileId, v_data));
                            $("#file_img_" + v_fileId).attr("id", "file_img_" + v_data);

                            // 如果是文件上传组件，则修改上传组件内的文件信息
                            if ($(v_loader).attr("fileuploadItemId") && $("#" + $(v_loader).attr("fileuploadItemId"))) {
                                var v_uploadifyItem = $("#" + $(v_loader).attr("fileuploadItemId"));
                                $(v_uploadifyItem).attr("fileIds", $(v_uploadifyItem).attr("fileIds").replace(v_fileId, v_data));
                            }
                            // 如果需要回调函数,则调用回调函数
                            if ($(v_loader).attr("editImgCallback")) {
                                window[$(v_loader).attr("editImgCallback")].call(window, v_data);
                            }
                        });
                    }
                } finally {
                    if (window.top["f_hideImgEdit"]) {
                        window.top["f_hideImgEdit"].call(window.top);
                    }
                }
            });

            // 显示裁剪文字
            window.top.$("#img-edit-alert").html("最佳裁剪图片尺寸：" + v_aspectRatio[0] + "*" + v_aspectRatio[1]);

            // 设置裁剪控件
            window.top["frame.img.editer"].setOptions({
                aspectRatio: v_aspectRatio[0] + ":" + v_aspectRatio[1],
                onSelectEnd: function (img, selection) {
                    // 有可能选择出-1
                    window.top["frame.img.editer.param.x1"] = selection.x1 < 0 ? 0 : selection.x1;
                    window.top["frame.img.editer.param.y1"] = selection.y1 < 0 ? 0 : selection.y1;
                    window.top["frame.img.editer.param.width"] = selection.width;
                    window.top["frame.img.editer.param.height"] = selection.height;

                    var v_realWidth = (selection.width / window.top["frame.img.editer.param.scale"]).toFixed(0);
                    var v_realHeight = (selection.height / window.top["frame.img.editer.param.scale"]).toFixed(0);

                    if (parseInt(v_aspectRatio[0]) > v_realWidth || parseInt(v_aspectRatio[1]) > v_realHeight) {
                        window.top.$("#img-edit-alert").html("<font color='red'>裁剪后尺寸：" + v_realWidth + "*" + v_realHeight
                            + "；小于最佳裁剪尺寸：" + v_aspectRatio[0] + "*" + v_aspectRatio[1] + "，显示图片将会模糊，请注意！</font>");
                    } else {
                        window.top.$("#img-edit-alert").html("最佳裁剪图片尺寸：" + v_aspectRatio[0] + "*" + v_aspectRatio[1] + "；裁剪后尺寸："
                            + v_realWidth + "*" + v_realHeight);
                    }
                }
            });
        }
    },
    popBalloon: function (v_msg, v_href, v_title) {

        v_title = v_title ? v_title : "信息提醒";

        if (window.top.$("#frame-balloons").length == 0) {
            alert("没有找到框架页面的pop区!");
            return;
        }

        // 设置pop随机号
        var v_balloonId = Utils.createUUID();

        window.top
            .$("#frame-balloons")
            .append("<div class=\"balloon\" id=\"frame_pop_"
                + v_balloonId
                + "\"><div class=\"title\"><h1>"
                + v_title
                + "</h1><a href=\"#\" onclick=\"window.top.$('#frame_pop_"
                + v_balloonId
                + "').remove();\"></a></div>"
                + "<table cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><td width=\"95px\"><img src=\"base/images/balloon_l_01.jpg\" alt=\"\"></td>"
                + "<td><a href=\"#\" onclick=\"window.top.$('#main_iframe').attr('src', '" + v_href
                + "');window.top.$('#frame_pop_" + v_balloonId + "').remove();\" >" + v_msg
                + "</a></td></tr></tbody></table></div>");

    },
    addNotify: function (v_cnt) {

        v_cnt = v_cnt ? v_cnt : 1;

        if (window.top.$("a.on[name='menu_03_a']").length == 1) {

            // 自身加消息数
            var v_currMenu3 = window.top.$("a.on[name='menu_03_a']");
            var v_currMenuNext3 = v_currMenu3.next();
            if (v_currMenuNext3.attr("name") == "notifyLabel") {
                v_currMenuNext3.html(Number(v_currMenuNext3.html()) + Number(v_cnt));
            } else {
                v_currMenu3.after("<label style=\"display:block;\" name=\"notifyLabel\">" + v_cnt + "</label>");
            }

            // 找到menu_02_a
            var v_currMenu2 = v_currMenu3.parents("[name='menu_02_li']").children("[name='menu_02_a']");
            if (v_currMenu2.length == 1) {

                var v_currMenuNext2 = v_currMenu2.next();
                if (v_currMenuNext2.attr("name") == "notifyLabel") {
                    v_currMenuNext2.html(Number(v_currMenuNext2.html()) + Number(v_cnt));
                } else {
                    v_currMenu2.after("<label style=\"display:block;\" name=\"notifyLabel\">" + v_cnt + "</label>");
                }
            }

            // 找到menu01[menu_02_parent_0101->0101]
            var v_currMenu1 = window.top.$("#menu01_" + v_currMenu3.parents("[name='menu_02_parent']").attr("id").substring(15));

            if (v_currMenu1.length == 1) {

                var v_currMenuNext1 = v_currMenu1.children("[name='notifyLabel']");
                if (v_currMenuNext1.length == 1) {
                    v_currMenuNext1.html(Number(v_currMenuNext1.html()) + Number(v_cnt));
                } else {
                    v_currMenu1.append("<label style=\"display:block;\" name=\"notifyLabel\">" + v_cnt + "</label>");
                }
            }
        }

    },
    clearNotify: function () {

        if (window.top.$("a.on[name='menu_03_a']").length == 1) {

            var v_clearCnt = 0;

            // 自身清空消息数
            var v_currMenu3 = window.top.$("a.on[name='menu_03_a']");
            var v_currMenuNext3 = v_currMenu3.next();

            if (v_currMenuNext3.attr("name") == "notifyLabel") {
                v_clearCnt = Number(v_currMenuNext3.html());
                v_currMenuNext3.remove();
            }

            // 找到menu_02_a
            var v_currMenu2 = v_currMenu3.parents("[name='menu_02_li']").children("[name='menu_02_a']");
            if (v_currMenu2.length == 1) {

                var v_currMenuNext2 = v_currMenu2.next();
                if (v_currMenuNext2.attr("name") == "notifyLabel") {

                    if (Number(v_currMenuNext2.html()) - v_clearCnt > 0) {
                        v_currMenuNext2.html(Number(v_currMenuNext2.html()) - v_clearCnt);
                    } else {
                        v_currMenuNext2.remove();
                    }
                }
            }

            // 找到menu01[menu_02_parent_0101->0101]
            var v_currMenu1 = window.top.$("#menu01_" + v_currMenu3.parents("[name='menu_02_parent']").attr("id").substring(15));

            if (v_currMenu1.length == 1) {

                var v_currMenuNext1 = v_currMenu1.children("[name='notifyLabel']");

                if (Number(v_currMenuNext1.html()) - v_clearCnt > 0) {
                    v_currMenuNext1.html(Number(v_currMenuNext1.html()) - v_clearCnt);
                } else {
                    v_currMenuNext1.remove();
                }
            }

        }
    }
};
