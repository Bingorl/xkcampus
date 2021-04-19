function Tree(v_item, v_url, v_options) {

    // 对外参数
    var _options = {
        lazy: false,
        enableCheck: false,
        showIcon: true,
        // 如果有复选框
        // 设置所选状态是否需要父子关联：默认（checked：“ps”选中父子都关联，unchecked：“ps”未选中父子也都关联）只是父关联则2个可设为p，只是子关联则可设为s，
        // 都不需关联则都设为空字符串即可“”
        checkType: {
            checked: "ps",
            unchecked: "ps"
        },
        fontCss: {},
        f_loaded: null,
        f_alwaysLoaded: null,
        f_checked: null,
        f_click: null,
        f_dbclick: null,
        f_move: null,
        addDiyDom: null,
        addHoverDom: null,
        removeHoverDom: null
    };

    $.extend(_options, v_options);

    if (typeof v_item == "string") {

        if ($("#" + v_item).length == 0) {
            alert("找到不此id对应的对象");
            return;
        }
        v_item = $("#" + v_item);
    } else {
        alert("第一个参数必须为div的id值");
        return;
    }

    // 组织ztree的CSS
    var v_realId = Utils.createUUID();

    v_item.html("<ul id='" + v_realId + "' class='ztree'></ul>");

    this.options = {
        tree: null,
        item: $("#" + v_realId),
        url: v_url + (v_url.indexOf("?") > -1 ? "&" : "?") + "lazy=" + _options.lazy,
        isFirstLoad: true
    };

    var setting = {
        async: {
            enable: true,
            url: this.options.url,
            autoParam: ["id", "lazy=" + _options.lazy, "level=lv"],
            dataFilter: function (treeId, parentNode, childNodes) {
                return childNodes;
            }
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        check: {
            enable: _options.enableCheck,
            chkboxType: {
                "Y": _options.checkType.checked,
                "N": _options.checkType.unchecked
            }
        },
        edit: {
            enable: true,
            showRemoveBtn: false,
            showRenameBtn: false,
            drag: {
                isCopy: false,
                isMove: false
            }
        },
        callback: {

            // 增加不允许移动到自身节点的判断
            beforeDrop: function (treeId, treeNodes, targetNode, moveType, isCopy) {

                if (treeNodes[0].id == targetNode.id) {
                    alert("不允许移动到自身下!");
                    return false;
                }
            }
        },
        view: {
            dblClickExpand: false,
            expandSpeed: (jq.browser.msie && parseInt(jq.browser.version) <= 6) ? "" : 100,
            addDiyDom: _options.addDiyDom || null,
            addHoverDom: _options.addHoverDom || null,
            removeHoverDom: _options.removeHoverDom || null,
            fontCss: _options.fontCss,
            showIcon: _options.showIcon
        }
    };

    // 设置回调函数
    var v_loader = this;

    setting.callback["onAsyncSuccess"] = function (event, treeId, treeNode, msg) {
        try {

            // 第一次加载数据完成执行
            if (_options.f_loaded && v_loader.options.isFirstLoad) {
                _options.f_loaded.call(v_loader);
            } else {
                // 每次刷新节点都执行
                if (_options.f_alwaysLoaded) {
                    _options.f_alwaysLoaded.call(v_loader);
                }
            }
        } catch (e) {
            alert("f_loaded回调函数异常" + e);
        } finally {
            v_loader.options.isFirstLoad = false;
        }
    };

    setting.callback["onCheck"] = function (event, treeId, treeNode) {
        try {
            if (_options.f_checked) {
                _options.f_checked.call(v_loader, treeNode.id, treeNode.checked);
            }
        } catch (e) {
            alert("f_checked回调函数异常" + e);
        }
    };

    setting.callback["onDrop"] = function (event, treeId, treeNodes, targetNode, moveType, isCopy) {

        try {
            if (_options.f_move && targetNode && moveType) {

                var v_parentNodeId = null;

                // 得到父节点id
                if (moveType == "inner") {
                    v_parentNodeId = targetNode.id;
                } else if (moveType == "prev" || moveType == "next") {
                    v_parentNodeId = v_loader.getParentId(targetNode.id);
                }

                _options.f_move.call(v_loader, treeNodes[0].id, v_parentNodeId);
                v_loader.reload(v_parentNodeId);
            }
        } catch (e) {
            alert("f_move回调函数异常" + e);
        }
    };

    setting.callback["onClick"] = function (event, treeId, treeNode, clickFlag) {

        try {
            // 如果节点没有打开,单击打开节点
            if (!treeNode.open) {
                v_loader.options.tree.expandNode(treeNode);
            }

            if (_options.f_click) {
                _options.f_click.call(v_loader, treeNode.id);
            }
        } catch (e) {
            alert("f_click回调函数异常" + e);
        }
    };

    setting.callback["onDblClick"] = function (event, treeId, treeNode) {

        try {
            if (treeNode && _options.f_dbclick) {
                _options.f_dbclick.call(v_loader, treeNode.id);
            }
        } catch (e) {
            alert("f_dbclick回调函数异常" + e);
        }
    };

    this.options.tree = jq.fn.zTree.init(jq(this.options.item), setting);
}

Tree.prototype = {

    getParentId: function (nodeId) {
        var node = this.options.tree.getNodeByParam("id", nodeId, null);
        if (node && node.getParentNode()) {
            return node.getParentNode().id;
        } else {
            return null;
        }
    },

    getSelectId: function () {
        if (this.options.tree.getSelectedNodes().length > 0) {
            return this.options.tree.getSelectedNodes()[0].id;
        }
    },

    /**
     * 是否存在子节点
     */
    hasChildren: function (nodeId) {
        var node = this.options.tree.getNodeByParam("id", nodeId, null);
        return node ? node.isParent : false;
    },

    /**
     * 设置选中
     *
     * @param 节点id
     * @param 是否选中
     * @param 是否调用回调函数,默认不调用
     */
    setCheck: function (nodeId, checked, callCallback) {
        var node = this.options.tree.getNodeByParam("id", nodeId, null);
        if (node) {
            this.options.tree.checkNode(node, checked, true, typeof (callCallback) !== 'undefined' ? callCallback : true);
        }
    },

    /**
     * 得到所有子节点id
     */
    getChildIdArray: function (nodeId) {
        var childNodes = this.options.tree.getNodeByParam("id", nodeId, null).children;

        var childIdArray = new Array();

        if (childNodes && childNodes.length > 0) {
            for (var i = 0; i < childNodes.length; i++) {

                childIdArray.push(childNodes[i].id);

                if (childNodes[i].isParent) {
                    childIdArray = childIdArray.concat(this.getChildIdArray(childNodes[i].id));
                }
            }
        }

        return childIdArray;
    },

    /**
     * 得到所有父节点id
     */
    getParentIdArray: function (nodeId) {

        var parentIdArray = new Array();

        var parentId = this.getParentId(nodeId);

        if (parentId != null) {
            parentIdArray.push(parentId);

            var _parentArray = this.getParentIdArray(parentId);
            if (_parentArray.length > 0) {
                parentIdArray = parentIdArray.concat(_parentArray);
            }
        }

        return parentIdArray;
    },

    getNodeData: function (nodeId, paramName) {
        var node = this.options.tree.getNodeByParam("id", nodeId, null);
        if (node && typeof (node[paramName]) != 'undefined') {
            return node[paramName];
        }
        return null;
    },

    getNode: function (nodeId) {
        return this.options.tree.getNodeByParam("id", nodeId, null);
    },

    getSelectNodes: function () {
        return this.options.tree.getCheckedNodes(true);
    },

    isExists: function (nodeId) {
        var node = this.options.tree.getNodeByParam("id", nodeId, null);
        return (typeof (node) != 'undefined' && node != null);
    },

    openNode: function (nodeId, opened, sonSign) {
        var node = this.options.tree.getNodeByParam("id", nodeId, null);
        if (node) {
            this.options.tree.expandNode(node, opened, sonSign || false, true, true);
        }
    },

    enableMove: function (enabled) {
        this.options.tree.setting.edit.drag.isMove = enabled;
    },

    reload: function (nodeId) {

        var node = null;
        if (nodeId) {
            node = this.options.tree.getNodeByParam("id", nodeId, null);
        }
        this.options.tree.reAsyncChildNodes(node, "refresh");
    }

};