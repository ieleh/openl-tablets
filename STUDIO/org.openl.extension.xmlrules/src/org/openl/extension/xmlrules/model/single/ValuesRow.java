package org.openl.extension.xmlrules.model.single;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "values-row")
public class ValuesRow {
    private List<ArrayValue> list = new ArrayList<ArrayValue>();

    @XmlElement(name = "value", type = ArrayValue.class, required = true)
    public List<ArrayValue> getList() {
        return list;
    }

    public void setList(List<ArrayValue> list) {
        this.list = list;
    }
}
