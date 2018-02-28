package com.annotation.mvp.annotationmvp.process;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

/**
 * Created by rahul.abrol on 12/02/18.
 */

public class ElementTypePair {
    private final TypeElement element;
    private final DeclaredType type;

    public ElementTypePair(TypeElement element, DeclaredType type) {
        this.element = element;
        this.type = type;
    }

    public TypeElement getElement() {
        return this.element;
    }

    public DeclaredType getType() {
        return this.type;
    }
}
