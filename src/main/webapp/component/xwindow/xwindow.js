/*---------------------------------------------------------------------------*\
|  Subject:       Core xwindow                                          								 |
|  Version:       2.0.0                                                     									 |
|  FileName:      xwindows.js                                               								 |
|  Created:       2012-03      xiaoxi                                            								 |
|  Comment:       核心界面控制封装,此类暂时依赖于core jquery prototype        |
\*---------------------------------------------------------------------------*/

var winmode = false;
var zInxWin = 998;
var zInxTop = zInxWin + 10;
var Ie = /msie/i.test(navigator.userAgent);
var MyWin = new Win();

var Winid = 0;

function Win() {
    this.winId = 0;
    this.sizable = true;
    this.closeable = true;

    this.Create = function (mask, title, wbody, w, h, sizable, closeable) {
        this.winId = Winid++;
        title = title || "";
        this.sizable = sizable || false, this.closeable = closeable || false, imgfile = contextPath + "/component/xwindow/dialog/"// 设置图片路径
        wbody = wbody || " <p align='center'>正在载入…</p>";
        w = w || 750;
        h = h || 550;
        sw = document.documentElement.scrollWidth;
        sh = document.documentElement.scrollHeight;

        if (mask != "no") {
            var ndiv = $("dialog_ndiv" + this.winId);
            if (!ndiv) {
                ndiv = document.createElement("DIV");
                ndiv.setAttribute("id", "dialog_ndiv" + this.winId);
            }
            ndiv.style.cssText = "width:"
                + sw
                + "px;height:"
                + sh
                + "px;left:0px;top:0px;position:absolute;overflow:hidden;background:#fff;filter:alpha(opacity=20); opacity:0.2;-moz-opacity:0.2;z-index:998";
            document.body.appendChild(ndiv);

            if (Ie) {
                var niframe = document.createElement("iframe");
                niframe.style.width = sw;
                niframe.style.height = sh;
                niframe.style.top = "0px";
                niframe.style.left = "0px";
                niframe.style.visibility = "inherit";
                niframe.style.filter = "alpha(opacity=30)";
                niframe.style.position = "absolute";
                niframe.style.zIndex = -1;
                ndiv.insertAdjacentElement("afterBegin", niframe);
            }
        }

        var mywin = $("dialog_win" + this.winId);
        if (!mywin) {
            mywin = document.createElement("DIV");
        }
        mywin.setAttribute("id", "dialog_win" + this.winId);
        mywin.style.cssText = "width:" + w + "px;height:" + h
            + "px;left:0px;top:0px;position:absolute;overflow:hidden;padding:0px;font-family:Arial, 宋体;display:block;";
        document.body.appendChild(mywin);

        var tbStr = new StringBuffer();

        tbStr.append("<table class='moz-user' border='0' cellpadding='0' cellspacing='0'  width='100%'>");

        // head
        tbStr
            .append("<tr id='_draghandle_abc'><td width='13' height='33' id='dialog_lt' class='dialog_bar_left'><div style='width: 13px;'></div></td>"
                + "<td height='33' id='dialog_ct' class='dialog_bar_center'><div class='dialog_name'><img src="
                + imgfile
                + "icon_dialog.gif align='absmiddle'>&nbsp;<span id='dialog_title" + this.winId + "'>" + title + "</span></div>");

        if (this.closeable) {
            tbStr.append("<div id='dialogBoxClose' class='dialog_bt' onMouseOver=\"this.style.backgroundImage='url(" + imgfile
                + "dialog_closebtn_over.gif)'\" onMouseOut=\"this.style.backgroundImage='url(" + imgfile
                + "dialog_closebtn.gif)'\" drag='false'></div>");
        }

        if (this.sizable) {
            tbStr.append("<div id='dialogBoxFd' class='dialog_bt3' onMouseOver=\"this.style.backgroundImage='url(" + imgfile
                + "dialog_fd_over.gif)'\" onMouseOut=\"this.style.backgroundImage='url(" + imgfile
                + "dialog_fd.gif)'\" drag='false' ></div>"
                + "<div id='dialogBoxSx' class='dialog_bt2'  onMouseOver=\"this.style.backgroundImage='url(" + imgfile
                + "dialog_sx_over.gif)'\" onMouseOut=\"this.style.backgroundImage='url(" + imgfile
                + "dialog_sx.gif)'\" drag='false' ></div></td>");
        }

        tbStr.append("<td width='13' height='33' id='dialog_rt' class='dialog_bar_right'><div style='width: 13px;'></div></td></tr>");

        // body
        tbStr
            .append("<tr drag='false'>"
                + "<td width='13' id='dialog_mlm' class='dialog_body_left'></td>"
                + "<td align='center' valign='top'><a href='#;' id='_forTab_abc'></a>"
                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'  bgcolor='#FFFFFF' height='100%'>"
                + "<tr>"
                + "<td align='center' valign='top'>"
                + "<div class='dialog_A'>"
                + "<iframe src='"
                + wbody
                + "' id='_DialogFrame_abc"
                + this.winId
                + "' name='_DialogFrame_abc"
                + this.winId
                + "'  allowTransparency='true' frameborder='0'  style='background-color: #transparent; border: none;width:100%;height:100%;'></iframe>"
                + "</div>" + "</td>" + "</tr>" + "</table> <a href='#;' onfocus=''></a>" + "</td>"
                + "<td width='13' id='dialog_mrm' class='dialog_body_right'></td>" + "</tr>");

        // foot
        tbStr.append("<tr>" + "<td width='13' height='13' id='dialog_lb' class='dialog_foot_left'></td>"
            + "<td id='dialog_cb' class='dialog_foot_center'></td>"
            + "<td id='dialog_rb' width='13' height='13' class='dialog_foot_right'></td>" + "</tr>");

        tbStr.append("</table>");

        jq(mywin).html(tbStr.toString());

        this.bindEvent();
        this.Show();
        this.setTitle(title);

        winmode = true;
        return (mywin);
    };

    this.setTitle = function (title) {
        if (typeof title == 'string' && title != "") return;

        var ifrm = $("_DialogFrame_abc" + this.winId);
        var wid = this.winId;

        ifrm.onreadystatechange = function () {

            if (this.readyState && this.readyState == 'complete') {

                var t = this.contentWindow.document.title;
                if ($('dialog_title' + wid) != null) {
                    $('dialog_title' + wid).innerHTML = t;
                }
            }
        };
    };

    this.bindEvent = function () {
        var loader = this;
        if (this.closeable) {
            Event.observe($('dialogBoxClose'), "click", function () {
                loader.Close();
            }, false);
        }

        if (this.sizable) {
            Event.observe($('dialogBoxFd'), "click", function () {
                loader.maxWin();
            }, false);
            Event.observe($('dialogBoxSx'), "click", function () {
                loader.maxWin();
            }, false);
        }

        loader.drag("#_draghandle_abc", "#dialog_win" + this.winId);

        // Event.observe($('_draghandle_abc'), "mousedown", function() {
        // loader.dragDrop(event)
        // }, false);
    };

    // TODO chrome 往下拖动断断续续是因为iframe的mousemove没有绑定
    this.drag = function (dragControl, dragContent) {
        var _x, _y, cw, ch, sw, sh;
        var dragContent = typeof dragContent == "undefined" ? dragControl : dragContent;

        jq(dragControl).mousedown(function (e) {
            _x = e.pageX - parseInt(jq(dragContent).css("left"));
            _y = e.pageY - parseInt(jq(dragContent).css("top"));
            cw = jq(window).width();
            ch = jq(window).height();
            sw = parseInt(jq(dragContent).outerWidth());
            // sh = parseInt(jq(dragContent).outerHeight());
            sh = 30;

            window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty(); // 禁止拖放对象文本被选择的方法
            document.body.setCapture && jq(dragContent)[0].setCapture(); // IE下鼠标超出视口仍可被监听

            var f_mousemove = function (e) {
                var x = e.pageX - _x;
                var y = e.pageY - _y;
                x = x < 0 ? x = 0 : x < (cw - sw) ? x : (cw - sw);
                y = y < 0 ? y = 0 : y < (ch - sh) ? y : (ch - sh);

                jq(dragContent).css({
                    top: y,
                    left: x
                });
            };

            var f_mouseup = function () {
                AE.unevent(document, "mousemove", f_mousemove);
                AE.unevent(document, "mouseup", f_mouseup);
                document.body.releaseCapture && jq(dragContent)[0].releaseCapture();
            };

            AE.event(document, "mousemove", f_mousemove);
            AE.event(document, "mouseup", f_mouseup);
        });
    };

    this.Show = function () {
        if (!$("dialog_win" + this.winId)) return;
        // alert(o.id);
        var oDialog = $("dialog_win" + this.winId);
        oDialog['style']['position'] = "absolute";
        oDialog['style']['display'] = '';

        var sClientWidth = document.body.clientWidth;
        var sClientHeight = document.documentElement.clientHeight;

        var scrollPos;
        if (typeof window.pageYOffset != 'undefined') {
            scrollPos = window.pageYOffset;
        } else if (typeof document.compatMode != 'undefined' && document.compatMode != 'BackCompat') {
            scrollPos = document.documentElement.scrollTop;
        } else if (typeof document.body != 'undefined') {
            scrollPos = document.body.scrollTop;
        }

        var sScrollTop = scrollPos;

        var sleft = (sClientWidth / 2) - (oDialog.offsetWidth / 2) + 50;

        var iTop = -80 + (sClientHeight / 2 + sScrollTop) - (oDialog.offsetHeight / 2);
        var sTop = iTop > 0 ? iTop : (sClientHeight / 2 + sScrollTop) - (oDialog.offsetHeight / 2);

        if (sTop < 1) sTop = "20";
        if (sleft < 1) sleft = "20";

        oDialog['style']['left'] = ((document.body.clientWidth) / 2 - oDialog.offsetWidth / 2) + "px";
        oDialog['style']['top'] = (document.body.clientHeight / 2 - oDialog.offsetHeight / 2) + "px";

        window.onscroll = function () {
            oDialog['style']['top'] = (document.documentElement.scrollTop + (document.documentElement.clientHeight) / 10) + "px";
            // $('dialog_ndiv'+this.winId).style.top=(document.documentElement.scrollTop+(document.documentElement.clientHeight)/10+4)+"px";
        };
        oDialog.style.zIndex = ++zInxTop;
    };

    this.Close = function () {

        // 如果子页面存在系统函数f_frame_close,则调用
        // 此处要区别f_frame_close,f_frame_closeWin
        {
            var v_ifrm = $("_DialogFrame_abc" + this.winId);

            if (typeof v_ifrm.contentWindow.f_frame_closeWin == "function") {
                v_ifrm.contentWindow.f_frame_closeWin.call(v_ifrm.contentWindow.f_frame_closeWin);
            }
        }

        var o = $("dialog_win" + this.winId);
        if (!o) return;
        o.style.display = "none";

        try {
            if (o.getElementsByTagName("IFRAME").length != 0) {
                if (o.getElementsByTagName("IFRAME")[0].contentWindow.document
                    && o.getElementsByTagName("IFRAME")[0].contentWindow.document.body) {
                    o.getElementsByTagName("IFRAME")[0].contentWindow.document.body.innerHTML = "";
                }

                o.getElementsByTagName("IFRAME")[0].src = "about:blank";
            }
            document.body.removeChild(o);

        } catch (e) {
            // TODO: handle exception
        } finally {
            this.ndiv();

            winmode = false;
            this.winId = 0;
            if (Winid != 0) Winid--;
        }

    };

    this.ndiv = function () {
        var o = $("dialog_ndiv" + this.winId);
        if (!o) return;
        o.style.display = "none";
        document.body.removeChild(o);
    };

    this.maxWin = function () {
        var win = $("dialog_win" + this.winId);
        if (!win) return;

        if (this.sizable) {
            $('dialogBoxFd').style.display = win.getAttribute("winMax") == "1" ? "" : "none";
            $('dialogBoxSx').style.display = win.getAttribute("winMax") == "1" ? "none" : "block";
        }

        if (win.getAttribute("winMax") == "1") {
            win["winMax"] = "0";
            this.setSize(win.getAttribute("nowWidth"), win.getAttribute("nowHeight"));
            this.setLocation(win.getAttribute("nowLeft"), win.getAttribute("nowTop"));

        } else {
            win.setAttribute("nowWidth", win.style.width);
            win.setAttribute("nowHeight", win.style.height);
            win.setAttribute("nowLeft", win.style.left);
            win.setAttribute("nowTop", win.style.top);
            win.setAttribute("winMax", 1);

            this.setSize(gh().x, gh().y);
            this.setLocation(0, 0);
        }
    };

    this.setSize = function (w, h) {
        if (!$("dialog_win" + this.winId)) return;

        $("dialog_win" + this.winId).style.height = h;
        $("dialog_win" + this.winId).style.width = w;
    };

    this.setLocation = function (l, t) {
        if (!$("dialog_win" + this.winId)) return;

        $("dialog_win" + this.winId).style.left = l;
        $("dialog_win" + this.winId).style.top = t;
    };
}

/*------------------------------------------------------------------------------------------external interface PI-------------------------------------------------------------------------------------------------*/

function gh() {
    var windowWidth, windowHeight;

    // all except Explorer
    if (self.innerHeight) {
        windowWidth = self.innerWidth;
        windowHeight = self.innerHeight;
    } else if (document.documentElement && document.documentElement.clientHeight) {

        // Explorer 6 Strict Mode
        windowWidth = document.documentElement.clientWidth;
        windowHeight = document.documentElement.clientHeight;
    } else if (document.body) {

        // other Explorers
        windowWidth = document.body.clientWidth;
        windowHeight = document.body.clientHeight;
    }
    return {
        x: windowWidth,
        w: windowWidth,
        y: windowHeight,
        h: windowHeight
    };
}

var getSize = window.gh;

// 关闭弹出窗口
function closeWindow() {

    //modify by tyotann 2012.12.28 放入this.Close方法中,不然用户直接点击关闭图标后不触发事件
    //if (typeof parent.f_frame_closeWin == "function") {
    //	parent.f_frame_closeWin.call(this);
    //}
    parent.MyWin.Close();
}

function openXwin() {
    alert("打开xwin弹出窗口请统一调用AE.Ui.xWinEx接口！");
}

function closeXwin() {
    alert("关闭xwin弹出窗口请统一调用AE.Ui.closexWin接口！");
}
