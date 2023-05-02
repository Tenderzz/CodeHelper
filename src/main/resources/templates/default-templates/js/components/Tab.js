define(
    [],
    function(){
        function Tab(container){
            this.container = container;
            this.tabDivs = [];

            var _that = this;

            this.hideAllTabDiv = function(){
                _that.tabDivs.forEach(
                    function(e){
                        _that.container.find('#'+e).hide();
                    }
                )
            }

            this.init = function(){
                
                this.container.find('li.mytab').each(
                    function(){
                        var tid = $(this).find('a').attr('tag');
                        _that.tabDivs.push(tid);
                    }
                );                
                this.hideAllTabDiv();
                this.container.find('li.mytab a').click(
                    function( event ){
                        $(this).closest('.nav').find('li.mytab a').removeClass('active');
                        var tid = $(this).attr('tag');
                        _that.hideAllTabDiv();
                        _that.container.find('#' + tid).show();
                        $(this).addClass('active');
                        event.stopPropagation();    
                    }                            
                );
                this.container.find('li.mytab a').eq(0).click();
            }
        }

        return Tab;
    }
)