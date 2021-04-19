var appGrids = [];
var obj_old = null;
var g_data_storage = [];

function onclicked(obj) {
    if (obj_old != null) {
        obj_old.className = '';
    }
    obj.className = 'click';
    obj_old = obj;
}

function GridModel(g_model) {
    this.g = g_model;
}

GridModel.prototype = {

    loadConfig: function () {

        var loader = this.g;

        $("#" + loader.options.target).html(this.toHtml());

        loader.render = true;
    },

    // 加载数据
    loadata: function () {
        var pager = {
            "limit.length": this.g.options.pagesize,
            "limit.enable": this.g.options.usepager
        };

        $.extend(this.g.options.param, pager);

        var serviceName = this.g.options.url;
        var loader = this.g;
        var model = this;
        if (serviceName.indexOf("http") != -1) {
            $.ajax({
                url: this.g.options.url,
                type: "post",
                data: this.g.options.param,
                dataType: 'JSON',
                success: function (v_data, v_pageLimit) {
                    loader.clear.call(loader);
                    if (v_data != null) {
                        g_data_storage[loader.indexed] = v_data;
                        var tb = $("#grid_tb" + loader.indexed);
                        var count = v_data.length;
                        var tbStr = new StringBuffer();
                        var reg_ = null;
                        var tdlen = loader.options["colModel"].length;

                        for (var i = 0; i < count; i++) {
                            var o = v_data[i];
                            // 过滤单引号
                            tbStr.append("<tr rum='" + i + "'>");

                            for (var k = 0; k < tdlen; k++) {
                                var cm = loader.options["colModel"][k];
                                tbStr.append(model.addTableCell(cm, k, o, i));
                            }
                            tbStr.append("</tr>");
                            o = null;
                        }

                        // 加入底部按钮
                        if (loader.options["footer"]) {
                            tbStr
                                .append("<tr style='height: 33px;'><td align=\"center\" ><input type=\"checkbox\" onclick='AppGrid.setAllChecked(this,"
                                    + loader.indexed
                                    + ");' gridSelect=true toolbarSelect='true' /></td><td colspan='"
                                    + (tdlen - 1)
                                    + "' >").append(loader.options["footer"]).append("</td></tr>");
                        }

                        // 分页信息(启用分页，并且后台返回分页信息，并且页数>1页时才出现分页信息)
                        if (loader.options["usepager"] && v_pageLimit && (parseInt(v_pageLimit.totalCount) > parseInt(loader.options["pagesize"]))) {
                            var p = model.genPagination(v_pageLimit);
                            tbStr.append("<tr><td style=\"text-align: center;\" id=grid_pagelimit" + loader.indexed + " colspan='" + tdlen + "'>"
                                + p + "</td></tr>");
                            loader.runtime.page = v_pageLimit;
                        }

                        tb.html(tbStr.toString());

                        loader.runtime.count = v_data.length;

                        tbStr = null;
                        reg_ = null;
                        loader.selectRow = null; // 数据重载重设已选择项

                        if (loader.options.loadedcallback) {
                            loader.options.loadedcallback.call(loader);
                        }

                        // 触发重新设置页面高度事件
                        if (window.f_event_resize) {
                            window.f_event_resize.call(window);
                        }
                    }
                }
            });

        } else {
            AE.ServiceEx(serviceName, this.g.options.param, function (v_data, v_pageLimit) {
                loader.clear.call(loader);

                if (v_data) {
                    g_data_storage[loader.indexed] = v_data;

                    var tb = $("#grid_tb" + loader.indexed);
                    var count = v_data.length;
                    var tbStr = new StringBuffer();
                    var reg_ = null;
                    var tdlen = loader.options["colModel"].length;

                    for (var i = 0; i < count; i++) {
                        var o = v_data[i];
                        // 过滤单引号
                        tbStr.append("<tr rum='" + i + "'>");

                        for (var k = 0; k < tdlen; k++) {
                            var cm = loader.options["colModel"][k];
                            tbStr.append(model.addTableCell(cm, k, o, i));
                        }
                        tbStr.append("</tr>");
                        o = null;
                    }

                    // 加入底部按钮
                    if (loader.options["footer"]) {
                        tbStr
                            .append("<tr style='height: 33px;'><td align=\"center\" ><input type=\"checkbox\" onclick='AppGrid.setAllChecked(this,"
                                + loader.indexed
                                + ");' gridSelect=true toolbarSelect='true' /></td><td colspan='"
                                + (tdlen - 1)
                                + "' >").append(loader.options["footer"]).append("</td></tr>");
                    }

                    // 分页信息(启用分页，并且后台返回分页信息，并且页数>1页时才出现分页信息)
                    if (loader.options["usepager"] && v_pageLimit && (parseInt(v_pageLimit.totalCount) > parseInt(loader.options["pagesize"]))) {
                        var p = model.genPagination(v_pageLimit);
                        tbStr.append("<tr><td style=\"text-align: center;\" id=grid_pagelimit" + loader.indexed + " colspan='" + tdlen + "'>"
                            + p + "</td></tr>");
                        loader.runtime.page = v_pageLimit;
                    }

                    tb.html(tbStr.toString());

                    loader.runtime.count = v_data.length;

                    tbStr = null;
                    reg_ = null;
                    loader.selectRow = null; // 数据重载重设已选择项

                    if (loader.options.loadedcallback) {
                        loader.options.loadedcallback.call(loader);
                    }

                    // 触发重新设置页面高度事件
                    if (window.f_event_resize) {
                        window.f_event_resize.call(window);
                    }
                }

            }, {
                async: loader.options.async
            });
        }
    },

    // 拼装单元格
    addTableCell: function (cm, k, o, i) {
        var tdStr = new StringBuffer("<td");

        if (cm["type"] && (cm["type"] == "checkbox" || cm["type"] == "radio")) {
            tdStr.append(" align=\"center\" width=\"15px\" ");
        }

        if (cm["css"]) {
            tdStr.append(" class='" + cm["css"] + "' ");
        }

        if (cm["style"]) {
            tdStr.append(" style='" + cm["style"] + "' ");
        }

        tdStr.append(">");

        // 构造td中HTML
        tdStr.append(this.creatTDContentHTML(cm, k, o, i));

        tdStr.append("</td>");

        return tdStr.toString();
    },

    // 构造单元格中的内容
    creatTDContentHTML: function (cm, k, o, index) {

        var td_str = "";

        var val = o[cm["name"]];

        // type,tpl,f_callback任意设置
        if (cm["type"]) {

            if (cm["type"] == "checkbox") {
                td_str += "<input type=" + cm["type"] + " value='" + ((val != null && typeof val != 'undefined') ? val : "") + "' name='rd"
                    + this.g.indexed + "' id=id" + k + " onclick='selectChangeFunction(this, " + this.g.indexed
                    + ");' gridSelect=true />";
            } else {
                td_str += "<input type=" + cm["type"] + " value='" + ((val != null && typeof val != 'undefined') ? val : "") + "' name='rd"
                    + this.g.indexed + "' id=id" + k + " gridSelect=true />";
            }
        } else if (cm["tpl"]) {

            var tpl = cm["tpl"];
            tpl = tpl.replaceAll("#rowN#", (index || 0));

            for (var one in o) {
                if (typeof one == "string") {

                    // 修正字符是0的话不显示的问题
                    tpl = tpl.replaceAll("#" + one + "#", (typeof (o[one]) == "undefined" || o[one] == null) ? "" : o[one].toString());
                }
            }
            // tpl = tpl.replaceAll("\\@", ";");
            // tpl = tpl.replaceAll("\\$", ":");
            // tpl = tpl.replaceAll("\\!", ",");
            td_str += tpl;
        } else if (cm["f_callback"]) {

            // 如果是自定义函数,则手动设置
            var v_result = cm["f_callback"].call(this.g, o, index);
            if (v_result) {
                td_str += v_result;
            }
        } else {
            var val = o[cm["name"]];
            td_str += (val != null && typeof val != 'undefined') ? val.toString() : "";
        }

        return td_str;
    },

    // 组装grid
    toHtml: function () {
        var sb = new StringBuffer("");

        // 实际数据表格
        sb.append('<div id="tableContainer' + this.g.indexed + '" class="' + this.g.options.css + '" ><table id="pagegrid' + this.g.indexed
            + '"  cellpadding="0" cellspacing="0">');
        sb.append("<thead  id='grid_th" + this.g.indexed + "'><tr>");

        if (this.g.options["colModel"]) {
            var colmodel = this.g.options["colModel"];

            var len = colmodel.length;
            for (var i = 0; i < len; i++) {
                var col = colmodel[i];

                if (col["sort"]) {
                    sb.append('<th class="grid_sort_td_class" onclick="AppGrid.sort(\'' + col.display + '\',\'' + col["sort"]
                        + '\');" columnName="' + col.name + '"');
                } else {
                    sb.append('<th columnName="' + col.name + '"');
                }

                if (col["width"]) {
                    sb.append(' width="').append(col["width"]).append('"');
                }

                if (col["type"] && col["type"] == "checkbox") {
                    sb.append(" align=\"center\" width=\"15px\"><input type=\"checkbox\" onclick='AppGrid.setAllChecked(this,"
                        + this.g.indexed + ");' gridSelect=true toolbarSelect='true'  >");
                } else if (col["type"] && col["type"] == "radio") {
                    sb.append(">选择");
                } else {
                    sb.append(">" + col["display"]);
                }

                // 如果启用排序
                if (col["sort"]) {
                    sb.append('<span name="grid_sort" class="grid_sort_sc" >&nbsp;</span>');
                }

                sb.append("</th>");
            }
        }

        sb.append("</tr></thead>");
        sb.append("<tbody id='grid_tb" + this.g.indexed + "'>");
        sb.append("</tbody>");

        sb.append('</table></div>');

        return sb.toString();
    },

    // 组装分页信息
    genPagination: function (page) {

        var str = new StringBuffer("<ul class=\"grid_pagelimit\">");

        // 如果不是第一页
        if (page.currentPageNo != 1) {

            str.append("<li><a style=\"border-top-left-radius: 6px;border-bottom-left-radius: 6px;\" href=\"javascript:AppGrid.goPage('"
                + this.g.indexed + "',\'1\',\'" + page.totalCount + "\')\">««</a></li>");

            str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (page.currentPageNo - 1) + "\',\'"
                + page.totalCount + "\')\">«</a></li>");
        }

        if (page.totalPageCount < 5) {

            var i = 0;
            while (i < page.totalPageCount) {
                if (i + 1 == page.currentPageNo) {
                    str.append("<li><a class=\"on\" >" + (i + 1) + "</a></li>");
                } else {
                    str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (i + 1) + "\',\'" + page.totalCount
                        + "\')\">" + (i + 1) + "</a></li>");
                }
                i++;
            }
        } else {

            // 当前页+2>总页
            if (page.currentPageNo + 2 > page.totalPageCount) {

                var i = 0;

                while (i < 5) {
                    if ((page.totalPageCount - 4 + i) == page.currentPageNo) {
                        str.append("<li><a class=\"on\" >" + (page.totalPageCount - 4 + i) + "</a></li>");
                    } else {
                        str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (page.totalPageCount - 4 + i)
                            + "\',\'" + page.totalCount + "\')\">" + (page.totalPageCount - 4 + i) + "</a></li>");
                    }
                    i++;
                }
            } else if (page.currentPageNo - 2 <= 0) {
                // 当前页-2<0
                var i = 0;

                while (i < 5) {
                    if (i + 1 == page.currentPageNo) {
                        str.append("<li><a class=\"on\" >" + (i + 1) + "</a></li>");
                    } else {
                        str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (i + 1) + "\',\'"
                            + page.totalCount + "\')\">" + (i + 1) + "</a></li>");
                    }
                    i++;
                }
            } else {
                str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (page.currentPageNo - 2) + "\',\'"
                    + page.totalCount + "\')\">" + (page.currentPageNo - 2) + "</a></li>");
                str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (page.currentPageNo - 1) + "\',\'"
                    + page.totalCount + "\')\">" + (page.currentPageNo - 1) + "</a></li>");
                str.append("<li><a class=\"on\" >" + (page.currentPageNo) + "</a></li>");
                str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (page.currentPageNo + 1) + "\',\'"
                    + page.totalCount + "\')\">" + (page.currentPageNo + 1) + "</a></li>");
                str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (page.currentPageNo + 2) + "\',\'"
                    + page.totalCount + "\')\">" + (page.currentPageNo + 2) + "</a></li>");
            }
        }

        // 如果不是最后一页,且有数据的情况下
        if (page.totalPageCount > 0 && page.currentPageNo != page.totalPageCount) {
            str.append("<li><a href=\"javascript:AppGrid.goPage('" + this.g.indexed + "',\'" + (page.currentPageNo + 1) + "\',\'"
                + page.totalCount + "\')\">»</a></li>");
            str.append("<li><a style=\"border-top-right-radius: 6px;border-bottom-right-radius: 6px;\" href=\"javascript:AppGrid.goPage('"
                + this.g.indexed + "',\'" + page.totalCount + "\',\'" + page.totalCount + "\')\">»»</a></li>");
        }
        if (page.totalPageCount > 1) {
            // str.append("<li><input type='text' id='goPageNum_" +
            // this.g.indexed + "' dateType='number' style='width:20px;'>"
            // + "<a href=\"javascript:f_goPage('" + this.g.indexed + "',\'" +
            // page.totalCount
            // + "\');\">&nbsp;&nbsp;跳&nbsp;&nbsp;</a></li>");

        }
        str.append("</ul><span class='grid_pagelimit_text'>[ 共 " + page.totalCount + " 条记录， 每页 " + page.pageLength + " 条 ]</span>");

        return str.toString();
    }
};

// checkbox改变回调函数
function selectChangeFunction(e, index) {
    if ($(e).attr("checked") != "checked") {
        // 如果取消选中，则头部和底部多选框也取消选择
        $("input[type='checkbox'][gridSelect=true][toolbarSelect=true]").attr("checked", false);
    } else {
        // 如果选中，则判断所有的多选框是否全部选择，若全部选中，则头部和底部也选中
        var v_checkboxSelect_flag = true;
        $("input[type='checkbox'][gridSelect=true][toolbarSelect!=true]").each(function () {
            if ($(this).attr("checked") != "checked") v_checkboxSelect_flag = false;
        });

        if (v_checkboxSelect_flag) {
            $("input[type='checkbox'][gridSelect=true][toolbarSelect=true]").attr("checked", true);
        }
    }
}

function f_goPage(v_indexed, v_totalCount) {
    var v_pageNum = jq("#goPageNum_" + v_indexed).val();
    if (v_pageNum == null || v_pageNum == "") {
        AE.UI.alert("请填写需要跳到的页数.");
        return;
    }
    AppGrid.goPage(v_indexed, v_pageNum, v_totalCount);
}

function AppGrid(t, opt) {
    this.container = t;
    this.render = false;
    this.selectRow = null;
    this.indexed = appGrids.length;
    this.scolltop = 0;
    // this.len = 0;
    this.dataGrid = null;

    this.options = {
        url: "#", // 请求服务
        param: {}, // 查询参数
        colModel: [], // 数据列
        usepager: true, // 分页
        pagesize: null, // 每页显示
        width: "100%", // 宽
        height: "100%", // 高
        gridId: null, // 交易代码
        cellheadtitle: "", // 标题名
        normal: false, // 常态 无列表伸缩 最大化
        load: false, // 是否自动加载
        showHead: true, // 外层table
        exp: false, // 导出
        onClickEvent: null, // 单击触发事件
        onDbClickEvent: null, // 双击触发事件
        title: "", // 标题
        async: true, // 同步/异步
        showFooter: false, // 是否需要页脚
        footColModel: [], // 自定义页脚
        loadedcallback: null, // 回调函数
        // onDataLoaded : null, // 数据加载
        contextMenu: null, // 右键菜单
        onCheckAllEvent: null, // 全选后触发事件
        showWordBreak: false, // 单元格内容是否折行

        css: "table_con_01",
        footer: null,

        userDefineColumn: false, // 自定义列
        target: null, // 绑定的对象

        onLineEdt: false, // 可在线编辑
        onSubmitEvent: null

    };

    // 执行时的临时参数
    this.runtime = {

        // 表格是否最大化
        isMaxed: false,

        // 数据表格是否展开
        isLarge: false,

        normalHeight: null,

        normalWidth: null,

        style: null

    };

    this.op_data = {
        add: [],
        update: [],
        del: []
    };

    this.appcell = null;
    this.oldparam = {};

    $.extend(this.options, opt);
    $.extend(this.oldparam, this.options.param);
    appGrids[this.indexed] = this;
}

AppGrid.prototype = {

    bindTo: function (target, opt) {
        this.options.target = target || this.container;
        this.container = this.options.target;

        if (opt && opt != 'undefined') {
            $.extend(this.options, opt);
            $.extend(this.oldparam, this.options.param);
        }

        var g = new GridModel(this);

        if (!this.render) {
            // 装载配置
            g.loadConfig();

            // 装载数据
            if (this.options["load"]) {
                this.load();
            }
        }

        var loader = this;

        return this;
    },

    // 设置大小
    setSize: function (w, h) {
        try {
            var ww = (typeof w == "undefined" || !w) ? null : (w.toString().indexOf('%') > 0 ? w.toString() : eval(w.toString().replaceAll(
                'px', '')));
            var hh = (typeof h == "undefined" || !h) ? null : (h.toString().indexOf('%') > 0 ? h.toString() : eval(h.toString().replaceAll(
                'px', '')));

            this.dataGrid.style.width = ww ? ww : this.dataGrid.style.width;
            this.dataGrid.style.height = hh ? hh : this.dataGrid.style.height;

            // TODO 加上注释,外部xwin大小变化时,最里面的grid宽度不变 mantis:0009358
            // if (this.options.showHead && this.dataGrid.clientWidth >
            // document.body.clientWidth) {
            var g_w = this.dataGrid.style.width.toString().indexOf('%') > 0 ? Math.round(document.body.clientWidth
                * (parseFloat(this.dataGrid.style.width.toString().replace("%", "")) / 100) - 1) : this.dataGrid.style.width;

            g_w = (typeof g_w == "string") ? g_w.replaceAll("px", "") : g_w;
            // $('tableContainer' + this.indexed).style.width =g_w - 9;
            $('tableContainer' + this.indexed).style.width = this.options.showHead ? g_w - 9 : g_w;
            // }

            if (this.options.showHead && hh) {
                $('tableContainer' + this.indexed).style.height = (hh.toString().replaceAll("px", "") || this.dataGrid.style.height
                        .toString().replaceAll("px", ""))
                    - 34 - (($('butbar') && $('butbar').offsetHeight) || 0);
            }
        } catch (e) {
            // TODO: "【0012613
            // 当鼠标在图片红色位置变成双箭头时，点击左键就会出现另一张图片上的js错误】防止拖动主页框架布局dhxLayout
            // 调整子页面大小时报错问题
        }
    },

    // 获取大小
    getSize: function () {
        return {
            "width": this.dataGrid.offsetWidth,
            "height": this.dataGrid.offsetHeight
        };
    },

    // ---------------------------------------------------------------grid操作接口-------------------------------------------------

    // 删除选择的记录
    delSelected: function () {
        var tb = $("grid_tb" + this.indexed);

        try {
            for (var i = tb.rows.length - 1; i >= 0; i--) {

                if (tb.rows[i].firstChild.firstChild.firstChild.checked) {
                    // 先删数据再删行
                    var data = g_data_storage[this.indexed];
                    // 为保持数据与行rum一致，不能在数据集中直接删除，设null
                    data[tb.rows[i].rum] = null;

                    tb.deleteRow(i);
                }
            }
        } catch (e) {
            alert(e.toString());
        }
    },

    // 获取查询记录数 release
    getRowCnt: function () {
        return g_data_storage[this.indexed] ? g_data_storage[this.indexed].length : '0';
    },

    // 得到指定行数据 release
    getRowData: function (index) {
        var data = g_data_storage[this.indexed];
        return data ? (data[index - 1] || null) : null;
    },

    getAllRowCnt: function () {
        return this.runtime.page ? this.runtime.page.totalCount : 0;
    },

    // 得到选择列的配置PK信息 release
    getSelectPk: function () {
        var retval = [];

        var selects = $("#grid_tb" + this.indexed + " input[type='checkbox'][gridSelect=true][toolbarSelect!=true]");
        if (selects && selects.length == 0) {
            selects = $("#grid_tb" + this.indexed + " input[type='radio'][gridSelect=true][toolbarSelect!=true]");
        }
        for (var i = 0; i < selects.length; i++) {
            var sel = selects[i];
            if (sel.checked == true) {
                retval.push(sel.value);
            }
        }

        return retval;
    },

    // 得到选择列的所有数据 release
    getSelectData: function () {
        var retval = [];

        var selects = $("#grid_tb" + this.indexed + " input[type='checkbox'][gridSelect=true][toolbarSelect!=true]");
        if (selects && selects.length == 0) {
            selects = $("#grid_tb" + this.indexed + " input[type='radio'][gridSelect=true][toolbarSelect!=true]");
        }
        for (var i = 0, sel, tr_obj; i < selects.length; i++) {
            sel = selects[i];
            if (sel.checked == true) {
                tr_obj = sel.parentNode;
                while (tr_obj.tagName != "TR") {
                    tr_obj = tr_obj.parentNode;
                }
                retval.push(g_data_storage[this.indexed][$(tr_obj).attr("rum")]);
            }
        }

        return retval;
    },
    // 获取选择数据(单选)
    getClickData: function () {
        var tr = $("grid_tb" + this.indexed).rows[this.selectRow - 1];
        var data = g_data_storage[this.indexed];

        if (data && tr) {
            return data[$(tr).attr("rum")];
        } else {
            return null;
        }
        // return data ? (data[tb.rows[this.selectRow].rum] || null) : null;
    },

    // 得到所有行数据
    getAllData: function () {
        return g_data_storage[this.indexed];
    },

    /*
     * 设置单元格中的内容 r : 行号， name：coModel中对应的列名, val：值，
     * cssText：自定义css样式文本，overwrite：修改单元格内容是否回写数据，默认回写，当设fasle不回写
     */
    setCellContent: function (r, name, val, cssText, overwrite) {
        var rows = $("grid_tb" + this.indexed).rows;
        var ow = overwrite != null && typeof overwrite != 'undfined' ? overwrite : true;
        if (r >= rows.length) return;

        try {
            var cols = this.options.colModel;

            for (var i = 0, c; i < cols.length; i++) {
                cname = cols[i].name;

                if (name == cname) {
                    var cell_obj = rows[r].cells[i].firstChild.firstChild;

                    if (cell_obj && cell_obj.tagName) {
                        cell_obj.value = val;
                    } else {
                        cell_obj = rows[r].cells[i].firstChild

                        if (typeof cssText != "string" && cssText.tagName) {
                            cell_obj.innerText = "";
                            var valObj = document.createTextNode(val);
                            var oFragment = document.createDocumentFragment();
                            oFragment.appendChild(valObj);
                            oFragment.appendChild(cssText);
                            cell_obj.appendChild(oFragment);
                        } else {
                            cell_obj.innerText = val;
                        }
                    }
                    cell_obj.style.cssText += (";" + (cssText && typeof cssText == "string" ? cssText : ""));
                    break;
                }
            }

            // 回写数据 保持数据一致
            var data = g_data_storage[this.indexed];
            if (data && ow) {
                data[rows[r].rum][name] = val;
            }
        } catch (e) {
        }

    },

    // 设置第几行为选中或非选中
    setChecked: function (v_index, v_checked) {
        var selects = $$("#grid_tb" + this.indexed + " input[type='checkbox']");

        if (selects && selects.length == 0) {
            selects = $$("#grid_tb" + this.indexed + " input[type='radio']");
        }

        if (selects && selects.length != 0) {
            selects[v_index].checked = Boolean(v_checked);
        }
    },

    // 获得第几行选中状态
    isChecked: function (v_index) {
        var selects = $$("#grid_tb" + this.indexed + " input[type='checkbox']");

        if (selects && selects.length == 0) {
            selects = $$("#grid_tb" + this.indexed + " input[type='radio']");
        }

        if (selects && selects[v_index] && selects.length != 0) {
            return selects[v_index].checked;
        } else {
            return false;
        }
    },

    // 清空所有数据
    clear: function () {
        var tb = $("grid_tb" + this.indexed);
        jq("#grid_tb" + this.indexed).html("");

        // 同时清空数据集
        if (g_data_storage[this.indexed]) {
            g_data_storage[this.indexed].length = 0;
        }
        this.op_data["add"].length = this.op_data["update"].length = this.op_data["del"].length = 0;
    },

    /**
     * <pre>
     * 表格装载数据
     * </pre>
     *
     * @param v_param
     *            传入json对象,或者form，div的名称，如果传入form,div等tag名,则会使用form内所有tag做参数
     * @param v_resetPager
     *            是否重新设置翻页计数器，默认为true。
     *
     */
    load: function (v_param, v_resetPager) {
        var reset_ = v_resetPager == false ? false : true;
        var param = {};

        // 分页信息
        if (reset_) {
            var pager = {
                "limit.start": 1,
                "limit.length": this.options.pagesize,
                'limit.count': 0
            };
            $.extend(this.options.param, pager);
        }

        // 如果是页面容器ID 则序列化容器内元素值
        if (typeof v_param == 'string' && v_param != '') {
            param = Form.serialize(v_param, true);
            // 移除button
            var objs = eval(v_param).getElementsByTagName("input");
            for (var i = 0; i < objs.length; i++) {
                if (objs[i].type == "button") {
                    delete param[objs[i].name];
                }
            }
        }

        // JSON对象则直接更新参数
        if (typeof (v_param) == "object" && Object.prototype.toString.call(v_param).toLowerCase() == "[object object]" && !v_param.length) {
            param = v_param;
        }

        $.extend(this.options.param, param);

        var g = new GridModel(this);
        g.loadata();
    },

    // 重装载配置与数据
    reload: function () {
        var g = new GridModel(this);
        g.loadConfig();

        if (this.options["load"]) {
            g.loadata();
        }
    }
};

/*******************************************************************************
 * grid外调接口
 ******************************************************************************/
// 全选事件 release
AppGrid.setAllChecked = function (o, indexed) {

    var loader = o;
    var target = appGrids[indexed];

    // 全部选择
    $.each($("#tableContainer" + indexed + " input[type='checkbox'][gridSelect=true]"), function (i, v_item) {
        v_item.checked = loader.checked;
    });
    if (target.options.onCheckAllEvent && typeof target.options.onCheckAllEvent == 'function') {
        target.options.onCheckAllEvent.call(this);
    }
};

// 右键菜单跳转
AppGrid.showWin = function (ctrl, indexed) {
    var target = appGrids[indexed];
    var arg_obj = null;
    var arg_str = "";

    if (typeof target.options['contextMenu'].param == 'function') {
        arg_obj = eval("(target.options['contextMenu'].param)")();
    }

    if (typeof target.options['contextMenu'].param == 'object') {
        arg_obj = target.options['contextMenu'].param;
    }

    if (arg_obj && typeof arg_obj == 'object') {
        for (prop in arg_obj) {
            arg_str += "&" + prop + "="
                + (/.*[\u4e00-\u9fa5]+.*$/.test(arg_obj[prop]) == true ? encodeURIComponent(arg_obj[prop]) : arg_obj[prop]);
        }
    }

    var title = "";
    try {
        var e_obj = event.srcElement ? event.srcElement : event.target;
        title = e_obj.tagName == "TR" ? e_obj.firstChild.innerText : e_obj.innerText;
    } catch (e) {
        // TODO: handle exception
    }

    AppEngine.UI.xWinEx(ctrl + arg_str, "", target.options['contextMenu'].w || "760", target.options['contextMenu'].h || "550", {
        title: title
    });
};

// 打印
AppGrid.print = function (indexed) {
    var target = appGrids[indexed];
    var div_print = null;

    if (target.runtime.count == null || target.runtime.count <= 0) {
        alert("请先执行表格检索操作后再做数据打印操作!");
        return;
    }

    if (target.options["dataTpl"]) {
        if (!$('grid_print_div')) {
            div_print = document.createElement("div");
            div_print.id = "grid_print_div";
            div_print.style.display = "none";

            document.body.appendChild(div_print);
        }

        var report = new Report(target.options["dataTpl"], "grid_print_div", {
            param: target.options.param,
            onCompleted: function () {
                this.cell.printsheet(1, 0);
            }
        });
    } else {
        var g = new GridModel(target);
        g.print();
    }

};

AppGrid.goPage = function (indexed, start, size) {

    var target = appGrids[indexed];
    var p = Math.ceil(size / target.options.pagesize);

    var pager = {
        "limit.start": start < p ? start : p,
        "limit.length": target.options.pagesize,
        'limit.count': size
    };
    $.extend(target.options.param, pager);

    var g = new GridModel(target);
    g.loadata();
};

AppGrid.sort = function (v_colName, v_callbackName) {

    var v_span = $(event.srcElement).find("[name=grid_sort]");

    // 如果span节点，则用父节点来找
    if (v_span.length == 0) {
        v_span = $(event.srcElement).parent().find("[name=grid_sort]");
    }

    var v_type = "";
    if (v_span.length == 1) {
        var v_item = $(v_span[0]);
        if (v_item.attr("class") == "grid_sort_desc") {
            v_type = "asc";
            v_item.removeClass("grid_sort_desc").addClass("grid_sort_asc");
        } else {
            v_type = "desc";
            v_item.removeClass("grid_sort_asc").addClass("grid_sort_desc");
        }
    }

    if (window[v_callbackName]) {
        window[v_callbackName].call(window, v_colName, v_type);
    }
};

AppGrid.setPageLength = function (obj, indexed) {
    var target = appGrids[indexed];
    var reg = /^\+?[1-9]+([0-9]{1,2})?$/;

    if (!reg.test(obj.value)) {
        alert("请输入1~3位有效数字！");
        obj.value = target.options.pagesize || '20';

    } else {
        if (parseInt(obj.value) > 500) {
            alert("单页显示量上限为500！");
            obj.value = target.options.pagesize || '20';
        }
        target.options.pagesize = obj.value || '20';
    }
};

// 表格的最大化,正常化
AppGrid.resize = function (indexed, flag) {
    var target = appGrids[indexed];

    // 如果没有最大化
    if (!target.runtime.isMaxed) {
        target.runtime.normalHeight = target.options.height;
        target.runtime.style = {
            marginTop: $(target.options.target).style.marginTop || '',
            marginBottom: $(target.options.target).style.marginBottom || '',
            position: $(target.options.target).style.position || ''
        };

        $(target.options.target).style.position = 'absolute';
        $(target.options.target).style.zIndex = 10;
        $(target.options.target).style.marginTop = '0px';
        $(target.options.target).style.marginBottom = '0px';
        $(target.options.target).style.left = '0px';
        $(target.options.target).style.top = '0px';

        // 重新设置表格的高
        target.options.height = 'document.body.clientHeight';
    } else {
        for (prop in target.runtime.style) {
            $(target.options.target).style[prop] = target.runtime.style[prop];
        }

        target.options.height = target.runtime.normalHeight;
    }

    target.runtime.isMaxed = !target.runtime.isMaxed;
    target.setSize(null, target.options.height);

};

/**
 * 导出数据为excel<br>
 * v_exportAll:true(导出全部数据);false:导出当页数据<br>
 * v_index:表格的索引号<br>
 * v_type:导出格式
 */
AppGrid.exported = function (v_obj, v_index) {

    var target = appGrids[v_index];
    var div_exp = null;
    var type = "xls";

    // 如果表格中没有数据
    if (target.runtime.count == null || target.runtime.count <= 0) {
        alert("无相关数据需要导出!");
        return;
    }

    // 如果设置了cell数据模板则用定义的cell模板导出
    if (target.options["dataTpl"]) {
        if (!$('grid_exp_div')) {
            div_exp = document.createElement("div");
            div_exp.id = "grid_exp_div";
            div_exp.style.display = "none";

            document.body.appendChild(div_exp);
        }

        var report = new Report(target.options["dataTpl"], "grid_exp_div", {
            param: target.options.param,
            onCompleted: function () {
                this.exportxls();
            }
        });

    } else {
        var v_params = {};

        v_params["functionCode"] = target.options.gridId ? target.options.gridId : target.options.url;

        if (!v_params["functionCode"] || v_params["functionCode"].indexOf(".") > 0) {
            alert("暂不支持非交易代码配置！");
            return;
        }

        // 如果是esb服务
        v_params["functionCode"] += target.options.esb ? "@ESB" : "";

        // 如果初始化是自定义列信息，并非交易代码配置， 则导出沿用自定义列信息
        if (!target.options.gridId) {
            var cols = target.options.colModel;
            var name_buff = [];
            var code_buff = [];

            for (var i = 0, col; i < cols.length; i++) {
                col = cols[i];

                if (col.name != "tpl") {
                    name_buff.push(col.display);
                    code_buff.push(col.name);
                }
            }

            v_params["gridExportColumnName"] = name_buff.join(",");
            v_params["gridExportColumnCode"] = code_buff.join(",");
        }

        var v_param = target.options.param;

        // 超过系统指定行用csv格式
        if (target.runtime.count > 100000) {
            if (confirm("数据量过大将改用csv格式导出，是否继续？")) {
                type = "csv";
            }
        }

        v_param["limit.start"] = 1;
        v_param["limit.length"] = target.runtime.count;
        v_params["gridExportType"] = type;

        // 塞入单元格的查询参数
        $.extend(v_params, v_param);

        // post方式提交表单导出
        AE.Form.exportDataPOST("GridService.export", v_params);

        /*
         * v_obj.target = '_blank'; v_obj.href =
         * "invoke.do?FRAMEserviceName=GridService.export&FRAMEactionMode=2&FRAMEinvokeSeqno=" +
         * Utils.createUUID() + "&FRAMEparams=" +
         * encodeURIComponent(Object.toJSON(v_params || {}));
         */
    }
};

// -----------------------------------用户自定义表格显示列----------------------------------------
AppGrid.Config = {};

AppGrid.Config.move = function (v_src_name, v_desc_name) {

    var v_src_ops = $A($(v_src_name).options);

    if ($(v_src_name).selectedIndex >= 0) {

        v_select = v_src_ops[$(v_src_name).selectedIndex];

        // 对于多选,单选框之类的,含有tpl的不能移动
        if (!v_select.unmove) {
            var v_desc_op = document.createElement('option');
            v_desc_op.text = v_select.text;
            v_desc_op.value = v_select.value;
            $(v_desc_name).add(v_desc_op);
            $(v_src_name).remove($(v_src_name).selectedIndex);
        } else {
            alert("此列数据关闭显示会影响使用!");
        }
    }
};

AppGrid.Config.sort = function (v_src_name, arg) {

    var index = $(v_src_name).selectedIndex || -1;
    var v_opt = null;

    if (index >= 0) {
        var i_ = index + parseInt(arg);

        if (i_ < $(v_src_name).options.length && i_ > 0) {
            v_opt = {
                value: $(v_src_name).options[index].value || '',
                text: $(v_src_name).options[index].text || ''
            };

            $(v_src_name).options[index].text = $(v_src_name).options[i_].text || '';
            $(v_src_name).options[index].value = $(v_src_name).options[i_].value || '';

            $(v_src_name).options[i_].text = v_opt.text;
            $(v_src_name).options[i_].value = v_opt.value;

            $(v_src_name).selectedIndex = i_;
        }
    }
    v_opt = null;
};

/**
 * 用户设置列装载信息
 */
AppGrid.Config.load = function () {

    AE.ServiceEx("GridService.getShowColumns", {
        gridId: $F("gridId")
    }, function (v_columns) {

        for (var i = 0; i < v_columns.length; i++) {

            /*
             * var v_show = true;
             *
             * $A($("cfg_unshow").options).each(function(v_data) { if
             * (v_data.text == v_columns[i].display) { v_show = false; }
             * });
             *
             * if (v_show) {
             */

            var v_option = document.createElement('option');
            v_option.text = v_columns[i].display;
            v_option.value = v_columns[i].name;

            // 如果存在checkbox,radio,tpl的不允许关闭显示
            if (v_columns[i].type || v_columns[i].tpl) {
                v_option.unmove = true;
            } else {
                v_option.unmove = false;
            }

            $('cfg_show').add(v_option);
            // }
        }
    });

    // 不显示的列
    AE.ServiceEx("GridService.getUnShowColumns", {
        gridId: $F("gridId")
    }, function (v_columns) {
        // 显示的表格列
        for (var i = 0; i < v_columns.length; i++) {
            var v_option = document.createElement('option');
            v_option.text = v_columns[i].display;
            v_option.value = v_columns[i].name;

            // 如果存在checkbox,radio,tpl的不允许关闭显示
            if (v_columns[i].type || v_columns[i].tpl) {
                v_option.unmove = true;
            } else {
                v_option.unmove = false;
            }

            $('cfg_unshow').add(v_option);
        }
    });

};

/**
 * 用户自定义设置列保存
 */
AppGrid.Config.save = function () {

    var v_showColumns = "";

    $A($("cfg_show").options).each(function (data) {
        v_showColumns = v_showColumns + data.text + ",";
    });

    AE.ServiceEx("GridService.saveUserDefColumns", {
        gridId: $F("gridId"),
        showColumns: v_showColumns
    }, function (data) {

        alert("用户自定义列信息保存成功");

        // 刷新父窗口表格表格
        if (parent.appGrids[$F("gridIndex")]) {
            parent.appGrids[$F("gridIndex")].reload();
        }

        // 关闭自身
        AE.UI.closexWin();
    });

};
