package transpiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.StringJoiner;

public class TsCode extends JavaCode{

    private static Map<String,String> typeToTs = Map.of(
        "int","number",
        "float","number",
        "long","number"
    );

    private String convertType(String type) {
        String convert = type;
        if(typeToTs.containsKey(type)) {
            convert = typeToTs.get(type);
        }
        return convert;
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
            sj.add("const");
        }
        return sj.toString();
    }


    @Override
    protected String convertMethod(Method m) {
        StringJoiner mj = new StringJoiner(" ");
        mj.add(this.convertAnnotation(m)+"\n ");
        mj.add(this.convertModifier(m.getModifiers()));
        this.importedPackage.add(m.getReturnType().getPackageName());
        mj.add(m.getName().concat(this.convertParameter(m)));
        mj.add(":"+this.convertType(m.getReturnType().getSimpleName())+";");
        return  mj.toString();
    }

    @Override
    protected String convertField(Field f) {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(this.convertAnnotation(f)+"\n ");
        sj.add(this.convertModifier(f.getModifiers()));
        sj.add(f.getName());
        sj.add(":");
        sj.add(this.convertType(f.getType().getSimpleName()));
        return sj.toString().concat(";");
    }
    protected String convertConstructor(Constructor<?> c) {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(" constructor".concat(this.convertParameter(c)));
        return sj.toString();
    }
    private String tsOverloadConstructor(Constructor<?> c,Integer index) {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(" public static ");
        sj.add("constructor"+this.classFile.getSimpleName().concat("OverLoad"+index.toString()).concat(this.convertParameter(c)));
        sj.add(": "+this.classFile.getSimpleName());
        return sj.toString();    
    }
    protected void generateConstructor() {
        var constructors = this.classFile.getConstructors();
        if(!(constructors.length>0)) return; 
        this.constructorsDef.add(this.convertConstructor(constructors[0]));
        for (int index = 1; index < constructors.length; index++) {
            this.constructorsDef.add(this.tsOverloadConstructor(constructors[index], index));
        }
    }

    @Override
    protected String convertParameter(Executable m) {
        StringJoiner pj = new StringJoiner(", ");
        for(Parameter parameter: m.getParameters()) {
            this.importedPackage.add(parameter.getType().getPackageName());
            pj.add(parameter.getName().concat(": ").concat(this.convertType(parameter.getType().getSimpleName())));
        }
        return "("+pj.toString()+")";
    }

    public void assembleCode() {
        String listField = String.join("\n ",this.fields);
        String listMethod = String.join("\n ",this.methods);
        String listConstructor = String.join("\n ",this.constructorsDef);
        this.fields.clear();
        this.methods.clear();
        System.out.println(this.classDefinition + " {\n"+ listConstructor + listField + listMethod + "\n}");
    }

    public Code copy() {
        TsCode tsCode = new TsCode();
        tsCode.classFile = this.classFile;
        tsCode.classLoader = this.classLoader;
        return tsCode;
    }

}
