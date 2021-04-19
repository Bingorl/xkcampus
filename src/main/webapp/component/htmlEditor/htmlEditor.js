function HtmlEditor(v_id, v_options) {

    // 对外参数
    this.options = {

        // 如果有复选框
        // 设置所选状态是否需要父子关联：默认（checked：“ps”选中父子都关联，unchecked：“ps”未选中父子也都关联）只是父关联则2个可设为p，只是子关联则可设为s，
        // 都不需关联则都设为空字符串即可“”
        checkType: {
            checked: "ps",
            unchecked: "ps"
        },
        fontCss: {},
        pasteType: 1,// 0.没有黏贴功能,1.去掉所有的HTML格式,2.保持黏贴HTML样式
        f_loaded: null,
        f_alwaysLoaded: null,
        f_checked: null,
        f_click: null,
        f_dbclick: null,
        f_move: null,
        addDiyDom: null,
        addHoverDom: null,
        removeHoverDom: null,
        //length : 100, // 最大字数长度
        items: ['source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste', 'plainpaste',
            'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', 'insertorderedlist',
            'insertunorderedlist', 'indent', 'outdent', 'subscript', 'superscript', 'clearhtml', 'quickformat', 'selectall', '|',
            'fullscreen', '/', 'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
            'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage', 'table', 'hr', 'emoticons', 'baidumap',
            'pagebreak', 'anchor', 'link', 'unlink', '|', 'about']
    };

    $.extend(this.options, v_options);

    if (!KindEditor) {
        alert("请先加载htmlEditor的JS文件!");
        return;
    }

    var v_loader = this;

    // 初始化
    this.options.editor = KindEditor.create('#' + v_id, {
        cssPath: 'component/htmlEditor/kindeditor-4.1.7/plugins/code/prettify.css',
        uploadJson: 'uploadFile.do?paramName=imgFile',
        afterCreate: function () {
        },
        pasteType: 2,
        items: this.options.items,
        resizeType: 0,
        newlineTag: "br"
    });

    return this;
}

HtmlEditor.prototype = {

    getHtml: function () {
        return this.options.editor.html();
    },

    setHtml: function (v_html) {
        if (v_html != null) {
            return this.options.editor.html(v_html);
        }
    },

    isEmpty: function () {
        return this.options.editor.isEmpty();
    }
};