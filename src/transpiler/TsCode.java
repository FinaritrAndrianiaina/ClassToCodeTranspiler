package transpiler;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.StringJoiner;

public class TsCode extends JavaCode{
    @Override
    protected String convertMethod(Method m) {
        StringJoiner mj = new StringJoiner(" ");
        mj.add(this.convertAnnotation(m)+"\n ");
        mj.add(this.convertModifier(m.getModifiers()));
        this.importedPackage.add(m.getReturnType().getPackageName());
        mj.add(m.getName().concat(this.convertParameter(m)));
        mj.add(":"+m.getReturnType().getSimpleName()+";");
        return  mj.toString();
    }

    @Override
    protected String convertField(Field f) {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(this.convertAnnotation(f)+"\n ");
        sj.add(this.convertModifier(f.getModifiers()));
        sj.add(f.getName());
        sj.add(":");
        sj.add(f.getType().getSimpleName());
        return sj.toString().concat(";");
    }

    @Override
    protected String convertParameter(Executable m) {
        StringJoiner pj = new StringJoiner(", ");
        for(Parameter parameter: m.getParameters()) {
            this.importedPackage.add(parameter.getType().getPackageName());
            pj.add(parameter.getName().concat(": ").concat(parameter.getType().getSimpleName()));
        }
        return "("+pj.toString()+")";
    }


}
