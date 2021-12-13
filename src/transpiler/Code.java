package transpiler;

import java.lang.Class;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Code {

    protected Class<?> classFile;
    protected FileClassLoader classLoader;
    protected List<String> methods = new ArrayList<>();
    protected List<String> fields = new ArrayList<>() ;
    protected List<String> importedPackage = new ArrayList<>();
    protected List<String> constructorsDef = new ArrayList<>();
    protected LinkedList<String> interfaces = new LinkedList<>();
    protected String classDefinition = "";
    protected String superClass = "";


    /*
    * Les méthodes convert permettent de convertir les types reflect en langage cible
    */
    protected abstract String convertMethod(Method m);
    protected abstract String convertField(Field f);
    protected abstract String convertExtends();
    protected abstract String convertInterface();
    protected abstract String convertParameter(Executable m);
    protected abstract String convertAnnotation(AccessibleObject m);
    protected abstract String convertModifier(int modifiers);
    protected abstract void convertClassName();


    /*
     * Les méthodes generate remplissent les listes de méthodes du code
     */
    protected  void generateSuperClass(){
        if(this.classFile.isInterface() || this.classFile.getSuperclass().getSimpleName().equals("Object")){return;}
        this.superClass = this.classFile.getSuperclass().getSimpleName();
    }
    protected void generateFields() {
        for(Field f: this.classFile.getDeclaredFields()) {
            this.fields.add(this.convertField(f));
        }
    }
    protected void generateConstructor() {
        for (Constructor<?> c : this.classFile.getConstructors()){
            this.constructorsDef.add(this.convertConstructor(c));
        }
    }

    protected abstract String convertConstructor(Constructor<?> c);

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
    public void generate() {
        this.generateMethods();
        this.generateSuperClass();
        this.generateFields();
        this.generateInterfaces();
        this.generateConstructor();
        this.convertClassName();
    }

    public abstract Code copy();

    public void setClassFile(Class<?> classFile) {
        this.classFile = classFile;
    }
    public void setClassLoader(FileClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /*
    * Les méthodes assemble assemble les codes à partir des listes de méthodes
    * */
    protected abstract void assembleCode();
    public void assembleSuperClass() {
        if(!superClass.equals("")){
            FileClassLoader fCL = this.classLoader.copy();
            fCL.setFileName(this.superClass);
            Code interfaceCode = this.copy();
            interfaceCode.classFile = fCL.loadIt();
            interfaceCode.assemble();
        }
    }
    public void assembleInterfaces() {
        while (!this.interfaces.isEmpty()) {
            FileClassLoader fCL = this.classLoader.copy();
            fCL.setFileName(this.interfaces.removeFirst());
            Code interfaceCode = this.copy();
            interfaceCode.classFile = fCL.loadIt();
            interfaceCode.assemble();
        }
    }
    public void assemble() {
        this.generate();
        this.assembleCode();
        this.assembleSuperClass();
        this.assembleInterfaces();
    }
}

