package edu.nuist.codehelper.render.bean;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.ReferenceAttrNoFind;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import edu.nuist.codehelper.render.Render;
import lombok.Data;

@Data
public class BeanDAORender extends Render {
    private Node n;
    private Map<String, String> attrs;

    private String repsRst;
    private String servRst;
    private boolean hasPage;
    private boolean hasList;

    private boolean hasUpdate;

    public static final String PAGE_FUN = "page-fun";
    public static final String LIST_FUN = "list-fun";
    public static final String FUN = "fun";
    public static final String UPDATE_FUN = "update-fun";

    public BeanDAORender(Node dao, Map<String, String> ctx, Map<String, String> attrs) {
        super(dao, ctx);
        this.n = dao;
        this.attrs = attrs;
    }

    public void exec() throws SplitCharNotFind, ReferenceAttrNoFind {
        List<String> methods = methodRender(n);
        String b = FileTemplator.getRepo();
        String s = FileTemplator.getServ();
        String beanName = this.attrs.get("bean");
        String pack = this.attrs.get("pack");

        String table = this.attrs.get("table");

        b = decorateByFlags(this.hasList, b, "haslist");
        b = decorateByFlags(this.hasPage, b, "haspage");
        b = decorateByFlags(this.hasPage, b, "haspage");

        s = decorateByFlags(this.hasList, s, "haslist");
        s = decorateByFlags(this.hasPage, s, "haspage");

        b = b.replaceAll("<!--bean-->", beanName);
        b = b.replaceAll("<!--table-->", table);
        b = b.replaceAll("<!-- package -->", pack);
        b = b.replaceAll("<!--define fun-->", methods.get(0));

        s = s.replaceAll("<!--bean-->", beanName);
        s = s.replaceAll("<!-- package -->", pack);
        s = s.replaceAll("<!--define fun-->", methods.get(1));

        this.repsRst = b;
        this.servRst = s;

    }

    public final static String PAGE_FUN_MODE_REPS = "Page<{0}> {1}(Pageable pageRequest, {2} );  ";
    public final static String LIST_FUN_MODE_REPS = "List<{0}> {1}({2});";
    public final static String FUN_MODE_REPS = "{0} {1}({2}); ";
    public final static String UPDATE_FUN_MODE_REPS = "void {0}({1}); ";

    public final static String PAGE_FUN_MODE_SERV =
            "public Page<{0}> {1}(Pageable pageRequest, {2} )'{'\n" +
                    "        return reps.{1}(pageRequest,{3});\n" +
                    "    '}'";
    public final static String LIST_FUN_MODE_SERV =
            "public List<{0}> {1}({2})'{'\n" +
                    "        return reps.{1}({3});\n" +
                    "    '}'";
    public final static String FUN_MODE_SERV =
            "public {0} {1}({2}) '{'\n" +
                    "        return reps.{1}({3}); \n" +
                    "    '}'";
    public final static String UPDATE_FUN_MODE_SERV =
            "public void {0}({1})'{'\n" +
                    "        reps.{0}({2}); \n" +
                    "    '}'";

    public final static String QUERY_MODE = "@Query(value = \"{0}\", nativeQuery = true)";


    public List<String> methodRender(Node n) throws SplitCharNotFind, ReferenceAttrNoFind {
        String className = this.attrs.get("bean");

        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for (Node sub : n.getSubNodes()) {
            String funType = sub.getKey();
            String method = sub.getVale();

            String paramStr = getParamter(sub.getSubNodes());
            String paramStrName = getParamterName(sub.getSubNodes());
            String funStrReps = "";
            String funStrServ = "";

            switch (funType) {
                case PAGE_FUN:
                    funStrReps = MessageFormat.format(PAGE_FUN_MODE_REPS, className, method, paramStr);
                    funStrServ = MessageFormat.format(PAGE_FUN_MODE_SERV, className, method, paramStr, paramStrName);
                    this.hasPage = true;
                    break;
                case LIST_FUN:
                    funStrReps = MessageFormat.format(LIST_FUN_MODE_REPS, className, method, paramStr);
                    funStrServ = MessageFormat.format(LIST_FUN_MODE_SERV, className, method, paramStr, paramStrName);
                    this.hasList = true;
                    break;
                case FUN:
                    funStrReps = MessageFormat.format(FUN_MODE_REPS, className, method, paramStr);
                    funStrServ = MessageFormat.format(FUN_MODE_SERV, className, method, paramStr, paramStrName);
                    break;
                case UPDATE_FUN:
                    funStrReps = MessageFormat.format(UPDATE_FUN_MODE_REPS, method, paramStr);
                    funStrServ = MessageFormat.format(UPDATE_FUN_MODE_SERV, method, paramStr, paramStrName);
                    this.hasUpdate = true;
                    break;
            }
            funStrReps = "    " + funStrReps;
            funStrServ = "    " + funStrServ;

            String query = getSQL(sub.getSubNodes());
            if (query != null) {
                funStrReps = "    " + query + "\n" + funStrReps + "\n";
                funStrServ = funStrServ + "\n";
            }

            if (funType.equals(UPDATE_FUN)) {
                funStrReps = "    @Transactional\n    @Modifying\n" + funStrReps + "\n";
                funStrServ = "    @Transactional\n" + funStrServ + "\n";
            }

            sb1.append(funStrReps + "\n");
            sb2.append(funStrServ + "\n");

        }
        List<String> list=new LinkedList<>();
        list.add(sb1.toString());
        list.add(sb2.toString());
        return list;
    }

    public String getSQL(List<Node> nodes) throws SplitCharNotFind {
        for (Node n : nodes) {
            if (n.getKey().equals("sql")) {
                return MessageFormat.format(QUERY_MODE, n.getVale());
            }
        }

        return null;
    }

    public String getParamter(List<Node> nodes) throws SplitCharNotFind, ReferenceAttrNoFind {
        String sb = "";
        for (Node n : nodes) {
            if (!n.getKey().equals("sql")) {
                if (n.getKey().startsWith("&")) {
                    String dataType = this.attrs.get(n.getKey().substring(1));
                    if (dataType == null) throw new ReferenceAttrNoFind();
                    String dataName = n.getKey().substring(1);

                    sb = dataType + " " + dataName + ", " + sb;
                } else {
                    sb = n.getVale() + " " + n.getKey() + ", " + sb;
                }
            }
        }
        return sb.substring(0, sb.length() - 2);
    }

    //得到属性名
    public String getParamterName(List<Node> nodes) throws SplitCharNotFind, ReferenceAttrNoFind {
        String sb = "";
        for (Node n : nodes) {
            if (!n.getKey().equals("sql")) {
                if (n.getKey().startsWith("&")) {
                    String dataType = this.attrs.get(n.getKey().substring(1));
                    if (dataType == null) throw new ReferenceAttrNoFind();
                    String dataName = n.getKey().substring(1);

                    sb = dataName + ", " + sb;
                } else {
                    sb = n.getKey() + ", " + sb;
                }
            }
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Override
    public boolean check() {
        return false;
    }


}
