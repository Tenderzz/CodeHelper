define(
    function () {
        function code(container) {
            this.name = "code";
            this.container = container;
            var _that = this;
            this.init = function () {
                var user = localStorage.getItem('user');
                user = JSON.parse(user);
                var index = 0;
                var funpropindex = 0;
                var dataid;
                //查询已有的项目 如果有 则显示在页面
                if (user != null) {
                    var account = {
                        "account": user.account
                    };
                    $.post(
                        "/proj/",
                        account,
                        function (rst) {
                            $.each(rst, function (i, project) {
                                _that.sort();
                                index++;
                                // console.log(project.projname)
                                project.id--;
                                var hadprojhtm = "<div class=\"proj\" data_index='" + project.id + "' data_uuid=\"" + project.projid + "\" data_id=\"" + (project.id) + "\">\n" +
                                    "                <p class=\"delproj\" data_index='" + project.id + "'>X</p>\n" +
                                    "                <p class='projname' data_index='" + project.id + "' >" + project.projname + "</p>\n" +
                                    "                <p class=\"showproj\" data_index='" + project.id + "'>edit</p>\n" +
                                    "            </div>";

                                $('#projs').append(hadprojhtm);

                                var hadcodehtm = '            <div class="side-right-content" style="display: none;" data_index="' + project.id + '">\n' +
                                    '                <div class="nav" id="codenav">\n' +
                                    '                    <div class="item active"><i>Project</i></div>\n' +
                                    '                    <div class="item"><i>Bean</i></div>\n' +
                                    '                    <div class="item"><i>Page</i></div>\n' +
                                    '                </div>\n' +
                                    '                <div class="content" id="codecontent">\n' +
                                    '                    <section class="active">' +
                                    '<input data_index="' + project.id + '" class="tempprojname" type="text" placeholder="' + project.projname + '">' +
                                    '<input data_index="' + project.id + '" class="code" type="text" placeholder="' + project.content + '">' +
                                    '<button class="tempsave">save</button> ' +
                                    '<button class="gocode">Let\'s go!</button> ' +
                                    '</section>\n' +
                                    '                    <section><span>2</span></section>\n' +
                                    '                    <section><span>3</span></section>\n' +
                                    '                </div>\n' +
                                    '            </div>\n' +
                                    '        </div>\n';

                                // var codehtm='<div th:replace=\"~{coderender::coderender}\"></div>';
                                $('.side-right').append(hadcodehtm);
                                _that.selectitem();

                                //edit按钮
                                $("#projs").children("div:last-child").children("p:last-child").on("click", function () {
                                    var currentindex = $(this).attr("data_index")
                                    $(".side-right-content[data_index!=" + currentindex + "]").hide()
                                    $(".side-right-content[data_index=" + currentindex + "]").toggle()

                                })

                                //删除按钮
                                $("#projs").children("div:last-child").children("p:first-child").on("click", function () {
                                    var flag = confirm("确定要删除此项目吗？");
                                    if (flag === true) {
                                        var currentindex = $(this).closest(".proj").children(".delproj").attr("data_index")
                                        /* console.log(currentindex)*/
                                        var _this = this;
                                        var projid = $(this).closest(".proj").attr("data_uuid");
                                        projid = {
                                            "projid": projid
                                        }
                                        $.post(
                                            '/proj/delproj',
                                            projid,
                                            function (rst) {
                                                $(".side-right-content[data_index=" + currentindex + "]").remove()
                                                $(_this).closest(".proj").remove();
                                                _that.sort();
                                            }
                                        )
                                    }

                                })

                                //保存按钮
                                $(".side-right").children("div:last-child").children("div:last-child").children(".active").children(".tempsave").on("click", function () {
                                    var currentindex = $(this).closest(".content").closest(".side-right-content").attr("data_index");
                                    var projid = $(".proj[data_index=" + currentindex + "]").attr("data_uuid");
                                    var projname = $(".tempprojname[data_index=" + currentindex + "]").val()
                                    var content = project.content;
                                    if ($(".code[data_index=" + currentindex + "]").val() != '') {
                                        content = $(".code[data_index=" + currentindex + "]").val();
                                    }
                                    // console.log(currentindex);
                                    // console.log(projname);
                                    // console.log(projid);
                                    // console.log(content)

                                    if (projname === "") {
                                        $(".projname[data_index=" + currentindex + "]").text(project.projname);
                                        var proj = {
                                            "projname": project.projname,
                                            "content": content,
                                            "projid": projid,
                                        }
                                    } else {
                                        $(".projname[data_index=" + currentindex + "]").text(projname);
                                        var proj = {
                                            "projname": projname,
                                            "content": content,
                                            "projid": projid,
                                        }
                                    }
                                    $.post(
                                        '/proj/updateproj',
                                        proj,
                                        function (rst) {
                                            alert("更新成功！");
                                        }
                                    )

                                })


                                //生成代码按钮
                                $(".side-right").children("div:last-child").children("div:last-child").children(".active").children(".gocode").on("click", function () {
                                    var currentindex = $(this).closest(".content").closest(".side-right-content").attr("data_index");


                                    var content = project.content;
                                    if ($(".code[data_index=" + currentindex + "]").val() != '') {
                                        content = $(".code[data_index=" + currentindex + "]").val();
                                    }
                                    $.post(
                                        '/proj/coderender',
                                        content,
                                        /*                                        function (rst){
                                                                                    console.log(content)
                                                                                    alert("生成成功");
                                                                                }*/
                                    )
                                })
                            })

                        }
                    )
                }

                //添加项目
                $("#addproj").click(function () {
                    if (user === null) {
                        alert("请先登录！")
                    } else {
                        if (index === 0) {
                            dataid = 0;
                        } else {
                            dataid = $('#projs').children("div:last-child").attr("data_id");
                            dataid++;
                            // console.log(dataid);
                        }
                        var projhtm = "<div class=\"proj\" data_index='" + index + "' data_uuid=\"" + uuid() + "\" data_id=\"" + (dataid) + "\">\n" +
                            "                <p class=\"delproj\" data_index='" + index + "'>X</p>\n" +
                            "                <p class='projname' data_index='" + index + "' >A new project</p>\n" +
                            "                <p class=\"showproj\" data_index='" + index + "'>edit</p>\n" +
                            "            </div>";

                        $('#projs').append(projhtm);
                        var aindex = index;
                        var codehtm = '            <div class="side-right-content" style="display: none;" data_index="' + (index) + '">\n' +
                            '                <div class="nav" id="codenav">\n' +
                            '                    <div class="item"><i>Project</i></div>\n' +
                            '                    <div class="item"><i>Bean</i></div>\n' +
                            '                    <div class="item"><i>Page</i></div>\n' +
                            '                </div>\n' +
                            '                <div class="content" id="codecontent">\n';
                        var projecthtml = '<section id="project" class="" data_index="' + (index) + '">' +
                            '                  <div>\n' +
                            '                        <p>项目名称：</p>\n' +
                            '                        <input data_index="' + (index) + '" data_key="project: " class="projectname codedata" type="text" placeholder="请输入这个项目的名字">\n' +
                            '                    </div>\n' +
                            '                    <div>\n' +
                            '                        <p>项目地址：</p>\n' +
                            '                        <input data_index="' + (index) + '" class="dir codedata" data_key="  dir: " type="text" placeholder="请输入这个项目的地址">\n' +
                            '                    </div>\n' +
                            '                    <div>\n' +
                            '                        <p>项目包名：</p>\n' +
                            '                        <input data_index="' + (index) + '" class="package-name codedata" data_key="  package-name: " type="text" placeholder="请输入这个项目的包名">\n' +
                            '                    </div>\n' +
                            '                    <div>\n' +
                            '                        <p>数据库表名：</p>\n' +
                            '                        <input data_index="' + (index) + '" class="db-name codedata" data_key="  db-name: " type="text" placeholder="请输入数据库表名">\n' +
                            '                    </div>\n' +
                            '                    <div>\n' +
                            '                        <p>数据库用户名：</p>\n' +
                            '                        <input data_index="' + (index) + '" class="db-user codedata" data_key="  db-user: " type="text" placeholder="请输入数据库用户名">\n' +
                            '                    </div>\n' +
                            '                    <div>\n' +
                            '                        <p>数据库密码：</p>\n' +
                            '                        <input data_index="' + (index) + '" class="db-password codedata" data_key="  db-password: " type="text" placeholder="请输入数据库密码">\n' +
                            '                    </div>\n' +
                            '                    <div>\n' +
                            '                        <p>端口号：</p>\n' +
                            '                        <input data_index="' + (index) + '" class="port codedata" data_key="  port: " type="text" placeholder="请输入端口号">\n' +
                            '                    </div>\n' +
                            '                    <div class="dependencies">\n' +
                            '                        <p>选择所需要的依赖：</p>\n' +
                            '                        <div data_index="' + (index) + '" class="codedata iconfont" data_key="  dependencies: ">\n' +
                            '                            <div data_checked="0" class="dependency hutools icon-danxuankuang" data_key="    hutools: ">hutools</div>\n' +
                            '                            <div data_checked="0" class="dependency guava icon-danxuankuang" data_key="    guava: " >guava</div>\n' +
                            '                            <div data_checked="0" class="dependency json icon-danxuankuang" data_key="    json: " >json</div>\n' +
                            '                            <div data_checked="0" class="dependency html2pdf icon-danxuankuang" data_key="    html2pdf: " >html2pdf</div>\n' +
                            '                            <div data_checked="0" class="dependency stripe icon-danxuankuang" data_key="    stripe: " >stripe</div>\n' +
                            '                            <div data_checked="0" class="dependency redis icon-danxuankuang" data_key="    redis: " >redis</div>\n' +
                            '                            <div data_checked="0" class="dependency jsoup icon-danxuankuang" data_key="    jsoup: " >jsoup</div>\n' +
                            '                        </div>\n' +
                            '                    </div>\n' +
                            '                    <button id="tempsave">save</button>\n' +
                            '                    <button id="gocode">Let\'s go!</button>' +
                            '                    <button id="tempershow">tempershow</button>' +
                            '</section>\n';
                        var beanhtml = '<section id="bean" class="">\n' +
                            '                    <div class="beans">\n' +
                            '                        <div>\n' +
                            '                            <i class="beantitle">Bean：</i>\n' +
                            '                            <button id="addbean">+ 添加Bean</button>\n' +
                            '                            <button id="addbeanuser">+ 添加user</button>\n' +
                            '                        </div>\n' +
                            '                        <div>\n' +
                            '                            <p>Bean(实体类)的名称：</p>\n' +
                            '                            <input data_index="' + (index) + '" class="beanname codedata" data_key="bean: " type="text" placeholder="请输入实体类的名称">\n' +
                            '                        </div>\n' +
                            '                        <div>\n' +
                            '                            <p>数据库表名：</p>\n' +
                            '                            <input data_index="' + (index) + '" class="table codedata" data_key="  table: " type="text" placeholder="请输入数据库表名">\n' +
                            '                        </div>\n' +
                            '                        <div id="propdiv">\n' +
                            '                            <div class="allbeanprops">\n' +
                            '                                <p class="codedata" data_key="  prop: ">属性(变量)：</p>\n' +
                            '                                <div class="beanprops">\n' +
                            '                                   <div class="beanprop">\n' +
                            '                                       <p>数据类型：</p>\n' +
                            '                                       <input data_index="' + (index) + '" class="datatype codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                            '                                       <p>变量名：</p>\n' +
                            '                                       <input data_index="' + (index) + '" class="dataname codedata" data_key="" type="text" placeholder="请输入变量名">\n' +
                            '                                       <p>对应表中字段名(选填)：</p>\n' +
                            '                                       <input data_index="' + (index) + '" class="col-name codedata" data_key="        col-name: " type="text" placeholder="选填">\n' +
                            '                                       <p>对应表中数据类型(选填)：</p>\n' +
                            '                                       <input data_index="' + (index) + '" class="col-type codedata" data_key="        col-type: " type="text" placeholder="选填">\n' +
                            '                                       <p>是否需要@Transient</p>\n' +
                            '                                       <div class="iconfont"><div data_checked="0" class="col-not icon-danxuankuang" data_key="        col-not: "></div></div>' +
                            '                                   </div>\n' +
                            '                                </div>\n' +
                            '                            </div>\n' +
                            '                            <button class="addprops">\n' +
                            '                                +添加变量\n' +
                            '                            </button>\n' +
                            '                        </div>\n' +
                            '                        <div id="fundiv">\n' +
                            '                            <div class="allbeanfuns">\n' +
                            '                                <div class="beanfuns">\n' +
                            '                                <p  class="codedata" data_key="  dao: ">方法：</p>\n' +
                            '                                    <select class="functions">\n' +
                            '                                        <option>--选择方法--</option>\n' +
                            '                                        <option>page-fun</option>\n' +
                            '                                        <option>list-fun</option>\n' +
                            '                                        <option>update-fun</option>\n' +
                            '                                        <option>fun</option>\n' +
                            '                                    </select>\n' +
                            '                                    <p>方法名：</p>\n' +
                            '                                    <input data_index="' + (index) + '" class="funname codedata" data_key="" type="text" placeholder="请输入方法名">\n' +
                            '                                    <p>Sql语句：</p>\n' +
                            '                                    <input data_index="' + (index++) + '" class="sql codedata" data_key="        sql: " type="text" placeholder="请输入sql语句">\n' +
                            '                                    <p>(参数至少一个)参数是否有id</p>\n' +
                            '                                    <div class="iconfont"><div data_checked="0" class="canshuid icon-danxuankuang" data_key="        &id: "></div></div>' +
                            '                                    <p>参数是否有name</p>\n' +
                            '                                    <div class="iconfont"><div data_checked="0" class="canshuname icon-danxuankuang" data_key="        &name: "></div></div>' +
                            '                                    <div class="funprops" data_index_funprop="' + (funpropindex) + '" >\n' +
                            '                                    </div>\n' +
                            '                                    <button class="addcanshus" data_index_funprop="' + (funpropindex) + '" >\n' +
                            '                                        +添加其他参数\n' +
                            '                                    </button>\n' +
                            '                                </div>\n' +
                            '                            </div>\n' +
                            '                            <button class="addfuns">\n' +
                            '                                +添加方法\n' +
                            '                            </button>\n' +
                            '                        </div>\n' +
                            '                    </div>\n' +
                            '                </section>\n';
                        var pagehtml = '<section id="page">' +
                            '               <button id="addpage">+ 添加Page</button>\n' +

                            '                </section>\n' +
                            '                </div>\n' +
                            '            </div>\n' +
                            '        </div>\n';


                        // var codehtm='<div th:replace=\"~{coderender::coderender}\"></div>';
                        var allhtml = codehtm + projecthtml + beanhtml + pagehtml;
                        $('.side-right').append(allhtml);
                        _that.selectitem();

                        //edit按钮
                        $("#projs").children("div:last-child").children("p:last-child").on("click", function () {
                            var currentindex = $(this).attr("data_index")
                            $(".side-right-content[data_index!=" + currentindex + "]").hide()
                            $(".side-right-content[data_index=" + currentindex + "]").toggle()

                        })

                        //删除按钮
                        $("#projs").children("div:last-child").children("p:first-child").on("click", function () {
                            var flag = confirm("确定要删除此项目吗？");
                            if (flag === true) {
                                var currentindex = $(this).closest(".proj").children(".delproj").attr("data_index")

                                var _this = this;
                                var projid = $(this).closest(".proj").attr("data_uuid");
                                projid = {
                                    "projid": projid
                                }
                                $.post(
                                    '/proj/delproj',
                                    projid,
                                    function (rst) {
                                        $(".side-right-content[data_index=" + currentindex + "]").remove()
                                        $(_this).closest(".proj").remove();
                                        _that.sort();
                                    }
                                )
                            }

                        })

                        //保存按钮
                        $(".side-right").children("div:last-child").children(".content").children("section").children("#tempsave").on("click", function () {
                            var currentindex = $(this).closest(".content").closest(".side-right-content").attr("data_index");
                            var projid = $(".proj[data_index=" + currentindex + "]").attr("data_uuid");
                            var projname = $(".projectname[data_index=" + currentindex + "]").val()
                            var content = 123;
                            // console.log(projid);
                            // console.log(currentindex);
                            //console.log(projname);
                            // console.log(content)
                            var user = localStorage.getItem('user');
                            user = JSON.parse(user);
                            if (projname === null) {
                                alert("请输入项目名称！");
                            } else {
                                $(".projname[data_index=" + currentindex + "]").text(projname);
                                var proj = {
                                    "account": user.account,
                                    "projid": projid,
                                    "projname": projname,
                                    "content": content,
                                }
                                var updateproj = {
                                    "projname": projname,
                                    "content": content,
                                    "projid": projid,
                                }
                                projid = {
                                    "projid": projid,
                                }

                                $.post(
                                    '/proj/findprojcontent',
                                    projid,
                                    function (rst) {
                                        // console.log(rst.code)
                                        if (rst.code === 1) {
                                            $.post(
                                                '/proj/updateproj',
                                                updateproj,
                                                function (rst) {
                                                    alert("更新成功！");
                                                }
                                            )
                                        } else {
                                            $.post(
                                                '/proj/addproj',
                                                proj,
                                                function (rst) {
                                                    alert("保存成功！")
                                                }
                                            )
                                        }
                                    }
                                )
                            }


                        })

                        //生成按钮
                        $(".side-right").children("div:last-child").children(".content").children("section").children("#gocode").on("click", function () {
                            var code = [];
                            //project模块获取值
                            $("#project .codedata").each(function (i, e) {
                                if (i == 0) {
                                    var keyvalue = $(this).attr("data_key");
                                } else {
                                    var keyvalue = $(this).attr("data_key") + $(this).val();
                                }
                                code.push(keyvalue);
                            })

                            //bean模块获取值
                            $("#bean .codedata").each(function (i, e) {
                                //数据类型
                                if ($(this).attr("class") == "datatype codedata") {
                                    $(this).attr("data_key", $(this).val())
                                    return true;//跳出本次循环
                                }

                                //变量名
                                else if ($(this).attr("class") == "dataname codedata") {
                                    $(this).attr("data_key", "     " + $(this).val() + ": ")
                                    var keyvalue = $(this).attr("data_key") + $(this).prev().prev().attr("data_key");
                                }

                                //对应表中字段名(选填)：
                                else if ($(this).attr("class") == "col-name codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //对应表中数据类型(选填)：
                                else if ($(this).attr("class") == "col-type codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //是否需要@Transient
                                else if ($(this).attr("class") == "col-not icon-danxuankuang") {
                                    if ($(this).attr("data_checked") == 1) {
                                        $(this).addClass("codedata");
                                    }
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("codedata");
                                    }
                                }

                                //方法名
                                else if ($(this).attr("class") == "funname codedata") {
                                    var keyvalue = "     " + $(this).prev().prev().val() + ": " + $(this).val()
                                }

                                //sql语句
                                else if ($(this).attr("class") == "sql codedata") {
                                    var keyvalue = $(this).attr("data_key") + $(this).val();
                                }

                                //参数是否有id
                                else if ($(this).attr("class") == "canshuid icon-danxuankuang") {
                                    if ($(this).attr("data_checked") == 1) {
                                        $(this).addClass("codedata");
                                    }
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("codedata");
                                    }
                                }

                                //参数是否有name
                                else if ($(this).attr("class") == "canshuname icon-danxuankuang") {
                                    if ($(this).attr("data_checked") == 1) {
                                        $(this).addClass("codedata");
                                    }
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("codedata");
                                    }
                                }

                                else if ($(this).attr("class") == "funpropkey codedata") {
                                    var keyvalue = "        " + $(this).next().val() + ": " + $(this).val();
                                }

                                //判断user是否有特殊角色
                                else if ($(this).attr("class") == "alluserroles codedata") {
                                    if ($(this).children("div").length == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key");
                                    }
                                }

                                //user的角色和描述
                                else if ($(this).attr("class") == "datarolename codedata") {
                                    var keyvalue = "     " + $(this).val()+": "+$(this).next().next().val();
                                }


                                else {
                                    var keyvalue = $(this).attr("data_key") + $(this).val();
                                }
                                code.push(keyvalue);
                            })


                            //page模块获取值
                            $("#page .codedata").each(function (i, e) {

                                //div的class属性
                                if ($(this).attr("class") == "datadivclass codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //div的select公共模块
                                else if ($(this).attr("class") == "datadivth-include codedata") {
                                    if ($(this).prev(".th-include").val() == "--选择公共模块(如果有)--") {
                                        return true;
                                    } else {
                                        var keyvalue = "    " + $(this).prev().val() + ": " + $(this).val();
                                    }
                                }

                                //嵌套的div的select公共模块
                                else if ($(this).attr("class") == "datanesteddivth-include codedata") {
                                    if ($(this).prev(".th-include").val() == "--选择公共模块(如果有)--") {
                                        return true;
                                    } else {
                                        var keyvalue = "      " + $(this).prev().val() + ": " + $(this).val();
                                    }
                                }

                                //判断div是否有组件
                                else if ($(this).attr("class") == "comp datadivcomp codedata") {
                                    if ($(this).children("div").length == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key");
                                    }
                                }

                                //判断嵌套的div是否有组件
                                else if ($(this).attr("class") == "nestedcomp datanesteddivcomp codedata") {
                                    if ($(this).children("div").length == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key");
                                    }
                                }

                                //popup
                                else if ($(this).attr("class") == "datapopupid codedata") {
                                    var keyvalue = "      " + $(this).closest(".popupcomp").prev(".componentselect").val() + ": " + $(this).val();
                                }

                                //popup里按钮选择样式
                                else if ($(this).attr("class") == "popfontselect codedata") {
                                    if ($(this).val() == "--按钮样式(可无)--") return true;
                                    else {
                                        var keyvalue = "          font: " + $(this).val();
                                    }
                                }

                                //button
                                else if ($(this).attr("class") == "databuttonid codedata") {
                                    var keyvalue = "      " + $(this).closest(".butcomp").prev(".componentselect").val() + ": " + $(this).val();
                                } else if ($(this).attr("class") == "databutton-smallid codedata") {
                                    var keyvalue = "      " + $(this).closest(".butsmacomp").prev(".componentselect").val() + ": " + $(this).val();
                                } else if ($(this).attr("class") == "databutton-bigid codedata") {
                                    var keyvalue = "      " + $(this).closest(".butbigcomp").prev(".componentselect").val() + ": " + $(this).val();
                                } else if ($(this).attr("class") == "databutton-iconid codedata") {
                                    var keyvalue = "      " + $(this).closest(".buticcomp").prev(".componentselect").val() + ": " + $(this).val();
                                }

                                //button里的样式选择
                                else if ($(this).attr("class") == "buttonfontselect codedata") {
                                    if ($(this).val() == "--按钮样式(可无)--") return true;
                                    else {
                                        var keyvalue = "        font: " + $(this).val();
                                    }
                                } else if ($(this).attr("class") == "nestedpopfontselect codedata") {
                                    if ($(this).val() == "--按钮样式(可无)--") return true;
                                    else {
                                        var keyvalue = "          font: " + $(this).val();
                                    }
                                }


                                //按钮的tip
                                else if ($(this).attr("class") == "databuttontip codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //卡片页
                                else if ($(this).attr("class") == "datatabli codedata") {
                                    var keyvalue = $(this).attr("data_key") + $(this).val() + ", " + $(this).next().next().val();
                                }

                                //卡片页对应的div
                                else if ($(this).attr("class") == "datatablidiv codedata") {
                                    $("#page .datatablidydiv").each(function (i, e) {
                                        var keyvalue = "    div: " + $(this).val();
                                        code.push(keyvalue);
                                    })
                                    return true;
                                }

                                //嵌套的卡片页对应的div
                                else if ($(this).attr("class") == "datanestedtablidiv codedata") {
                                    $("#page .datanestedtablidydiv").each(function (i, e) {
                                        var keyvalue = "      div: " + $(this).val();
                                        code.push(keyvalue);
                                    })
                                    return true;
                                }

                                //table的param
                                else if ($(this).attr("class") == "datatabparam codedata") {
                                    var keyvalue = $(this).attr("data_key") + $(this).val() + ", " + $(this).next().next().val();
                                }

                                //嵌套的table的param
                                else if ($(this).attr("class") == "datanestedtabparam codedata") {
                                    var keyvalue = $(this).attr("data_key") + $(this).val() + ", " + $(this).next().next().val();
                                }

                                //判断form是否有组件
                                else if ($(this).attr("class") == "formcomp dataformcomp codedata") {
                                    if ($(this).children("div").length == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key");
                                    }
                                }

                                //一些选填的空
                                else if ($(this).attr("class") == "datachoose codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //form中的verifier
                                else if ($(this).attr("class") == "formcomponentselect codedata") {
                                    if ($(this).val() == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                } else {
                                    var keyvalue = $(this).attr("data_key") + $(this).val();
                                }
                                code.push(keyvalue);
                            })

                            console.log(code)


                            var data = {
                                "code": code
                            };
                            $.post(
                                "/proj/coderender",
                                data,
                                function (rst) {
                                    if (rst.code === 1) {
                                        alert("生成成功！请前去生成路径查看！")
                                    } else {
                                        alert(rst.msg);
                                    }
                                }
                            )
                        })


                        /*------------------Project----------------------*/
                        //project中dependencies的多选框
                        $(".side-right").children("div:last-child").children("div:last-child").children("#project").children(".dependencies").children(".iconfont").children(".dependency").on("click", function () {
                            if ($(this).attr("data_checked") == 0) {
                                $(this).removeClass("icon-danxuankuang");
                                $(this).addClass("icon-radio-checked-fill");
                                $(this).addClass("codedata");
                                $(this).attr("data_checked", 1);
                            } else {
                                $(this).addClass("icon-danxuankuang");
                                $(this).removeClass("icon-radio-checked-fill");
                                $(this).removeClass("codedata");
                                $(this).attr("data_checked", 0);
                            }
                        })


                        /*------------------Bean----------------------*/
                        //bean中 添加bean 按钮
                        $(".side-right").children("div:last-child").children("div:last-child").children("#bean").children("div:first-child").children("div:first-child").children("#addbean").on("click", function () {
                            var beancode = '           <div class="beans">\n' +
                                '                        <div class="iconfont">\n' +
                                '                        <div class="icon-chacha delbean"></div>\n' +
                                '                            <i class="beantitle">Bean：</i>\n' +
                                '                        </div>\n' +
                                '                        <div>\n' +
                                '                            <p>Bean(实体类)的名称：</p>\n' +
                                '                            <input data_index="' + (index) + '" class="beanname codedata" data_key="bean: " type="text" placeholder="请输入实体类的名称">\n' +
                                '                        </div>\n' +
                                '                        <div>\n' +
                                '                            <p>数据库表名：</p>\n' +
                                '                            <input data_index="' + (index) + '" class="table codedata" data_key="  table: " type="text" placeholder="请输入数据库表名">\n' +
                                '                        </div>\n' +
                                '                        <div id="propdiv">\n' +
                                '                            <div class="allbeanprops">\n' +
                                '                                <p class="codedata" data_key="  prop: ">属性(变量)：</p>\n' +
                                '                                <div class="beanprops">\n' +
                                '                                   <div class="beanprop">\n' +
                                '                                       <p>数据类型：</p>\n' +
                                '                                       <input data_index="' + (index) + '" class="datatype codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                '                                       <p>变量名：</p>\n' +
                                '                                       <input data_index="' + (index) + '" class="dataname codedata" data_key="" type="text" placeholder="请输入变量名">\n' +
                                '                                       <p>对应表中字段名(选填)：</p>\n' +
                                '                                       <input data_index="' + (index) + '" class="col-name codedata" data_key="        col-name: " type="text" placeholder="选填">\n' +
                                '                                       <p>对应表中数据类型(选填)：</p>\n' +
                                '                                       <input data_index="' + (index) + '" class="col-type codedata" data_key="        col-type: " type="text" placeholder="选填">\n' +
                                '                                       <p>是否需要@Transient</p>\n' +
                                '                                       <div class="iconfont"><div data_checked="0" class="col-not icon-danxuankuang" data_key="        col-not: "></div></div>' +
                                '                                   </div>\n' +
                                '                                </div>\n' +
                                '                            </div>\n' +
                                '                            <button class="addprops">\n' +
                                '                                +添加变量\n' +
                                '                            </button>\n' +
                                '                        </div>\n' +
                                '                        <div id="fundiv">\n' +
                                '                            <div class="allbeanfuns">\n' +
                                '                                <div class="beanfuns">\n' +
                                '                                <p class="codedata" data_key="  dao: ">方法：</p>\n' +
                                '                                    <select class="functions">\n' +
                                '                                        <option>--选择方法--</option>\n' +
                                '                                        <option>page-fun</option>\n' +
                                '                                        <option>list-fun</option>\n' +
                                '                                        <option>update-fun</option>\n' +
                                '                                        <option>fun</option>\n' +
                                '                                    </select>\n' +
                                '                                    <p>方法名：</p>\n' +
                                '                                    <input data_index="' + (index) + '" class="funname codedata" data_key="" type="text" placeholder="请输入方法名">\n' +
                                '                                    <p>Sql语句：</p>\n' +
                                '                                    <input data_index="' + (index++) + '" class="sql codedata" data_key="        sql: " type="text" placeholder="请输入sql语句">\n' +
                                '                                    <p>(参数至少一个)参数是否有id</p>\n' +
                                '                                    <div class="iconfont"><div data_checked="0" class="canshuid icon-danxuankuang" data_key="        &id: "></div></div>' +
                                '                                    <p>参数是否有name</p>\n' +
                                '                                    <div class="iconfont"><div data_checked="0" class="canshuname icon-danxuankuang" data_key="        &name: "></div></div>' +
                                '                                    <div class="funprops" data_index_funprop="' + (++funpropindex) + '" >\n' +
                                '                                    </div>\n' +
                                '                                    <button class="addcanshus" data_index_funprop="' + (funpropindex) + '" >\n' +
                                '                                        +添加其他参数\n' +
                                '                                    </button>\n' +
                                '                                </div>\n' +
                                '                            </div>\n' +
                                '                            <button class="addfuns">\n' +
                                '                                +添加方法\n' +
                                '                            </button>\n' +
                                '                        </div>\n' +
                                '                    </div>\n';
                            $(this).closest(".beans").closest("#bean").append(beancode)

                            //删除bean
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children(".iconfont").children(".delbean").on("click", function () {
                                $(this).closest()
                                var flag = confirm("确定要删除这个bean吗？");
                                if (flag == true) {
                                    $(this).closest(".beans").remove();
                                }
                            })

                            //添加bean 中@Transient的单选框
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children("#propdiv").children(".allbeanprops").children(".beanprops").children("div:last-child").children(".iconfont").children(".col-not").on("click", function () {
                                if ($(this).attr("data_checked") == 0) {
                                    $(this).removeClass("icon-danxuankuang");
                                    $(this).addClass("icon-radio-checked-fill");
                                    $(this).addClass("codedata");
                                    $(this).attr("data_checked", 1);
                                } else {
                                    $(this).addClass("icon-danxuankuang");
                                    $(this).removeClass("icon-radio-checked-fill");
                                    $(this).removeClass("codedata");
                                    $(this).attr("data_checked", 0);
                                }
                            })

                            //添加bean 中方法的单选框
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children("#fundiv").children(".allbeanfuns").children(".beanfuns").children(".iconfont").children(".icon-danxuankuang").on("click", function () {
                                if ($(this).attr("data_checked") == 0) {
                                    $(this).removeClass("icon-danxuankuang");
                                    $(this).addClass("icon-radio-checked-fill");
                                    $(this).addClass("codedata");
                                    $(this).attr("data_checked", 1);
                                } else {
                                    $(this).addClass("icon-danxuankuang");
                                    $(this).removeClass("icon-radio-checked-fill");
                                    $(this).removeClass("codedata");
                                    $(this).attr("data_checked", 0);
                                }
                            })

                            //添加bean 中添加变量按钮
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children("#propdiv").children(".addprops").on("click", function () {
                                var propcode = '                                <div class="beanprop">\n' +
                                    '                                    <div class="iconfont delprop"><div class="icon-chacha"></div></div>\n' +
                                    '                                    <p>数据类型：</p>\n' +
                                    '                                    <input data_index="' + (aindex) + '" class="datatype codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                    '                                    <p>变量名：</p>\n' +
                                    '                                    <input data_index="' + (aindex) + '" class="dataname codedata" data_key="" type="text" placeholder="请输入变量名">\n' +
                                    '                                    <p>对应表中字段名(选填)：</p>\n' +
                                    '                                    <input data_index="' + (aindex) + '" class="col-name codedata" data_key="        col-name: " type="text" placeholder="选填">\n' +
                                    '                                    <p>对应表中数据类型(选填)：</p>\n' +
                                    '                                    <input data_index="' + (aindex) + '" class="col-type codedata" data_key="        col-type: " type="text" placeholder="选填">\n' +
                                    '                                    <p>是否需要@Transient</p>\n' +
                                    '                                    <div class="iconfont"><div data_checked="0" class="col-not icon-danxuankuang" data_key="        col-not: "></div></div>' +
                                    '                                </div>\n';
                                $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").append(propcode);

                                //单选框
                                $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").children("div:last-child").children(".iconfont").children(".col-not").on("click", function () {
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("icon-danxuankuang");
                                        $(this).addClass("icon-radio-checked-fill");
                                        $(this).addClass("codedata");
                                        $(this).attr("data_checked", 1);
                                    } else {
                                        $(this).addClass("icon-danxuankuang");
                                        $(this).removeClass("icon-radio-checked-fill");
                                        $(this).removeClass("codedata");
                                        $(this).attr("data_checked", 0);
                                    }
                                })

                                //删除
                                $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").children("div:last-child").children(".delprop").children(".icon-chacha").on("click", function () {
                                    var flag = confirm("确定要删除此属性吗？");
                                    if (flag == true) {
                                        $(this).closest(".beanprop").remove();
                                    }
                                })
                            })

                            //添加bean 中添加其他参数按钮
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children("#fundiv").children(".allbeanfuns").children("div:last-child").children(".addcanshus").on("click", function () {
                                var canshus = '<div class="funprop" > \n' +
                                    '<input class="funpropkey codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                    '<input class="funpropvalue" data_key="" type="text" placeholder="请输入变量名">\n' +
                                    '<div class="iconfont delfunprop"><div class="icon-chacha"></div></div>\n' +
                                    '<div>\n';
                                var currentfunpropindex = $(this).attr("data_index_funprop");
                                $(".funprops[data_index_funprop=" + currentfunpropindex + "]").append(canshus);
                                //删除
                                $(".funprops[data_index_funprop=" + currentfunpropindex + "]").children("div:last-child").children(".delfunprop").on("click", function () {
                                    var flag = confirm("确定要删除此属性吗？");
                                    if (flag == true) {
                                        $(this).closest(".funprop").remove();
                                    }

                                })

                            })

                            //添加bean 中添加方法按钮
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children("#fundiv").children(".addfuns").on("click", function () {
                                var funcode = '                       <div class="beanfuns">\n' +
                                    '                                <div class="iconfont delfun"><div class="icon-chacha"></div></div>\n' +
                                    '                                <p>方法：</p>\n' +
                                    '                                    <select class="functions">\n' +
                                    '                                        <option>--选择方法--</option>\n' +
                                    '                                        <option>page-fun</option>\n' +
                                    '                                        <option>list-fun</option>\n' +
                                    '                                        <option>update-fun</option>\n' +
                                    '                                        <option>fun</option>\n' +
                                    '                                    </select>\n' +
                                    '                                    <p>方法名：</p>\n' +
                                    '                                    <input data_index="' + (index) + '" class="funname codedata" data_key="" type="text" placeholder="请输入方法名">\n' +
                                    '                                    <p>Sql语句：</p>\n' +
                                    '                                    <input data_index="' + (index++) + '" class="sql codedata" data_key="        sql: " type="text" placeholder="请输入sql语句">\n' +
                                    '                                    <p>(参数至少一个)参数是否有id</p>\n' +
                                    '                                    <div class="iconfont"><div data_checked="0" class="canshuid icon-danxuankuang" data_key="        &id: "></div></div>' +
                                    '                                    <p>参数是否有name</p>\n' +
                                    '                                    <div class="iconfont"><div data_checked="0" class="canshuname icon-danxuankuang" data_key="        &name: "></div></div>' +
                                    '                                    <div class="funprops" data_index_funprop="' + (++funpropindex) + '">\n' +
                                    '                                    </div>\n' +
                                    '                                    <button class="addcanshus" data_index_funprop="' + (funpropindex) + '">\n' +
                                    '                                        +添加其他参数\n' +
                                    '                                    </button>\n' +
                                    '                                </div>\n';

                                $(this).closest("#fundiv").children(".allbeanfuns").append(funcode);

                                //添加其他参数按钮
                                $(this).closest("#fundiv").children(".allbeanfuns").children("div:last-child").children(".addcanshus").on("click", function () {
                                    var canshus = '<div class="funprop" > \n' +
                                        '<input data_index="' + (aindex) + '" class="funpropkey codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                        '<input data_index="' + (aindex) + '" class="funpropvalue" data_key="" type="text" placeholder="请输入变量名">\n' +
                                        '<div class="iconfont delfunprop"><div class="icon-chacha"></div></div>\n' +
                                        '<div>\n';
                                    var currentfunpropindex = $(this).attr("data_index_funprop");
                                    $(".funprops[data_index_funprop=" + currentfunpropindex + "]").append(canshus);
                                    //删除按钮
                                    $(".funprops[data_index_funprop=" + currentfunpropindex + "]").children("div:last-child").children(".delfunprop").on("click", function () {
                                        var flag = confirm("确定要删除此属性吗？");
                                        if (flag == true) {
                                            $(this).closest(".funprop").remove();
                                        }
                                    })

                                })

                                //删除方法按钮
                                $(this).closest("#fundiv").children(".allbeanfuns").children("div:last-child").children(".delfun").on("click", function () {
                                    var flag = confirm("确定要删除此方法吗？");
                                    if (flag == true) {
                                        $(this).closest(".beanfuns").remove();
                                    }
                                })

                                //单选框
                                $(this).closest("#fundiv").children(".allbeanfuns").children("div:last-child").children(".iconfont").children(".icon-danxuankuang").on("click", function () {
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("icon-danxuankuang");
                                        $(this).addClass("icon-radio-checked-fill");
                                        $(this).addClass("codedata");
                                        $(this).attr("data_checked", 1);
                                    } else {
                                        $(this).addClass("icon-danxuankuang");
                                        $(this).removeClass("icon-radio-checked-fill");
                                        $(this).removeClass("codedata");
                                        $(this).attr("data_checked", 0);
                                    }
                                })
                            })
                        })

                        //bean中@Transient的单选框
                        $(".side-right").children("div:last-child").children("div:last-child").children("#bean").children("div:first-child").children("#propdiv").children(".allbeanprops").children(".beanprops").children("div:last-child").children(".iconfont").children(".col-not").on("click", function () {
                            if ($(this).attr("data_checked") == 0) {
                                $(this).removeClass("icon-danxuankuang");
                                $(this).addClass("icon-radio-checked-fill");
                                $(this).addClass("codedata");
                                $(this).attr("data_checked", 1);
                            } else {
                                $(this).addClass("icon-danxuankuang");
                                $(this).removeClass("icon-radio-checked-fill");
                                $(this).removeClass("codedata");
                                $(this).attr("data_checked", 0);
                            }
                        })

                        //bean中方法的单选框
                        $(".side-right").children("div:last-child").children("div:last-child").children("#bean").children("div:first-child").children("#fundiv").children(".allbeanfuns").children(".beanfuns").children(".iconfont").children(".icon-danxuankuang").on("click", function () {
                            if ($(this).attr("data_checked") == 0) {
                                $(this).removeClass("icon-danxuankuang");
                                $(this).addClass("icon-radio-checked-fill");
                                $(this).addClass("codedata");
                                $(this).attr("data_checked", 1);
                            } else {
                                $(this).addClass("icon-danxuankuang");
                                $(this).removeClass("icon-radio-checked-fill");
                                $(this).removeClass("codedata");
                                $(this).attr("data_checked", 0);
                            }
                        })

                        //bean中添加变量按钮
                        $(".side-right").children("div:last-child").children("div:last-child").children("#bean").children("div:first-child").children("#propdiv").children(".addprops").on("click", function () {
                            var propcode = '                                <div class="beanprop">\n' +
                                '                                    <div class="iconfont delprop"><div class="icon-chacha"></div></div>\n' +
                                '                                    <p>数据类型：</p>\n' +
                                '                                    <input data_index="' + (aindex) + '" class="datatype codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                '                                    <p>变量名：</p>\n' +
                                '                                    <input data_index="' + (aindex) + '" class="dataname codedata" data_key="" type="text" placeholder="请输入变量名">\n' +
                                '                                    <p>对应表中字段名(选填)：</p>\n' +
                                '                                    <input data_index="' + (aindex) + '" class="col-name codedata" data_key="        col-name: " type="text" placeholder="选填">\n' +
                                '                                    <p>对应表中数据类型(选填)：</p>\n' +
                                '                                    <input data_index="' + (aindex) + '" class="col-type codedata" data_key="        col-type: " type="text" placeholder="选填">\n' +
                                '                                    <p>是否需要@Transient</p>\n' +
                                '                                    <div class="iconfont"><div data_checked="0" class="col-not icon-danxuankuang" data_key="        col-not: "></div></div>' +
                                '                                </div>\n';
                            $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").append(propcode);


                            //单选框
                            $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").children("div:last-child").children(".iconfont").children(".col-not").on("click", function () {
                                if ($(this).attr("data_checked") == 0) {
                                    $(this).removeClass("icon-danxuankuang");
                                    $(this).addClass("icon-radio-checked-fill");
                                    $(this).addClass("codedata");
                                    $(this).attr("data_checked", 1);
                                } else {
                                    $(this).addClass("icon-danxuankuang");
                                    $(this).removeClass("icon-radio-checked-fill");
                                    $(this).removeClass("codedata");
                                    $(this).attr("data_checked", 0);
                                }
                            })

                            //删除
                            $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").children("div:last-child").children(".delprop").children(".icon-chacha").on("click", function () {
                                var flag = confirm("确定要删除此属性吗？");
                                if (flag == true) {
                                    $(this).closest(".beanprop").remove();
                                }
                            })
                        })

                        //bean中添加其他参数按钮
                        $(".side-right").children("div:last-child").children("div:last-child").children("#bean").children("div:first-child").children("#fundiv").children(".allbeanfuns").children("div:last-child").children(".addcanshus").on("click", function () {
                            var canshus = '<div class="funprop" > \n' +
                                '<input class="funpropkey codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                '<input class="funpropvalue" data_key="" type="text" placeholder="请输入变量名">\n' +
                                '<div class="iconfont delfunprop"><div class="icon-chacha"></div></div>\n' +
                                '<div>\n';
                            var currentfunpropindex = $(this).attr("data_index_funprop");
                            $(".funprops[data_index_funprop=" + currentfunpropindex + "]").append(canshus);
                            //删除
                            $(".funprops[data_index_funprop=" + currentfunpropindex + "]").children("div:last-child").children(".delfunprop").on("click", function () {
                                var flag = confirm("确定要删除此属性吗？");
                                if (flag == true) {
                                    $(this).closest(".funprop").remove();
                                }

                            })

                        })

                        //bean中添加方法按钮
                        $(".side-right").children("div:last-child").children("div:last-child").children("#bean").children("div:first-child").children("#fundiv").children(".addfuns").on("click", function () {
                            var funcode = '                       <div class="beanfuns">\n' +
                                '                                <div class="iconfont delfun"><div class="icon-chacha"></div></div>\n' +
                                '                                <p>方法：</p>\n' +
                                '                                    <select class="functions">\n' +
                                '                                        <option>--选择方法--</option>\n' +
                                '                                        <option>page-fun</option>\n' +
                                '                                        <option>list-fun</option>\n' +
                                '                                        <option>update-fun</option>\n' +
                                '                                        <option>fun</option>\n' +
                                '                                    </select>\n' +
                                '                                    <p>方法名：</p>\n' +
                                '                                    <input data_index="' + (index) + '" class="funname codedata" data_key="" type="text" placeholder="请输入方法名">\n' +
                                '                                    <p>Sql语句：</p>\n' +
                                '                                    <input data_index="' + (index++) + '" class="sql codedata" data_key="        sql: " type="text" placeholder="请输入sql语句">\n' +
                                '                                    <p>(参数至少一个)参数是否有id</p>\n' +
                                '                                    <div class="iconfont"><div data_checked="0" class="canshuid icon-danxuankuang" data_key="        &id: "></div></div>' +
                                '                                    <p>参数是否有name</p>\n' +
                                '                                    <div class="iconfont"><div data_checked="0" class="canshuname icon-danxuankuang" data_key="        &name: "></div></div>' +
                                '                                    <div class="funprops" data_index_funprop="' + (++funpropindex) + '">\n' +
                                '                                    </div>\n' +
                                '                                    <button class="addcanshus" data_index_funprop="' + (funpropindex) + '">\n' +
                                '                                        +添加其他参数\n' +
                                '                                    </button>\n' +
                                '                                </div>\n';
                            $(this).closest("#fundiv").children(".allbeanfuns").append(funcode);
                            //单选框
                            $(this).closest("#fundiv").children(".allbeanfuns").children("div:last-child").children(".iconfont").children(".icon-danxuankuang").on("click", function () {
                                if ($(this).attr("data_checked") == 0) {
                                    $(this).removeClass("icon-danxuankuang");
                                    $(this).addClass("icon-radio-checked-fill");
                                    $(this).addClass("codedata");
                                    $(this).attr("data_checked", 1);
                                } else {
                                    $(this).addClass("icon-danxuankuang");
                                    $(this).removeClass("icon-radio-checked-fill");
                                    $(this).removeClass("codedata");
                                    $(this).attr("data_checked", 0);
                                }
                            })
                            //添加其他参数按钮
                            $(this).closest("#fundiv").children(".allbeanfuns").children("div:last-child").children(".addcanshus").on("click", function () {
                                var canshus = '<div class="funprop" > \n' +
                                    '<input data_index="' + (aindex) + '" class="funpropkey codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                    '<input data_index="' + (aindex) + '" class="funpropvalue" data_key="" type="text" placeholder="请输入变量名">\n' +
                                    '<div class="iconfont delfunprop"><div class="icon-chacha"></div></div>\n' +
                                    '<div>\n';
                                var currentfunpropindex = $(this).attr("data_index_funprop");
                                $(".funprops[data_index_funprop=" + currentfunpropindex + "]").append(canshus);
                                //删除按钮
                                $(".funprops[data_index_funprop=" + currentfunpropindex + "]").children("div:last-child").children(".delfunprop").on("click", function () {
                                    var flag = confirm("确定要删除此属性吗？");
                                    if (flag == true) {
                                        $(this).closest(".funprop").remove();
                                    }
                                })
                            })
                            //bean中删除方法按钮
                            $(this).closest("#fundiv").children(".allbeanfuns").children("div:last-child").children(".delfun").on("click", function () {
                                var flag = confirm("确定要删除此方法吗？");
                                if (flag == true) {
                                    $(this).closest(".beanfuns").remove();
                                }
                            })

                        })

                        //bean中添加user按钮
                        $(".side-right").children("div:last-child").children("div:last-child").children("#bean").children("div:first-child").children("div:first-child").children("#addbeanuser").on("click",function (){
                            var beancode = '           <div class="beans">\n' +
                                '                        <div class="iconfont">\n' +
                                '                        <div class="icon-chacha delbean"></div>\n' +
                                '                            <i class="beantitle">User：</i>\n' +
                                '                        </div>\n' +
                                '                        <div>\n' +
                                '                            <p>User(特殊的bean)的名称：</p>\n' +
                                '                            <input data_index="' + (index) + '" class="beanname codedata" data_key="user: " type="text" placeholder="请输入实体类的名称">\n' +
                                '                        </div>\n' +
                                '                        <div>\n' +
                                '                            <p>数据库表名：</p>\n' +
                                '                            <input data_index="' + (index) + '" class="table codedata" data_key="  table: " type="text" placeholder="请输入数据库表名">\n' +
                                '                        </div>\n' +
                                '                        <div id="propdiv">\n' +
                                '                            <div class="allbeanprops">\n' +
                                '                                <p class="codedata" data_key="  prop: ">属性(变量)：</p>\n' +
                                '                                <div class="beanprops">\n' +
                                '                                   <div class="beanprop">\n' +
                                '                                       <p>数据类型：</p>\n' +
                                '                                       <input data_index="' + (index) + '" class="datatype codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                '                                       <p>变量名：</p>\n' +
                                '                                       <input data_index="' + (index) + '" class="dataname codedata" data_key="" type="text" placeholder="请输入变量名">\n' +
                                '                                       <p>对应表中字段名(选填)：</p>\n' +
                                '                                       <input data_index="' + (index) + '" class="col-name codedata" data_key="        col-name: " type="text" placeholder="选填">\n' +
                                '                                       <p>对应表中数据类型(选填)：</p>\n' +
                                '                                       <input data_index="' + (index) + '" class="col-type codedata" data_key="        col-type: " type="text" placeholder="选填">\n' +
                                '                                       <p>是否需要@Transient</p>\n' +
                                '                                       <div class="iconfont"><div data_checked="0" class="col-not icon-danxuankuang" data_key="        col-not: "></div></div>' +
                                '                                   </div>\n' +
                                '                                </div>\n' +
                                '                            </div>\n' +
                                '                            <button class="addprops">\n' +
                                '                                +添加变量\n' +
                                '                            </button>\n' +
                                '                        </div>\n' +
                                '                        <div id="rolediv">\n' +
                                '                            <div class="alluserroles codedata" data_key="  roles: ">\n' +
                                '                            </div>\n' +
                                '                            <button class="adduserroles">\n' +
                                '                                +添加用户的角色配置\n' +
                                '                            </button>\n' +
                                '                        </div>\n' +
                                '                    </div>\n';
                            $(this).closest(".beans").closest("#bean").append(beancode)
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children(".iconfont").children(".delbean").on("click", function () {
                                $(this).closest()
                                var flag = confirm("确定要删除这个user吗？");
                                if (flag == true) {
                                    $(this).closest(".beans").remove();
                                }
                            })

                            //添加变量
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children("#propdiv").children(".addprops").on("click", function () {
                                var propcode = '                                <div class="beanprop">\n' +
                                    '                                    <div class="iconfont delprop"><div class="icon-chacha"></div></div>\n' +
                                    '                                    <p>数据类型：</p>\n' +
                                    '                                    <input data_index="' + (aindex) + '" class="datatype codedata" data_key="" type="text" placeholder="请输入数据类型">\n' +
                                    '                                    <p>变量名：</p>\n' +
                                    '                                    <input data_index="' + (aindex) + '" class="dataname codedata" data_key="" type="text" placeholder="请输入变量名">\n' +
                                    '                                    <p>对应表中字段名(选填)：</p>\n' +
                                    '                                    <input data_index="' + (aindex) + '" class="col-name codedata" data_key="        col-name: " type="text" placeholder="选填">\n' +
                                    '                                    <p>对应表中数据类型(选填)：</p>\n' +
                                    '                                    <input data_index="' + (aindex) + '" class="col-type codedata" data_key="        col-type: " type="text" placeholder="选填">\n' +
                                    '                                    <p>是否需要@Transient</p>\n' +
                                    '                                    <div class="iconfont"><div data_checked="0" class="col-not icon-danxuankuang" data_key="        col-not: "></div></div>' +
                                    '                                </div>\n';
                                $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").append(propcode);

                                //单选框
                                $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").children("div:last-child").children(".iconfont").children(".col-not").on("click", function () {
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("icon-danxuankuang");
                                        $(this).addClass("icon-radio-checked-fill");
                                        $(this).addClass("codedata");
                                        $(this).attr("data_checked", 1);
                                    } else {
                                        $(this).addClass("icon-danxuankuang");
                                        $(this).removeClass("icon-radio-checked-fill");
                                        $(this).removeClass("codedata");
                                        $(this).attr("data_checked", 0);
                                    }
                                })

                                //删除
                                $(this).closest("#propdiv").children(".allbeanprops").children(".beanprops").children("div:last-child").children(".delprop").children(".icon-chacha").on("click", function () {
                                    var flag = confirm("确定要删除此属性吗？");
                                    if (flag == true) {
                                        $(this).closest(".beanprop").remove();
                                    }
                                })
                            })

                            //添加角色
                            $(this).closest(".beans").closest("#bean").children("div:last-child").children("#rolediv").children(".adduserroles").on("click", function () {
                                var propcode = '                                <div class="userroles">\n' +
                                    '                                    <div class="iconfont deluserroles"><div class="icon-chacha"></div></div>\n' +
                                    '                                    <p>角色名：</p>\n' +
                                    '                                    <input class="datarolename codedata"type="text" placeholder="admin">\n' +
                                    '                                    <p>描述：</p>\n' +
                                    '                                    <input placeholder="管理员">\n' +
                                    '                                </div>\n';
                                $(this).closest("#rolediv").children(".alluserroles").append(propcode);

                                //删除
                                $(this).closest("#rolediv").children(".alluserroles").children("div:last-child").children(".deluserroles").on("click", function () {
                                    var flag = confirm("确定要删除此属性吗？");
                                    if (flag == true) {
                                        $(this).closest(".userroles").remove();
                                    }
                                })
                            })

                        })

                        /*------------------Page----------------------*/
                        //page中添加page按钮
                        $(".side-right").children("div:last-child").children("div:last-child").children("#page").children("#addpage").on("click", function () {
                            var page = '<div class="pages">\n' +
                                '----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------' +
                                '                        <div class="iconfont">\n' +
                                '                           <div class="icon-chacha delpage"></div>\n' +
                                '                            <p>页面名称：</p>\n' +
                                '                            <input class="datapagename codedata" data_key="page: " placeholder="请输入页面的名称">\n' +
                                '                        </div>\n' +
                                '                        <div id="pagetitle">\n' +
                                '                            <p>Title：</p>\n' +
                                '                            <input class="datapagetitle codedata" data_key="  title: " placeholder="请输入title">\n' +
                                '                            <button class="adddivs">\n' +
                                '                                +添加div\n' +
                                '                            </button>\n' +
                                '                            <button class="addforms">\n' +
                                '                                +添加form\n' +
                                '                            </button>\n' +
                                '                        </div>\n' +
                                '                    </div>\n';
                            $(this).closest("#page").append(page);

                            //删除page按钮
                            $(this).closest("#page").children("div:last-child").children(".iconfont").children(".delpage").on("click", function () {
                                var flag = confirm("确定要删除此page吗？");
                                if (flag == true) {
                                    $(this).closest(".iconfont").closest(".pages").remove();
                                }
                            })

                            //page中添加div按钮
                            $(this).closest("#page").children("div:last-child").children("div:first-child").next().children(".adddivs").on("click", function () {
                                var div = "<div class='pagediv'>\n" +
                                    '           <div class="iconfont">\n' +
                                    '               <div class="icon-chacha deldiv"></div>\n' +
                                    '               <i class="divtitle">div：</i>\n' +
                                    '           </div>\n' +
                                    '           <div>\n' +
                                    '               <p>此div的id属性：</p>' +
                                    '               <input class="datadivid codedata" data_key="  div: "  placeholder="请输入div的id">\n' +
                                    '               <p>class属性(选填)：</p>' +
                                    '               <input class="datadivclass codedata" data_key="    class: " placeholder="请输入class">\n' +
                                    '               <select class="th-include">\n' +
                                    '                   <option>--选择公共模块(如果有)--</option>\n' +
                                    '                   <option>th-include</option>\n' +
                                    '                   <option>th-insert</option>\n' +
                                    '                   <option>th-replace</option>\n' +
                                    '               </select>\n' +
                                    '               <input class="datadivth-include codedata" placeholder="公共模块的名称">\n' +
                                    '           </div>\n' +
                                    '           <div>\n' +
                                    '               <p>此div里是否嵌套div</p>' +
                                    '               <div class="iconfont yesno">' +
                                    '                   <div data_checked="0" class="yesicon icon-danxuankuang"><strong>是</strong></div>' +
                                    '                   <div data_checked="0" class="noicon icon-danxuankuang"><strong>否</strong></div>' +
                                    '               </div>' +
                                    '           </div>\n' +
                                    "</div>\n";
                                $(this).closest("div").closest(".pages").append(div);

                                //div的删除按钮
                                $(this).closest("#pagetitle").closest(".pages").children("div:last-child").children("div:first-child").children(".deldiv").on("click", function () {
                                    var flag = confirm("确定要删除此div吗？");
                                    if (flag == true) {
                                        $(this).closest(".iconfont").closest(".pagediv").remove();
                                    }

                                })

                                //是 单选框
                                $(this).closest("#pagetitle").closest(".pages").children("div:last-child").children("div:first-child").next().next().children(".iconfont").children(".yesicon").on("click", function () {
                                    var nesteddiv = '<div class="nesteddivs">' +
                                        '<button class="addnesteddivs">\n' +
                                        '             +添加嵌套的div\n' +
                                        '            </button>\n' +
                                        '</div>';

                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("icon-danxuankuang");
                                        $(this).addClass("icon-radio-checked-fill");
                                        $(this).attr("data_checked", 1);
                                        $(this).next().removeClass("icon-radio-checked-fill");
                                        $(this).next().addClass("icon-danxuankuang");
                                        $(this).next().attr("data_checked", 0);
                                        $(this).closest(".iconfont").parent().closest(".pagediv").append(nesteddiv);
                                        $(this).closest(".iconfont").parent().closest(".pagediv").children(".comp").remove();
                                        //添加嵌套的div
                                        $(this).closest(".iconfont").parent().closest(".pagediv").children(".nesteddivs").children(".addnesteddivs").on("click", function () {
                                            var nesteddivs = "<div class='nesteddiv'>\n" +
                                                '           <div class="iconfont nesteddivhead">\n' +
                                                '               <div class="icon-chacha delnested-div" style="text-indent:2em"></div>\n' +
                                                '               <i class="divtitle">div：</i>\n' +
                                                '           </div>\n' +
                                                '           <div class="nesteddivprops">\n' +
                                                '               <p style="text-indent:2em">此div的id属性：</p>' +
                                                '               <input class="codedata" data_key="    div: " placeholder="请输入div的id">\n' +
                                                '               <p>class属性(选填)：</p>' +
                                                '               <input class="datachoose codedata" data_key="      class: " placeholder="请输入class">\n' +
                                                '               <select class="th-include">\n' +
                                                '                   <option>--选择公共模块(如果有)--</option>\n' +
                                                '                   <option>th-include</option>\n' +
                                                '                   <option>th-insert</option>\n' +
                                                '                   <option>th-replace</option>\n' +
                                                '               </select>\n' +
                                                '               <input class="datanesteddivth-include codedata" placeholder="公共模块的名称">\n' +
                                                '           </div>\n' +
                                                '           <div class="nestedcomp datanesteddivcomp codedata" data_key="      components: ">' +
                                                '                <button class="addnestedcomp">\n' +
                                                '                 +添加组件\n' +
                                                '                </button>\n' +
                                                '            </div>\n' +
                                                "</div>\n";
                                            $(this).closest(".nesteddivs").append(nesteddivs);
                                            //删除嵌套的div
                                            $(this).closest(".nesteddivs").children("div:last-child").children(".nesteddivhead").children(".delnested-div").on("click", function () {
                                                var flag = confirm("确定要删除此嵌套div吗？");
                                                if (flag == true) {
                                                    $(this).closest(".iconfont").closest(".nesteddiv").remove();
                                                }
                                            })

                                            //添加嵌套div里的组件
                                            $(this).closest(".nesteddivs").children("div:last-child").children("div:last-child").children(".addnestedcomp").on("click", function () {
                                                var nestedcomp = '<div class="nestedcomps iconfont" >' +
                                                    '        <div class="icon-chacha delnestedcomsel"></div>\n' +
                                                    '       <select class="nestedcomponentselect">\n' +
                                                    '           <option value="0">--选择组件--</option>\n' +
                                                    '           <option>popup</option>\n' +
                                                    '           <option>button</option>\n' +
                                                    '           <option>button-small</option>\n' +
                                                    '           <option>button-big</option>\n' +
                                                    '           <option>button-icon</option>\n' +
                                                    '           <option>uploader</option>\n' +
                                                    '           <option>downloader</option>\n' +
                                                    '           <option>tab</option>\n' +
                                                    '           <option>table</option>\n' +
                                                    '           <option>page-table</option>\n' +
                                                    '           <option>datepicker</option>\n' +
                                                    '           <option>selector</option>\n' +
                                                    '           <option>multi-selector</option>\n' +
                                                    '           <option>editor</option>\n' +
                                                    '        </select>\n' +
                                                    '</div>';
                                                $(this).closest(".nestedcomp").append(nestedcomp);

                                                //删除组件按钮
                                                $(this).closest(".nestedcomp").children("div:last-child").children(".delnestedcomsel").on("click", function () {
                                                    var flag = confirm("确定要删除此组件吗？");
                                                    if (flag == true) {
                                                        $(this).closest(".nestedcomps").remove();
                                                    }
                                                })

                                                //根据select的值来添加div
                                                $(this).closest(".nestedcomp").children("div:last-child").children(".nestedcomponentselect").on("change", function () {

                                                    if ($(this).val() == 0) {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                    }

                                                    if ($(this).val() == "popup") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var popup = '<div class="nestedpopupcomp nestedcomponent">\n' +
                                                            '    <p>弹窗id：</p>\n' +
                                                            '    <input class="codedata" data_key="        popup: "placeholder="输入组件id">' +
                                                            '    <p>弹窗title：</p>\n' +
                                                            '    <input class="codedata" data_key="          title: " placeholder="输入title">' +
                                                            '    <p>弹窗width：</p>\n' +
                                                            '    <input class="codedata" data_key="          width: " placeholder="width(px)">' +
                                                            '    <div class="nestedpopbutton">\n' +
                                                            '    </div>\n' +
                                                            '    <button class="addnestedpopbut">\n' +
                                                            '     + 弹窗里按钮\n' +
                                                            '    </button>\n' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(popup);
                                                        //添加弹窗按钮
                                                        $(this).closest(".nestedcomps").children("div:last-child").children(".addnestedpopbut").on("click", function () {
                                                            var popbutton = '    <div class="nestedpopbuttonprops iconfont">\n' +
                                                                '    <p>id：</p>\n' +
                                                                '    <input class="codedata" data_key="          pop-button: " placeholder="按钮id">' +
                                                                '    <p>内容：</p>\n' +
                                                                '    <input class="codedata" data_key="            text: " placeholder="输入按钮内容">' +
                                                                '       <select class="nestedpopfontselect codedata">\n' +
                                                                '           <option>--按钮样式(可无)--</option>\n' +
                                                                '           <option>fa-check</option>\n' +
                                                                '           <option>fa-times</option>\n' +
                                                                '           <option>fa-eercast</option>\n' +
                                                                '           <option>fa-eercast</option>\n' +
                                                                '           <option>fa-envelope-open</option>\n' +
                                                                '           <option>fa-grav</option>\n' +
                                                                '           <option>fa-id-card-o</option>\n' +
                                                                '        </select>\n' +
                                                                '        <div class="icon-chacha delnestedpopbut"></div>\n' +
                                                                '</div>';
                                                            $(this).closest(".nestedpopupcomp").children(".nestedpopbutton").append(popbutton);
                                                            //删除弹窗按钮
                                                            $(this).prev().children(".nestedpopbuttonprops").children(".delnestedpopbut").on("click", function () {
                                                                var flag = confirm("确定要删除此按钮吗？");
                                                                if (flag == true) {
                                                                    $(this).closest(".nestedpopbuttonprops").remove();
                                                                }
                                                            })
                                                        })
                                                    }

                                                    if ($(this).val() == "button") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var but = '<div class="nestedbutcomp nestedcomponent">\n' +
                                                            '    <p>按钮id：</p>\n' +
                                                            '    <input class="codedata" data_key="        button: " placeholder="输入按钮id">' +
                                                            '    <p>按钮内容：</p>\n' +
                                                            '    <input class="codedata" data_key="          text: " placeholder="输入text">' +
                                                            '    <select class="nestedpopfontselect codedata">\n' +
                                                            '         <option>--按钮样式(可无)--</option>\n' +
                                                            '         <option>fa-check</option>\n' +
                                                            '         <option>fa-times</option>\n' +
                                                            '         <option>fa-eercast</option>\n' +
                                                            '         <option>fa-eercast</option>\n' +
                                                            '         <option>fa-envelope-open</option>\n' +
                                                            '         <option>fa-grav</option>\n' +
                                                            '         <option>fa-id-card-o</option>\n' +
                                                            '    </select>\n' +
                                                            '    <p>tip(选填)：</p>\n' +
                                                            '    <input class="datachoose codedata" data_key="          tip: "placeholder="鼠标移入提示">' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(but);
                                                    }

                                                    if ($(this).val() == "button-small") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var butsma = '<div class="nestedbutsmacomp nestedcomponent">\n' +
                                                            '    <p>按钮id：</p>\n' +
                                                            '    <input class="codedata" data_key="        button-small: " placeholder="输入按钮id">' +
                                                            '    <p>按钮内容：</p>\n' +
                                                            '    <input class="codedata" data_key="          text: " placeholder="输入text">' +
                                                            '    <select class="nestedpopfontselect codedata">\n' +
                                                            '         <option>--按钮样式(可无)--</option>\n' +
                                                            '         <option>fa-check</option>\n' +
                                                            '         <option>fa-times</option>\n' +
                                                            '         <option>fa-eercast</option>\n' +
                                                            '         <option>fa-eercast</option>\n' +
                                                            '         <option>fa-envelope-open</option>\n' +
                                                            '         <option>fa-grav</option>\n' +
                                                            '         <option>fa-id-card-o</option>\n' +
                                                            '    </select>\n' +
                                                            '    <p>tip(选填)：</p>\n' +
                                                            '    <input class="datachoose codedata" data_key="          tip: "placeholder="鼠标移入提示">' +

                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(butsma);
                                                    }

                                                    if ($(this).val() == "button-big") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var butbig = '<div class="nestedbutbigcomp nestedcomponent">\n' +
                                                            '    <p>按钮id：</p>\n' +
                                                            '    <input class="codedata" data_key="        button-big: "placeholder="输入按钮id">' +
                                                            '    <p>按钮内容：</p>\n' +
                                                            '    <input class="codedata" data_key="          text: " placeholder="输入text">' +
                                                            '    <select class="nestedpopfontselect codedata">\n' +
                                                            '         <option>--按钮样式(可无)--</option>\n' +
                                                            '         <option>fa-check</option>\n' +
                                                            '         <option>fa-times</option>\n' +
                                                            '         <option>fa-eercast</option>\n' +
                                                            '         <option>fa-eercast</option>\n' +
                                                            '         <option>fa-envelope-open</option>\n' +
                                                            '         <option>fa-grav</option>\n' +
                                                            '         <option>fa-id-card-o</option>\n' +
                                                            '    </select>\n' +
                                                            '    <p>tip(选填)：</p>\n' +
                                                            '    <input class="datachoose codedata" data_key="          tip: "placeholder="鼠标移入提示">' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(butbig);
                                                    }

                                                    if ($(this).val() == "button-icon") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var butic = '<div class="nestedbuticcomp nestedcomponent">\n' +
                                                            '    <p>按钮id：</p>\n' +
                                                            '    <input class="codedata" data_key="        button-icon: "placeholder="输入按钮id">' +
                                                            '    <p>按钮内容：</p>\n' +
                                                            '    <input class="codedata" data_key="          text: " placeholder="输入text">' +
                                                            '    <select class="nestedpopfontselect codedata">\n' +
                                                            '         <option>--按钮样式(可无)--</option>\n' +
                                                            '         <option>fa-check</option>\n' +
                                                            '         <option>fa-times</option>\n' +
                                                            '         <option>fa-eercast</option>\n' +
                                                            '         <option>fa-eercast</option>\n' +
                                                            '         <option>fa-envelope-open</option>\n' +
                                                            '         <option>fa-grav</option>\n' +
                                                            '         <option>fa-id-card-o</option>\n' +
                                                            '    </select>\n' +
                                                            '    <p>tip(选填)：</p>\n' +
                                                            '    <input class="datachoose codedata" data_key="          tip: "placeholder="鼠标移入提示">' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(butic);
                                                    }

                                                    if ($(this).val() == "uploader") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var uploader = '<div class="nesteduploadercomp nestedcomponent">\n' +
                                                            '    <p>id：</p>\n' +
                                                            '    <input class="codedata" data_key="          uploader: "placeholder="输入id">' +
                                                            '    <p>上传文件地址：</p>\n' +
                                                            '    <input class="codedata" data_key="            upload-target-dir: "placeholder="输入target-dir">' +
                                                            '    <p>文件大小限制(MB)(选填)：</p>\n' +
                                                            '    <input class="datachoose codedata" data_key="            file-size-limit: "placeholder="fontsizelimit">' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(uploader);
                                                    }

                                                    if ($(this).val() == "downloader") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var downloader = '<div class="nesteddownloadercomp nestedcomponent">\n' +
                                                            '    <p>id：</p>\n' +
                                                            '    <input class="codedata" data_key="        downloader: " placeholder="输入id">' +
                                                            '    <p>对应的controller链接：</p>\n' +
                                                            '    <input class="codedata" data_key="          url: " placeholder="eg:/download/{file}">' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(downloader);
                                                    }

                                                    if ($(this).val() == "tab") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var tab = '<div class="nestedtabcomp nestedcomponent">\n' +
                                                            '    <p>tab栏id：</p>\n' +
                                                            '    <input class="codedata" data_key="        tab: "placeholder="输入id">' +
                                                            '    <button class="addnestedtab-li">\n' +
                                                            '     +添加卡片页\n' +
                                                            '    </button>\n' +
                                                            '    <div class="nestedtablis">\n' +
                                                            '    </div>' +
                                                            '    <div class="datanestedtablidiv codedata" ></div>' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(tab);
                                                        //添加卡片页
                                                        $(this).closest(".nestedcomps").children("div:last-child").children(".addnestedtab-li").on("click", function () {
                                                            var tabli = '<div class="nestedtabli iconfont">' +
                                                                '    <p>卡片页文字：</p>\n' +
                                                                '    <input class="datatabli codedata" data_key="          tab-li: "placeholder="">\n' +
                                                                '    <p>对应的div的id：</p>\n' +
                                                                '    <input class="datanestedtablidydiv" placeholder="DIV的id">\n' +
                                                                '    <div class="icon-chacha delnestedtabli"></div>\n' +
                                                                '</div>';
                                                            $(this).closest(".nestedtabcomp").children(".nestedtablis").append(tabli);
                                                            $(this).next().children("div:last-child").children(".delnestedtabli").on("click", function () {
                                                                var flag = confirm("确定要删除此卡片页吗？");
                                                                if (flag == true) {
                                                                    $(this).closest(".nestedtabli").remove();
                                                                }
                                                            })
                                                        });
                                                    }

                                                    if ($(this).val() == "table") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var table = '<div class="nestedtablecomp nestedcomponent">\n' +
                                                            '    <p>表id：</p>\n' +
                                                            '    <input class="codedata" data_key="        table: "placeholder="输入id">' +
                                                            '    <p>url：</p>\n' +
                                                            '    <input class="codedata" data_key="          url: "placeholder="数据接口">' +
                                                            '    <button class="addnestedtabparam">\n' +
                                                            '     +表的提交参数\n' +
                                                            '    </button>\n' +
                                                            '    <div class="nestedtabparams">\n' +
                                                            '    </div>' +
                                                            '    <button class="addnestedtabhead">\n' +
                                                            '     +表格内容\n' +
                                                            '    </button>\n' +
                                                            '    <div class="nestedtabheads">\n' +
                                                            '        <div class="nestedtablehead">\n' +
                                                            '        <p>表头：</p>' +
                                                            '        <input class="datatabhead codedata" data_key="          pri-head: "placeholder="文字, 数据key">\n' +
                                                            '        </div>\n' +
                                                            '    </div>' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(table);
                                                        var param = '<div class="nestedtabparam iconfont">\n' +
                                                            '     <p>数据类型：</p>\n' +
                                                            '     <input class="datanestedtabparam codedata" data_key="          param: ">\n' +
                                                            '     <p>变量名：</p>\n' +
                                                            '     <input>\n' +
                                                            '    <div class="icon-chacha delnestedtabparam"></div>\n' +
                                                            '    </div>';
                                                        //添加表的提交参数
                                                        $(this).closest(".nestedcomps").children("div:last-child").children(".addnestedtabparam").on("click", function () {
                                                            $(this).next().append(param);
                                                            $(this).next().children("div:last-child").children(".delnestedtabparam").on("click", function () {
                                                                var flag = confirm("确定要删除此参数吗？");
                                                                if (flag == true) {
                                                                    $(this).closest(".nestedtabparam").remove();
                                                                }
                                                            })
                                                        })

                                                        var head = '<div class="nestedtablehead iconfont">\n' +
                                                            '     <p>表头：</p>\n' +
                                                            '     <input class="datatabhead codedata" data_key="          head: "placeholder="文字, 数据key">\n' +
                                                            '    <div class="icon-chacha delnestedtablehead"></div>\n' +

                                                            '    </div>';
                                                        //添加表格内容
                                                        $(this).closest(".nestedcomps").children("div:last-child").children(".addnestedtabhead").on("click", function () {
                                                            $(this).next().append(head);
                                                            $(this).next().children("div:last-child").children(".delnestedtablehead").on("click", function () {
                                                                var flag = confirm("确定要删除此参数吗？");
                                                                if (flag == true) {
                                                                    $(this).closest(".nestedtablehead").remove();
                                                                }
                                                            })
                                                        })
                                                    }

                                                    if ($(this).val() == "page-table") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var pagetable = '<div class="nestedpagetablecomp nestedcomponent">\n' +
                                                            '    <p>表id：</p>\n' +
                                                            '    <input class="codedata" data_key="        page-table: "placeholder="输入id">\n' +
                                                            '    <p>url：</p>\n' +
                                                            '    <input class="codedata" data_key="          url: "placeholder="数据接口">\n' +
                                                            '    <button class="addnestedpagetabparam">\n' +
                                                            '     +表的提交参数\n' +
                                                            '    </button>\n' +
                                                            '    <div class="nestedpagetabparams">\n' +
                                                            '    </div>' +
                                                            '    <button class="addnestedpagetabhead">\n' +
                                                            '     +表格内容\n' +
                                                            '    </button>\n' +
                                                            '    <div class="nestedpagetabheads">\n' +
                                                            '        <div class="nestedpagetablehead">\n' +
                                                            '        <p>表头：</p>' +
                                                            '        <input class="codedata" data_key="          pri-head: " placeholder="文字, 数据key">\n' +
                                                            '        </div>\n' +
                                                            '    </div>' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(pagetable);
                                                        var param = '<div class="nestedpagetabparam iconfont">\n' +
                                                            '     <p>数据类型：</p>\n' +
                                                            '     <input class="datanestedtabparam codedata" data_key="          param: ">\n' +
                                                            '     <p>变量名：</p>\n' +
                                                            '     <input>\n' +
                                                            '    <div class="icon-chacha delnestedpagetabparam"></div>\n' +
                                                            '    </div>';
                                                        //添加表的提交参数
                                                        $(this).closest(".nestedcomps").children("div:last-child").children(".addnestedpagetabparam").on("click", function () {
                                                            $(this).next().append(param);
                                                            $(this).next().children("div:last-child").children(".delnestedpagetabparam").on("click", function () {
                                                                var flag = confirm("确定要删除此参数吗？");
                                                                if (flag == true) {
                                                                    $(this).closest(".nestedpagetabparam").remove();
                                                                }
                                                            })
                                                        })

                                                        var head = '<div class="nestedpagetablehead iconfont">\n' +
                                                            '     <p>表头：</p>\n' +
                                                            '     <input class="codedata" data_key="          head: " placeholder="文字, 数据key">\n' +
                                                            '    <div class="icon-chacha delnestedpagetablehead"></div>\n' +

                                                            '    </div>';
                                                        //添加表格内容
                                                        $(this).closest(".nestedcomps").children("div:last-child").children(".addnestedpagetabhead").on("click", function () {
                                                            $(this).next().append(head);
                                                            $(this).next().children("div:last-child").children(".delnestedpagetablehead").on("click", function () {
                                                                var flag = confirm("确定要删除此参数吗？");
                                                                if (flag == true) {
                                                                    $(this).closest(".nestedpagetablehead").remove();
                                                                }
                                                            })
                                                        })
                                                    }

                                                    if ($(this).val() == "datepicker") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var datepicker = '<div class="nesteddatepickercomp nestedcomponent">\n' +
                                                            '    <p>id：</p>\n' +
                                                            '    <input class="codedata" data_key="        datepicker: "placeholder="输入id">' +
                                                            '    <p>输入日期选择器的placeholder(可无)：</p>\n' +
                                                            '    <input class="codedata" data_key="          placeholder: "placeholder="输入placeholder">' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(datepicker);
                                                    }

                                                    if ($(this).val() == "selector") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var selector = '<div class="nestedselectorcomp nestedcomponent">\n' +
                                                            '    <p>id：</p>\n' +
                                                            '    <input class="codedata" data_key="        selector: "placeholder="输入id">' +
                                                            '    <button class="addnestedselector">\n' +
                                                            '     +添加选项\n' +
                                                            '    </button>\n' +
                                                            '    <div class="nestedsels">\n' +
                                                            '        <div class="nestedsel iconfont">\n' +
                                                            '             <p>选项：</p>\n' +
                                                            '             <input class="codedata" data_key="          sel-item: " placeholder="显示文字,值">' +
                                                            '        </div>\n' +
                                                            '    </div>\n' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(selector);
                                                        var sel = '<div class="nestedsel iconfont">\n' +
                                                            '             <p>选项：</p>\n' +
                                                            '             <input class="codedata" data_key="          sel-item: " placeholder="显示文字,值">' +
                                                            '             <div class="icon-chacha delnestedsel"></div>\n' +
                                                            '        </div>\n';
                                                        //添加选项 按钮
                                                        $(this).closest(".nestedcomps").children("div:last-child").children(".addnestedselector").on("click", function () {
                                                            $(this).next().append(sel);
                                                            $(this).next().children("div:last-child").children(".delnestedsel").on("click", function () {
                                                                var flag = confirm("确定要删除此选项吗？");
                                                                if (flag == true) {
                                                                    $(this).closest(".nestedsel").remove();
                                                                }
                                                            })
                                                        })
                                                    }

                                                    if ($(this).val() == "multi-selector") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var mutiselector = '<div class="mutiselectorcomp component">\n' +
                                                            '    <p>id：</p>\n' +
                                                            '    <input placeholder="输入id">' +
                                                            '    <button class="addmutiselector">\n' +
                                                            '     +添加选项\n' +
                                                            '    </button>\n' +
                                                            '    <div class="msels">\n' +
                                                            '        <div class="msel iconfont">\n' +
                                                            '             <p>选项：</p>\n' +
                                                            '             <input class="codedata" data_key="          sel-item: " placeholder="显示文字,值">' +
                                                            '        </div>\n' +
                                                            '    </div>\n' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(mutiselector);
                                                        var msel = '<div class="msel iconfont">\n' +
                                                            '             <p>选项：</p>\n' +
                                                            '             <input class="codedata" data_key="          sel-item: " placeholder="显示文字,值">' +
                                                            '             <div class="icon-chacha delmsel"></div>\n' +
                                                            '        </div>\n';
                                                        //添加选项 按钮
                                                        $(this).closest(".nestedcomps").children("div:last-child").children(".addmutiselector").on("click", function () {
                                                            $(this).next().append(msel);
                                                            $(this).next().children("div:last-child").children(".delmsel").on("click", function () {
                                                                var flag = confirm("确定要删除此选项吗？");
                                                                if (flag == true) {
                                                                    $(this).closest(".msel").remove();
                                                                }
                                                            })
                                                        })
                                                    }

                                                    if ($(this).val() == "editor") {
                                                        $(this).closest(".nestedcomps").children(".nestedcomponent").remove();
                                                        var edit = '<div class="nestededitcomp nestedcomponent">\n' +
                                                            '    <p>id：</p>\n' +
                                                            '    <input class="codedata" data_key="        editor: "placeholder="输入id">' +
                                                            '    <p>输入高度(px)：</p>\n' +
                                                            '    <input class="codedata" data_key="          height: " placeholder="文本框高度">' +
                                                            '</div>';
                                                        $(this).closest(".nestedcomps").append(edit);
                                                    }
                                                })
                                            })

                                        })
                                    }
                                })

                                //否 单选框
                                $(this).closest("#pagetitle").closest(".pages").children("div:last-child").children("div:first-child").next().next().children(".iconfont").children(".noicon").on("click", function () {
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("icon-danxuankuang");
                                        $(this).addClass("icon-radio-checked-fill");
                                        $(this).attr("data_checked", 1);
                                        $(this).prev().removeClass("icon-radio-checked-fill");
                                        $(this).prev().addClass("icon-danxuankuang");
                                        $(this).prev().attr("data_checked", 0);


                                        var addcomp = '<div class="comp datadivcomp codedata" data_key="    components: ">' +
                                            '            <button class="addcomp">\n' +
                                            '             +添加组件\n' +
                                            '            </button>\n' +
                                            '</div>';
                                        $(this).closest(".iconfont").parent().closest(".pagediv").append(addcomp);
                                        $(this).closest(".iconfont").parent().closest(".pagediv").children(".nesteddivs").remove();

                                        var comp = '<div class="comps iconfont">' +
                                            '        <div class="icon-chacha delcomsel"></div>\n' +
                                            '       <select class="componentselect">\n' +
                                            '           <option value="0">--选择组件--</option>\n' +
                                            '           <option>popup</option>\n' +
                                            '           <option>button</option>\n' +
                                            '           <option>button-small</option>\n' +
                                            '           <option>button-big</option>\n' +
                                            '           <option>button-icon</option>\n' +
                                            '           <option>uploader</option>\n' +
                                            '           <option>downloader</option>\n' +
                                            '           <option>tab</option>\n' +
                                            '           <option>table</option>\n' +
                                            '           <option>page-table</option>\n' +
                                            '           <option>datepicker</option>\n' +
                                            '           <option>selector</option>\n' +
                                            '           <option>multi-selector</option>\n' +
                                            '           <option>editor</option>\n' +
                                            '        </select>\n' +
                                            '</div>';

                                        //添加组件按钮
                                        $(this).closest(".iconfont").parent().closest(".pagediv").children(".comp").children(".addcomp").on("click", function () {
                                            $(this).closest(".comp").append(comp);

                                            //删除组件按钮
                                            $(this).closest(".comp").children("div:last-child").children(".delcomsel").on("click", function () {
                                                var flag = confirm("确定要删除此组件吗？");
                                                if (flag == true) {
                                                    $(this).closest(".comps").remove();
                                                }
                                            })

                                            //根据select的值来添加div
                                            $(this).closest(".comp").children("div:last-child").children(".componentselect").on("change", function () {

                                                if ($(this).val() == 0) {
                                                    $(this).closest(".comps").children(".component").remove();
                                                }

                                                if ($(this).val() == "popup") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var popup = '<div class="popupcomp component">\n' +
                                                        '    <p>弹窗id：</p>\n' +
                                                        '    <input class="datapopupid codedata" placeholder="输入组件id">' +
                                                        '    <p>弹窗title：</p>\n' +
                                                        '    <input class="codedata" data_key="        title: " placeholder="输入title">' +
                                                        '    <p>弹窗width：</p>\n' +
                                                        '    <input class="codedata" data_key="        width: " placeholder="width(px)">' +
                                                        '    <div class="popbutton">\n' +
                                                        '    </div>\n' +
                                                        '    <button class="addpopbut">\n' +
                                                        '     +弹窗里的按钮\n' +
                                                        '    </button>\n' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(popup);
                                                    //添加弹窗按钮
                                                    $(this).closest(".comps").children("div:last-child").children(".addpopbut").on("click", function () {
                                                        var popbutton = '    <div class="popbuttonprops iconfont">\n' +
                                                            '    <p>id：</p>\n' +
                                                            '    <input class="codedata" data_key="        pop-button: " placeholder="按钮id">' +
                                                            '    <p>内容：</p>\n' +
                                                            '    <input class="codedata" data_key="          text: " placeholder="输入按钮内容">' +
                                                            '       <select class="popfontselect codedata">\n' +
                                                            '           <option>--按钮样式(可无)--</option>\n' +
                                                            '           <option>fa-check</option>\n' +
                                                            '           <option>fa-times</option>\n' +
                                                            '           <option>fa-eercast</option>\n' +
                                                            '           <option>fa-eercast</option>\n' +
                                                            '           <option>fa-envelope-open</option>\n' +
                                                            '           <option>fa-grav</option>\n' +
                                                            '           <option>fa-id-card-o</option>\n' +
                                                            '        </select>\n' +
                                                            '        <div class="icon-chacha delpopbut"></div>\n' +
                                                            '</div>';
                                                        $(this).closest(".popupcomp").children(".popbutton").append(popbutton);
                                                        //删除弹窗按钮
                                                        $(this).prev().children("div:last-child").children(".delpopbut").on("click", function () {
                                                            var flag = confirm("确定要删除此按钮吗？");
                                                            if (flag == true) {
                                                                $(this).closest(".popbuttonprops").remove();
                                                            }
                                                        })
                                                    })
                                                }

                                                if ($(this).val() == "button") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var but = '<div class="butcomp component">\n' +
                                                        '    <p>按钮id：</p>\n' +
                                                        '    <input class="databuttonid codedata" placeholder="输入按钮id">' +
                                                        '    <p>按钮内容：</p>\n' +
                                                        '    <input class="codedata" data_key="        text: " placeholder="输入text">' +
                                                        '    <select class="buttonfontselect codedata">\n' +
                                                        '         <option>--按钮样式(可无)--</option>\n' +
                                                        '         <option>fa-check</option>\n' +
                                                        '         <option>fa-times</option>\n' +
                                                        '         <option>fa-eercast</option>\n' +
                                                        '         <option>fa-envelope-open</option>\n' +
                                                        '         <option>fa-grav</option>\n' +
                                                        '         <option>fa-id-card-o</option>\n' +
                                                        '    </select>\n' +
                                                        '    <p>tip(可无)：</p>\n' +
                                                        '    <input class="databuttontip codedata" data_key="        tip: " placeholder="鼠标移入提示">' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(but);
                                                }

                                                if ($(this).val() == "button-small") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var butsma = '<div class="butsmacomp component">\n' +
                                                        '    <p>按钮id：</p>\n' +
                                                        '    <input class="databutton-smallid codedata"placeholder="输入按钮id">' +
                                                        '    <p>按钮内容：</p>\n' +
                                                        '    <input class="codedata" data_key="        text: " placeholder="输入text">' +
                                                        '    <select  class="buttonfontselect codedata">\n' +
                                                        '         <option>--按钮样式(可无)--</option>\n' +
                                                        '         <option>fa-check</option>\n' +
                                                        '         <option>fa-times</option>\n' +
                                                        '         <option>fa-eercast</option>\n' +
                                                        '         <option>fa-envelope-open</option>\n' +
                                                        '         <option>fa-grav</option>\n' +
                                                        '         <option>fa-id-card-o</option>\n' +
                                                        '    </select>\n' +
                                                        '    <p>tip(可无)：</p>\n' +
                                                        '    <input class="databuttontip codedata" data_key="        tip: "placeholder="鼠标移入提示">' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(butsma);
                                                }

                                                if ($(this).val() == "button-big") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var butbig = '<div class="butbigcomp component">\n' +
                                                        '    <p>按钮id：</p>\n' +
                                                        '    <input class="databutton-bigid codedata"placeholder="输入按钮id">' +
                                                        '    <p>按钮内容：</p>\n' +
                                                        '    <input class="codedata" data_key="        text: "placeholder="输入text">' +
                                                        '    <select  class="buttonfontselect codedata">\n' +
                                                        '         <option>--按钮样式(可无)--</option>\n' +
                                                        '         <option>fa-check</option>\n' +
                                                        '         <option>fa-times</option>\n' +
                                                        '         <option>fa-eercast</option>\n' +
                                                        '         <option>fa-envelope-open</option>\n' +
                                                        '         <option>fa-grav</option>\n' +
                                                        '         <option>fa-id-card-o</option>\n' +
                                                        '    </select>\n' +
                                                        '    <p>tip(可无)：</p>\n' +
                                                        '    <input class="databuttontip codedata" data_key="        tip: "placeholder="鼠标移入提示">' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(butbig);
                                                }

                                                if ($(this).val() == "button-icon") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var butic = '<div class="buticcomp component">\n' +
                                                        '    <p>按钮id：</p>\n' +
                                                        '    <input class="databutton-iconid codedata"placeholder="输入按钮id">' +
                                                        '    <p>按钮内容：</p>\n' +
                                                        '    <input class="codedata" data_key="        text: "placeholder="输入text">' +
                                                        '    <select  class="buttonfontselect codedata">\n' +
                                                        '         <option>--按钮样式(可无)--</option>\n' +
                                                        '         <option>fa-check</option>\n' +
                                                        '         <option>fa-times</option>\n' +
                                                        '         <option>fa-eercast</option>\n' +
                                                        '         <option>fa-envelope-open</option>\n' +
                                                        '         <option>fa-grav</option>\n' +
                                                        '         <option>fa-id-card-o</option>\n' +
                                                        '    </select>\n' +
                                                        '    <p>tip(可无)：</p>\n' +
                                                        '    <input class="databuttontip codedata" data_key="        tip: "placeholder="鼠标移入提示">' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(butic);
                                                }

                                                if ($(this).val() == "uploader") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var uploader = '<div class="uploadercomp component">\n' +
                                                        '    <p>id：</p>\n' +
                                                        '    <input class="codedata" data_key="      uploader: " placeholder="输入id">' +
                                                        '    <p>上传文件地址：</p>\n' +
                                                        '    <input class="codedata" data_key="        upload-target-dir: "placeholder="输入target-dir">' +
                                                        '    <p>文件大小限制(MB)(选填)：</p>\n' +
                                                        '    <input class="datachoose codedata" data_key="        file-size-limit: "placeholder="fontsizelimit">' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(uploader);
                                                }

                                                if ($(this).val() == "downloader") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var downloader = '<div class="downloadercomp component">\n' +
                                                        '    <p>id：</p>\n' +
                                                        '    <input class="codedata" data_key="      downloader: " placeholder="输入id">' +
                                                        '    <p>对应的controller链接：</p>\n' +
                                                        '    <input class="codedata" data_key="        url: " placeholder="eg:/download/{file}">' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(downloader);
                                                }

                                                if ($(this).val() == "tab") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var tab = '<div class="tabcomp component">\n' +
                                                        '    <p>tab栏id：</p>\n' +
                                                        '    <input class="codedata" data_key="      tab: " placeholder="输入id">' +
                                                        '    <button class="addtab-li">\n' +
                                                        '     +添加卡片页\n' +
                                                        '    </button>\n' +
                                                        '    <div class="tablis">\n' +
                                                        '    </div>' +
                                                        '    <div class="datatablidiv codedata" ></div>' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(tab);
                                                    //添加卡片页
                                                    $(this).closest(".comps").children("div:last-child").children(".addtab-li").on("click", function () {
                                                        var tabli = '<div class="tabli iconfont">' +
                                                            '    <p>卡片页文字：</p>\n' +
                                                            '    <input class="datatabli codedata" data_key="        tab-li: "placeholder="">\n' +
                                                            '    <p>对应的div的id：</p>\n' +
                                                            '    <input class="datatablidydiv" placeholder="DIV的id">\n' +
                                                            '    <div class="icon-chacha deltabli"></div>\n' +
                                                            '</div>';
                                                        $(this).closest(".tabcomp").children(".tablis").append(tabli);
                                                        $(this).next().children("div:last-child").children(".deltabli").on("click", function () {
                                                            var flag = confirm("确定要删除此卡片页吗？");
                                                            if (flag == true) {
                                                                $(this).closest(".tabli").remove();
                                                            }
                                                        })
                                                    });
                                                }

                                                if ($(this).val() == "table") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var table = '<div class="tablecomp component">\n' +
                                                        '    <p>表id：</p>\n' +
                                                        '    <input  class="codedata" data_key="      table: " placeholder="输入id">' +
                                                        '    <p>url：</p>\n' +
                                                        '    <input  class="codedata" data_key="        url: "  placeholder="数据接口">' +
                                                        '    <button class="addtabparam">\n' +
                                                        '     +表的提交参数\n' +
                                                        '    </button>\n' +
                                                        '    <div class="tabparams">\n' +
                                                        '    </div>' +
                                                        '    <button class="addtabhead">\n' +
                                                        '     +表格内容\n' +
                                                        '    </button>\n' +
                                                        '    <div class="tabheads">\n' +
                                                        '        <div class="tablehead">\n' +
                                                        '        <p>表头(唯一标识)：</p>' +
                                                        '        <input class="datatabhead codedata" data_key="        pri-head: " placeholder="文字, 数据key">\n' +
                                                        '        </div>\n' +
                                                        '    </div>' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(table);
                                                    var param = '<div class="tabparam iconfont">\n' +
                                                        '     <p>数据类型：</p>\n' +
                                                        '     <input class="datatabparam codedata" data_key="        param: ">\n' +
                                                        '     <p>变量名：</p>\n' +
                                                        '     <input>\n' +
                                                        '    <div class="icon-chacha deltabparam"></div>\n' +
                                                        '    </div>';
                                                    //添加表的提交参数
                                                    $(this).closest(".comps").children("div:last-child").children(".addtabparam").on("click", function () {
                                                        $(this).next().append(param);
                                                        $(this).next().children("div:last-child").children(".deltabparam").on("click", function () {
                                                            var flag = confirm("确定要删除此参数吗？");
                                                            if (flag == true) {
                                                                $(this).closest(".tabparam").remove();
                                                            }
                                                        })
                                                    })

                                                    var head = '<div class="tablehead iconfont">\n' +
                                                        '     <p>表头：</p>\n' +
                                                        '     <input class="datatabhead codedata" data_key="        head: "placeholder="文字, 数据key">\n' +
                                                        '    <div class="icon-chacha deltablehead"></div>\n' +
                                                        '    </div>';
                                                    //添加表格内容
                                                    $(this).closest(".comps").children("div:last-child").children(".addtabhead").on("click", function () {
                                                        $(this).next().append(head);
                                                        $(this).next().children("div:last-child").children(".deltablehead").on("click", function () {
                                                            var flag = confirm("确定要删除此参数吗？");
                                                            if (flag == true) {
                                                                $(this).closest(".tablehead").remove();
                                                            }
                                                        })
                                                    })
                                                }

                                                if ($(this).val() == "page-table") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var pagetable = '<div class="pagetablecomp component">\n' +
                                                        '    <p>表id：</p>\n' +
                                                        '    <input class="codedata" data_key="      page-table: "placeholder="输入id">' +
                                                        '    <p>url：</p>\n' +
                                                        '    <input class="codedata" data_key="        url: "placeholder="数据接口">' +
                                                        '    <button class="addpagetabparam">\n' +
                                                        '     +表的提交参数\n' +
                                                        '    </button>\n' +
                                                        '    <div class="pagetabparams">\n' +
                                                        '    </div>' +
                                                        '    <button class="addpagetabhead">\n' +
                                                        '     +表格内容\n' +
                                                        '    </button>\n' +
                                                        '    <div class="pagetabheads">\n' +
                                                        '        <div class="pagetablehead">\n' +
                                                        '        <p>表头(唯一标识)：</p>' +
                                                        '        <input class="datatabhead codedata" data_key="        pri-head: "placeholder="文字, 数据key">\n' +
                                                        '        </div>\n' +
                                                        '    </div>' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(pagetable);
                                                    var param = '<div class="pagetabparam iconfont">\n' +
                                                        '     <p>数据类型：</p>\n' +
                                                        '     <input class="datatabparam codedata" data_key="        param: ">\n' +
                                                        '     <p>变量名：</p>\n' +
                                                        '     <input>\n' +
                                                        '    <div class="icon-chacha delpagetabparam"></div>\n' +
                                                        '    </div>';
                                                    //添加表的提交参数
                                                    $(this).closest(".comps").children("div:last-child").children(".addpagetabparam").on("click", function () {
                                                        $(this).next().append(param);
                                                        $(this).next().children("div:last-child").children(".delpagetabparam").on("click", function () {
                                                            var flag = confirm("确定要删除此参数吗？");
                                                            if (flag == true) {
                                                                $(this).closest(".pagetabparam").remove();
                                                            }
                                                        })
                                                    })

                                                    var head = '<div class="pagetablehead iconfont">\n' +
                                                        '     <p>表头：</p>\n' +
                                                        '     <input class="datatabhead codedata" data_key="        head: "placeholder="文字, 数据key">\n' +
                                                        '    <div class="icon-chacha delpagetablehead"></div>\n' +

                                                        '    </div>';
                                                    //添加表格内容
                                                    $(this).closest(".comps").children("div:last-child").children(".addpagetabhead").on("click", function () {
                                                        $(this).next().append(head);
                                                        $(this).next().children("div:last-child").children(".delpagetablehead").on("click", function () {
                                                            var flag = confirm("确定要删除此参数吗？");
                                                            if (flag == true) {
                                                                $(this).closest(".pagetablehead").remove();
                                                            }
                                                        })
                                                    })
                                                }

                                                if ($(this).val() == "datepicker") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var datepicker = '<div class="datepickercomp component">\n' +
                                                        '    <p>id：</p>\n' +
                                                        '    <input class="codedata" data_key="      datepicker: "placeholder="输入id">' +
                                                        '    <p>输入日期选择器的placeholder(可无)：</p>\n' +
                                                        '    <input class="codedata" data_key="        placeholder: "placeholder="输入placeholder">' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(datepicker);
                                                }

                                                if ($(this).val() == "selector") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var selector = '<div class="selectorcomp component">\n' +
                                                        '    <p>id：</p>\n' +
                                                        '    <input class="codedata" data_key="      selector: "placeholder="输入id">' +
                                                        '    <button class="addselector">\n' +
                                                        '     +添加选项\n' +
                                                        '    </button>\n' +
                                                        '    <div class="sels">\n' +
                                                        '        <div class="sel iconfont">\n' +
                                                        '             <p>选项：</p>\n' +
                                                        '             <input class="codedata" data_key="        sel-item: " placeholder="显示文字,值">' +
                                                        '        </div>\n' +
                                                        '    </div>\n' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(selector);
                                                    var sel = '<div class="sel iconfont">\n' +
                                                        '             <p>选项：</p>\n' +
                                                        '             <input class="codedata" data_key="        sel-item: "placeholder="显示文字,值">' +
                                                        '             <div class="icon-chacha delsel"></div>\n' +
                                                        '        </div>\n';
                                                    //添加选项 按钮
                                                    $(this).closest(".comps").children("div:last-child").children(".addselector").on("click", function () {
                                                        $(this).next().append(sel);
                                                        $(this).next().children("div:last-child").children(".delsel").on("click", function () {
                                                            var flag = confirm("确定要删除此选项吗？");
                                                            if (flag == true) {
                                                                $(this).closest(".sel").remove();
                                                            }
                                                        })
                                                    })
                                                }

                                                if ($(this).val() == "multi-selector") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var mutiselector = '<div class="mutiselectorcomp component">\n' +
                                                        '    <p>id：</p>\n' +
                                                        '    <input class="codedata" data_key="      multi-selector: "placeholder="输入id">' +
                                                        '    <button class="addmutiselector">\n' +
                                                        '     +添加选项\n' +
                                                        '    </button>\n' +
                                                        '    <div class="msels">\n' +
                                                        '        <div class="msel iconfont">\n' +
                                                        '             <p>选项：</p>\n' +
                                                        '             <input class="codedata" data_key="        sel-item: "placeholder="显示文字,值">' +
                                                        '        </div>\n' +
                                                        '    </div>\n' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(mutiselector);
                                                    var msel = '<div class="msel iconfont">\n' +
                                                        '             <p>选项：</p>\n' +
                                                        '             <input class="codedata" data_key="        sel-item: " placeholder="显示文字,值">' +
                                                        '             <div class="icon-chacha delmsel"></div>\n' +
                                                        '        </div>\n';
                                                    //添加选项 按钮
                                                    $(this).closest(".comps").children("div:last-child").children(".addmutiselector").on("click", function () {
                                                        $(this).next().append(msel);
                                                        $(this).next().children("div:last-child").children(".delmsel").on("click", function () {
                                                            var flag = confirm("确定要删除此选项吗？");
                                                            if (flag == true) {
                                                                $(this).closest(".msel").remove();
                                                            }
                                                        })
                                                    })
                                                }

                                                if ($(this).val() == "editor") {
                                                    $(this).closest(".comps").children(".component").remove();
                                                    var edit = '<div class="editcomp component">\n' +
                                                        '    <p>id：</p>\n' +
                                                        '    <input class="codedata" data_key="      editor: "placeholder="输入id">' +
                                                        '    <p>输入高度(px)：</p>\n' +
                                                        '    <input class="codedata" data_key="        height: " placeholder="文本框高度">' +
                                                        '</div>';
                                                    $(this).closest(".comps").append(edit);
                                                }
                                            })
                                        })
                                    }
                                })
                            })

                            //page中添加form按钮
                            $(this).closest("#page").children("div:last-child").children("div:first-child").next().children(".addforms").on("click", function () {
                                var form = "<div class='pageform'>\n" +
                                    '           <div class="iconfont">\n' +
                                    '               <div class="icon-chacha delform"></div>\n' +
                                    '               <i class="divtitle">form：</i>\n' +
                                    '           </div>\n' +
                                    '           <div>\n' +
                                    '               <p>id：</p>' +
                                    '               <input class="codedata" data_key="  form:" placeholder="请输入id">\n' +
                                    '               <p>url：</p>' +
                                    '               <input class="codedata" data_key="    url: " placeholder="接口地址">\n' +
                                    '               <p>class(选填)：</p>' +
                                    '               <input class="codedata" data_key="    class: " placeholder="请输入class">\n' +
                                    '           </div>\n' +
                                    '           <div class="formcomp dataformcomp codedata" data_key="    components: ">' +
                                    '                <button class="addformcomp">\n' +
                                    '                 +添加组件\n' +
                                    '                </button>\n' +
                                    '               <div class="buttonform">' +
                                    '                   <p>form的提交按钮id：</p>' +
                                    '                   <input class="codedata" data_key="      button-form: " placeholder="请输入id">\n' +
                                    '                   <p>按钮text：</p>' +
                                    '                   <input class="codedata" data_key="        text: " placeholder="请输入text">\n' +
                                    '                   <p>按钮font(选填)：</p>' +
                                    '                   <input class="codedata" data_key="        font: " placeholder="请输入font">\n' +
                                    '               </div>\n' +
                                    '           </div>\n' +
                                    "</div>\n";
                                $(this).closest("div").closest(".pages").append(form);

                                //删除form
                                $(this).closest("#pagetitle").closest(".pages").children("div:last-child").children("div:first-child").children(".delform").on("click", function () {
                                    var flag = confirm("确定要删除此form吗？");
                                    if (flag == true) {
                                        $(this).closest(".iconfont").closest(".pageform").remove();
                                    }

                                })

                                //添加form组件按钮
                                $(this).closest("#pagetitle").closest(".pages").children("div:last-child").children(".formcomp").children(".addformcomp").on("click", function () {
                                    var formcomp = '<div class="formcomps iconfont">' +
                                        '        <div class="icon-chacha delformcomsel"></div>\n' +
                                        '       <select class="formcomponentselect">\n' +
                                        '           <option value="0">--选择组件--</option>\n' +
                                        '           <option>input</option>\n' +
                                        '           <option>downlist(json)</option>\n' +
                                        '           <option>downlist(数组)</option>\n' +
                                        '           <option>autocomplete</option>\n' +
                                        '        </select>\n' +
                                        '</div>';
                                    $(this).closest(".formcomp").children(".buttonform").before(formcomp);

                                    //删除组件按钮
                                    $(this).closest(".formcomp").children("div:last-child").prev().children(".delformcomsel").on("click", function () {
                                        var flag = confirm("确定要删除此组件吗？");
                                        if (flag == true) {
                                            $(this).closest(".formcomps").remove();
                                        }
                                    })

                                    //根据select的值来添加div
                                    $(this).closest(".formcomp").children("div:last-child").prev().children(".formcomponentselect").on("change", function () {

                                        if ($(this).val() == 0) {
                                            $(this).closest(".formcomps").children(".formcomponent").remove();
                                        }

                                        if ($(this).val() == "input") {
                                            $(this).closest(".formcomps").children(".formcomponent").remove();
                                            var input = '<div class="inputcomp formcomponent">\n' +
                                                '    <p>id：</p>\n' +
                                                '    <input class="codedata" data_key="      input: " placeholder="输入组件id">' +
                                                '    <p>placeholder(选填)：</p>\n' +
                                                '    <input class="datachoose codedata" data_key="        placeholder: " placeholder="placeholder">' +
                                                '    <p>label(选填)：</p>\n' +
                                                '    <input class="datachoose codedata" data_key="        label: " placeholder="输入提示">' +
                                                '    <p>value(选填)：</p>\n' +
                                                '    <input class="datachoose codedata" data_key="        value: "  placeholder="输入输入框的内容">' +
                                                '    <select class="formcomponentselect codedata" data_key="        verifier: ">\n' +
                                                '        <option value="0">--校验种类(可无)--</option>\n' +
                                                '        <option>email</option>\n' +
                                                '        <option>nonull</option>\n' +
                                                '        <option>int</option>\n' +
                                                '        <option>double</option>\n' +
                                                '        <option>date</option>\n' +
                                                '        <option>year</option>\n' +
                                                '     </select>\n' +
                                                '</div>';
                                            $(this).closest(".formcomps").append(input);

                                        }

                                        if ($(this).val() == "downlist(json)") {
                                            $(this).closest(".formcomps").children(".formcomponent").remove();
                                            var downlistjson = '<div class="downlist-jsoncomp formcomponent">\n' +
                                                '    <p>id：</p>\n' +
                                                '    <input class="codedata" data_key="      downlist: " placeholder="输入组件id">' +
                                                '    <p>url(获取列表内容的接口地址，返回JSON数组)：</p>\n' +
                                                '    <input class="codedata" data_key="        url: " placeholder="输入组件id">' +
                                                '    <p>placeholder(选填)：</p>\n' +
                                                '    <input class="datachoose codedata" data_key="        placeholder: " placeholder="输入placeholder">' +
                                                '</div>';
                                            $(this).closest(".formcomps").append(downlistjson);
                                        }

                                        if ($(this).val() == "downlist(数组)") {
                                            $(this).closest(".formcomps").children(".formcomponent").remove();
                                            var downlist = '<div class="downlistcomp formcomponent">\n' +
                                                '    <p>id：</p>\n' +
                                                '    <input class="codedata" data_key="      downlist: " placeholder="输入组件id">' +
                                                '    <p>items[第一个选项, 第二个选项, 第三个选项]：</p>\n' +
                                                '    <input class="codedata" data_key="        items: " placeholder="eg:[fir,sec,thr]">' +
                                                '    <p>placeholder(选填)：</p>\n' +
                                                '    <input class="datachoose codedata" data_key="        placeholder: " placeholder="输入placeholder">' +
                                                '</div>';
                                            $(this).closest(".formcomps").append(downlist);
                                        }

                                        if ($(this).val() == "autocomplete") {
                                            $(this).closest(".formcomps").children(".formcomponent").remove();
                                            var autocomplete = '<div class="downlistcomp formcomponent">\n' +
                                                '    <p>id：</p>\n' +
                                                '    <input class="codedata" data_key="      autocomplete: " placeholder="输入组件id">' +
                                                '    <p>url：</p>\n' +
                                                '    <input class="codedata" data_key="        url: " placeholder="/inputtest/autocomplete">' +
                                                '    <p>placeholder(选填)：</p>\n' +
                                                '    <input class="datachoose codedata" data_key="        placeholder: " placeholder="输入placeholder">' +
                                                '</div>';
                                            $(this).closest(".formcomps").append(autocomplete);
                                        }
                                    })
                                })
                            })
                        })


                        $(".side-right").children("div:last-child").children(".content").children("section").children("#tempershow").on("click", function () {
                            //project模块获取用户输入内容
                            $("#project .codedata").each(function (i, e) {
                                if (i == 0) {
                                    var keyvalue = $(this).attr("data_key");
                                } else {
                                    var keyvalue = $(this).attr("data_key") + $(this).val();
                                }
                                console.log(keyvalue);
                            })

                            //bean模块获取用户输入内容
                            $("#bean .codedata").each(function (i, e) {
                                //数据类型
                                if ($(this).attr("class") == "datatype codedata") {
                                    $(this).attr("data_key", $(this).val())
                                    return true;//跳出本次循环
                                }

                                //变量名
                                else if ($(this).attr("class") == "dataname codedata") {
                                    $(this).attr("data_key", "     " + $(this).val() + ": ")
                                    var keyvalue = $(this).attr("data_key") + $(this).prev().prev().attr("data_key");
                                }

                                //对应表中字段名(选填)：
                                else if ($(this).attr("class") == "col-name codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //对应表中数据类型(选填)：
                                else if ($(this).attr("class") == "col-type codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //是否需要@Transient
                                else if ($(this).attr("class") == "col-not icon-danxuankuang") {
                                    if ($(this).attr("data_checked") == 1) {
                                        $(this).addClass("codedata");
                                    }
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("codedata");
                                    }
                                }

                                //方法名
                                else if ($(this).attr("class") == "funname codedata") {
                                    var keyvalue = "     " + $(this).prev().prev().val() + ": " + $(this).val()
                                }

                                //sql语句
                                else if ($(this).attr("class") == "sql codedata") {
                                    var keyvalue = $(this).attr("data_key") + $(this).val();
                                }

                                //参数是否有id
                                else if ($(this).attr("class") == "canshuid icon-danxuankuang") {
                                    if ($(this).attr("data_checked") == 1) {
                                        $(this).addClass("codedata");
                                    }
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("codedata");
                                    }
                                }

                                //参数是否有name
                                else if ($(this).attr("class") == "canshuname icon-danxuankuang") {
                                    if ($(this).attr("data_checked") == 1) {
                                        $(this).addClass("codedata");
                                    }
                                    if ($(this).attr("data_checked") == 0) {
                                        $(this).removeClass("codedata");
                                    }
                                }

                                else if ($(this).attr("class") == "funpropkey codedata") {
                                    var keyvalue = "        " + $(this).next().val() + ": " + $(this).val();
                                }

                                //判断user是否有特殊角色
                                else if ($(this).attr("class") == "alluserroles codedata") {
                                    if ($(this).children("div").length == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key");
                                    }
                                }

                                //user的角色和描述
                                else if ($(this).attr("class") == "datarolename codedata") {
                                    var keyvalue = "     " + $(this).val()+": "+$(this).next().next().val();
                                }


                                else {
                                    var keyvalue = $(this).attr("data_key") + $(this).val();
                                }
                                console.log(keyvalue);
                            })

                            //page模块获取用户输入内容
                            $("#page .codedata").each(function (i, e) {

                                //div的class属性
                                if ($(this).attr("class") == "datadivclass codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //div的select公共模块
                                else if ($(this).attr("class") == "datadivth-include codedata") {
                                    if ($(this).prev(".th-include").val() == "--选择公共模块(如果有)--") {
                                        return true;
                                    } else {
                                        var keyvalue = "    " + $(this).prev().val() + ": " + $(this).val();
                                    }
                                }

                                //嵌套的div的select公共模块
                                else if ($(this).attr("class") == "datanesteddivth-include codedata") {
                                    if ($(this).prev(".th-include").val() == "--选择公共模块(如果有)--") {
                                        return true;
                                    } else {
                                        var keyvalue = "      " + $(this).prev().val() + ": " + $(this).val();
                                    }
                                }

                                //判断div是否有组件
                                else if ($(this).attr("class") == "comp datadivcomp codedata") {
                                    if ($(this).children("div").length == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key");
                                    }
                                }

                                //判断嵌套的div是否有组件
                                else if ($(this).attr("class") == "nestedcomp datanesteddivcomp codedata") {
                                    if ($(this).children("div").length == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key");
                                    }
                                }

                                //popup
                                else if ($(this).attr("class") == "datapopupid codedata") {
                                    var keyvalue = "      " + $(this).closest(".popupcomp").prev(".componentselect").val() + ": " + $(this).val();
                                }

                                //popup里按钮选择样式
                                else if ($(this).attr("class") == "popfontselect codedata") {
                                    if ($(this).val() == "--按钮样式(可无)--") return true;
                                    else {
                                        var keyvalue = "          font: " + $(this).val();
                                    }
                                }

                                //button
                                else if ($(this).attr("class") == "databuttonid codedata") {
                                    var keyvalue = "      " + $(this).closest(".butcomp").prev(".componentselect").val() + ": " + $(this).val();
                                } else if ($(this).attr("class") == "databutton-smallid codedata") {
                                    var keyvalue = "      " + $(this).closest(".butsmacomp").prev(".componentselect").val() + ": " + $(this).val();
                                } else if ($(this).attr("class") == "databutton-bigid codedata") {
                                    var keyvalue = "      " + $(this).closest(".butbigcomp").prev(".componentselect").val() + ": " + $(this).val();
                                } else if ($(this).attr("class") == "databutton-iconid codedata") {
                                    var keyvalue = "      " + $(this).closest(".buticcomp").prev(".componentselect").val() + ": " + $(this).val();
                                }

                                //button里的样式选择
                                else if ($(this).attr("class") == "buttonfontselect codedata") {
                                    if ($(this).val() == "--按钮样式(可无)--") return true;
                                    else {
                                        var keyvalue = "        font: " + $(this).val();
                                    }
                                } else if ($(this).attr("class") == "nestedpopfontselect codedata") {
                                    if ($(this).val() == "--按钮样式(可无)--") return true;
                                    else {
                                        var keyvalue = "          font: " + $(this).val();
                                    }
                                }


                                //按钮的tip
                                else if ($(this).attr("class") == "databuttontip codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //卡片页
                                else if ($(this).attr("class") == "datatabli codedata") {
                                    var keyvalue = $(this).attr("data_key") + $(this).val() + ", " + $(this).next().next().val();
                                }

                                //卡片页对应的div
                                else if ($(this).attr("class") == "datatablidiv codedata") {
                                    $("#page .datatablidydiv").each(function (i, e) {
                                        var keyvalue = "    div: " + $(this).val();
                                        console.log(keyvalue);
                                    })
                                    return true;
                                }

                                //嵌套的卡片页对应的div
                                else if ($(this).attr("class") == "datanestedtablidiv codedata") {
                                    $("#page .datanestedtablidydiv").each(function (i, e) {
                                        var keyvalue = "      div: " + $(this).val();
                                        console.log(keyvalue);
                                    })
                                    return true;
                                }

                                //table的param
                                else if ($(this).attr("class") == "datatabparam codedata") {
                                    var keyvalue = $(this).attr("data_key") + $(this).val() + ", " + $(this).next().next().val();
                                }

                                //嵌套的table的param
                                else if ($(this).attr("class") == "datanestedtabparam codedata") {
                                    var keyvalue = $(this).attr("data_key") + $(this).val() + ", " + $(this).next().next().val();
                                }

                                //判断form是否有组件
                                else if ($(this).attr("class") == "formcomp dataformcomp codedata") {
                                    if ($(this).children("div").length == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key");
                                    }
                                }

                                //一些选填的空
                                else if ($(this).attr("class") == "datachoose codedata") {
                                    if ($(this).val() == "") {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                }

                                //form中的verifier
                                else if ($(this).attr("class") == "formcomponentselect codedata") {
                                    if ($(this).val() == 0) {
                                        return true;
                                    } else {
                                        var keyvalue = $(this).attr("data_key") + $(this).val();
                                    }
                                } else {
                                    var keyvalue = $(this).attr("data_key") + $(this).val();
                                }
                                console.log(keyvalue);
                            })
                        })
                    }
                })

            }

            //点击tab切换效果
            this.selectitem = function () {
                const items = document.querySelectorAll('.item')
                const sections = document.querySelectorAll('section');

                function removeActive() {
                    items.forEach(item => {
                        item.classList.remove('active')
                    });
                    sections.forEach(item => {
                        item.classList.remove('active')
                    });

                }

                items.forEach((item, index) => {
                    item.addEventListener('click', function () {
                        removeActive();
                        item.classList.add('active');
                        sections[index].classList.add('active');
                    })
                })
            }

            //删除后进行排序 便于分页
            this.sort = function () {
                let para = $("#projs .proj").length;//获取tbody删除一行后tr的个数
                for (let i = 0; i < para; i++) { //循环遍历
                    //将遍历后的i给到每行的序号中
                    $('#projs').find('.proj').eq(i).attr("data_id", i);
                }
            }

            function uuid() {
                var s = [];
                var hexDigits = "0123456789abcdef";
                for (var i = 0; i < 36; i++) {
                    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
                }
                s[14] = "4";   // bits 12-15 of the time_hi_and_version field to 0010
                s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);   // bits 6-7 of the clock_seq_hi_and_reserved to 01
                s[8] = s[13] = s[18] = s[23] = "-";

                var uuid = s.join("");
                return uuid;
            }
        }

        return code;
    }
);