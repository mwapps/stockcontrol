package com.manriqueweb.stockcontrol.stockcontrol.dto;

public enum ConceptType {
    ADD("ADD"),
    SUBTRACT("SUBTRACT"),
    REPLACE("REPLACE");

    private String concept;

    ConceptType(String a) {
    	this.concept = a;
    }

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

}
