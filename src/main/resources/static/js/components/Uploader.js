define(
    [  "plupload" ],
    function(uploader ){
        function Uploader( 
            id,
            container,      //容器
            eventcenter  //事件中心
        ){
            this.name = 'UPLOADER';
            this.container = container;
            this.fireLabel = container.attr('id');
            
            this.ec = eventcenter;
            this.ec.addHandler( this );

            this.uploaderHandler = null;

            this.upInterface = null;
            this.upComponent = null;

            this.currentFileName = null;    //上传原始文件名

            var _that = this;
            this.init = function(){
                _that.upInterface =  LocalUploader;   
                
                _that.initPluploader();
                if(!!_that.uploadBut){
                    _that.uploadBut.click(  //确定上传按键绑定点击事件
                        function(){
                            _that.upInterface.upload();
                        }
                    );
                }
            }
            
            var LocalUploader = {
                type: 'LOCAL',
                upload : function(){
                    $.post(
                        '/file/uploader/local/sign',
                        function( rst ){
                            _that.innerId = rst.uuid;
                            _that.upComponent.setOption({
                                'url': '/file/uploader/local/upload',
                                'multipart_params': {
                                    'uuid' : rst.uuid
                                }
                            });
                            _that.upComponent.start();

                        }
                    )
                }
            };

            //初始化plupload,绑定事件
            //事件1. 选择文件
            //事件2. 上传
            //上传前要把前台可能的表单事件一并传上去，再去拿签名
            //拿到签名，实现文件上传

            this.initPluploader = function(){
                this.upComponent = new uploader.Uploader(uploaderParams);
                this.upComponent.init();
            }


            var uploaderParams = {
                browse_button : 'selectbut', 
	            container: id,
	            flash_swf_url : '../third-party/plupload-2.1.2/js/Moxie.swf',
	            silverlight_xap_url : '../third-party/plupload-2.1.2/js/Moxie.xap',
                url : '/file/uploader/local/sign',
                http_method: "POST",
                multipart: true,    
                filters: {
                    mime_types : [ 
                        { title : "Image files", extensions : "jpg,gif,png,bmp" }, 
                        { title : "Zip files", extensions : "zip,rar" },
                        { title : "office files", extensions : "docx,doc,ppt,xls,xlsx" },
                        { title : "pdf files", extensions : "pdf" }
                    ],
                    max_file_size : '100mb', //最大只能上传10mb的文件
                    prevent_duplicates : true //不允许选取重复文件
                },
                init: {
                    PostInit: function() {},
                    FilesAdded: function(up, files) {
                        container.find('.note').text( files[0].name );
                        container.find('.pre').css( 'width',  '0px');
                        _that.currentFileName = files[0].name ;
                        _that.currentFile = files[0];
                        _that.upInterface.upload();
                        container.find('#uploading').show();
                        this.files = [];
                    },
            
                    BeforeUpload: function(up, file) {},
            
                    UploadProgress: function(up, file) {
                        container.find('.pre').css( 'width',  file.percent + '%');
                    },
            
                    FileUploaded: function(up, file, info) { //OSS, LOCAL二种上传最终的回调处理
                        container.find('#uploading').hide();
                        _that.ec.fire( 
                            _that.name,
                            "uploaded",                                  
                            {
                                type: _that.upInterface.type,
                                file: file,
                                originName : _that.currentFileName,
                                innerId: _that.innerId,
                                label: _that.fireLabel
                            }
                        ); 
                        _that.currentFileName = null;
                        _that.innerId = null;
                    },
            
                    Error: function(up, err) {         //出错处理
                        console.log(err);
                        _that.currentFileName = null;       //清除暂存变量
                        _that.innerId = null;
                        _that.ec.fire( 
                            _that.name,
                            _that.fireLabel,                                      
                            null    //回传为空，表示失败
                        ); 
                    }
                }
            }

        };
        
        return Uploader; 

    }
)