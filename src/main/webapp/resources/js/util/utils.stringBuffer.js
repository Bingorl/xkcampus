function StringBuffer() {
    this._strings = [];
    if (arguments.length == 1) {
        this._strings.push(arguments[0]);
    }
};

StringBuffer.prototype = {
    //连接
    append: function (str) {
        this._strings.push(str);
        return this;
    },
    //清空
    clear: function () {
        this._strings = [];
    },
    //获取长度
    length: function () {
        var str = this._strings.join("");
        return str.length;
    },
    //删除指定的字符串
    del: function (num) {
        var len = this.length();
        var str = this.toString();
        str = str.slice(0, len - num);
        this._strings = [];
        this._strings.push(str);
    },
    //返回所有字符
    toString: function () {
        return this._strings.join("");
    }
};