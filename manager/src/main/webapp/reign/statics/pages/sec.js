/**
 * Created by ji on 16-5-23.
 */

$.solders = new Array();

$.init = function () {

    //调整各部分宽高
    var mainHeight = $(document.body).height() - 45;
    $('#main_content,#log').height(mainHeight);


    //初始化伞兵和箱子
    var dcHeight = $('#main_content').height();
    var dcWidth = $('#main_content').width();
    for (i = 1; i < 10; i++) {
        $.solders.push('solder' + i);
        var randomX = Math.floor(Math.random() * dcWidth + 1);
        var randomY = Math.floor(Math.random() * dcHeight + 1);
        $('#content').append('<div id="solder' + i + '" class="element" style="top: ' + randomY + 'px;left:' + randomX + 'px">solder' + i + '</div>')
    }

    var desX = Math.floor(Math.random() * dcWidth + 1);
    var desY = Math.floor(Math.random() * dcHeight + 1);
    $('body').append('<img src="/reign/statics/desc.jpg" id="target" class="target" style="top: ' + desY + 'px;left:' + desX + 'px">')
}

$.getLogs = function () {
    $.ajax({
        url: "/api/getLog",
        type: "POST",
        dataType: "JSON",
        success: function (data) {
            if (data.code == 0) {
                $.each(data.rows, function (i, value) {
                    $('#log').prepend(value + '\n');
                })
            }
        }
    });
}


//判断是否停止
$.stop = false
//计数到达箱子旁边的士兵数量
$.reached = 0

$(document).ready(function () {

    $.init()

    setInterval(function () {
        $.getLogs()
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
        $.stop=false
        $.each($.solders, function (i, v) {
            move(v)
        })
    })

    /**
     * 具体移动过程
     */
    function move(solderName) {
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
        var stepLen = xlen / absXlen


        timeId = setInterval(function () {
            if ($.stop == true) {
                clearInterval(timeId)
            }
            doStep()
        }, 13)


        function doStep() {
            solderX = solderX + stepLen
            solder.css({
                position: "absolute",
                left: solderX,
                top: a * (solderX) + b
            })
            if ((Math.abs(solderX - targetX)) < 5) {
                $.reached = $.reached + 1;
                if ($.reached >= 1) {
                    $.stop = true
                }
                $.solders.splice($.inArray(solderName, $.solders), 1);
                console.log('士兵' + solderName + '找到箱子')
                clearInterval(timeId)
            }
        }

        function toInteger(text) {
            text = parseInt(text);
            return isFinite(text) ? text : 0;
        }
    }
})
