define(
    [],
    function(){
        function I18N( container ){
            this.container = container;

            this.init = function( page ){
                var _that = this;
                $.post(
                    '/i18n',
                    { page : page },
                    function( rst ){
                        //刷新整个页面输出
                        _that.container.find('[i18n]').each(
                            function(){
                                var text = rst[$(this).attr("i18n")];
                                if( !!text )
                                    $(this).text(text);
                            }
                        );

                        //将国际化信息填充到本对象中
                        for(var key in rst){
                            _that[key] = rst[key];
                        }
                    }
                )
            }
        }

        return I18N;
    }
)