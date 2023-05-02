package edu.nuist.codehelper.render.page;

public class HtmlRender {
    private StringBuffer sb = new StringBuffer();

    public HtmlRender(){}

    public void append(String t){
        this.sb.append("\r\n" + t);
    }

    public String getHtml(){
        return this.sb.toString();
    }

    public void reset( String html){
        sb = new StringBuffer();
        sb.append(html);
    }
}
