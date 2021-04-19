webAlert = function (title, content) {
    if (typeof (content) == "undefined") {
        content = title;
        title = "提示框";
    }
    ;
    easyDialog.open({
        container: {
            header: title,
            content: content
        },
        drag: false
    });
};