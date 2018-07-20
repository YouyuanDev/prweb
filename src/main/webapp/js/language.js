/**
 * cookie操作
 */
var getCookie = function(name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        var path = options.path ? '; path=' + options.path : '';
        var domain = options.domain ? '; domain=' + options.domain : '';
        var s = [cookie, expires, path, domain, secure].join('');
        var secure = options.secure ? '; secure' : '';
        var c = [name, '=', encodeURIComponent(value)].join('');
        var cookie = [c, expires, path, domain, secure].join('')
        document.cookie = cookie;
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};
var i18nLanguage = "zh-CN";
var webLanguage = ['zh-CN','en'];
var execI18n = function(languagePath){
    //获取用户浏览器之前选择的语言类型

    if(getCookie("userLanguage")){
        i18nLanguage=getCookie("userLanguage");
    }else{
        //获取浏览器语言
        var navLanguage=$.i18n.browserLang();
        //判断是否支持语言数组
        if($.inArray(navLanguage,webLanguage)>-1){
            i18nLanguage=navLanguage;
            //缓存语言选择
            getCookie("userLanguage",navLanguage);
        }else{
            navLanguage='en';
        }
    }
    jQuery.i18n.properties({
        name : 'strings', //资源文件名称
        language:i18nLanguage,
        path :"../i18n/",
        mode : 'map', //用Map的方式使用资源文件中的值
        callback : function() {//加载成功后设置显示内容
            var insertEle = $(".i18n");
            insertEle.each(function() {
                $(this).attr("title",$.i18n.prop($(this).attr('name')));
            });
            var insertEle1 = $(".i18n1");
            insertEle1.each(function() {
                $(this).text($.i18n.prop($(this).attr('name')));
            });
            var insertEle2 = $(".i18n2");
            insertEle2.each(function() {
                $(this).children('.l-btn-left').children('.l-btn-text').text($.i18n.prop($(this).attr('name')));
            });
        }
    });
        
}
function getLanguage() {
    var language;
    if(getCookie("userLanguage")){
        language=getCookie("userLanguage");
    }else{
        //获取浏览器语言
        var navLanguage=$.i18n.browserLang();
        //判断是否支持语言数组
        if($.inArray(navLanguage,webLanguage)>-1){
            language=navLanguage;
            //缓存语言选择
            getCookie("userLanguage",navLanguage);
        }else{
            language='en';
        }
    }
    return language;
}
/*页面执行加载执行*/
function hlLanguage(languagePath) {
    execI18n(languagePath);
    /*将语言选择默认选中缓存中的值*/
    $("#language option[value="+i18nLanguage+"]").attr("selected",true);

    /* 选择语言 */
    $("#language").bind('change', function() {
        var language = $(this).children('option:selected').val();
        getCookie("userLanguage",language,{
            expires: 30,
            path:'/'
        });
        location.reload();
    });
}
