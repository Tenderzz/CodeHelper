define(
    ['layui'],
    function(layer){
        function Popup(){
            var _that = this;
            this.init = function(){
                var layer = layui.layer;//弹层组件
                layer.config({
                    path: '/js/third-party/layui/lay/modules'
                });

                this.layer = layer;
                return this;
            };

            this.pop = function(id, size, buttons, mask){
                //$.post()是jquery一个简单的 POST 请求功能以取代复杂 $.ajax
                $.post(
                    '/pop/'+id,//发送请求的url地址
                    function(rst){
                        var dom = $(rst);
                        var title = dom.find('.popcontent').attr("title");
                        dom.find('#title span').text(title);

                        var callback1 = function(dom, index){
                            buttons.forEach(
                                function( i ){
                                    dom.find('#'+ i.id).click( i.handler );
                                }
                            );
                        }
                        var m = !!mask ? mask : [0.3, '#000'];
                        var layIdnex =  _that.layer.open({
                            title: false,
                            closeBtn: 1,
                            offset: '80px',
                            fixed: false,
                            type: 1,
                            content: dom.html(),
                            area: size + 'px',
                            shade: m,
                            success: function(layero, index){ //弹出层加载成功后执行
                                callback1(layero, index);
                            }
                        });
        
                        _that.layIdnex = layIdnex;
                    }
                )
            }

            this.close = function(){
                this.layer.close(_that.layIdnex);
            }
        }

        return Popup;
    }
)