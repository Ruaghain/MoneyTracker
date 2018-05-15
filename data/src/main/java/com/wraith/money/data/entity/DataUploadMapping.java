package com.wraith.money.data.entity;

import com.wraith.money.data.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class represents the mappings of columns to xls (or csv) columns for a data upload.
 *
 * User: rowan.massey Date: 09/09/2014
 */
@Entity
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "datauploadmapping_id")),
        @AttributeOverride(name = "version", column = @Column(name = "datauploadmapping_version"))})
public class DataUploadMapping extends BaseEntity implements Serializable {

    private DataUpload dataUpload;
    private String entityName;
    private String localField;
    private String importedField;
    private String dataType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datauploadmapping_dataupload_id")
    public DataUpload getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(DataUpload dataUpload) {
        this.dataUpload = dataUpload;
    }

    @Column(name = "datauploadmapping_entityname", nullable = false)
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Column(name = "datauploadmapping_localfield", nullable = false)
    public String getLocalField() {
        return localField;
    }

    public void setLocalField(String localField) {
        this.localField = localField;
    }

    @Column(name = "datauploadmapping_importedfield", nullable = false)
    public String getImportedField() {
        return importedField;
    }

    public void setImportedField(String importedField) {
        this.importedField = importedField;
    }

    @Column(name = "datauploadmapping_fieldtype", nullable = false)
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
