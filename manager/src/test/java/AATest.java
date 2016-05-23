
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class AATest {
    public Document parse(String string) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(string);
        return document;
    }

    public void bar(Document document) throws DocumentException {

        Element root = document.getRootElement();//获得根节点；
        //进行迭代；读取根节点下的所有节点和子节点下的所有节点
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            System.out.println(element.getName());
            for (Iterator j = element.elementIterator(); j.hasNext(); ) {
                System.out.println(((Element) j.next()).getName());
            }
        }

        //读取节点名为foo的所有子节点
        for (Iterator i = root.elementIterator("foo"); i.hasNext(); ) {
            Element foo = (Element) i.next();
            System.out.println(foo.getName());
        }

        //读取根节点的所有属性
        for (Iterator i = root.attributeIterator(); i.hasNext(); ) {
            Attribute attribute = (Attribute) i.next();
            System.out.println(attribute.getName());
        }
    }

    /*
     * 可以根据节点名字读取节点，也可以读取节点里的key和value
     */
    public void readNodes(Document document) {
        String path = "//workflow-app/action";
        List list = document.selectNodes(path);   //foo为根节点，获得根节点下的bar节点
        Node node = document.selectSingleNode("//workflow-app/start");  //获得名为author的第一 节点
        String name = node.valueOf("@name"); //获得节点名属性名为name的value
    }

    /*
     * 如果xml文件很大的情况下，用上面的方法很费时，这样 可以用递归遍历整个xml文件
     */
    public void treeWalk(Document document) {
        treeWalk(document.getRootElement());
    }

    /*
     * 递归调用，传递每一个父节点做为参数
     */
    public void treeWalk(Element element) {
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {//如果node实现了Element接口，那么就表示node是一个节点。再递归
                treeWalk((Element) node);
                System.out.println(((Element) node).getName() + ":" + node.valueOf("@name"));
            } else {//如果没有实现Element接口，那么就表示这个node不是节点了，输出节点等操作；
            }
        }
    }

    //获得节点属性名key为name的value
    public void findLinks(Document document) throws DocumentException {
        List list = document.selectNodes("//ehcache/cache/@name");
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Attribute attribute = (Attribute) iter.next();
            String url = attribute.getValue();
            System.out.println(url);
        }
    }

    /*
     * 创建一个documnet文档
     */
    public Document createDocument() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("root");

        Element author1 = root.addElement("author")
                .addAttribute("name", "James")
                .addAttribute("location", "UK")
                .addText("James Strachan");

        Element author2 = root.addElement("author")
                .addAttribute("name", "Bob")
                .addAttribute("location", "US")
                .addText("Bob McWhirter");
        return document;
    }

    //写入xml文件
    public void write(Document document) throws IOException {

        // lets write to a file
        XMLWriter writer = new XMLWriter(
                new FileWriter("output.xml")
        );
        writer.write(document);
        writer.close();


        // Pretty print the document to System.out
        OutputFormat format = OutputFormat.createPrettyPrint();
        writer = new XMLWriter(System.out, format);
        writer.write(document);

        // Compact format to System.out
        format = OutputFormat.createCompactFormat();
        writer = new XMLWriter(System.out, format);
        writer.write(document);
    }

    /*
     * 主函数，用来测试
     */
    public static void main(String[] args) throws DocumentException,
            IOException {
        AATest mjrx = new AATest();
        Document d = mjrx.parse("/tmp/workflow.xml");
//        mjrx.readNodes(d);

        mjrx.bar(d);
        System.out.println("------one----------");
        mjrx.treeWalk(d);
        System.out.println("------two----------");
        mjrx.findLinks(d);
    }
}