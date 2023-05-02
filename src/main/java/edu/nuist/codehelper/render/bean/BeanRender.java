package edu.nuist.codehelper.render.bean;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.nuist.codehelper.component.BeanCreator;
import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.RequiredPropertyNoFind;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import edu.nuist.codehelper.render.Render;
import edu.nuist.codehelper.utils.ApplicationContextHelperUtil;
import edu.nuist.codehelper.utils.ImportPackConfig;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class BeanRender extends Render{
    private boolean hasTimestamp = false;
    private boolean hasTransient = false;
    private boolean hasColDef = false;
    private boolean hasTable = false;

    private Node prop;
    private Node dao;
    
    private String table;
    private String beanName;
    private String pack;
    private Map<String, String> attributes = new HashMap<String, String>();
    private String beanRendRst;
    private String repsRendRst;
    private String servRendRst;

    public BeanRender(Node n, Map<String, String> ctx){
        super(n, ctx);
        this.pack = this.getCTX().get(NodeRenderFactory.PACKAGE_KEY);
        attributes.put("pack", pack + ".entity");
        try{
            this.beanName = n.getVale();
            attributes.put("bean", this.beanName);
            for(Node sub: n.getSubNodes()){
                if( sub.getKey().equals("prop")){
                    this.prop = sub;
                } 
                if( sub.getKey().equals("dao")){
                    this.dao = sub;
                } 
                if( sub.getKey().equals("table")){
                    this.hasTable = true;
                    this.table = sub.getVale(); 
                    attributes.put("table", this.table);
                }     
            }
            
        }catch(SplitCharNotFind e){
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean check() {
        return this.prop != null && this.beanName != null;
    }

    @Override
    public void exec() throws Exception {
        
        if( !this.check() ) throw new RequiredPropertyNoFind();
        
        String b = FileTemplator.getBean();
        StringBuffer sb = new StringBuffer();
        for(Node sub: this.prop.getSubNodes()){
            String attrStr = attrRender(sub);
            sb.append(attrStr + "\r\n");
        }

        StringBuffer sbImport = new StringBuffer();
        HashSet<String> def = new HashSet<>();
        for (Node sub : this.prop.getSubNodes()) {
            def.add(importRender(sub)+"\n");
        }
        for (String s : def) {
            sbImport.append(s);
        }
        
        //设置属性
        b = b.replace("<!-- imports -->", sbImport.toString());
        b = b.replace("<!-- attributes -->", sb.toString());   
        b = decorateByFlags(this.hasTable, b, "hastable");
        b = decorateByFlags(this.hasTimestamp, b, "hastimestamp");
        b = decorateByFlags(this.hasTransient, b, "hasTransient");
        b = decorateByFlags(this.hasColDef, b, "hasColDef");

    
        if(this.hasTable){
            this.attributes.put("id", "long");
        }

        if(this.hasTable) b = b.replace("<!--table name -->", this.table);
        b = b.replace("<!-- bean name -->", this.beanName);
        b = b.replace("<!-- package -->", "package " + this.pack + ".entity;");



        //生成REPO, SERV
        if(this.hasTable && this.dao!=null){
            BeanDAORender brr = new BeanDAORender(this.dao, this.getCTX(), this.attributes);
            brr.exec();
            this.repsRendRst = brr.getRepsRst();
            this.servRendRst = brr.getServRst();
        }
        this.beanRendRst = b;

        BeanCreator bc = new BeanCreator();
        bc.exec(
                this.getCTX().get(NodeRenderFactory.DIR_KEY),
                this.pack,
                this.beanRendRst,
                this.repsRendRst == null ? "" : this.repsRendRst,
                this.servRendRst == null ? "" : this.servRendRst,
                this.beanName
        );
    }


    public static final String attrMode = "private {0} {1};";
    public static final String timestamp = "@Column(name=\"{0}\",columnDefinition=\"TIMESTAMP DEFAULT CURRENT_TIMESTAMP\", insertable = false,updatable = false)\r\n@CreatedDate";
	public static final String colNameAndType = "@Column ({0} {1})";
    public static final String colNot = "@Transient";

    public String attrRender(Node n) throws SplitCharNotFind{
        String key = n.getKey();
        String value = n.getVale();
        
        String def = MessageFormat.format(attrMode, value, key);
        this.attributes.put(key, value); //属性名， 类型

        if( value.toLowerCase().equals("timestamp")){
            def = "    "+MessageFormat.format(timestamp, key) + "\r\n    " + def;
            this.hasTimestamp = true;
        }

        String transit = "";
        String colNameDef = ""; 
        String colTypeDef = "";

        if(n.getSubNodes().size()>0){
            
            for(Node sub: n.getSubNodes()){
                String type = sub.getKey();
                switch(type){
                    case "col-name":
                        colNameDef = "name=\"" + sub.getVale() + "\"";
                        break;
                    case "col-type":
                        colTypeDef = "columnDefinition=\"" + sub.getVale()+ "\"";
                        break;
                    case "col-not":
                        transit = "@Transient";
                        break;
                }
            }
            if( !"".equals(transit) ){
                this.hasTransient = true;
                def = transit + "\r\n    " + def;
            }else{
                if( !"".equals(colNameDef) && !"".equals(colTypeDef)){
                    colTypeDef = ", " +colTypeDef; 
                }
                
                if( !"".equals(colNameDef) || !"".equals(colTypeDef) ){
                    this.hasColDef = true;
                    def = "    "+MessageFormat.format(colNameAndType, colNameDef, colTypeDef) + "\r\n    " + def;
                }
            }
            
        }
        return def;

    }

    public static final String[] checkPoints = new String[]{
        "prop"
    };
    
    private static ImportPackConfig ymlImportC = (ImportPackConfig)ApplicationContextHelperUtil.getBean(ImportPackConfig.class);
        
    public String importRender(Node n) throws SplitCharNotFind {
        Map<String, String> imports = ymlImportC.getImportr();
        String def = "";
        String value = n.getVale();
        String valuesub = "";
        if (value.contains("<")) {
            valuesub = value.substring(0, value.indexOf("<"));
        } else valuesub = value;


        for (String key : imports.keySet()) {
            if (key.equals(valuesub)) {
                def += imports.get(key);
            }
        }


        return def;
    }

}
