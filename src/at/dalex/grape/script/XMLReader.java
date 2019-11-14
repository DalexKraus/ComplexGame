package at.dalex.grape.script;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * The <code>XMLReader</code> can be used to read values from an XML file
 * @author dalex
 */
public class XMLReader {

	private Document doc;
	private SAXBuilder builder;
	private Element rootElement;
	
	public XMLReader(InputStream source) {
		builder = new SAXBuilder();
		try {
			doc = builder.build(source);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rootElement = doc.getRootElement();
	}
	
	/**
	 * Gives you the root element of this XML-Object
	 * @return Root-Element of this XML Object
	 */
	public Element getRootElement() {
		return this.rootElement;
	}
}
