function FileUploader(v_item) {

    if ($(v_item).attr("auto") == "false") {
        return;
    }

    var v_itemId = v_item.id;

    var v_fileType = $(v_item).attr("fileType");

    var v_fileImgSize = $(v_item).attr("fileImgSize") || "";

    var v_fileEditSize = $(v_item).attr("fileEditSize") || "";

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

    try {

        $(v_item).uploadify({
            'queueID': v_item.id + '_queue',
            swf: 'component/fileuploader/uploadify/uploadify.swf',
            uploader: 'uploadFile.do?fileImgSize=' + v_fileImgSize,
            buttonText: "<div class=\"uploadify_button_defined\"><a href=\"javascript:void(0);\">" + $(v_item).attr("fileText")
                + "</a></div>",
            fileSizeLimit: v_fileSizeLimit,
            'removeCompleted': false,
            itemTemplate: '<div id="${fileID}" class="uploadify-queue-item">\
                    <div class="cancel"><a id="${fileID}_cancelBtn" href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\')">X</a></div>\
                    <span class="fileName">${fileName} (${fileSize})</span><span class="data"></span></div>',
            multi: v_fileMultiple,
            onSelect: function (file) {

                // ??????????????????
                var v_fileCnt = $("#" + v_itemId).data("frame-fileuploader-fileCnt");
                v_fileCnt = v_fileCnt ? v_fileCnt + 1 : 1;

                // ??????????????????[???????????????]
                $("#" + v_itemId).data("frame-fileuploader-fileCnt", v_fileCnt);

                if (v_fileCntLimit != "" && parseInt(v_fileCntLimit) < v_fileCnt) {
                    AE.UI.alert("??????????????????????????????:" + v_fileCntLimit + "??????!");
                    this.cancelUpload(file.id);
                    $("#" + v_itemId).uploadify('cancel', file.id);
                    return false;
                }

                // mac???safari????????????file.type,??????????????????file.name
                var v_type = file.type ? file.type.toLowerCase() : file.name.substring(file.name.lastIndexOf("."), file.name.length);

                // TODO
                // ??????????????????
                if (v_fileType && v_fileType != "") {

                    var v_fileTypeArray = v_fileType.split(",");
                    for (var i = 0; i < v_fileTypeArray.length; i++) {
                        if (("*" + v_type) == v_fileTypeArray[i]) {
                            return true;
                        }
                    }
                    AE.UI.alert("????????????:" + v_type + "?????????????????????????????????");
                    this.cancelUpload(file.id);
                    $("#" + v_itemId).uploadify('cancel', file.id);
                    return false;
                }
            },
            onUploadSuccess: function (file, data, response) {

                var v_fileId = eval("[" + data + "]")[0].fileIdList[0];

                if (v_fileCallback != "" && window[v_fileCallback]) {
                    window[v_fileCallback].call(window, v_fileId);
                }

                // val????????????+1???????????????-1
                $("#" + v_itemId).data("frame-fileuploader-fileCnt", $("#" + v_itemId).data("frame-fileuploader-fileCnt") - 1);

                // ??????val,????????????????????????
                if (v_fileMultiple) {
                    $("#" + v_itemId).val($("#" + v_itemId).val() + "," + v_fileId + ",");
                } else {
                    $("#" + v_itemId).val(v_fileId);
                }

                // ??????????????????
                $("#" + v_itemId).uploadify('cancel', file.id);
                $("#" + file.id).remove();
            },
            onUploadError: function (file, errorCode, errorMsg, errorString) {
                // ????????????????????????????????????????????????????????????????????????????????????
                $("#" + v_itemId).data("frame-fileuploader-fileCnt", $("#" + v_itemId).data("frame-fileuploader-fileCnt") - 1);
            }
        });
    } catch (e) {
    }

    $.valHooks.div = {

        get: function (v_item) {

            // ??????uploadify?????????,?????????????????????
            if ($(v_item).attr("class") && $(v_item).attr("class") == "uploadify") {

                var v_result = $(v_item).attr('fileIds');

                if (v_result && v_result.endWith(",")) {
                    v_result = v_result.substring(0, v_result.length - 1);
                    $(v_item).attr('fileIds', v_result);
                }

                return $(v_item).attr('fileIds');
            } else {

                // ??????
                return $(v_item).attr('value');
            }
        },

        set: function (v_item, v_value) {

            // ??????uploadify?????????,?????????????????????
            if (v_value != "" && $(v_item).attr("class") && $(v_item).attr("class") == "uploadify") {

                if (!$(v_item).uploadify('settings', 'multi')) {
                    if ($("#" + v_item.id + "_img_queue").length > 0) {
                        $("#" + v_item.id + "_img_queue").html("");
                    }
                }

                // ???????????????????????????
                AE.ServiceEx("FileSupportService.getInfoByIds", {
                    ids: v_value
                }, function (v_data) {

                    v_value = "";

                    for (var i = 0; i < v_data.length; i++) {

                        // ??????
                        if (v_data[i].dataInfo) {

                            var v_enableEdit = window["frame.fileuploader.fileEditSize" + v_item.id] ? " enableEdit="
                                + window["frame.fileuploader.fileEditSize" + v_item.id] + " alt='????????????????????????' fileuploadItemId='"
                                + v_item.id + "' " : "";

                            var v_editCallback = window["frame.fileuploader.fileEditCallback" + v_item.id] ? " editImgCallback='"
                                + window["frame.fileuploader.fileEditCallback" + v_item.id] + "' " : "";

                            var v_imgSrc = "downloadFile.do?fileImgSize=80x80&id=" + v_data[i].dataInfo.id;

                            var t_fileName = v_data[i].dataInfo.fileName.toLowerCase().toString();
                            if (!(t_fileName.endWith('.jpg') || t_fileName.endWith('.png'))) {
                                v_imgSrc = "base/images/moren.png";
                            }


                            if ($("#file_img_" + v_data[i].dataInfo.id).length == 0 && $("#" + v_item.id + "_img_queue").length > 0) {
                                $("#" + v_item.id + "_img_queue").append("<div id='file_img_" + v_data[i].dataInfo.id
                                    + "' class='uploadify-img-queue-div'><img src='" + v_imgSrc + "' " + v_enableEdit + v_editCallback
                                    + " /><a href='#' onclick='FileUploader.remove_file(\"" + v_data[i].dataInfo.id + "\",\""
                                    + v_item.id + "\", \"" + v_delFileCallback + "\");'></a></div>");

                                // ??????????????????
                                $(v_item).data("frame-fileuploader-fileCnt",
                                    ($(v_item).data("frame-fileuploader-fileCnt") || 0) + 1);
                            }

                            // ????????????????????????
                            v_value += v_data[i].dataInfo.id + ",";

                        }
                    }

                    // ????????????????????????????????????
                    if (window.f_event_resize) {
                        window.f_event_resize.call(window);
                    }
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

// ?????????????????????
FileUploader.remove_file = function (v_fileId, v_itemId, v_delFileCallback) {

    // ???????????????
    $("#file_img_" + v_fileId).remove();

    // ?????????
    $("#" + v_itemId).attr("fileIds", $("#" + v_itemId).val().replace(v_fileId + ",", ""));

    // ?????????(???????????????????????????????????????????????????????????????)
    $("#" + v_itemId).attr("fileIds", $("#" + v_itemId).val().replace(v_fileId, ""));

    // ???????????????-1
    $("#" + v_itemId).data("frame-fileuploader-fileCnt", $("#" + v_itemId).data("frame-fileuploader-fileCnt") - 1);

    // ??????????????????
    if (v_delFileCallback != "" && window[v_delFileCallback]) {
        window[v_delFileCallback].call(window, v_fileId);
    }
}
