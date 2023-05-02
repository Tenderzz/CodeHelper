define(
    ["dataverifier"],
    function(df){
        function InputWidget( container ){
            var _that = this;
            this.container = container;

            this.hideAutoListDom = function(){
                $('.autolist').hide();
            }

            this.init = function(){
                var inputs = this.container.find('div[inputwidget]');
                inputs.each(
                    function(){
                        var type = $(this).attr('inputwidget');
                        switch( type ){
                            case 'input': 
                                _that.initInput($(this));
                                break;
                            case 'downlist':
                                _that.initDownlist($(this));
                                break;
                            case 'autocomplete':
                                _that.initAutocomplete($(this));
                                break;
                        }
                    }
                );
                
                //添加页面中点击后
                $(document).unbind('click', _that.hideAutoListDom ).click(
                    function(){
                        _that.hideAutoListDom();
                    }
                    
                );
            }

            this.initAutocomplete = function( dom ){
                var url = dom.attr('url');
                dom.find('input').keyup(
                    function(){
                        try{
                            $.post(
                                url,
                                function(rst){
                                    dom.find('.autolist').empty();
                                    var value = dom.find('input').val();
                                    dom.attr('data', value);
                                    rst.forEach( function( i ){
                                        if(i.item.startsWith( value )){
                                            var item = $('<div>');
                                            item.attr('value', i.value );
                                            item.text(i.item);
                                            dom.find('.autolist').append(item);    
                                        }                        
                                    })
                                    dom.find('.autolist').show();
                                    dom.find('.autolist>div').unbind().click(
                                        _that.itemClicked
                                    );
                                }
                            )
                        }catch(e){

                        }
                        var val = dom.find('input').val();
                        dom.attr('data', val);
                    }
                    
                )
            }

            this.itemClicked = function(e){
                $(this).closest('.autolist').parent()
                    .attr('data', $(this).attr('value')).find('input')
                    .val($(this).text());
                _that.hideAutoListDom();
                e.stopPropagation();
            }

            this.initDownlist = function( dom ){
                dom.find('input').attr('readonly', 'readonly'); //禁止输入
                var url = dom.attr('url');
                var list = dom.find('.autolist');

                dom.find('.autolist>div').click(
                    _that.itemClicked
                );

                if( !!url ){
                    $.post(
                        url,
                        function( rst ){
                            rst.forEach( function( i ){
                                var item = $('<div>');
                                item.attr('value', i.value );
                                item.text(i.item);
                                list.append(item);                                
                            })

                            dom.find('.autolist>div').unbind().click(
                                _that.itemClicked
                            );
                        }
                    )
                }
                dom.click(
                    function(e){
                        dom.find('.autolist').show();
                        e.stopPropagation();
                    }
                );
            }

            this.initInput = function( dom ){
                dom.find('input').keyup(
                    function(){
                        var val = dom.find('input').val();
                        var verifier =  df.verify(dom.attr('verifer'));
                        if(!!verifier && !verifier.exec(val)){
                            dom.attr('verified', 'false').find('label').text( verifier.metion );
                        }else{
                            dom.find('label').text( "" );
                            dom.attr('data', val).removeAttr('verified');
                        }
                    }
                )
            }
                
        }
        return InputWidget;
    }
)