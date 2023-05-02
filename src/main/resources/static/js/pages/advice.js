define(
    ["editor", "inputwidgets", "eventcenter"],
    function (editor, inputwidgets, eventcenter) {
        function advice(container) {
            this.name = "advice";
            this.container = container;
            var _that = this;


            this.ec = new eventcenter('debug');
            this.ec.addHandler(this);


            this.init = function () {

                //初始化编辑器
                new editor(this.container, this.ec).init();
                (function (obj, id, callback) {
                    $('#' + id).click(
                        callback
                    )
                })(this,
                    'advice-submit',
                    _that.advicebuttonClick
                );
            }


            // this.editorChange = function (data) {
            //     console.log(data);
            // }
            this.advicebuttonClick = function () {
                var user = localStorage.getItem('user');
                user = JSON.parse(user);
                if (user === null) {
                    alert("请先登录！")
                }
                var content=$("#advice").text().trim();
                var user=localStorage.getItem('user');
                user=JSON.parse(user);
                console.log(content);
                var advice= {
                    "account": user.account,
                    "username":user.username,
                    "content": content
                }
                $.post(
                    '/user/adviceacc',
                    advice,
                    function (rst) {
                        if(rst.code===1){
                            alert("提议成功~")
                            window.location.href = "/advice";
                        }
                    }
                )
            };
            this.exports = {
                // 'editorchanged': this.editorChange,
            }

        }

        return advice;
    }
)