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
    protected String convertExtends() {
        if(this.superClass.equals("")) return "";
        return ": "+this.superClass;
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

    public void assembleCode() {
        this.generate();
        StringJoiner packageName = new StringJoiner(".");
        packageName.add("namespace App");
        packageName.add(this.classFile.getPackageName());
        String listField = String.join("\n ",this.fields);
        String listMethod = String.join("\n ",this.methods);
        this.fields.clear();
        this.methods.clear();
        System.out.println(packageName+" { \n "+this.classDefinition + " {\n" + listField + listMethod + "\n }\n}");
    }
}
