function compareDate(strDate1, strDate2) {
    var date1 = new Date(strDate1.replace(/\./g, "\/"));
    var date2 = new Date(strDate2.replace(/\./g, "\/"));
    return date1 - date2;
}

// 图片点击放大事件

function f_imageToBig(path, width, heigth) {

    window.top["frame.app.imgDialog"] = jq
        .dialog({
            title: false,
            lock: true,
            drag: false,
            resize: false,
            max: false,
            min: false,
            content: '<img src="downloadFile.do?id='
                + path
                + '" width="'
                + width
                + '" height="'
                + heigth
                + '" onclick="closeImgDialog();" style="cursor: pointer;" title="点击关闭" />',
            padding: 0
        });
}

// 图片放大关闭事件
function closeImgDialog() {
    if (window.top["frame.app.imgDialog"]) {
        window.top["frame.app.imgDialog"].close();
    }
}