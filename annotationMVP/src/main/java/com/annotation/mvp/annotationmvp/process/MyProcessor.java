package com.annotation.mvp.annotationmvp.process;

import com.annotation.mvp.annotationmvp.core.Core;
import com.annotation.mvp.annotationmvp.core.MVP;
import com.annotation.mvp.annotationmvp.core.Presenter;
import com.annotation.mvp.annotationmvp.core.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

public class MyProcessor extends AbstractProcessor {
    private String packageName;
    private String className;
    private ElementTypePair mvp;
    private ElementTypePair presenter;
    private ElementTypePair view;
    private ElementTypePair core;
    private MethodFinder finder;
    private List<String> coreMethods = new ArrayList();

    public MyProcessor() {
    }

    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.mvp = this.getType(MVP.class.getCanonicalName());
        this.presenter = this.getType(Presenter.class.getCanonicalName());
        this.view = this.getType(View.class.getCanonicalName());
        this.core = this.getType(Core.class.getCanonicalName());
        this.finder = new MethodFinder();
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet();
        annotations.add(MVP.class.getCanonicalName());
        annotations.add(Presenter.class.getCanonicalName());
        annotations.add(View.class.getCanonicalName());
        annotations.add(Core.class.getCanonicalName());
        return annotations;
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        if (!this.generateCore(roundEnv)) {
            this.generateMVP(roundEnv);
        }

        return false;
    }

    private boolean generateCore(RoundEnvironment env) {
        this.coreMethods.clear();
        Set<? extends Element> elements = env.getElementsAnnotatedWith(this.core.getElement());
        if (elements.size() > 1) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Only one Core per application");
            return true;
        } else {
            Iterator var3 = ElementFilter.typesIn(elements).iterator();

            while (var3.hasNext()) {
                TypeElement type = (TypeElement) var3.next();
                if (type.getKind() != ElementKind.INTERFACE) {
                    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, this.className + " Must be interface", type);
                    return true;
                }

                Iterator var5 = type.getEnclosedElements().iterator();

                while (var5.hasNext()) {
                    Element element = (Element) var5.next();
                    this.finder.getMethod(element, this.coreMethods);
                }
            }

            return false;
        }
    }

    private final void generateMVP(RoundEnvironment env) {
        List<String> presenterMethods = new LinkedList();
        List<String> interactorMethods = new LinkedList();
        List<String> callbackMethods = new LinkedList();
        Set<? extends Element> element = env.getElementsAnnotatedWith(this.mvp.getElement());
        Iterator var6 = ElementFilter.typesIn(element).iterator();

        while (var6.hasNext()) {
            TypeElement type = (TypeElement) var6.next();
            presenterMethods.clear();
            interactorMethods.clear();
            callbackMethods.clear();
            this.packageName = type.getEnclosingElement().toString();
            this.className = type.getSimpleName().toString();
            if (type.getKind() != ElementKind.INTERFACE) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, this.className + " Must be interface", type);
                return;
            }

            if (!this.checkForPresenter(type, presenterMethods, interactorMethods)) {
                return;
            }

            this.checkForView(type, callbackMethods);
            FileGenerator generator = new FileGenerator(this.processingEnv.getFiler(), this.packageName, this.className, !this.coreMethods.isEmpty());
            generator.generateCore(this.coreMethods);
            generator.generatePresenter(presenterMethods);
            generator.generateInteractor(interactorMethods);
            generator.generateInteractedListener(callbackMethods);
            generator.generateView(callbackMethods);
        }

    }

    private final boolean checkForPresenter(TypeElement type, List<String> presenter, List<String> interactor) {
        boolean hasPresenter = false;
        Iterator var5 = type.getEnclosedElements().iterator();

        while (var5.hasNext()) {
            Element element = (Element) var5.next();
            Iterator var7 = element.getAnnotationMirrors().iterator();

            while (var7.hasNext()) {
                AnnotationMirror annotation = (AnnotationMirror) var7.next();
                if (annotation.getAnnotationType() == this.presenter.getElement().asType()) {
                    this.finder.getMethod(element, presenter);
                    this.finder.getMethod(element, interactor, "On" + this.className + "InteractedListener");
                    hasPresenter = true;
                }
            }
        }

        if (!hasPresenter) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to find Presenter", type);
        }

        return hasPresenter;
    }

    private final void checkForView(TypeElement type, List<String> methods) {
        Iterator var3 = type.getEnclosedElements().iterator();

        while (var3.hasNext()) {
            Element element = (Element) var3.next();
            Iterator var5 = element.getAnnotationMirrors().iterator();

            while (var5.hasNext()) {
                AnnotationMirror annotation = (AnnotationMirror) var5.next();
                if (annotation.getAnnotationType() == this.view.getElement().asType()) {
                    this.finder.getMethod(element, methods);
                }
            }
        }

    }

    private final ElementTypePair getType(String className) {
        TypeElement typeElement = this.processingEnv.getElementUtils().getTypeElement(className);
        DeclaredType declaredType = this.processingEnv.getTypeUtils().getDeclaredType(typeElement, new TypeMirror[0]);
        return new ElementTypePair(typeElement, declaredType);
    }
}
