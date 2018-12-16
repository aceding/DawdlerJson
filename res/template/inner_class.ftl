    public static class ${className} {
        <#list attrs as a>

        @Alias("${a.field}")
        public ${a.type} ${a.field};
        </#list>

        public static ${className} convertFromJSONObject(String content){
            ${className} result = JSONConverter.convertFromJSONObject(content, ${className}.class);
            return result;
        }

        public static List<${className}> convertFromJSONArray(String content){
            Type type = new ${className}(){}.getClass().getGenericSuperclass();
            List<${className}> result = JSONConverter.convertFromJSONArray(content, type);
            return result;
        }

        public JSONObject convert2JSONObject(){
            JSONObject result = JSONConverter.convert2JSONObject(this);
            return result;
        }

        <#if hasInnerClass == "true">
        <#list innerClasses as innerClassName>

            <#include innerClassName encoding="UTF-8" parse=false>

        </#list>
        </#if>
    }