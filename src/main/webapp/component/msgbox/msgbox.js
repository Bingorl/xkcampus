var ethanMsgbox = function () {
    this.showTime = 6000;//显示时间,默认30秒
    this.content;
    this.box;
    this.divHeight;
    this.docHeight;
    this.objTimer;
    this.isLoading = false;
}

//创建消息提示窗口
ethanMsgbox.prototype.createMsgbox = function () {
    var boxTitle = "温馨提示:";
    var box = document.createElement("div");
    box.className = "msgBox";

    var table = document.createElement("table");
    table.className = "box";
    table.cellSpacing = "0";
    table.cellPadding = "0";

    var titleRow = table.insertRow();
    titleRow.className = "topLine";

    titleRow.insertCell().className = "titlePic";

    var titleCell = titleRow.insertCell();
    titleCell.className = "titleContent";
    titleCell.innerText = boxTitle;

    var closeCell = titleRow.insertCell();
    closeCell.className = "closeTd";
    closeCell.title = "关闭";
    closeCell.attachEvent('onclick', ethanMsgbox.closeMsgbox);

    //内容
    var dvContent = document.createElement("div");
    dvContent.className = "content";

    //主体行
    var contentRow = table.insertRow();

    var contentCell = contentRow.insertCell();
    contentCell.className = "contentBorder";
    contentCell.colSpan = "3";
    contentCell.appendChild(dvContent);

    box.appendChild(table);
    document.body.appendChild(box);

    this.content = dvContent;
    this.box = box;
}

//关闭提醒
ethanMsgbox.prototype.closeMsgbox = function () {
    ethanMsgbox.box.style.visibility = "hidden";
    if (this.box && this.objTimer) window.clearInterval(this.objTimer);
}

ethanMsgbox.prototype.resizeMsgbox = function () {
    this.showTime -= 1;
    if (this.showTime <= 0) {
        this.closeMsgbox();
    }//到达时间自动消失
    try {
        var divHeight = parseInt(this.box.offsetHeight, 10);
        var divWidth = parseInt(this.box.offsetWidth, 10);
        var docWidth = this.getWindowWidth();
        var docHeight = this.getWindowHeight();
        this.box.style.top = docHeight - divHeight + parseInt(this.getScrollTop(), 10);
        this.box.style.left = docWidth - divWidth + parseInt(this.getScrollLeft(), 10) - 1;
    } catch (e) {
    }
}
//更新消息框位置
ethanMsgbox.prototype.moveMsgBox = function () {
    try {
        if (parseInt(this.box.style.top, 10) <= (this.docHeight - this.divHeight + parseInt(this.getScrollTop(), 10))) {
            window.clearInterval(this.objTimer);
            this.objTimer = window.setInterval("ethanMsgbox.resizeMsgbox()", 10);
        }
        var divTop = parseInt(this.box.style.top, 10);
        this.box.style.top = divTop - 1;
    } catch (e) {
    }
}

ethanMsgbox.prototype.showMsgBox = function (message) {
    var hour, minute, seconds;
    this.content.innerHTML = message;
    try {
        this.divHeight = parseInt(this.box.offsetHeight, 10);

        var divWidth = parseInt(this.box.offsetWidth, 10);
        var docWidth = this.getWindowWidth();

        this.docHeight = this.getWindowHeight();

        this.box.style.top = parseInt(this.getScrollTop(), 10) + this.docHeight + 10;

        this.box.style.left = parseInt(this.getScrollLeft(), 10) + docWidth - divWidth - 1;

        this.box.style.visibility = "visible";
        this.objTimer = window.setInterval("ethanMsgbox.moveMsgBox()", 10);
    } catch (e) {
    }
}

ethanMsgbox.prototype.getWindowWidth = function () {
    var width;
    if (window.innerWidth)
        width = window.innerWidth;
    else if ((document.body) && (document.body.clientWidth))
        width = document.body.clientWidth;
    if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
        width = document.documentElement.clientWidth;
    }
    return width;
}

ethanMsgbox.prototype.getWindowHeight = function () {
    var height;
    if (window.innerHeight)
        height = window.innerHeight;
    else if ((document.body) && (document.body.clientHeight))
        height = document.body.clientHeight;
    if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
        height = document.documentElement.clientHeight;
    }
    return height;
}

ethanMsgbox.prototype.getScrollTop = function () {
    if (window.innerHeight) {
        return window.pageYOffset;
    } else if (document.documentElement && document.documentElement.scrollTop) {
        return document.documentElement.scrollTop;
    } else if (document.body) {
        return document.body.scrollTop;
    }
}

ethanMsgbox.prototype.getScrollLeft = function () {
    if (window.innerHeight) {
        return window.pageXOffset;
    } else if (document.documentElement && document.documentElement.scrollTop) {
        return document.documentElement.scrollLeft;
    } else if (document.body) {
        return document.body.scrollLeft;
    }
}

//显示Message
function displayMsgbox(message, displayTime) {
    if (!ethanMsgbox.isLoading) {
        ethanMsgbox = new ethanMsgbox();
        ethanMsgbox.createMsgbox();
        ethanMsgbox.isLoading = true;
    }

    if (displayTime) ethanMsgbox.showTime = displayTime;
    else ethanMsgbox.showTime = 1800;

    ethanMsgbox.closeMsgbox();

    ethanMsgbox.showMsgBox(message);
}

