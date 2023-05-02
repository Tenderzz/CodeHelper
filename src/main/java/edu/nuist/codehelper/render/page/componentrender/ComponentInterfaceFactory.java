package edu.nuist.codehelper.render.page.componentrender;

import java.util.HashMap;

import edu.nuist.codehelper.component.NodeRenderFactory;

public class ComponentInterfaceFactory {

    
    public static ComponentRenderInterface get(String type, HashMap<String, String> attrs){
        ComponentRenderInterface rst = null;
        String dir = attrs.get(NodeRenderFactory.DIR_KEY);
        String pack = attrs.get(NodeRenderFactory.PACKAGE_KEY);
        switch( type ){
            case "uploader":
                String targetdir = attrs.get(UploaderComponentSerier.ATTR_TAG_DIR_KEY);
                String limit = attrs.get(UploaderComponentSerier.FILE_SIZE_LIMIT);
                rst = new UploaderComponentSerier(dir, pack, targetdir, limit);
                break;
            case "downloader":  
                String url = attrs.get(DownloaderComponentSerier.DOWNLOAD_URL);
                rst = new DownloaderComponentSerier(dir, pack, url);
                break;
            case "button":
            case "button-icon":
            case "button-small":
            case "button-big":
                String text = attrs.get(ButtonComponentSerier.TEXT);
                //设置表格提交的按键样式与普通按键相同
                rst = new ButtonComponentSerier(dir, pack, type, text);
                break;
            case "button-form":
                rst = new FormButtonComponentSerier(dir, pack, attrs.get(ButtonComponentSerier.TEXT));
                break;
            case "popup":
                rst = new PopComponentSerier(dir, pack);
                break;
            case "input":
            case "downlist":
            case "autocomplete":
                rst = new InputComponentSerier(dir, pack);
                break;
            case "datepicker":
                rst = new DatePickerComponentSerier(dir, pack);
                break;
            case "selector":
            case "multi-selector":
                rst = new SelectorComponentSerier(dir, pack);
                break;
            case "editor":
                rst = new EditorComponentSerier(dir, pack);
                break;
            case "table":
                rst = new TableComponentSerier(dir, pack);
                break;
            case "page-table":
                rst = new PageTableComponentSerier(dir, pack);
                break;
            case "tab":
                rst = new TabComponentSerier(dir, pack);
                break;
        }

        return rst;
    }
}
