JSLoader.loadJavaScript("component/fileuploader/plupload-2.1.2/plupload.full.min.js");
JSLoader.loadJavaScript("component/fileuploader/plupload-2.1.2/i18n/zh_CN.js");
JSLoader.loadStyleSheet("component/fileuploader/fileuploader.css");

function FileUploader(v_item) {

    if ($(v_item).attr("auto") == "false") {
        return;
    }
    var v_itemId = v_item.id;
    var v_showImgs = "showImg=false";
    var v_fileType = $(v_item).attr("fileType");

    var v_fileText = $(v_item).attr("fileText") || "选择文件";

    var v_fileImgSize = $(v_item).attr("fileImgSize") || "";
    var v_fileEditSize = $(v_item).attr("fileEditSize") || "";

    if (v_fileEditSize == "") {
        var showImgs = $(v_item).attr("showImg") || false;
        if (showImgs) {
            v_showImgs = "showImg=true";
        }
    }

    if ($(v_item).attr("fileEditSize")) {
        window["frame.fileuploader.fileEditSize" + v_item.id] = $(v_item).attr("fileEditSize")
    }

    if ($(v_item).attr("fileEditCallback")) {
        window["frame.fileuploader.fileEditCallback" + v_item.id] = $(v_item).attr("fileEditCallback")
    }

    var v_fileCallback = $(v_item).attr("fileCallback");

    var v_delFileCallback = $(v_item).attr("delFileCallback");

    var v_fileSizeLimit = $(v_item).attr("fileSizeLimit") || "1024KB";

    var v_fileCntLimit = $(v_item).attr("fileCntLimit") || "";

    var v_fileMultiple = ("true" == $(v_item).attr("fileMultiple"));

    var v_fileShowList = ("false" != $(v_item).attr("fileShowList"));

    var v_fileShowQueue = ("false" != $(v_item).attr("fileShowQueue"));

    if (v_fileShowQueue) {
        $("#" + v_itemId).after("<div id='" + v_itemId + "_queue'></div>");
    }

    if (v_fileShowList) {
        $("#" + v_item.id).before('<div id="' + v_itemId
            + '_img_queue" style="width:100%;height:100%;"></div><div style=\"clear:both;\"></div>');
    }

    $(v_item).hide();
    $(v_item).attr("id", v_itemId + "frame_hide");

    if ($(v_item).attr("required")) {
        $("#" + v_itemId).attr("required", true);
    }

    $(v_item).after(("<a class='plupload' id=\"" + v_itemId + "\" href=\"javascript:;\">" + v_fileText + "</a><div id='" + v_itemId + "_process' style='display: inline;'></div>"));

    $(v_item).remove();

    var v_filters = {
        max_file_size: v_fileSizeLimit
    };

    if (v_fileType && v_fileType != "") {
        v_filters.mime_types = [{
            title: "自定义文件",
            extensions: v_fileType.replace(/(\*\.)/g, "")
        }];
    }

    try {

        var uploader = new plupload.Uploader({
            runtimes: 'html5,flash,silverlight,html4',
            browse_button: v_itemId,
            // container : document.getElementById('xxxxx_container'),
            url: 'uploadFile.do?paramName=file&fileImgSize=' + v_fileImgSize,
            flash_swf_url: v_contextPath + '/component/fileuploader/plupload-2.1.2/Moxie.swf',
            silverlight_xap_url: v_contextPath + '/component/fileuploader/plupload-2.1.2/Moxie.xap',
            multi_selection: v_fileMultiple,
            filters: v_filters,
            init: {
                PostInit: function () {
                    // document.getElementById('filelist').innerHTML =
                    // '';
                    //
                    // document.getElementById('uploadfiles').onclick =
                    // function() {
                    // uploader.start();
                    // return false;
                    // };
                },

                FilesAdded: function (up, files) {

                    // 判断文件个数限制
                    var v_fileCnt = $("#" + v_itemId).data("frame-fileuploader-fileCnt");
                    v_fileCnt = v_fileCnt ? v_fileCnt + files.length : files.length;

                    if (v_fileCntLimit != "" && parseInt(v_fileCntLimit) < v_fileCnt) {
                        if (window.top.$("#frame-tips").length == 0) {
                            //微信上传附件
                            alert("文件上传数量超过限制:" + v_fileCntLimit + "个数!");
                        } else {
                            //web上传附件
                            AE.UI.alert("文件上传数量超过限制:" + v_fileCntLimit + "个数!");
                        }
                        uploader.splice(0, uploader.files.length);
                        return false;
                    }

                    for (var i = 0; i < files.length; i++) {

                        // mac的safari中取不到file.type,当时可以取到file.name
                        var v_type = files[i].name.toLowerCase()
                            .substring(files[i].name.lastIndexOf(".") + 1, files[i].name.length);

                        // 文件类型检查
                        if (v_fileType && v_fileType != "" && v_fileType.indexOf(v_type) == -1) {
                            AE.UI.alert("文件类型:" + v_type + "不符合规范，请重新上传");
                            uploader.splice(0, uploader.files.length);
                            return false;
                        }
                    }

                    // 增加文件个数[队列中照算]
                    $("#" + v_itemId).data("frame-fileuploader-fileCnt", v_fileCnt);
                    uploader.start();
                },

                UploadProgress: function (up, file) {
                    $("#" + v_itemId + "_process").html("&nbsp;&nbsp;" + file.name + "&nbsp;已上传:" + file.percent + "%");
                },

                FileUploaded: function (up, file, info) {
                    var v_fileId = eval("[" + info.response + "]")[0].fileIdList[0];

                    // 执行成功回调函数
                    if (v_fileCallback != "" && window[v_fileCallback]) {
                        window[v_fileCallback].call(window, v_fileId);
                    }
                    console.log(v_fileMultiple);
                    // 设置val,单文件上传不叠加
                    if (v_fileMultiple) {
                        $("#" + v_itemId).val($("#" + v_itemId).val() + "," + v_fileId + ",");
                    } else {
                        $("#" + v_itemId).val(v_fileId);
                        /** 这边需要处理如果单个上传需要覆盖前面的图片 */
                        $("#" + v_itemId + "_img_queue div").hide();
                        $("#file_img_" + v_fileId).show();
                    }
                    // val方法中会+1，所以此处-1
                    $("#" + v_itemId).data("frame-fileuploader-fileCnt", $("#" + v_itemId).data("frame-fileuploader-fileCnt") - 1);
                    // 修正第一次，预览界面出现后，导致隐藏的div.top没有算对，遮住删除icon的bug
                    try {
                        this.refresh();
                    } catch (e) {
                    }
                },
                Error: function (up, err) {
                    alert(err.message);

                    // TODO // 经测试，文件选择异常，上传中取消，上传异常都会触发此事件
                    $("#" + v_itemId).data("frame-fileuploader-fileCnt", $("#" + v_itemId).data("frame-fileuploader-fileCnt") - 1);
                }
            }
        });

        uploader.init();
    } catch (e) {
    }
    $.valHooks.a = {
        get: function (v_item) {
            // 设置uploadify的取值,去除无用的文件
            if ($(v_item).attr("class") && $(v_item).attr("class") == "plupload") {
                var v_result = $(v_item).attr('fileIds');
                if (v_result && v_result.endWith(",")) {
                    v_result = v_result.substring(0, v_result.length - 1);
                    $(v_item).attr('fileIds', v_result);
                }
                return $(v_item).attr('fileIds');
            } else {
                // 默认
                return $(v_item).attr('value');
            }
        },

        set: function (v_item, v_value) {

            // 设置plupload的取值,去除无用的文件
            if (v_value != "" && $(v_item).attr("class") && $(v_item).attr("class") == "plupload") {

                // TODO 忘了这段代码
                // if (!$(v_item).uploadify('settings', 'multi')) {
                // if ($("#" + v_item.id + "_img_queue").length > 0) {
                // $("#" + v_item.id + "_img_queue").html("");
                // }
                // }

                // 找到对应的文件信息
                AE.ServiceEx("FileSupportService.getInfoByIds", {
                    ids: v_value
                }, function (v_data) {
                    v_value = "";
                    for (var i = 0; i < v_data.length; i++) {
                        // 存在
                        if (v_data[i].dataInfo) {
                            var v_enableEdit = window["frame.fileuploader.fileEditSize" + v_item.id] ? " enableEdit="
                                + window["frame.fileuploader.fileEditSize" + v_item.id] + " alt='点击图片进行裁剪' fileuploadItemId='"
                                + v_item.id + "' " : "";

                            var v_editCallback = window["frame.fileuploader.fileEditCallback" + v_item.id] ? " editImgCallback='"
                                + window["frame.fileuploader.fileEditCallback" + v_item.id] + "' " : "";

                            var v_imgSrc = "downloadFile.do?fileImgSize=80x80&id=" + v_data[i].dataInfo.id;

                            var t_fileName = v_data[i].dataInfo.fileName.toLowerCase().toString();
                            if (!(t_fileName.endWith('.jpg') || t_fileName.endWith('.png'))) {
                                v_imgSrc = "base/images/moren.png";
                            }

                            if ($("#file_img_" + v_data[i].dataInfo.id).length == 0 && $("#" + v_item.id + "_img_queue").length > 0) {
                                $("#" + v_item.id + "_img_queue").append("<div id='file_img_" + v_data[i].dataInfo.id + "' class='uploadify-img-queue-div' style='width:100%;'><img " + v_showImgs + " src='" + v_imgSrc + "' " + v_enableEdit + v_editCallback + " style='width:80px;width:80px;'/><a href='#' style='left:58px;' onclick='FileUploader.remove_file(\"" + v_data[i].dataInfo.id + "\",\"" + v_item.id + "\", \"" + v_delFileCallback + "\");'></a><span>" + v_data[i].dataInfo.fileName + "</span></div>");
                                // 文件个数限制
                                $(v_item).data("frame-fileuploader-fileCnt",
                                    ($(v_item).data("frame-fileuploader-fileCnt") || 0) + 1);
                            }
                            // 只存放存在的文件
                            v_value += v_data[i].dataInfo.id + ",";
                        }
                    }
                    // 触发重新设置页面高度事件
                    if (window.f_event_resize) {
                        window.f_event_resize.call(window);
                    }
                }, {
                    unShowWait: true,
                    sessionCheck: false
                });
                if (v_value.endWith(",")) {
                    v_value = v_value.substring(0, v_value.length - 1);
                }
                $(v_item).attr("fileIds", v_value);
            } else {
                $(v_item).attr("value", v_value);
            }
        }
    };

    v_item.setAttribute("bindType", true);
}

// 不真正删除图片
FileUploader.remove_file = function (v_fileId, v_itemId, v_delFileCallback) {
    // 移除此元素
    $("#file_img_" + v_fileId).remove();
    // 移除值
    $("#" + v_itemId).attr("fileIds", $("#" + v_itemId).val().replace(v_fileId + ",", ""));
    // 移除值(防止该值在最后，没有“，”号，重新删除一次)
    $("#" + v_itemId).attr("fileIds", $("#" + v_itemId).val().replace(v_fileId, ""));
    // 上传限制数-1
    $("#" + v_itemId).data("frame-fileuploader-fileCnt", $("#" + v_itemId).data("frame-fileuploader-fileCnt") - 1);
    // 调用删除回调
    if (v_delFileCallback != "" && window[v_delFileCallback]) {
        window[v_delFileCallback].call(window, v_fileId);
    }
}
