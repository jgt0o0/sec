/**
 * Created by ji on 16-5-23.
 */
$.solders = new Array();

$.onlineSolders = new Array();

//判断是否停止
$.stop = false
//计数到达箱子旁边的士兵数量
$.reached = 0

$.init = function () {
    //调整各部分宽高
    var mainHeight = $(document.body).height() - 45;
    $('#main_content,#log_div').height(mainHeight);
}

$.getSolders = function () {
    //初始化伞兵和箱子
    var dcHeight = $('#main_content').height() - 30;
    var dcWidth = $('#main_content').width() - 30;

    var desX = Math.floor(Math.random() * dcWidth + 1);
    var desY = Math.floor(Math.random() * dcHeight + 1);
    $('#content').append('<img src="/reign/statics/desc.jpg" id="target" class="target" style="top: ' + desY + 'px;left:' + desX + 'px">')

    $.ajax({
        url: "/api/getSolders",
        type: "POST",
        dataType: "JSON",
        success: function (data) {
            if (data.code == 0) {
                if (data.rows) {
                    var table = "<table class='table table-striped table-bordered table-hover' style='height: 100%;width: 100%'>";
                    table += "<tr><td></td>";
                    $.each(data.rows, function (j, tmpS) {
                        table += "<td id=c_c_" + tmpS.name + ">" + tmpS.name + "</td>";
                    });
                    table += "</tr>"

                    $.each(data.rows, function (i, solder) {
                        $.solders.push(solder.name);
                        var randomX = Math.floor(Math.random() * dcWidth + 1);
                        var randomY = Math.floor(Math.random() * dcHeight + 1);
                        $('#content').append('<div id="' + solder.name + '" class="element" style="top: ' + randomY + 'px;left:' + randomX + 'px">' + solder.name + '</div>');

                        table += "<tr><td id='r_r_" + solder.name + "'>" + solder.name + "</td>";
                        $.each(data.rows, function (j, tmpS) {
                            table += "<td id=" + solder.name + "_" + tmpS.name + "></td>";
                        });
                        table += "</tr>"
                    })
                    table += "</table>"
                    $('#connect_div').append(table)
                }
            }
        }
    });
}

$.getOnlineSolder = function () {
    $.ajax({
        url: "/api/getOnlineSolder",
        type: "POST",
        dataType: "JSON",
        success: function (data) {
            if (data.code == 0) {
                var tmpArray = new Array();
                if (data.obj) {
                    $.each(data.obj, function (solderName, publicKey) {
                        $('#' + solderName).attr('class', 'element online');
                        tmpArray.push(solderName);
                        $.onlineSolders.splice($.inArray(solderName, $.onlineSolders), 1)
                    });
                }
                if ($.onlineSolders) {
                    $.each($.onlineSolders, function (i, solderName) {
                        $('#' + solderName).attr('class', 'element');
                    });
                }
                $.onlineSolders = tmpArray;
            }
        }
    });
}

$.getAuthInfo = function () {
    $.ajax({
        url: "/api/getAuthInfo",
        type: "POST",
        dataType: "JSON",
        success: function (data) {
            if (data.code == 0) {
                if (data.obj) {
                    $.each(data.obj, function (key, value) {
                        if (value == '1') {
                            $('#' + key).html("<i class='icon-ok green'></i>");
                        } else {
                            $('#' + key).html("");
                        }
                    });
                }
            }
        }
    });
}

$.getOpenBoxRequest = function () {
    $.ajax({
        url: "/api/getOpenBoxRequest",
        type: "POST",
        dataType: "JSON",
        success: function (data) {
            if (data.code == 0) {
                if (data.rows) {
                    $.each(data.rows, function (i, solder) {
                        $.move(solder)
                    })
                }
            }
        }
    });
}

$.getLogs = function () {
    $.ajax({
        url: "/api/getLog",
        type: "POST",
        dataType: "JSON",
        success: function (data) {
            if (data.code == 0) {
                if (data.rows) {
                    $.each(data.rows, function (i, value) {
                        $('#log').prepend(value + '\n');
                    })
                }
            }
        }
    });
}

/**
 * 具体移动过程
 */
$.move = function (solderName) {
    var targetEle = $('#target');
    var targetX = toInteger(targetEle.css('left'))
    var targetY = toInteger(targetEle.css('top'))

    var solder = $('#' + solderName)
    var solderX = toInteger(solder.css('left'));
    var solderY = toInteger(solder.css('top'))

    console.log("sourceX:" + solderX + ",sourceY:" + solderY)

    console.log("targetX:" + targetX + ",targetY:" + targetY);

    var a = (solderY - targetY) / (solderX - targetX)
    var b = solderY - a * solderX

    var timeId;
    var xlen = targetX - solderX;
    var absXlen = Math.abs(xlen);
    var stepLen = xlen / absXlen;


    timeId = setInterval(function () {
        if ($.stop == true) {
            clearInterval(timeId)
        }
        doStep()
    }, 13)


    function doStep() {
        solderX = solderX + stepLen
        solderY = a * (solderX) + b
        solder.css({
            position: "absolute",
            left: solderX,
            top: solderY
        })
        if ((Math.abs(solderX - targetX)) < 5 && Math.abs(solderY - targetY) < 5) {
            $.reached = $.reached + 1;
            if ($.reached >= 1) {
                //$.stop = true
            }
            $.solders.splice($.inArray(solderName, $.solders), 1);
            console.log('士兵' + solderName + '找到箱子')
            clearInterval(timeId)
            $.openBox(solderName);
        }
    }

    function toInteger(text) {
        text = parseInt(text);
        return isFinite(text) ? text : 0;
    }
}

$.openBox = function (solderName) {
    $.ajax({
        url: "/api/openBox",
        type: "POST",
        dataType: "JSON",
        data: {message: '{"name": "' + solderName + '"}'},
        success: function (data) {
            if (data.code == 0) {
                if (data.rows) {
                    $.each(data.rows, function (i, value) {
                        $('#log').prepend(value + '\n');
                    })
                }
            }
        }
    });
}


$(document).ready(function () {

    $.init()

    $.getSolders()

    setInterval(function () {
        $.getOnlineSolder()
    }, 500)

    setInterval(function () {
        $.getLogs()
    }, 500)

    setInterval(function () {
        $.getAuthInfo()
    }, 500)


    setInterval(function () {
        $.getOpenBoxRequest()
    }, 500)
    /**
     * 初始化伞兵位置
     */

    $('#init').click(function () {
        $.init();
    })
    /**
     * 标记集结位置
     */
    $('#des').click(function () {
        $('body').css('cursor', 'crosshair').bind('dblclick', function (e) {
            $('body').css('cursor', 'default').unbind('dblclick')
            $('body').append('<img src="/reign/statics/desc.jpg" id="target" class="target" style="top: ' + e.pageY + 'px;left:' + e.pageX + 'px">')
        })
    })

    $(document).mousemove(function (e) {
        $("span").text("X: " + e.pageX + ", Y: " + e.pageY);
    });

    /**
     * Esc 键取消标记
     */
    $(document).keyup(function (event) {
        switch (event.keyCode) {
            case 27:
                $('body').css('cursor', 'default').unbind('dblclick')
        }
    });

    /**
     * 集结命令下达后,开始集结
     */
    $('#move').click(function () {
        $.stop = false
        $.each($.solders, function (i, v) {
            $.move(v)
        })
    })

})
