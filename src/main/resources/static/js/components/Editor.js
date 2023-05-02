define(
	["wangEditor.min", "xss.min", "i18next.min"],
	function( et, xss, it ){
		
		function Editor( container, ec ){
			this.container = container;

			this.ec = ec;
			this.name = "EDITOR";

			var _that = this;
			
			var menus = [
			    'head',  // 标题
			    'italic',  // 斜体
			    'list',  // 列表
			    'justify',  // 对齐方式
			    'undo',  // 撤销
			    'redo'  // 重复
			];

/*			this.change = function(id){
				_that.ec.fire(
					_that.name,
                    'editorchanged',
                    {id:id, text: _that.txt(id)}
				)
			}*/

			this.editors = [];
			
			this.initDom = function( id, height ){
				var editor = new et('#' + id );
				editor.i18next = it;
				editor.config.fontSizes = {
					'x-small': { name: '10px', value: '1' },
					'small': { name: '13px', value: '2' },
					'normal': { name: '13px', value: '3' },
					'large': { name: '18px', value: '4' },
					'x-large': { name: '24px', value: '5' },
					'xx-large': { name: '32px', value: '6' },
					'xxx-large': { name: '48px', value: '7' },
				}

/*				editor.config.onchange = function(){
					_that.change(id);
				}*/
				editor.config.height = height
				editor.config.lang = 'zh-CN';
				editor.config.fontSizes = {'normal': { name: '12px', value: '2' }};
				editor.config.menus = menus;
				editor.config.pasteFilterStyle = false;
				editor.config.pasteText= false;
				editor.config.pasteTextHandle = function (content){
				     if (content == '' && !content) return '';
				     var html = content;
				     html = html.replace(/<\/?SPANYES[^>]*>/gi, "");//  Remove  all  SPAN  tags
				     html = html.replace(/<(\w[^>]*)  lang=([^|>]*)([^>]*)/gi, "<$1$3");//  Remove  Lang  attributes
				     html = html.replace(/<\\?\?xml[^>]*>/gi, "");//  Remove  XML  elements  and  declarations
				     html = html.replace(/<\/?\w+:[^>]*>/gi, "");//  Remove  Tags  with  XML  namespace  declarations:  <o:p></o:p>
				     html = html.replace(/&nbsp;/, "");//  Replace  the  &nbsp;
				     html = html.replace(/\n(\n)*( )*(\n)*\n/gi, '\n');
				     return html;
				};
				editor.create();

				this.editors[id] = editor;
			}

			this.init = function(){
				this.container.find('.editor').each(
					function(){
						var height = $(this).attr('height');
						var id = $(this).attr('id');
						_that.initDom(id, height);
					}
				)
				
				return this;
			}
			
			this.html = function(id){
				return filterXSS(this.editors[id].txt.html());	
			}

			this.insert = function(tag, html){
				var content = $('#'+this.editors[id].txt.editor.textElemId);
				content.find(tag).append(html);
			}

			this.update = function(tag, html){
				var content = $('#'+this.editors[id].txt.editor.textElemId);
				content.find(tag).html(html);
			}
			
			this.txt = function(id){
				return filterXSS(this.editors[id].txt.text());	
			}
			
			this.set = function( id, html ){
				this.editors[id].txt.html( html );
			}
			
			this.clear = function(id){
				this.editors[id].clear();
			}
			
			this.scrollToHead = function(id){
				this.editors[id].scrollToHead(id);
			}
			
		}
		return Editor;
	}
)