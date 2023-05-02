define(
    [],
    function(){
        function Button( container, ec ){
            this.name = "BUTTON";
            this.container = container;

            this.ec = ec;
            this.ec.addHandler( this );

            var _that = this;
            
            this.init = function(){
                $('.buttoncomponent').click(
                    function(){
                        _that.ec.fire(
                            _that.name,
                            "button",                                  
                            {
                                id: $(this).attr('id')
                            }
                        );
                    }
                );
            }

            
        };

        return Button;
    }
)