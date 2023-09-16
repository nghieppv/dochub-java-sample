package com.dochub.common;

import java.util.List;

public class CreateBatchImportDataDto {
    private String name;
    private int departmentId;
    private int documentTypeId;
    private int documentTemplateId;
    private List<String> parameters;
    private List<List<String>> rows;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(int documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public int getDocumentTemplateId() {
        return documentTemplateId;
    }

    public void setDocumentTemplateId(int documentTemplateId) {
        this.documentTemplateId = documentTemplateId;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }
}
