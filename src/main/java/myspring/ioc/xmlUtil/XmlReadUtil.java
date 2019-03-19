package myspring.ioc.xmlUtil;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @Auther: jxy
 * @Date: 2019/3/18 9:57
 * @Description:
 */
public class XmlReadUtil {
    public static void main(String[] args) throws Exception {
        XmlReadUtil xmlReadUtil = new XmlReadUtil();
        xmlReadUtil.readMain();
    }

    public void readMain() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(getPath("student.xml"));
        Element element = document.getRootElement();
        readElement(element);
    }

    //读取文件转为输入流
    public InputStream getPath(String xmlPath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(xmlPath);
        return inputStream;
    }

    //递归遍历节点
    public void readElement(Element root) {
        System.out.println("当前节点：" + root.getName());
        //节点参数<bean id="5555"></bean>
        List<Attribute> attributes = root.attributes();
        for (Attribute attribute : attributes) {
            System.out.println("参数：" + attribute.getName() + "值：" + attribute.getValue());
        }
        //获取标签文本
        String value = root.getTextTrim();
        if (!StringUtils.isEmpty(value)) {
            System.out.println("Text：" + value);
        }
        //获取子元素
        List<Element> elements = root.elements();
        for (Element element : elements) {
            readElement(element);
        }
    }
}
