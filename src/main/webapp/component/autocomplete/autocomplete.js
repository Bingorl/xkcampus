function AutoComplete(v_item, v_options) {

    // 对外参数
    this.options = {

        url: null,
        clickCallback: null,
        param: {}
    };

    $.extend(this.options, v_options);

    this.options.param.appid = window.top["frame_appid"];

    jq(v_item).autocomplete(this.options.url, {
        selectFirst: false,
        // delay : 1000,
        extraParams: this.options.param,
        formatResult: function (row) {
            return row[0];
        },
        matchCase: true,
        width: $(v_item).width() + 9
    });

    if (this.options.clickCallback) {

        var loader = this;
        jq(v_item).result(function (event, row, formatted) {
            loader.options.clickCallback(row);
        });
    }

}
