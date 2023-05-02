define(
    [],
    function(){
        function Selector( container){
            this.name = "SELECTOR";
            this.container = container;

            var _that = this;
            
            this.init = function(){
                $('.selector span.nocheck').click(
                    function(){
                        //closest 返回第一个祖先节点；找到其type值 判断是selector还是muti
                        var type = $(this).closest('.selector').attr('type');
                        type = (type=='selector' ? true : false);

                        var dom = $(this).closest('.selector');

                        if( type ){
                            $(this).closest('.selector').find('span.nocheck').removeClass("checked");
                            $(this).addClass('checked');  
                            dom.attr('data', $(this).attr('value'));                      
                        }else{
                            if( !$(this).hasClass('checked') ){
                                $(this).addClass('checked');
                            }else{
                                $(this).removeClass('checked');     
                            };
                            var value = [];
                            $(this).closest('.selector').find('span.checked').each(
                                function(){
                                    value.push($(this).attr('value'));
                                }
                            );
                            dom.attr('data', value.join(','));    
                        }
                        console.log(dom.attr('data'));
                    }
                );
            }

            
        };

        return Selector;
    }
)