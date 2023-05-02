/** TEST java
 * 
        JSONObject obj1 = new JSONObject();
        obj1.put("name", "name1");        
        obj1.put("id", "1");  
        obj1.put("frt", "frt1");
        obj1.put("sec", "sec1");
        obj1.put("thr", "thr1");

        JSONObject obj2 = new JSONObject();
        obj2.put("name", "name2");        
        obj2.put("id", "2");  
        obj2.put("frt", "frt2");
        obj2.put("sec", "sec2");
        obj2.put("thr", "thr2");

        JSONObject obj3 = new JSONObject();
        obj3.put("name", "name3");        
        obj3.put("id", "3");  
        obj3.put("frt", "frt3");
        obj3.put("sec", "sec3");
        obj3.put("thr", "thr3");

        rst.add(obj1); rst.add(obj2); rst.add(obj3);

        TEST js
        this.tables.fill('firsttable', {key: 'test', id: '1'});
 */
define([
    "template"
], function( template ) {
   function t(url, dom, render){
        this.url = url;
        this.dom = dom;
        this.render = render;
   };


   function Table( container , ec ){
        this.ec = ec;
        this.container = container;
    
        var _that = this;
        this.tables = {};

        this.fill = function( id, params, dataCallback, domCallback ){
            var t = _that.tables[id];
            $.post(
                t.url,
                params,
                function(rst){
                    if( !!rst ){
                        if( !!dataCallback ){
                            rst = callback(rst);                            
                        }
                        var data = {}; data.list = rst;
                        var dom = t.render(data);

                        t.dom.find('.tablebody').empty().append(dom);
                        if( !!domCallback ){
                            domCallback(t.dom.find('.tablebody .row'));
                        }
                    }
                }
            )
        }



        this.init = function(){
            var table = this.container.find('.table');
            table.each(
                function(){
                    var id = $(this).attr("id");
                    var url = $(this).attr("url");
                    var render = _that.getRender($(this));
                    _that.tables[id] = new t(url, $(this), render);
                }
            );
            return _that;
        }

        this.getRender = function(dom){
            var row = "{{ each list as data }}<div class='row'>";
            var heads = dom.find('.tablehead>div');
            
            heads.each(
                function(){
                    row += "<div class='cell'>{{data." + $(this).attr('data') + "}}</div>";
                }
            );
            row += "</div>{{ /each }}";

            return template.compile( row );
        }

   }
   return Table;
});