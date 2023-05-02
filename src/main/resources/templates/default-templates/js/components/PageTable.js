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

    JSONObject obj4 = new JSONObject();
    obj4.put("name", "name4");        
    obj4.put("id", "4");  
    obj4.put("frt", "frt4");
    obj4.put("sec", "sec4");
    obj4.put("thr", "thr4");

    if( pageNumber == 1 ) {
        list.add(obj1); list.add(obj2); 
    }else{
        list.add(obj3); list.add(obj4);
    }
    
    rst.put("content", list);
    rst.put("totalElements", 4); //结果集总条数
    rst.put("number",  pageNumber); //第几页
    rst.put("size", pageSize); //页面大小
    rst.put("totalPages", 2); //总页数

        TEST js
        this.pageTables.fill('pagetable', {key: 'test', id: '1'}, 2);
 */
        define([
            "template", "pagization"
        ], function( template, pagization) {
            function t(url, dom, render ){
                this.url = url;
                this.dom = dom;
                this.render = render;
            };
        
            function PageTable( container , ec ){
                this.ec = ec;
                this.container = container;
            
                var _that = this;
                this.tables = {};
        
                this.fill = function( id, params, size, dataCallback, domCallback ){
                    var table = this.tables[id];
                    var paramstr = '';
                    
                    for( var key in params ){
                        paramstr += key + "=" +params[key] + '&';
                    }				

                    if( paramstr.endsWith('&') ) paramstr = paramstr.substring(0, paramstr.length-1);
                    var dataSourceUrl = table.url + "?" + paramstr;
                    var render = table.render;
                    var paginateParam = {
                        dataSource: dataSourceUrl,
                        showGoInput: true,
                        showGoButton: true,
                        locator: 'content',
                        totalNumberLocator: function(response) {
                            return response.totalElements;
                        },
                        pageSize: size,
                        showNavigator: true,
                        formatNavigator: '当前页' +' <span style="color:#f00"><%= currentPage %></span>  of  <%= totalPage %>  ,  总页数  <%= totalPage %>',
                        prevText : '<i class="fa fa-long-arrow-left"></i>',
                        nextText : '<i class="fa fa-long-arrow-right"></i>',
                        ajax: { },
                        callback:  function(data, pg){ 
                            if(!!dataCallback)  dataCallback(data);
                            var rst = {}; rst.list = data;
                            table.dom.find('.body').empty().append( render( rst) );
                            if(!!domCallback)
                                domCallback( table.dom.find('.body'));
                        }
                    }
                    
                    table.dom.find('.page').pagination( paginateParam );
                }
        
        
        
                this.init = function(){
                    var table = this.container.find('.pagetable');
                    table.each(
                        function(){
                            var id = $(this).attr("id");
                            var url = $(this).attr("url");
                            _that.tables[id] = new t(url, $(this), _that.getRender($(this)));
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
            return PageTable;
        });