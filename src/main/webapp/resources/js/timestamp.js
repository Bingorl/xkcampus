function add0(m) {
    return m < 10 ? '0' + m : m;
}

//时间戳转化时间
function formatDate(timestamp) {
    if (timestamp != null && timestamp != "") {
        var now = new Date(timestamp * 1000);
        var year = now.getFullYear();
        var month = now.getMonth() + 1;
        var date = now.getDate();
        var hour = now.getHours();
        var minute = now.getMinutes();
        var second = now.getSeconds();
        return year + "-" + add0(month) + "-" + add0(date) + "   " + add0(hour) + ":" + add0(minute) + ":" + add0(second);
    } else {
        return "";
    }
}

//时间转化时间戳
function get_unix_time(dateStr) {
    var newstr = dateStr.replace(/-/g, '/');
    var date = new Date(newstr);
    var time_str = date.getTime().toString();
    return time_str.substr(0, 10);
}

//获取当前时间戳
function gettimestamp() {
    return (new Date().getTime() + "").substring(0, 10);
}