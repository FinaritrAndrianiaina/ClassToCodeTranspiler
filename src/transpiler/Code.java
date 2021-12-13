package transpiler;

import java.lang.Class;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public abstract class Code {

    protected Class<?> classFile;
    protected FileClassLoader classLoader;
    protected List<String> methods = new ArrayList<>();
    protected List<String> fields = new ArrayList<>();
    protected List<String> importedPackage = new ArrayList<>();
    protected LinkedList<String> interfaces = new LinkedList<>();
    protected String classDefinition = "";
    protected String superClass = "";

    protected abstract String convertMethod(Method m);
    protected abstract String convertField(Field f);
    protected abstract String convertExtends();
    protected abstract void assembleCode();
    protected abstract String convertInterface();
    protected abstract String convertParameter(Method m);
    protected abstract String convertAnnotation(AccessibleObject m);
    protected abstract String convertModifier(int modifiers);

    protected abstract void generateClassName();
    protected abstract void generateSuperClass();
    protected void generateFields() {
        for(Field f: this.classFile.getDeclaredFields()) {
            this.fields.add(this.convertField(f));
        }
    }
    protected void generateMethods() {
        for(Method m: this.classFile.getDeclaredMethods()) {
            this.methods.add(this.convertMethod(m));
        }
    }
    protected void generateInterfaces() {
        for (Class<?> anInterface : this.classFile.getInterfaces()) {
            this.interfaces.add(anInterface.getSimpleName());
        }
    }
    public void setClassFile(Class<?> classFile) {
        this.classFile = classFile;
    }
    public void setClassLoader(FileClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void assembleSuperClass() {
        if(!superClass.equals("")){
            this.classLoader = this.classLoader.copy();
            this.classLoader.setFileName(this.superClass);
            Class<?> superClassToAssemble = this.classLoader.loadIt();
            this.setClassFile(superClassToAssemble);
            this.superClass = "";
            this.assembleCode();
        }
    }
    public void assembleInterfaces() {
        while (!this.interfaces.isEmpty()) {
            this.classLoader = this.classLoader.copy();
            this.classLoader.setFileName(this.interfaces.removeFirst());
            Class<?> superClassToAssemble = this.classLoader.loadIt();
            this.setClassFile(superClassToAssemble);
            this.assembleCode();
        }
    }
    public void assemble() {
        this.assembleCode();
        this.assembleSuperClass();
        this.assembleInterfaces();
    }
    public void generate() {
        this.generateMethods();
        this.generateSuperClass();
        this.generateFields();
        this.generateInterfaces();
        this.generateClassName();
    }
}

