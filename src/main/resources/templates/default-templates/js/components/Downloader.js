define(
    [''],
    function(){
        function Downloader(){
            this.down = function(url, filename){
                var xhr = new XMLHttpRequest();
                xhr.open('GET', url, true);
                xhr.responseType = 'blob';
                xhr.onload = function () {
                    var blob = xhr.response;
                    if (xhr.status === 200) {
                        if (window.navigator.msSaveOrOpenBlob) {
                            navigator.msSaveBlob(blob, filename);
                        }else {
                            var link = document.createElement('a');
                            var body = document.querySelector('body');
                            link.href = window.URL.createObjectURL(blob);
                            link.download = filename;
                            // fix Firefox
                            link.style.display = 'none';
                            body.appendChild(link);
                            link.click();
                            body.removeChild(link);
                            window.URL.revokeObjectURL(link.href);
                        };
                    }
                };
                xhr.send();
            }
        }

        return Downloader;
    }
)