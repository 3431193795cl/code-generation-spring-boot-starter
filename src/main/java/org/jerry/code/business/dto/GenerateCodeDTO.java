package org.jerry.code.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateCodeDTO implements Serializable {

    private static final long serialVersionUID = -5176200076641210975L;

    private String tableSql;

    private TableInfoDTO options;
}
