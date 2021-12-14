package transpiler;

import java.util.StringJoiner;

public class CsharpCode extends JavaCode{
    protected String convertInterface() {
        if(this.interfaces.isEmpty()){ return "";}
        String interfaces = String.join(", ",this.interfaces);
        if(this.superClass.equals("")){
            return ":" + interfaces;
        }
        return interfaces;
    }

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

        return sj.toString();
    }
    protected String convertExtends() {
        if(this.superClass.equals("")) return "";
        return ": "+this.superClass;
    }

    protected void convertClassName() {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(this.convertModifier(this.classFile.getModifiers()));
        sj.add(!this.classFile.isInterface()?"class":"");
        sj.add(this.classFile.getSimpleName());
        sj.add(this.convertInterface());
        sj.add(this.convertExtends());
        this.classDefinition = sj.toString();
    }

    public void assembleCode() {
        StringJoiner packageName = new StringJoiner(".");
        packageName.add("namespace App");
        packageName.add(this.classFile.getPackageName());
        String listField = String.join("\n ",this.fields);
        String listMethod = String.join("\n ",this.methods);
        String listConstructor = String.join("\n ",this.constructorsDef);
        System.out.println(packageName+" { \n "+this.classDefinition + " {\n " + listConstructor + listField + listMethod + "\n }\n}");
    }

    public Code copy() {
        CsharpCode cCode = new CsharpCode();
        cCode.classFile = this.classFile;
        cCode.classLoader = this.classLoader;
        return cCode;
    }
}
