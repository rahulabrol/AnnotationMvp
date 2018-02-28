package com.annotation.mvp.annotationmvp.process;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

/**
 * Created by rahul.abrol on 12/02/18.
 */

public class FileGenerator {

    private final Filer mFiler;
    private final String packageName;
    private final String className;
    private final String signature;
    private boolean hasCore;

    public FileGenerator(Filer filer, String packageName, String className, boolean hasCore) {
        this.mFiler = filer;
        this.packageName = packageName;
        this.className = className;
        this.hasCore = hasCore;
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        StringBuilder builder = new StringBuilder();
        builder.append("/**");
        builder.append("\n");
        builder.append(" * Created by annotationMVP on ");
        builder.append(format.format(Calendar.getInstance().getTime()) + ".");
        builder.append("\n");
        builder.append(" */");
        this.signature = builder.toString();
    }

    public void generateCore(List<String> methods) {
        String fileName = "Core";
        this.generateFile(fileName, methods, false);
    }

    public void generatePresenter(List<String> methods) {
        String fileName = this.className + "Presenter";
        this.generateFile(fileName, methods, false);
    }

    public void generateInteractor(List<String> methods) {
        String fileName = this.className + "Interactor";
        this.generateFile(fileName, methods, false);
    }

    public void generateInteractedListener(List<String> methods) {
        String fileName = "On" + this.className + "InteractedListener";
        this.generateFile(fileName, methods, this.hasCore);
    }

    public void generateView(List<String> methods) {
        String fileName = this.className + "View";
        this.generateFile(fileName, methods, this.hasCore);
    }

    private void generateFile(String fileName, List<String> methods, boolean needCore) {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + this.packageName + ";");
        builder.append("\n");
        builder.append("\n");
        builder.append(this.signature);
        builder.append("\n");
        builder.append("\n");
        builder.append("public interface " + fileName);
        if (needCore) {
            builder.append(" extends Core");
        }

        builder.append(" {");
        builder.append("\n");
//        int index = false;
        Iterator source = methods.iterator();

        while (source.hasNext()) {
            String method = (String) source.next();
            builder.append("\t");
            builder.append(method);
            builder.append("\n");
        }

        builder.append("}");
        source = null;

        try {
            JavaFileObject sourceFile = this.mFiler.createSourceFile(this.packageName + "." + fileName, new Element[0]);
            Writer writer = sourceFile.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException var11) {
            var11.printStackTrace();
        } finally {
            source = null;
            builder = null;
        }

    }

}
