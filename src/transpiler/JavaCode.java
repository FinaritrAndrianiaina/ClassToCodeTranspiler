package transpiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.StringJoiner;

import transpiler.Code;

public class JavaCode extends Code {

    protected String convertModifier(int mod) {
        StringJoiner sj = new StringJoiner(" ");
        if ((mod & 1) != 0) {
            sj.add("public");
        }


        if ((mod & 4) != 0) {
            sj.add("protected");
        }

        if ((mod & 2) != 0) {
            sj.add("private");
        }

        if ((mod & 512) != 0) {
            return "interface";
        }

        if ((mod & 1024) != 0) {
            sj.add("abstract");
        }

        if ((mod & 8) != 0) {
            sj.add("static");
        }

        if ((mod & 16) != 0) {
            sj.add("final");
        }

        if ((mod & 128) != 0) {
            sj.add("transient");
        }

        if ((mod & 64) != 0) {
            sj.add("volatile");
        }

        if ((mod & 32) != 0) {
            sj.add("synchronized");
        }

        if ((mod & 256) != 0) {
            sj.add("native");
        }

        if ((mod & 2048) != 0) {
            sj.add("strictfp");
        }

        return sj.toString();
    }



    protected String convertInterface() {
        if(this.interfaces.isEmpty()){ return "";}
        String interfaces = String.join(", ",this.interfaces);
        return "implements "+interfaces;
    }
    protected String convertExtends() {
        return "extends "+this.superClass;
    }
    protected String convertMethod(Method m) {
        StringJoiner mj = new StringJoiner(" ");
        mj.add(this.convertAnnotation(m)+"\n ");
        mj.add(this.convertModifier(m.getModifiers()));
        mj.add(m.getReturnType().getSimpleName());
        this.importedPackage.add(m.getReturnType().getPackageName());
        mj.add(m.getName().concat(this.convertParameter(m)));
        return  mj.toString();
    }
    protected String convertField(Field f){
        StringJoiner sj = new StringJoiner(" ");
        sj.add(this.convertAnnotation(f)+"\n ");
        sj.add(this.convertModifier(f.getModifiers()));
        sj.add(f.getType().getSimpleName());
        sj.add(f.getName());
        return sj.toString().concat(";");
    }
    protected String convertAnnotation(AccessibleObject m) {
        StringJoiner sj = new StringJoiner("\n");
        for(Annotation annotation:m.getAnnotations()){
            sj.add(annotation.toString());
        }
        return " "+sj.toString();
    }
    protected String convertParameter(Method m) {
        StringJoiner pj = new StringJoiner(", ");
        for(Parameter parameter: m.getParameters()) {
            this.importedPackage.add(parameter.getType().getPackageName());
            pj.add(parameter.getType().getSimpleName().concat(" ").concat(parameter.getName()));
        }
        return "("+pj.toString()+");";
    }

    protected void generateClassName() {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(this.convertModifier(this.classFile.getModifiers()));
        sj.add(!this.classFile.isInterface()?"class":"");
        sj.add(this.classFile.getSimpleName());
        sj.add(this.convertInterface());
        sj.add(this.convertExtends());
        this.classDefinition = sj.toString();
    }

    protected void generateSuperClass() {
        if(this.classFile.getSuperclass().getSimpleName().equals("Object")){return;}
        this.superClass = this.classFile.getSuperclass().getSimpleName();
    }

    public void assembleCode() {
        this.generate();
        String packageName = "";
        if(!this.classFile.getPackageName().equals("")){
            packageName = "package "+this.classFile.getPackage().getName()+";";
        }
        String listField = String.join("\n ",this.fields);
        String listMethod = String.join("\n ",this.methods);
        this.fields.clear();
        this.methods.clear();
                    System.out.println(packageName+"\n"+this.classDefinition + " {\n" + listField + listMethod + "\n}");
    }
 
 }