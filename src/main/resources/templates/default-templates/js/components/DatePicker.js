define(
    ['layui'],
    function(layui){
        function DatePicker(container){
            this.name = "DatePicker";
            this.container = container;
            var _that = this;

            this.init = function(){  
                this.container.find('.datepacker').each(
                    function(){
                        var id = $(this).attr('id');
                        var _that = $(this);
                        layui.laydate.render({
                            elem: '#'+id,
                            lang: 'cn',
                            done: function(value, date, endDate){
                                _that.text( value );
                                _that.attr('data', value);
                            }
                        });
                    }
                )
                
                return this;
            };
        }

        return DatePicker;
    }
);