// ==UserScript==
// @name         buglist
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        http://192.168.0.100/buglist.cgi?*
// @match        http://192.168.0.100/show_bug.cgi?*
// @grant        none
// @require      http://code.jquery.com/jquery-3.2.1.min.js
// ==/UserScript==

(function() {
    'use strict';
    var user = 'fkong@jhinno.com';
    $("#banner").append("我的Bugs<input id='mybugs' type='checkbox'/>");
    $("#mybugs").change(function(){
        $(".bz_bugitem").each(function(i) {
            //console.log($(this).find("span[title='fkong@jhinno.com']"));
        });
        if($("#mybugs").is(':checked') === true) {
            $(".bz_bugitem").each(function(i) {
                $(this).find("td[class='bz_assigned_to_column']").find("span[title!='"+user+"']").each(function(i) {
                    $(this).parent().parent().css('display', 'none');
                });
            });
        } else {
            $(".bz_bugitem").each(function(i) {
                $(this).find("td[class='bz_assigned_to_column']").find("span[title!='"+user+"']").each(function(i) {
                    $(this).parent().parent().show();
                });
            });
        }
    });

    $("span[title='"+user+"']").each(function(i) {
        $(this).parent().parent().css("background-color","#00FF00");
    });

    $("pre[class='bz_comment_text']").each(function(i) {
        $(this).css("width","100%");
    });
})();
