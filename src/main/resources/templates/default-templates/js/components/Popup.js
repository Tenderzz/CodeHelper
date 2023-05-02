define(
    ['layui'],
    function(layer){
        function Popup(ec){
            this.name = "POPUP";
            this.ec = ec;
            this.ec.addHandler( this );

            var _that = this;
            this.init = function(){
                var layer = layui.layer;
                layer.config({
                    path: '/js/third-party/layui/lay/modules'
                });

                this.layer = layer;
                return this;
            };

            this.pop = function(id, size, buttons, mask){
                $.post(
                    '/pop/'+id,
                    function(rst){
                        var dom = $(rst);
                        var title = dom.find('[title]').attr("title");
                        var font  = dom.find('[font]').attr("font");
                        dom.find('#title span').text(title);
                        dom.find('#title i').addClass(font);

                        var domId = id;
                        var callback = function(dom, index){
                            var buttons = $(dom).find('.popbut');
                            buttons.each(
                                function(  ){
                                    $(this).click( 
                                        function(){
                                            _that.ec.fire(
                                                _that.name,
                                                'popbuttion',
                                                {
                                                    dom: domId,
                                                    bid: $(this).attr('id'), 
                                                    layer: _that,
                                                    dom: dom
                                                }
                                            )
                                        }
                                        
                                    );
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
                            success: function(layero, index){
                                callback(layero, index);
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