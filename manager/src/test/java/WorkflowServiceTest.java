import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

/**
 * Created by ji on 16-4-29.
 */
public class WorkflowServiceTest {
    public static void main(String[] args) throws Exception {
        File file = new File("/home/ji/桌面/oozie/oozie-4.2.0/examples/target/oozie-examples-4.2.0-examples/examples/apps/hive/workflow.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element root = document.getRootElement();
        System.out.println(root);


        Iterator<Element> tasks = root.elementIterator();
        if (tasks != null) {
            while (tasks.hasNext()) {
                Element task = tasks.next();
                Iterator<Element> props = task.elementIterator();
                System.out.println(task.getName());
                System.out.println("============");
                while (props.hasNext()) {
                    System.out.println(props.next().getName());
                }
                System.out.println("****************");
            }
        }
    }

}
