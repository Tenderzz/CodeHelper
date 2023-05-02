define(
    ["pop", "eventcenter"],
    function (pop, eventcenter) {
        function index(container) {
            this.name = "index";
            this.container = container;
            var _that = this;


            this.ec = new eventcenter('debug');
            this.ec.addHandler(this);


            this.init = function () {

                var user = localStorage.getItem('user');
                user = JSON.parse(user);
                if (user != null) {
                    $("#pops").hide();
                    $("#username").html(user.username);
                    $(".username-li").show();
                }

                //初始化弹窗组件
                this.popup = new pop().init();

                (function (obj, id, callback) {
                    $('#' + id).click(
                        callback
                    )
                })(this,
                    'loginbutton',
                    _that.button1Click
                );

                (function (obj, id, callback) {
                    $('#' + id).click(
                        callback
                    )
                })(this,
                    'logout',
                    _that.logoutbuttunClick
                );

            }


            //this.popup.pop()弹出弹窗,四个参数, id，弹窗组件ID， size宽度，PopButtons按键处理数组， mask是JS数组, 前面是透明度，颜色，或者为""
            //_that.popup.pop("secpop", "580", _that.secpopPopButtons, "");
            //<!--popup button event handler--> 
            this.firstpopconfirmHandler = function () {
                var user =
                    {
                        "account": $('#input-account').val(),
                        "password": $('#input-password').val()
                    };

                $.post(
                    '/user/login',
                    user,
                    function (rst) {
                        if (rst.code === 1) {
                            localStorage.setItem('user', JSON.stringify(rst.data));//将用户信息转为json存放在浏览器
                            window.location.href = "/index";
                        } else {
                            alert(rst.msg);
                        }

                    }
                )

            }
            //<!--popup buttons-->
            this.firstpopPopButtons = [
                {id: "confirm", handler: this.firstpopconfirmHandler},
            ];

            this.logoutbuttunClick = function () {
                $.post(
                    '/user/logout',
                    function (rst) {
                        if (rst.code === 1) {
                            localStorage.removeItem('user');
                            window.location.href = "/index";
                            alert("退出成功！");
                        } else {
                            alert(rst.msg);
                        }

                    }
                )
            }

            this.button1Click = function () {
                _that.popup.pop("loginpop", "450", _that.firstpopPopButtons, "");
            };
            this.exports = {}

        }

        return index;
    }
)