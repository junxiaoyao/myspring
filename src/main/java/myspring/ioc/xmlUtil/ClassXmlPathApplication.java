package myspring.ioc.xmlUtil;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: jxy
 * @Date: 2019/3/18 10:45
 * @Description:dom4j读取spring.xml并解析bean
 */
public class ClassXmlPathApplication {
    private String xmlPath;
    Map<String, String> idMap = new HashMap<>();
    public ClassXmlPathApplication(String xmlPath) throws DocumentException {
        this.xmlPath = xmlPath;
        readMain();
    }

    public void readMain() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(getPath(xmlPath));
        Element element = document.getRootElement();
        readElement(element);
    }

    //读取文件转为输入流
    public InputStream getPath(String xmlPath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(xmlPath);
        return inputStream;
    }
    //判断元素是否为bean，并将其加入到map
    public void getIdBeans(Element root) {
        String id = root.attributeValue("id");
        String classPath = root.attributeValue("class");
        if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(classPath)) {
            idMap.put(id, classPath);
        }
    }
    //根据id获取bean
    public Object getBean(String id) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String classPath = idMap.get(id);
        if (classPath == null) {
            return null;
        }
        Class classSe = Class.forName(classPath);
        return classSe.newInstance();
    }

    //递归遍历节点
    public void readElement(Element root) {
        getIdBeans(root);
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
