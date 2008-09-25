package org.openl.rules.webstudio.web.tableeditor;

public class MultiChoiceCellEditor extends ComboBoxCellEditor {
    public MultiChoiceCellEditor(String[] choices, String[] displayValues) {
        super(choices, displayValues);
    }

    @Override
    public TableEditorController.EditorTypeResponse getEditorTypeAndMetadata() {
        TableEditorController.EditorTypeResponse typeResponse = new TableEditorController.EditorTypeResponse(CE_MULTICHOICE);
        typeResponse.setParams(new MultiChoiceParam(choices, displayValues, ","));
        return typeResponse;
    }

    public static class MultiChoiceParam extends ComboBoxParam {
        private String separator;
        
        public MultiChoiceParam(String[] choices, String[] displayValues, String separator) {
            super(choices, displayValues);
            this.separator = separator;
        }

        public String getSeparator() {
            return separator;
        }

        public void setSeparator(String separator) {
            this.separator = separator;
        }
    }
}
