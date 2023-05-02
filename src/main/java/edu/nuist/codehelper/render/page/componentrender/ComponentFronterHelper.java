package edu.nuist.codehelper.render.page.componentrender;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import lombok.Data;

@Data
public class ComponentFronterHelper {
    private HashMap<String, String> attrs;
    private String compType;

    public static final int HTML = 0;
    public static final int CSS = 1;
    public static final int JS = 2; 
    public static final int REGIST = 3; 
    public static final int RESP = 4; 

    public ComponentFronterHelper(HashMap<String, String> attrs, String type){
        this.attrs = attrs;
        this.compType = type;
    }

    public String exec(String c, int frontType){
        if( "".equals(c) || c == null ) return "";
        String rst = "";
        switch(this.compType){
            case "uploader":
                rst = upload(c);
                break;
            case "button":
            case "button-icon":
            case "button-small":
            case "button-big":
            case "button-form":
                rst = button(this.compType, c, frontType);
                break;
            case "popup":
                PopFronterHelper pf = new PopFronterHelper(attrs);
                if( frontType == HTML){
                    rst = pf.html();
                }else if( frontType == JS){
                    rst = pf.js(c);
                }else if( frontType == RESP){
                    rst = pf.response(c);
                }else{
                    rst = c;
                }
                break;
            case "input":
            case "downlist":
            case "autocomplete":
                rst = input( this.compType,  c ,  frontType);
                break;
            case "datepicker":
                rst = datepicker( this.compType,  c ,  frontType);
                break;
            case "selector":
            case "multi-selector":
                rst = selector( this.compType,  c ,  frontType);
                break;
            case "editor":
                rst = editor( this.compType,  c ,  frontType);
                break;
            case "table":
            case "page-table":
                rst = table(this.compType, c, frontType, attrs);
                break;
            case "tab":
                rst = tab(this.compType, c, frontType);
                break;
            default:
                rst = c;
        }
        return rst;
    }

    public String tab(String cType, String c , int frontType){
        String li = "<li class=\"mytab \" ><a tag=\"{1}\">{0}</a></li>";
        if( frontType == HTML){
            StringBuffer sb = new StringBuffer();
            for(Entry<String, String> e: this.attrs.entrySet()){
                //System.out.println(e.getKey());
                if( e.getKey().startsWith(ComponentRender.SUB_COMP_PREFIX + "tab-li") ){
                    String v = e.getKey().substring(e.getKey().indexOf(",")+1);
                    String text = v.split(",")[0].trim();
                    String value = v.split(",")[1].trim();
                    sb.append(MessageFormat.format(li, text, value) + "\r\n");
                }
            }    
            c = c.replaceAll("<!--items-->", sb.toString());
        }
        return c;
    }

    public String table(String cType, String c , int frontType, HashMap<String, String> attrs){
        if( frontType == HTML){
            //选择在处理HTML时，序列化后台CONTROLLER
            TableFronterHelper th = new TableFronterHelper(cType, attrs);
            th.serizal();
            //System.out.println(th.html(c));
            return th.html(c);
        }

        return c;
    }

    public String editor( String cType, String c , int frontType){
        String id = this.attrs.get(ComponentRender.ID);
        c = c.replaceAll("<!--id-->", id);

        String height = this.attrs.get("height");
        c = c.replaceAll("<!--height-->", height==null?"240":height);
        return c;
    }

    String check = "<span class=\"selector-item\"><span class=\"nocheck\" value=\"{1}\"></span><span>{0}</span></span>\r\n";
    public String selector( String cType, String c , int frontType){
        String id = this.attrs.get(ComponentRender.ID);
        c = c.replaceAll("<!--id-->", id);

        String num = this.attrs.get("num");
        num = (num == null) ? "1" : num;
        c = c.replaceAll("<!--type-->", cType);
        
        if( frontType == HTML){
            StringBuffer sb = new StringBuffer();
            for(Entry<String, String> e: this.attrs.entrySet()){
                //System.out.println(e.getKey());
                if( e.getKey().startsWith(ComponentRender.SUB_COMP_PREFIX + "sel-item") ){
                    String v = e.getKey().substring(e.getKey().indexOf(",")+1);
                    String text = v.split(",")[0].trim();
                    String value = v.split(",")[1].trim();
                    sb.append(MessageFormat.format(check, text, value));
                }
            }    
            c = c.replaceAll("<!--items-->", sb.toString());
        }
        return c;
    }

    public String datepicker(String cType, String c , int frontType){
        String id = this.attrs.get(ComponentRender.ID);
        String placeholder = this.attrs.get("placeholder");
        c = c.replaceAll("<!--id-->", id)
            .replaceAll("<!--placeholder-->", placeholder);
        
        return c;
    }


    public static final String ITEM = "<div value=''{0}'' class='listitem'>{1}</div>\r\n";
    public String input(String cType, String c , int frontType){
        String id = this.attrs.get(ComponentRender.ID);
        String label = this.attrs.get("label");
        String placeholder = this.attrs.get("placeholder");
        String verifier = this.attrs.get("verifier");
        String value = this.attrs.get("value");

        label = (label == null? "" : label);
        placeholder = (placeholder == null? "" : placeholder);
        verifier = (verifier == null? "" : verifier);
        value = (value == null? "" : value);

        c = c.replaceAll("<!--id-->", id)
            .replaceAll("<!--type-->", cType)
            .replaceAll("<!--placeholder-->", placeholder)
            .replaceAll("<!--label-->", label)
            .replaceAll("<!--init value-->", value == null ? "" : value)
            .replaceAll("<!--verifer-->", verifier);
        if( frontType==HTML && (cType.equals("downlist") || cType.equals("autocomplete"))){
            String url = this.attrs.get("url");
            String items = this.attrs.get("items");
            if( cType.equals("autocomplete")) {
                items = null;
            }

            String tmp = "";
            if( items != null ){
                String[] listitem = items.substring(1, items.length()-1).split(",");
                int index = 0;
                for(String s: listitem){
                    tmp += MessageFormat.format(ITEM, (index++)+"", s);
                } 
            }
            c = c.replaceAll("<!--items-->", "<!--items-->\r\n"+ (tmp==null?"":tmp))
                .replaceAll("<!--url-->", (url==null?"":url));

        }
            
        return c;
    }


    public String button(String cType, String c , int frontType){
        String rst = c;
        String id = this.attrs.get(ComponentRender.ID);
        String type = cType;
        String font = this.attrs.get(ButtonComponentSerier.FONT);
        String text = this.attrs.get(ButtonComponentSerier.TEXT);
        String title = this.attrs.get(ButtonComponentSerier.TIP);

        rst = rst.replaceAll("<!--type-->", type);
        rst = rst.replaceAll("<!--id-->", id);
        rst = rst.replaceAll("<!--font-->", font == null ? "" : font); //可以不设置font
        rst = rst.replaceAll("<!--text-->", text);
        rst = rst.replaceAll("<!--title-->", title == null ? "" : title);//可以不设置title

        //是按钮
        if(frontType==HTML){
            if(!cType.equals("button-icon") ){
                rst = rst.substring(rst.indexOf("button")+6, rst.indexOf("end"));
            }else{
                rst = rst.substring(rst.indexOf("iconbut")+7, rst.lastIndexOf("end"));
            }
        }
        return rst;
    }

    public String upload(String c ){
        String id = this.attrs.get(ComponentRender.ID);
        return c.replaceAll("<!--id-->", id);
    }

    public String getCssType( ){
        String type = this.compType;
        switch(this.compType){
            case "button":
            case "button-icon":
            case "button-small":
            case "button-big":
            case "button-form":
                type = "button";
                break;
            case "input":
            case "downlist":
            case "autocomplete":
                type = "input";
                break;
            case "selector":
            case "multi-selector":
                type = "selector";
                break;
            case "page-table":
            case "table":
                type = "table";
                break;
        }
        return type;
    }
}
