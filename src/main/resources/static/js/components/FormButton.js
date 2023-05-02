define(
    function(){
        function FormButton(container, ec){            
            this.name = "FORMBUTTON";
            this.ec = ec;
            this.container = container;

            var _that = this;

            this.init = function(){
                var forms = this.container.find('div.form');
                forms.each(
                    function(){
                        $(this).find('[form-button]').click(
                            function(){
                                _that.submit($(this));
                            }
                        )
                    }
                )
            }

            this.submit = function( but ){
                var data = _that.gether(but.closest('.form'));
                if(!!data){
                    _that.ec.fire(
                        _that.name,
                        "formbutton",
                        data
                    )
                }
            }

            this.gether = function( form ){
                var datas = form.find('[data]');
                console.log(datas);
                var data = [];
                var flag = false;
                datas.each( function(){
                    if(!!$(this).attr('verified') && $(this).attr('verified') == 'false'){
                        flag = true;
                    }
                    data[$(this).attr('id')] = $(this).attr('data');
                });
                data['formid'] = form.attr('id')
                if( flag ) return null;
                return data;
            }

        }
        return FormButton;
    }
)