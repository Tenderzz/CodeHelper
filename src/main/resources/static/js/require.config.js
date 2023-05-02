/**
 *
 */
require.config({
    baseUrl: '/js/third-party',
    paths: {
        "jquery": "jquery",

        //<!--third-party-->
        "layui": "layui/layui.all",

        //<!--component-->
        formbutton: "../components/FormButton",
        inputwidgets: "../components/InputWidgets",
        dataverifier: "../components/DataVerifier",
        editor: "../components/Editor",
        selector: "../components/Selector",
        datepicker: "../components/DatePicker",
        "eventcenter": "../components/EventCenter",
        "pop": '../components/Popup',

        //<!--pages-->
        "index": "../pages/index",
        "advice": "../pages/advice",
        "code": "../pages/code",
    },
    shim: {
        //<!--shim-->
        'layer': {
            deps: ['jquery'],
            exports: 'layer'
        },
        'layui': {
            deps: ["jquery"],
            exports: "layui"
        },
    }
});


require(["jquery"], function ($) {

    var currentPage = $("#current-page").attr("current-page");
    var targetModule = $("#current-page").attr("target-module");
    switch (targetModule) {
        //<!--page init-->
        case 'index':
            require(['index'], function (index) {
                new index($('#container')).init();
            });
            break;
        case 'advice':
            require(['advice'], function (advice) {
                new advice($('#container')).init();
            });
            require(['index'], function (index) {
                new index($('#container')).init();
            });
            break;
        case 'code':
            require(['code'], function (code) {
                new code($('#container')).init();
            });
            require(['index'], function (index) {
                new index($('#container')).init();
            });
    }
    return;
});