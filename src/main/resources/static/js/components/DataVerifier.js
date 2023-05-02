define(
	function(){
       var DataVerifier = {};

       DataVerifier.qj2bj = function(str) {
            var tmp = "";
            for (var i = 0; i < str.length; i++) {
                if (str.charCodeAt(i) >= 65281 && str.charCodeAt(i) <= 65374) {// 如果位于全角！到全角～区间内
                    tmp += String.fromCharCode(str.charCodeAt(i) - 65248)
                } else if (str.charCodeAt(i) == 12288) {// 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
                    tmp += ' ';
                } else {// 不处理全角空格，全角！到全角～区间外的字符
                    tmp += str[i];
                }
            }
            return tmp;
        };


        DataVerifier.verifyEmail = function(email) {
            var myreg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            var flag = true;
            if (!myreg.test(email.trim())) {
                flag = false;
            }
            return flag;
        };	

        DataVerifier.isInt = function(value){
            return /^(\-)?[0-9]+$/.test(value);
        }
        
        DataVerifier.isDouble = function(value){
            return /^\d+(\.\d+)?$/.test(value);
        }

        DataVerifier.isObject = function( obj ){
            return Object.prototype.toString.call(obj) === '[object Object]';
        }


        DataVerifier.verifyDay = function( value ){
            return /^\d{4}(\-|\/|\.)\d{1,2}\1\d{1,2}$/.test(value);
        }

        DataVerifier.verifyYear = function( value ){
            return /^\d{4}$/.test(value);
        }
        
        DataVerifier.verify = function( type ){
            var obj = {};
            switch( type ){
                case 'email': 
                    obj.exec = this.verifyEmail;
                    obj.metion = this.verifyFailMsg.email;
                    break;
                case 'int':
                    obj.exec = this.isInt;
                    obj.metion = this.verifyFailMsg.int;
                    break;
                case 'double':
                    obj.exec = this.isDouble;
                    obj.metion = this.verifyFailMsg.double;
                    break;
                case 'date':
                    obj.exec = this.isDouble;
                    obj.metion = this.verifyFailMsg.date;
                    break;
                case 'year':
                    obj.exec = this.verifyYear;
                    obj.metion = this.verifyFailMsg.year;
                    break;
                default:
                    obj = null;
            }
            return obj;
        }

        DataVerifier.verifyFailMsg = {
            'email' : '电子邮件的格式不正确，请确认后再输入! ',
            'nonull' : '必填项，请输入信息!',
            'int' : '请输入整数值！',
            'double': '请输入浮点数！',
            'date' : '请正确输入日期，日期格式为年/月/日，以/、-、.分隔!',
            'year': '年份应输入四位整数!'
        }
    
       return DataVerifier;
})