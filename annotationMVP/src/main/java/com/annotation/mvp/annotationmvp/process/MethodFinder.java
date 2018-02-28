package com.annotation.mvp.annotationmvp.process;

import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Created by rahul.abrol on 12/02/18.
 */

public class MethodFinder {

    public MethodFinder() {
    }

    public void getMethod(Element element, List<String> methods) {
        ExecutableElement exe = (ExecutableElement)element;
        StringBuilder builder = new StringBuilder();
        builder.append(this.getModifier(exe));
        builder.append(" ");
        builder.append(exe.getReturnType().toString());
        builder.append(" ");
        builder.append(exe.getSimpleName());
        builder.append("(");
        builder.append(this.getParameters(exe, (String)null));
        builder.append(")");
        builder.append(";");
        methods.add(builder.toString());
    }

    public void getMethod(Element element, List<String> methods, String extraParam) {
        ExecutableElement exe = (ExecutableElement)element;
        StringBuilder builder = new StringBuilder();
        builder.append(this.getModifier(exe));
        builder.append(" ");
        builder.append(exe.getReturnType().toString());
        builder.append(" ");
        builder.append(exe.getSimpleName());
        builder.append("(");
        builder.append(this.getParameters(exe, extraParam));
        builder.append(")");
        builder.append(";");
        methods.add(builder.toString());
    }

    private String getModifier(ExecutableElement exe) {
        Iterator<Modifier> mod = exe.getModifiers().iterator();
        return ((Modifier)mod.next()).toString();
    }

    private String getParameters(ExecutableElement exe, String extraParam) {
        StringBuilder builder = new StringBuilder();
        int index = 0;

        for(Iterator var5 = exe.getParameters().iterator(); var5.hasNext(); ++index) {
            VariableElement params = (VariableElement)var5.next();
            builder.append(params.asType().toString());
            builder.append(" ");
            builder.append(params.getSimpleName());
            if(index < exe.getParameters().size() - 1) {
                builder.append(", ");
            }
        }

        if(extraParam != null) {
            if(builder.length() > 0) {
                builder.append(", ");
            }

            builder.append(extraParam);
            builder.append(" ");
            builder.append("listener");
        }

        return builder.toString();
    }

}
